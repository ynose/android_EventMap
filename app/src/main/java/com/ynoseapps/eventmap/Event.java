package com.ynoseapps.eventmap;

import java.util.Date;

/**
 * Created by yoshio on 15/12/25.
 */
public class Event {

    Long id;
    String title;
    Date startAt;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return new Long(1); //id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getStartAt() {
        return startAt;
    }

}
