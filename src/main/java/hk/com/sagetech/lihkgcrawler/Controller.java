package hk.com.sagetech.lihkgcrawler;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

final class Controller {

    private int mLower;
    private int mUpper;

    Controller(int lower, int upper){
        mLower = lower;
        mUpper = upper;
    }

    void crawl() throws ExecutionException, InterruptedException {
        final List<Future<?>> futures = new ArrayList<>();
        final ExecutorService service = Executors.newFixedThreadPool(10);
        for(int i=mLower; i<=mUpper; i++){
            final String url = "https://lihkg.com/thread/" + i;
            final CrawlerRunnable crawlerRunnable = new CrawlerRunnable(url);
            futures.add(service.submit(crawlerRunnable));
        }

        for(Future<?> future : futures){
            future.get();
        }
        service.shutdown();
    }
}
