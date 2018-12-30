package hk.com.sagetech.lihkgcrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class Application {

    //Configuration parameter
    private static final int KEEP_ALIVE_SECONDS = 600;
    private static final int THREAD_POOL_SIZE = 15;

    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }

    //Configuration
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        threadPoolTaskExecutor.setCorePoolSize(THREAD_POOL_SIZE);
        return threadPoolTaskExecutor;
    }
}
