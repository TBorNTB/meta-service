package com.sejong.chatservice.infrastructure.redis;

import com.sejong.chatservice.core.postlike.domain.PostLike;
import com.sejong.chatservice.core.postlike.repository.LikeRepository;
import com.sejong.chatservice.infrastructure.postlike.entity.PostLikeEntity;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class PostLikeRedisJobConfig {

    private final RedisService redisService;
    private final EntityManagerFactory entityManagerFactory;

    private static final int CHUNK_SIZE = 1000;

    @Bean
    public Job postLikeRedisSyncJob(JobRepository jobRepository, Step postLikeRedisSyncStep) {
        return new JobBuilder("postLikeRedisSyncJob", jobRepository)
                .start(postLikeRedisSyncStep)
                .build();
    }

    @Bean
    public Step postLikeRedisSyncStep(JobRepository jobRepository,
                                       PlatformTransactionManager transactionManager) {
        return new StepBuilder("postLikeRedisSyncStep", jobRepository)
                .<PostLikeEntity, PostLike>chunk(CHUNK_SIZE, transactionManager)
                .reader(postLikeReader())
                .processor(postLikeProcessor())
                .writer(postLikeWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<PostLikeEntity> postLikeReader() {
        JpaPagingItemReader<PostLikeEntity> reader = new JpaPagingItemReader<>();
        reader.setName("postLikeReader");
        reader.setQueryString("SELECT p FROM PostLikeEntity p");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setPageSize(CHUNK_SIZE);
        return reader;
    }

    @Bean
    public ItemProcessor<PostLikeEntity, PostLike> postLikeProcessor() {
        return PostLikeEntity::toDomain;
    }

    @Bean
    public ItemWriter<PostLike> postLikeWriter() {
        return postLikes -> {
            for (PostLike postLike : postLikes) {
                String redisKey = RedisKeyUtil.likeKey(postLike.getPostType(), postLike.getPostId());
                redisService.addUserToLikeSet(redisKey, postLike.getUserId());
            }
        };
    }
}