package com.example.eventco.Attendee.Models;

import java.util.Date;

public class Event {
    private String title , bannerUrl , venue , desc ;
    private Date date;

    public Event(){

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Event(String title, String bannerUrl, String venue, String desc, Date date){
        this.bannerUrl = bannerUrl;
        this.title = title;
        this.venue = venue;
        this.desc = desc;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
