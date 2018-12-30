package hk.com.sagetech.lihkgcrawler;

import hk.com.sagetech.lihkgcrawler.jpa.LihkgUserActivityRepo;
import hk.com.sagetech.lihkgcrawler.jpa.UserActivityModel;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Crawler implements Runnable{

    //WebDriver-centric parameter
    private static final String THREAD_URL= "https://lihkg.com/thread/";
    private static final String PAGE_CLASS_NAME = "_1H7LRkyaZfWThykmNIYwpH";
    private static final String SCROLL_PANEL_ID = "rightPanel";
    private static final int SCROLL_TOP_PARAM = 2000;
    private static final int TIMEOUT_LIMIT = 60;
    private static final int SCROLL_WAITING_PERIOD = 5000;

    private final WebDriver mDriver;
    private final LihkgUserActivityRepo mUserActivityRepo;
    private final String mUrl;
    private final int mThreadId;
    private final CountDownLatch mLatch;

    Crawler(int threadId, LihkgUserActivityRepo repo, CountDownLatch latch){
        mThreadId = threadId;
        mUrl = THREAD_URL + threadId + "/";
        mUserActivityRepo = repo;
        mLatch = latch;
        mDriver = new ChromeDriver(new ChromeOptions().setHeadless(true));
    }

    @Override
    public void run() {
        System.out.println("Crawling URL: " + mUrl);
        String html = getHtml();
        if(html==null){
            System.out.println("Crawler finished for " + mUrl + " and HTML is null");
        }else{
            System.out.println("Crawler finished for " + mUrl + " and HTML is not null");
        }
        mDriver.close();
        List<UserActivityModel> userActivityModels = parseHtml(html);
        if(userActivityModels!=null){
            System.out.println("Parsing finished for " + mUrl + " and the results are not null");
            mUserActivityRepo.saveAll(userActivityModels);
        }else{
            System.out.println("Parsing finished for " + mUrl + " and the result is null");
        }
        System.out.println("Crawling of thread #" + mThreadId + " is completed");
        mLatch.countDown();
    }

    private String getHtml(){
        mDriver.get(mUrl);
        WebDriverWait wait = new WebDriverWait(mDriver, TIMEOUT_LIMIT);
        wait.until((webDriver -> ((JavascriptExecutor)webDriver).executeScript("return document.readyState").equals("complete")));
        System.out.println(mUrl + " source: " + mDriver.getPageSource());
        int page;
        page = mDriver.findElements(By.xpath("//div[@class=\"" + PAGE_CLASS_NAME + "\"][1]/select/option")).size();
        System.out.println(mUrl + " : page " + page);
        if(page > 0){
            JavascriptExecutor jsExecutor = (JavascriptExecutor) mDriver;
            String targetUrl = mUrl + "page/" + page;
            int scrollTop = 0;
            do{
                scrollTop += SCROLL_TOP_PARAM;
                jsExecutor.executeScript("document.getElementById(\"" + SCROLL_PANEL_ID + "\").scrollTop=" + scrollTop);
                try {
                    Thread.sleep(SCROLL_WAITING_PERIOD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
                wait.until((webDriver -> ((JavascriptExecutor)webDriver).executeScript("return document.readyState").equals("complete")));
            }while(!mDriver.getCurrentUrl().equals(targetUrl));
            return mDriver.getPageSource();
        }
        return null;
    }

    private List<UserActivityModel> parseHtml(String html){
        if(html!=null){
            return Parser.getUserActivities(mThreadId, html);
        }
        return null;
    }


}
