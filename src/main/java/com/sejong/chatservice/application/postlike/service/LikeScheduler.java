package com.sejong.chatservice.application.postlike.service;

import com.sejong.chatservice.core.postlike.domain.PostLike;
import com.sejong.chatservice.core.postlike.repository.LikeRepository;
import com.sejong.chatservice.infrastructure.redis.RedisKeyUtil;
import com.sejong.chatservice.infrastructure.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeScheduler {
    private final JobLauncher jobLauncher;
    private final Job postLikeRedisSyncJob;
    private final RedisService redisService;

    @Scheduled(cron = "0 0 7 * * *") // 매일 오전 7시
    public void runRedisLikeSyncJob() {
        try {
            redisService.clearAllLikeKeys();

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis()) // 매 실행 시마다 다른 값으로 중복 방지
                    .toJobParameters();

            jobLauncher.run(postLikeRedisSyncJob, jobParameters);
        } catch (Exception e) {
            // 로깅 혹은 알림
            throw new RuntimeException("Redis 좋아요 배치 작업 실패", e);
        }
    }
}
