package com.baron.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Jason on 2017/6/12.
 */
@Document(collection = "house")
public class House {
    @Id
    private String id;
    private Integer totalPrice;
    private Integer averagePrice;
    private Float area;
    private String address;
    private String no;
    private Date collectedAt;

    public Date getCollectedAt() {
        return collectedAt;
    }

    public void setCollectedAt(Date collectedAt) {
        this.collectedAt = collectedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Integer averagePrice) {
        this.averagePrice = averagePrice;
    }

    public Float getArea() {
        return area;
    }

    public void setArea(Float area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
