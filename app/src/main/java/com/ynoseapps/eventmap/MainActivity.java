package com.ynoseapps.eventmap;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private GoogleMap googleMap;
    private ListView lv;
    private ArrayList<Event> upcomingEvents = new ArrayList<Event>();
    private EventAdapter eventAdapter;
    //private ArrayList markerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // MapFragmentの取得（2）
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        try {
            // マップオブジェクトを取得する（3）
            googleMap = mapFragment.getMap();

            // Activityが初めて生成されたとき（4）
            if (savedInstanceState == null) {

                // フラグメントを保存する（5）
                mapFragment.setRetainInstance(true);

                // 地図の初期設定を行う（6）
                mapInit();
            }
        }
        // GoogleMapが使用できないとき
        catch (Exception e) {
        }

        lv = (ListView) findViewById(R.id.eventListView);
        eventAdapter = new EventAdapter(MainActivity.this);
        eventAdapter.setEventList(this.upcomingEvents);
        lv.setAdapter(eventAdapter);

        //リスト項目がクリックされた時の処理
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Uri uri = Uri.parse(upcomingEvents.get(position).getUrl());
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//
                // イベントサイト画面に繊維
                Intent intent = new Intent(getApplicationContext(), WebsiteView.class);
                intent.putExtra("url", upcomingEvents.get(position).getUrl());
                startActivity(intent);

            }
        });


        // HttpURLConnectionに必要
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        checkNetwork();

        getEvent("iOS");
    }

    private void checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info.isConnected()) {
            Toast.makeText(this, info.getTypeName() + " connected", Toast.LENGTH_LONG).show();
        }
    }

    private void getEvent(String keyword) {

        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://api.doorkeeper.jp/events?sort=starts_at&q=" + keyword);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() != 200) {
                return;
            }

            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream responseArray = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];

            int length;
            while((length = inputStream.read(buff)) != -1) {
                if(length > 0) {
                    responseArray.write(buff, 0, length);
                }
            }

            this.upcomingEvents = new ArrayList<Event>();
            EventAdapter adapter = new EventAdapter(MainActivity.this);
            adapter.setEventList(this.upcomingEvents);
            lv.setAdapter(adapter);

            // JSONをパース
            JSONArray result = new JSONArray(new String(responseArray.toByteArray()));
            for(int i = 0; i < result.length(); i++) {
                JSONObject eventJson = result.getJSONObject(i).getJSONObject("event");

                Event event = new Event();
                event.setTitle(eventJson.getString("title"));
                event.setStartAtString(eventJson.getString("starts_at"));
                event.setUrl(eventJson.getString("public_url"));
                this.upcomingEvents.add(event);
            }

            eventAdapter.notifyDataSetChanged();

          // mapDropPin();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            connection.disconnect();
        }
    }

    private void mapDropPin() {

        for (int i = 0; i < upcomingEvents.size(); i++) {
            Event event = upcomingEvents.get(i);
            LatLng location = new LatLng(event.latitude, event.longitude);

            // マーカーの設定
            MarkerOptions options = new MarkerOptions();
            options.position(location);
            options.title(event.venueName);
            options.snippet(event.address);

            // マップにマーカーを追加
            googleMap.addMarker(options);
        }

    }

    // 地図の初期設定
    //@TargetApi(Build.VERSION_CODES.M)
    private void mapInit() {

        // 地図タイプ設定（1）
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // 現在位置ボタンの表示（2）
        // ここで落ちる
        //if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
        //    return;
        //}
//        googleMap.setMyLocationEnabled(true);

        // 東京駅の位置、ズーム設定（3）
        CameraPosition camerapos = new CameraPosition.Builder()
                .target(new LatLng(35.681382, 139.766084)).zoom(15.5f).build();

        // 地図の中心を変更する（4）
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camerapos));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView)menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();

        getEvent(query);

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

}