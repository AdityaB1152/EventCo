package com.example.eventco.Attendee.Models;

import java.util.Date;

public class Event {
    private String eventId ,title ,category, bannerUrl , venue , desc , organiserId , status;
    private String date;

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

    public Event(String eventId, String title, String category, String bannerUrl, String venue, String desc, String date, String organiserId,String status){
        this.category = category;
        this.bannerUrl = bannerUrl;
        this.title = title;
        this.eventId = eventId;
        this.venue = venue;
        this.desc = desc;
        this.date = date;
        this.organiserId = organiserId;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOrganiserId() {
        return organiserId;
    }

    public void setOrganiserId(String organiserId) {
        this.organiserId = organiserId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
