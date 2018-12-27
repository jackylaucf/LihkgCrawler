package hk.com.sagetech.lihkgcrawler;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class CrawlerRunnable implements Runnable{

    private String mUrl;

    CrawlerRunnable(String url){
        mUrl = url;
    }

    @Override
    public void run() {
        System.out.println("Crawling URL: " + mUrl);

        final ChromeOptions option = new ChromeOptions();
        option.setHeadless(true);
        final WebDriver driver = new ChromeDriver(option);

        driver.get(mUrl);

        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        System.out.println(driver.getTitle());

        driver.close();
    }
}
