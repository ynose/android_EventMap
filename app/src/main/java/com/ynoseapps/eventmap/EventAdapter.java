package com.ynoseapps.eventmap;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by yoshio on 15/12/25.
 */
public class EventAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Event> events;

    public EventAdapter (Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setEventList(ArrayList<Event> eventList) {
        this.events = eventList;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return events.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.event_row, parent, false);

        Event event = events.get(position);

        // イベントタイトル
        ((TextView)convertView.findViewById(R.id.title)).setText(event.title);


        // 開始日時
        TextView startAt = ((TextView) convertView.findViewById(R.id.startAt));
        startAt.setText(event.getStartAtFormatedString());

        // 曜日によって色を変える
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.startAt);
        GregorianCalendar calendarStartAt = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        int startAtColor;
        switch (calendarStartAt.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SATURDAY:
                startAtColor = R.color.colorStartAtSaturday;
                break;
            case Calendar.SUNDAY:
                startAtColor = R.color.colorStartAtSunday;
                break;
            default:
                startAtColor = R.color.colorStartAt;
                break;
        }
        startAt.setTextColor(getColorResource(startAtColor));


        // 参加者数/定員
        TextView accepted = ((TextView) convertView.findViewById(R.id.accepted));
        accepted.setText(event.accepted.toString());

        // 参加者数の割合に応じで色を変える
        Double percent = event.limit > 0 ? event.accepted / event.limit : 0.0;
        if (event.limit - event.accepted == 1) {        // 残り１人 = 黄色
            accepted.setTextColor(getColorResource(R.color.colorLimitYellow));
        } else if (percent < 0.9) {                     // 90%未満 = グレー（デフォルト）
            accepted.setTextColor(getColorResource(R.color.colorAccepted));
        } else if ((0.9 < percent && percent < 1.0)) {  // 90%以上 = 黄色
            accepted.setTextColor(getColorResource(R.color.colorLimitYellow));
        } else {                                        // 100%   = 赤色
            accepted.setTextColor(getColorResource(R.color.colorLimitMax));
        }

        TextView limit = ((TextView) convertView.findViewById(R.id.limit));
        limit.setText("/" + event.limit + "人");

        return convertView;
    }

    private int getColorResource(int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(color, null);
        } else {
            return context.getResources().getColor(color);
        }

    }

}
