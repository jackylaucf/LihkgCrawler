package hk.com.sagetech.lihkgcrawler;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public final class CrawlerRunnable implements Runnable{

    //WebDriver-centric parameter
    private static final String THREAD_URL= "https://lihkg.com/thread/";
    private static final String PAGE_CLASS_NAME = "_1H7LRkyaZfWThykmNIYwpH";
    private static final String SCROLL_PANEL_ID = "rightPanel";
    private static final int SCROLL_TOP_PARAM = 2000;

    private static final WebDriver DRIVER = new ChromeDriver(new ChromeOptions().setHeadless(false));
    private String mUrl;

    CrawlerRunnable(int threadId){
        mUrl = THREAD_URL + threadId + "/";
    }

    @Override
    public void run() {
        System.out.println("Crawling URL: " + mUrl);
        String html = getHtml();
        System.out.println(html);

    }

    private String getHtml(){
        DRIVER.get(mUrl);
        WebDriverWait wait = new WebDriverWait(DRIVER, 60);
        wait.until((webDriver -> ((JavascriptExecutor)webDriver).executeScript("return document.readyState").equals("complete")));

        int page = 0;
        page = DRIVER.findElements(By.xpath("//div[@class=\"" + PAGE_CLASS_NAME + "\"][1]/select/option")).size();
        if(page > 0){
            JavascriptExecutor jsExecutor = (JavascriptExecutor) DRIVER;
            String targetUrl = mUrl + "page/" + page;
            int scrollTop = 0;
            do{
                scrollTop += SCROLL_TOP_PARAM;
                jsExecutor.executeScript("document.getElementById(\"" + SCROLL_PANEL_ID + "\").scrollTop=" + scrollTop);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
                wait.until((webDriver -> ((JavascriptExecutor)webDriver).executeScript("return document.readyState").equals("complete")));
            }while(!DRIVER.getCurrentUrl().equals(targetUrl));
            return DRIVER.getPageSource();
        }
        return null;
    }

    private List<UserActivityModel> parseHtml(String html){
        if(html!=null){

        }
        return null;
    }
}
