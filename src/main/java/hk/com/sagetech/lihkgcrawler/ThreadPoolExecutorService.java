package hk.com.sagetech.lihkgcrawler;


import hk.com.sagetech.lihkgcrawler.jpa.LihkgUserActivityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;


@Service
class ThreadPoolExecutorService {

    @Autowired
    private LihkgUserActivityRepo repo;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    public ThreadPoolExecutorService(){
        //Empty public constructor
    }

    void start(int lower, int upper){
        CountDownLatch latch = new CountDownLatch(upper-lower+1);
        for(int i=lower; i<=upper; i++){
            taskExecutor.execute(new Crawler(i, repo, latch));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        taskExecutor.shutdown();
    }
}
