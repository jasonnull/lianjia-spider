import com.baron.downloader.SeleniumDownloader;
import com.baron.pageprocessor.LianjiaPageProcessor;
import com.baron.pipeline.LianjiaPipeline;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * Created by Jason on 2017/6/12.
 */
@SpringBootApplication
@ComponentScan("com.baron")
@EnableScheduling
@EnableAutoConfiguration
public class Application {
    private Spider spider;
    private QueueScheduler queueScheduler;
    private MongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public CommandLineRunner cmd(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        return null;
    }

    @Scheduled(initialDelay = 1000 * 30, fixedRate = 1000 * 3600 * 24 * 3)
    public void startSpider() {
        if (spider != null || mongoTemplate == null) {
            return;
        } // if

        queueScheduler = new QueueScheduler();
        spider = Spider.create(new LianjiaPageProcessor())
                .setScheduler(queueScheduler)
                .addPipeline(new LianjiaPipeline(mongoTemplate, queueScheduler))
                .addUrl("http://sh.lianjia.com/ershoufang/sh4527866.html")
                .setDownloader(new SeleniumDownloader())
                .thread(4);
        spider.run();
        spider = null;
    }
}
