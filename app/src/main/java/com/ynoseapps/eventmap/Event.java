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

    public Long getId() {
        return (long) 1; //id;
    }

    public void setStartAtString(String startAt) { this.startAt = parseDate(startAt); }
    public String getStartAtFormatedString() {
        SimpleDateFormat output = new SimpleDateFormat("M月d日 EEEE H:mm");
        output.setTimeZone(TimeZone.getDefault());
        return output.format(this.startAt);
    }
    public void setEndedAtString(String endedAt) { this.endedAt = parseDate(endedAt); }
    public void setUpdateAtString(String updateAt) { this.updateAt = parseDate(updateAt); }

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

        event.siteType = Doorkeeper;
        try {
            event.title = eventJson.getString("title");
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
            event.venueName = eventJson.getString("venue_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.address = eventJson.getString("address");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.latitude = eventJson.getDouble("lat");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.longitude = eventJson.getDouble("long");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.url = eventJson.getString("public_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.limit = eventJson.getInt("ticket_limit");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.accepted = eventJson.getInt("participants");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            event.waiting = eventJson.getInt("waitlisted");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event.join = false;
        try {
            event.setUpdateAtString(eventJson.getString("updated_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return event;
    }
}
