package com.sejong.metaservice.infrastructure.redis;

import com.sejong.metaservice.core.postlike.domain.PostLike;
import com.sejong.metaservice.infrastructure.postlike.entity.PostLikeEntity;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class PostLikeRedisJobConfig {

    private static final int CHUNK_SIZE = 1000;
    private final RedisService redisService;
    private final EntityManagerFactory entityManagerFactory;

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
            Map<String, Long> likeCountMap = new HashMap<>();
            for (PostLike postLike : postLikes) {
                String redisKey = RedisKeyUtil.likeCountKey(postLike.getPostType(), postLike.getPostId());
                likeCountMap.merge(redisKey, 1L, Long::sum);
            }

            // TODO: 통계쿼리로는 안되나?

            for (Map.Entry<String, Long> entry : likeCountMap.entrySet()) {
                redisService.setLikeCount(entry.getKey(), entry.getValue());
            }
        };
    }
}