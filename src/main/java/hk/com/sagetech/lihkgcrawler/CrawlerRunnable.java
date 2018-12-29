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

public class CrawlerRunnable implements Runnable{

    //WebDriver-centric parameter
    private static final String THREAD_URL= "https://lihkg.com/thread/";
    private static final String PAGE_CLASS_NAME = "_1H7LRkyaZfWThykmNIYwpH";
    private static final String SCROLL_PANEL_ID = "rightPanel";
    private static final int SCROLL_TOP_PARAM = 2000;
    private static final int TIMEOUT_LIMIT = 60;
    private static final int SCROLL_WAITING_PERIOD = 2000;

    private final WebDriver mDriver;
    private final LihkgUserActivityRepo mUserActivityRepo;
    private final String mUrl;
    private final int mThreadId;

    CrawlerRunnable(int threadId, LihkgUserActivityRepo repo){
        mThreadId = threadId;
        mUrl = THREAD_URL + threadId + "/";
        mUserActivityRepo = repo;
        mDriver = new ChromeDriver(new ChromeOptions().setHeadless(true));
    }

    @Override
    public void run() {
        System.out.println("Crawling URL: " + mUrl);
        String html = getHtml();
        mDriver.close();
        List<UserActivityModel> userActivityModels = parseHtml(html);
        if(userActivityModels!=null){
            mUserActivityRepo.saveAll(userActivityModels);
        }
        System.out.println("Crawling of thread #" + mThreadId + " is completed");
    }

    private String getHtml(){
        mDriver.get(mUrl);
        WebDriverWait wait = new WebDriverWait(mDriver, TIMEOUT_LIMIT);
        wait.until((webDriver -> ((JavascriptExecutor)webDriver).executeScript("return document.readyState").equals("complete")));

        int page;
        page = mDriver.findElements(By.xpath("//div[@class=\"" + PAGE_CLASS_NAME + "\"][1]/select/option")).size();
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
