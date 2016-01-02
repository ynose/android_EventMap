package com.ynoseapps.eventmap;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private GoogleMap googleMap;
    private ListView lv;
    private ArrayList<Event> upcomingEvents = new ArrayList<Event>();
    private EventAdapter eventAdapter;
    private ArrayList<Marker> markers = new ArrayList<Marker>();

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
                intent.putExtra("url", upcomingEvents.get(position).url);
                startActivity(intent);

            }
        });


        // HttpURLConnectionに必要
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

//        checkNetwork();

        getEvent("iOS");
    }

//    private void checkNetwork() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//        NetworkInfo info = cm.getActiveNetworkInfo();
//        if (info.isConnected()) {
//            Toast.makeText(this, info.getTypeName() + " connected", Toast.LENGTH_LONG).show();
//        }
//    }

    // イベント情報を取得
    private void getEvent(String keyword) {

        this.upcomingEvents.clear();

        try {

            URL url = new URL("http://api.doorkeeper.jp/events?sort=starts_at&q=" + keyword);
            JSONArray result = EventRequestor.requestJason(url);

            // JSONをパース
            for(int i = 0; i < result.length(); i++) {
                Event event = Event.createEventDoorkeeper(result.getJSONObject(i));
                this.upcomingEvents.add(event);
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        this.eventAdapter.setEventList(this.upcomingEvents);
        this.eventAdapter.notifyDataSetChanged();
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

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                // マップにピンを表示
                mapDropPin();
                mapZoom();
            }
        });

        googleMap.setPadding(0, 48, 0, 0);

        // 東京駅の位置、ズーム設定（3）
        CameraPosition camerapos = new CameraPosition.Builder()
                .target(new LatLng(35.681382, 139.766084)).zoom(15.5f).build();

        // 地図の中心を変更する（4）
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camerapos));
    }

    // マップにピンを表示
    private void mapDropPin() {

        // 古いピンを削除
        for (Marker marker : this.markers) {
            marker.remove();
        }
        this.markers.clear();

        // 開催場所のピンを表示
        for (Event event: this.upcomingEvents) {

            if (event.latitude != 0 && event.longitude != 0) {
                LatLng location = new LatLng(event.latitude, event.longitude);

                // マーカーの設定
                MarkerOptions options = new MarkerOptions();
                options.position(location);
                options.title(event.venueName);
                options.snippet(event.address);

                // マップにマーカーを追加
                Marker marker = googleMap.addMarker(options);
                this.markers.add(marker);
            }

        }

    }

    // ピンが表示される範囲にマップをズーム
    private void mapZoom() {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : this.markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 16;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        googleMap.animateCamera(cu);
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

        // イベントを検索
        getEvent(query);

        // マップにピンを表示
        mapDropPin();
        mapZoom();

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