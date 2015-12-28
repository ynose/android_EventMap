package com.ynoseapps.eventmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
    public void setStartAtString(String startAt) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.startAt = dateFormat.parse(startAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getStartAt() { return startAt; }
    public String getStartAtString() {
        SimpleDateFormat output = new SimpleDateFormat("M月d日 EEEE H:mm");
        output.setTimeZone(TimeZone.getDefault());
        return output.format(this.startAt);
    }

}
