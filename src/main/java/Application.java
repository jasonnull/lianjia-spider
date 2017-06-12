import com.baron.downloader.SeleniumDownloader;
import com.baron.pageprocessor.LianjiaPageProcessor;
import com.baron.pipeline.LianjiaPipeline;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Jason on 2017/6/12.
 */
@SpringBootApplication
@ComponentScan("com.baron")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public CommandLineRunner cmd(final MongoTemplate mongoTemplate) {
        return (args) -> {
            QueueScheduler scheduler = new QueueScheduler();
            Spider spider = Spider.create(new LianjiaPageProcessor())
                    .setScheduler(scheduler)
                    .addPipeline(new LianjiaPipeline(mongoTemplate, scheduler))
                    .addUrl("http://sh.lianjia.com/ershoufang/sh4572051.html")
                    .addUrl("http://sh.lianjia.com/ershoufang/sh4534309.html")
                    .setDownloader(new SeleniumDownloader())
                    .thread(2);
            spider.runAsync();
        };
    }
}
