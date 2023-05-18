package com.yqkj.data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PreDestroy;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // 设置核心线程数
        executor.setMaxPoolSize(10); // 设置最大线程数
        executor.setQueueCapacity(10000); // 设置队列容量
        executor.setKeepAliveSeconds(20);
//        executor.setThreadNamePrefix("MyAsyncThread-");

        executor.setThreadFactory(new ThreadFactory() {
            AtomicInteger atomicInteger= new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "userThreadPool: " + atomicInteger.incrementAndGet());
            }
        });

        executor.setWaitForTasksToCompleteOnShutdown(true); // 设置等待所有任务完成后再关闭线程池
        executor.setAwaitTerminationSeconds(30); // 设置等待任务完成的超时时间

        executor.initialize();
        return executor;
    }

//    @PreDestroy
//    public void destroy() {
//        taskExecutor().shutdown(); // 在容器销毁前调用线程池的 shutdown() 方法关闭线程池
//    }

}

