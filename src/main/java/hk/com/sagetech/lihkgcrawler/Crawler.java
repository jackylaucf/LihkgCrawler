package hk.com.sagetech.lihkgcrawler;


import hk.com.sagetech.lihkgcrawler.jpa.LihkgUserActivityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
class Crawler {

    @Autowired
    private LihkgUserActivityRepo repo;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    public Crawler(){
        //Empty public constructor
    }

    void crawl(int lower, int upper) throws ExecutionException, InterruptedException {
        final List<Future<?>> futures = new ArrayList<>();
        for(int i=lower; i<=upper; i++){
            futures.add(taskExecutor.submit(new CrawlerRunnable(i, repo)));
        }
        for(Future<?> future : futures){
            future.get();
        }
        taskExecutor.shutdown();
    }
}
