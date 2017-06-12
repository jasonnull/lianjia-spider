package com.baron.pageprocessor;

import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2017/6/12.
 */
public class LianjiaPageProcessor implements PageProcessor {
    private static final Logger logger = Logger.getLogger(LianjiaPageProcessor.class);
    private static final Site site = Site.me()
            .setDomain("sh.lianjia.com")
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

    public void process(Page page) {
        List<String> targets = new ArrayList<>();
        Object o = page.getHtml().links().all();
        for (String link : page.getHtml().links().all()) {
            if (link.matches(".*ershoufang/[\\w\\.]{1,}")) {
                targets.add(link);
            } // if
        } // for

        if (page.getRequest().getUrl().indexOf(".html") == -1) {
            return;
        } // if

        try {
            page.getResultItems().put("price-num", page.getHtml().xpath("/html/body/section/div[2]/aside/div[1]/div[1]/span[1]/text()"));
            page.getResultItems().put("price-unit", page.getHtml().xpath("/html/body/section/div[2]/aside/div[1]/div[1]/span[2]/text()"));
            page.getResultItems().put("price-average", page.getHtml().xpath("/html/body/section/div[2]/aside/div[1]/div[2]/p[1]/span/text()"));
            page.getResultItems().put("area", page.getHtml().xpath("/html/body/section/div[2]/aside/ul[1]/li[3]/p[1]/text()"));
            page.getResultItems().put("address", page.getHtml().xpath("/html/body/section/div[2]/aside/ul[2]/li[3]/span[2]/text()"));
            page.getResultItems().put("no", page.getHtml().xpath("/html/body/section/div[2]/aside/ul[2]/li[5]/span[2]/text()").toString().trim());
        } catch (Throwable e) {
            logger.error("error at: " + page.getRequest().getUrl());
        } // catch
        page.addTargetRequests(targets);
    }

    public Site getSite() {
        return site;
    }
}
