package hk.com.sagetech.lihkgcrawler;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

final class Crawler {

    static void crawl(int lower, int upper) throws ExecutionException, InterruptedException {
        final List<Future<?>> futures = new ArrayList<>();
        final ExecutorService service = Executors.newFixedThreadPool(10);
        for(int i=lower; i<=upper; i++){
            final CrawlerRunnable crawlerRunnable = new CrawlerRunnable(i);
            futures.add(service.submit(crawlerRunnable));
        }

        for(Future<?> future : futures){
            future.get();
        }
        service.shutdown();
    }
}
