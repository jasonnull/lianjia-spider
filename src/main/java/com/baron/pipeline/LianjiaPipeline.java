package com.baron.pipeline;

import com.baron.model.House;
import com.mongodb.Mongo;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.query.Query;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.Date;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Jason on 2017/6/12.
 */
public class LianjiaPipeline implements Pipeline {
    private static final Logger logger = Logger.getLogger(LianjiaPipeline.class);
    private final MongoTemplate mongoTemplate;
    private QueueScheduler scheduler;

    public LianjiaPipeline(MongoTemplate mongoTemplate, QueueScheduler scheduler) {
        this.mongoTemplate = mongoTemplate;
        this.scheduler = scheduler;
    }

    public void process(ResultItems resultItems, Task task) {
        logger.info(String.format("There are %d urls to go", scheduler.getLeftRequestsCount(task)));

        // skip
        if (resultItems.getAll().size() == 0) {
            return;
        } // if
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getValue() == null) {
                return;
            } // if
        } // for

        try {
            String averagePrice = resultItems.get("price-average").toString();
            String totalPrice = resultItems.get("price-num").toString();
            String priceUnit = resultItems.get("price-unit").toString();
            String address = resultItems.get("address").toString();
            String no = resultItems.get("no");
            String area = resultItems.get("area").toString();

            House house = new House();
            if (priceUnit.equals("万")) {
                house.setTotalPrice(Integer.parseInt(totalPrice) * 10000);
            } else {
                house.setTotalPrice((((Float) (Float.parseFloat(totalPrice) * 10000 * 10000)).intValue()));
            } // else
            house.setAddress(address);
            house.setArea(Float.parseFloat(area.substring(0, area.length() - 1)));
            house.setAveragePrice(Integer.parseInt(averagePrice));
            house.setNo(no);
            house.setCollectedAt(new Date());

            // delete old house data
            mongoTemplate.remove(Query.query(where("no").is(no)), House.class);
            mongoTemplate.insert(house);
        } catch (Throwable e) {
            logger.error("error happened: " + resultItems.getRequest().getUrl());
        } // catch
    }
}
