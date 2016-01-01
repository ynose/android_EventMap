package com.ynoseapps.eventmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.ynoseapps.eventmap.Event.SiteType.*;

/**
 * Created by yoshio on 15/12/25.
 */
public class Event {

    public static enum SiteType {
        Doorkeeper {
            @Override
            public String toString() {
                return "D";
            }
        },
        Connpass {
            @Override
            public String toString() {
                return "C";
            }
        },
        Atnd {
            @Override
            public String toString() {
                return "A";
            }
        },
        Zusaar {
            @Override
            public String toString() {
                return "Z";
            }
        },
        Unknown {
            @Override
            public String toString() {
                return "X";
            }
        }
    }

    SiteType siteType = Unknown;
    Long id = (long) 1;
    String pk = "";
    String title = "";
    Date startAt = null;
    Date endedAt = null;
    String venueName = "";
    String address = "";
    Double latitude = (double) 0;
    Double longitude = (double) 0;
    String url = "";
    Integer limit = 0;
    Integer accepted = 0;
    Integer waiting = 0;
    Boolean join = false;
    Date updateAt = null;

    public void setSiteType(SiteType siteType) { this.siteType = siteType; }
    public SiteType getSiteType() { return siteType; }

    public void setId(Long id) { this.id = id; }
    public Long getId() {
        return (long) 1; //id;
    }

    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return title;}

    public void setStartAt(Date startAt) { this.startAt = startAt; }
    public void setStartAtString(String startAt) { this.startAt = parseDate(startAt); }
    public Date getStartAt() { return startAt; }
    public String getStartAtFormatedString() {
        SimpleDateFormat output = new SimpleDateFormat("M月d日 EEEE H:mm");
        output.setTimeZone(TimeZone.getDefault());
        return output.format(this.startAt);
    }

    public void setEndedAt(Date endedAt) { this.endedAt = endedAt; }
    public void setEndedAtString(String endedAt) { this.endedAt = parseDate(endedAt); }
    public Date getEndedAt() { return endedAt; }

    public void setVenueName(String venueName) { this.venueName = venueName; }
    public String getVenueName() { return venueName; }

    public void setAddress(String address) { this.address = address; }
    public String getAddress() { return address; }

    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLatitude() { return latitude; }

    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getLongitude() { return longitude; }

    public void setUrl(String url) { this.url = url;}
    public String getUrl() { return url; }

    public void setLimit(Integer limit) { this.limit = limit; }
    public Integer getLimit() { return limit; }

    public void setAccepted(Integer accepted) { this.accepted = accepted; }
    public Integer getAccepted() { return accepted; }

    public void setWaiting(Integer waiting) { this.waiting = waiting; }
    public Integer getWaiting() { return waiting; }

    public void setJoin(Boolean join) { this.join = join; }
    public Boolean getJoin() { return join; }

    public void setUpdateAt(Date updateAt) { this.updateAt = updateAt; }
    public void setUpdateAtString(String updateAt) { this.updateAt = parseDate(updateAt); }
    public Date getUpdateAt() { return updateAt; }

    // 文字列の日付をDate型に変換
    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Event createEventDoorkeeper(JSONObject doorkeeperJson) {

        JSONObject eventJson = null;
        try {
            eventJson = doorkeeperJson.getJSONObject("event");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        Event event = new Event();

        event.setSiteType(Doorkeeper);
        try {
            event.setTitle(eventJson.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.setStartAtString(eventJson.getString("starts_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.setEndedAtString(eventJson.getString("ends_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.setVenueName(eventJson.getString("venue_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.setAddress(eventJson.getString("address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.setLatitude(eventJson.getDouble("lat"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.setLongitude(eventJson.getDouble("long"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.setUrl(eventJson.getString("public_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.setLimit(eventJson.getInt("ticket_limit"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.setAccepted(eventJson.getInt("participants"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.setWaiting(eventJson.getInt("waitlisted"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event.setJoin(false);
        try {
            event.setUpdateAtString(eventJson.getString("updated_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return event;
    }
}
