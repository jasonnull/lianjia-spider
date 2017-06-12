package com.baron.downloader;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.selector.PlainText;

/**
 * Created by Jason on 2017/6/12.
 */
public class SeleniumDownloader extends HttpClientDownloader {
    private static final WebDriverPool driverPool = new WebDriverPool(10);
    private static final Logger logger = Logger.getLogger(SeleniumDownloader.class);

    @Override
    public Page download(Request request, Task task) {
        String url = request.getUrl();
        WebDriver driver = null;
        String content = null;
        try {
            driver = driverPool.get();
        } catch (InterruptedException e) {
            logger.error(e);
        } // catch
        try {
            driver.get(url);
            content = driver.findElement(By.xpath("/html")).getAttribute("outerHTML");
        } finally {
            if (driver != null) {
                driverPool.returnToPool(driver);
            } // if
        } // try

        Page page = new Page();
        page.setRawText(content);
        page.setRequest(request);
        page.setUrl(new PlainText(url));
        return page;
    }
}
