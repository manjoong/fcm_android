package com.example.jeonse_alarm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ScheduledExecutorService;

public class SelectStationActivity extends AppCompatActivity implements TextWatcher{

    RecyclerView recyclerView;
    EditText editSearch;
    StationListAdapter adapter;
    ArrayList<String> item = new ArrayList<>();
    ArrayList<StationConstruct> stationItem = new ArrayList<>();


    private ArrayList<String> arraylist;

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    String strNickname, strProfile, strEmail, strGender, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);
//        editSearch = (EditText) findViewById(R.id.editSearch);
//        listView = (ListView) findViewById(R.id.listView);
        recyclerView = (RecyclerView)findViewById(R.id.recylcerview);
        editSearch = (EditText)findViewById(R.id.editSearch);
        editSearch.addTextChangedListener((TextWatcher) this);

        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");
        strEmail = intent.getStringExtra("email");
        strGender = intent.getStringExtra("gender");


        item.add("김씨");
        item.add("이씨");
        item.add("정씨");
        item.add("박씨");
        item.add("오씨");
        item.add("박씨");
        item.add("금씨");
        item.add("최씨");

        adapter = new StationListAdapter(getApplicationContext(), item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_name); //뒤로가기 버튼 이미지 지정

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.account){
                    Toast.makeText(context, title + ": 계정 정보를 확인합니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), SelectStationActivity.class);
                    intent.putExtra("name", strNickname);
                    intent.putExtra("profile", strProfile);
                    if(strEmail != null)
                        intent.putExtra("email", strEmail);
                    else
                        intent.putExtra("email", "none");
                    if(strGender != null)
                        intent.putExtra("gender", strGender);
                    else
                        intent.putExtra("gender", "none");
                    startActivity(intent);
                }
                else if(id == R.id.setting){
                    Toast.makeText(context, title + ": 설정 정보를 확인합니다.", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.logout){
                    Toast.makeText(context, title + ": 로그아웃 시도중", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
                    intent.putExtra("name", strNickname);
                    intent.putExtra("profile", strProfile);
                    if(strEmail != null)
                        intent.putExtra("email", strEmail);
                    else
                        intent.putExtra("email", "none");
                    if(strGender != null)
                        intent.putExtra("gender", strGender);
                    else
                        intent.putExtra("gender", "none");

                    startActivity(intent);
                }

                return true;
            }
        });

//        키보드가 떳을때 툴바를 안보이도록설정
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom <= oldBottom){
                    toolbar.setVisibility(View.GONE);
                    Toast.makeText(context, bottom + " and" + oldBottom, Toast.LENGTH_SHORT).show();
                } else {
                    toolbar.setVisibility(View.VISIBLE);
                    Toast.makeText(context, bottom + " and" + oldBottom, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        adapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

//    json파일을 스트링으로 반환
    private String getJsonString()
    {
        String json = "";

        try {
            InputStream is = getAssets().open("Station.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return json;
    }



    private void jsonParsing(String json)
    {
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray stationArray = jsonObject.getJSONArray("stations");

            for(int i=0; i<stationArray.length(); i++)
            {
                JSONObject stationObject = stationArray.getJSONObject(i);

                StationConstruct station = new StationConstruct();

                station.setName(stationObject.getString("name"));
                station.setStnId(stationObject.getInt("stnId"));
                station.setLine(stationObject.getString("line"));
                station.setLatitude(stationObject.getDouble("latitude"));
                station.setLongitude(stationObject.getDouble("line"));

                stationItem.add(station);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}