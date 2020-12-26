package com.example.jeonse_alarm;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.signature.EmptySignature;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;

public class SelectStationActivity extends AppCompatActivity implements TextWatcher, StationListAdapter.OnListItemSelectedInterface, SelectedStationListRenewAdapter.OnItemClick {
    Toolbar toolbar;
    RecyclerView recyclerView_stn;
    RecyclerView recyclerView_selected;
    EditText editSearch;
    StationListAdapter adapter_stn;
//    SelectedStationListAdapter adapter_selected;
    SelectedStationListRenewAdapter adapter_selected_renew;
    ArrayList<StationConstruct> stationItem = new ArrayList<>();
//    ArrayList<SelectedStationConstruct> seletedStationItem = new ArrayList<>();
    ArrayList<SelectedStationConstructRenew> seletedStationItemRenew = new ArrayList<>();
    private static String IP = "3.34.189.107:80";
//    Boolean only_last=false; //selected station 어댑터의 원소를 가져올때, 마지막 것만 가져올지, 전부 갖올지 확인하는 역


    private ArrayList<String> arraylist;

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    String strNickname, strProfile, strEmail, strGender, token, user_id, alarm, strChannel;
    private ProgressBar pgsBar;
    //키보다 올리기 내리기 제어



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);
//        editSearch = (EditText) findViewById(R.id.editSearch);
//        listView = (ListView) findViewById(R.id.listView);

        recyclerView_stn = (RecyclerView)findViewById(R.id.recylcerview_station);
        recyclerView_selected = (RecyclerView)findViewById(R.id.recylcerview_selected);
        editSearch = (EditText)findViewById(R.id.editSearch);
        editSearch.addTextChangedListener((TextWatcher) this);
        pgsBar = (ProgressBar) findViewById(R.id.pBar);



        Intent intent = getIntent();
        strChannel = intent.getStringExtra("channel");
        alarm = intent.getStringExtra("alarm");
        user_id = intent.getStringExtra("user_id");
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");
        strEmail = intent.getStringExtra("email");
//        strGender = intent.getStringExtra("gender");

        jsonParsingStn(getJsonStringStn());
//        jsonParsingSelected(getJsonStringSelected());
//        jsonParsingSeletedRenew(getJsonStringSelected());

        String url_stn_count = "http://" + IP + "/req_selected_stn_list.php?id=" + user_id;
        selectDatabaseSelectedStn selectDatabaseSelectedStn = new selectDatabaseSelectedStn(url_stn_count, null);
        selectDatabaseSelectedStn.execute(); // AsyncTask는 .excute()로 실행된다.



        adapter_stn = new StationListAdapter(getApplicationContext(), stationItem, this);
        recyclerView_stn.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView_stn.setAdapter(adapter_stn);

//        adapter_selected = new SelectedStationListAdapter(getApplicationContext(), seletedStationItem);
//        recyclerView_selected.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//        recyclerView_selected.setAdapter(adapter_selected);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_name); //뒤로가기 버튼 이미지 지정

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        ImageButton header_image= (ImageButton) header.findViewById(R.id.imageView);

        header_image.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("alarm", alarm);
                intent.putExtra("user_id", user_id);
                intent.putExtra("name", strNickname);
                intent.putExtra("profile", strProfile);
                intent.putExtra("channel", strChannel);
                if(strEmail != null)
                    intent.putExtra("email", strEmail);
                else
                    intent.putExtra("email", "none");
//                if(strGender != null)
//                    intent.putExtra("gender", strGender);
//                else
//                    intent.putExtra("gender", "none");
                startActivity(intent);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.account){
//                    Toast.makeText(context, title + ": 계정 정보를 확인합니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), SelectStationActivity.class);
                    intent.putExtra("alarm", alarm);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("name", strNickname);
                    intent.putExtra("profile", strProfile);
                    intent.putExtra("channel", strChannel);
                    if(strEmail != null)
                        intent.putExtra("email", strEmail);
                    else
                        intent.putExtra("email", "none");
//                    if(strGender != null)
//                        intent.putExtra("gender", strGender);
//                    else
//                        intent.putExtra("gender", "none");
                    startActivity(intent);
                }
                else if(id == R.id.setting){
//                    Toast.makeText(context, title + ": 설정 정보를 확인합니다.", Toast.LENGTH_SHORT).show();
                    Intent msg = new Intent(Intent.ACTION_SEND);
                    msg.addCategory(Intent.CATEGORY_DEFAULT);
                    msg.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.example.jeonse_alarm");
                    msg.putExtra(Intent.EXTRA_TITLE, "제목");
                    msg.setType("text/plain");
                    startActivity(Intent.createChooser(msg, "앱을 선택해 주세요"));
                }
                else if(id == R.id.logout){
//                    Toast.makeText(context, title + ": 로그아웃 시도중", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
                    intent.putExtra("alarm", alarm);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("name", strNickname);
                    intent.putExtra("profile", strProfile);
                    intent.putExtra("channel", strChannel);
                    if(strEmail != null)
                        intent.putExtra("email", strEmail);
                    else
                        intent.putExtra("email", "none");
//                    if(strGender != null)
//                        intent.putExtra("gender", strGender);
//                    else
//                        intent.putExtra("gender", "none");

                    startActivity(intent);
                }

                return true;
            }
        });





        //        키보드가 떳을때 툴바를 안보이도록설정
        recyclerView_selected.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom){
                    toolbar.setVisibility(View.GONE);
                    recyclerView_stn.setVisibility(View.VISIBLE);
                    editSearch.setText(null);
                    adapter_stn.notifyDataSetChanged();
                    Log.d("키보드가 올라와 있습니다.", String.valueOf(bottom) + "  "+ String.valueOf(oldBottom));
                    Log.d("키보드가 올라와 있습니다.", "왜 키보드가 올라와 있어...");


                } else {
                    toolbar.setVisibility(View.VISIBLE);
                    recyclerView_stn.setVisibility(View.GONE);
                }
            }
        });

//        recyclerView_stn.setOnClickListener();


    }
//내가 선택한 역의 타입 버튼을 눌렀을때 일어나는 이벤트
    @Override
    public void onClick (View v, Integer position, String type){
        SelectedStationListRenewAdapter.MyViewHolder viewHolder = (SelectedStationListRenewAdapter.MyViewHolder)recyclerView_selected.findViewHolderForAdapterPosition(position);
//        Log.d("역의 ID는 ", viewHolder.textViewStnId.getText().toString());
//        Toast.makeText(this, viewHolder.textViewStnName.getText().toString() +" "+viewHolder.textViewStnId.getText().toString()+ type, Toast.LENGTH_SHORT).show();
        String url ="";
        url = "http://" + IP + "/delete_selected_sale_list.php?user_id="+ user_id +"&station_id="+viewHolder.textViewStnId.getText().toString()+"&type="+type;
        reqDatabaseSelectedStn reqDatabaseSelectedStn = new reqDatabaseSelectedStn(url, null);
        reqDatabaseSelectedStn.execute();

        if(viewHolder.LayOutOpst.getVisibility()==View.GONE && viewHolder.LayOutApt.getVisibility()==View.GONE && viewHolder.LayOutOr.getVisibility()==View.GONE){
            refrashSelectedList();
        }

    }

    //stn List의 아이템을 눌렀을시 여기서 이벤트를 받을 수 있다.
    @Override
    public void onItemSelected(View v, int position) {
        StationListAdapter.MyViewHolder viewHolder = (StationListAdapter.MyViewHolder)recyclerView_stn.findViewHolderForAdapterPosition(position);

//        http://3.34.189.107/req_selected_apt.php?user_id=17&station_id=468&kor_stn_name=정자역
        String[] url_type = new String[3];
        url_type[0]="opst";
        url_type[1]="apt";
        url_type[2]="or";


//        Toast.makeText(this, viewHolder.textViewName.getText().toString(), Toast.LENGTH_SHORT).show();
        toolbar.setVisibility(View.VISIBLE);
        recyclerView_stn.setVisibility(View.GONE);
        pgsBar.setVisibility(View.VISIBLE);
        editSearch.setFocusable(false);
        editSearch.setClickable(false);

        for(int i = 0; i<3; i++){
            String url ="";
            url = "http://" + IP + "/req_selected_"+url_type[i]+".php?user_id="+user_id+"&station_id="+viewHolder.idTextView.getText().toString()+"&kor_stn_name="+viewHolder.textViewName.getText().toString();
            reqDatabaseSelectedStn reqDatabaseSelectedStn = new reqDatabaseSelectedStn(url, null);
            reqDatabaseSelectedStn.execute(); // AsyncTask는 .excute()로 실행된다.
        }

        editSearch.setText("");
        hideKeyboard();
        //역을 추가 했으니 목록을 새로 불러옴
        refrashSelectedList();

//        adapter_selected_renew.notifyDataSetChanged();
//        synchronized (recyclerView_selected) {
//            Toast.makeText(this, viewHolder.textViewName.getText().toString() +"을 신크로중", Toast.LENGTH_SHORT).show();
//            recyclerView_selected.notify();
//        }
//        pgsBar.setVisibility(View.GONE);


//        recyclerView_selected.notifyAll();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        adapter_stn.getFilter().filter(charSequence);
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

//    json파일을 스트링으로 반환 --> 관심지역 선택////////////////
    private String getJsonStringStn()
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



    private void jsonParsingStn(String json)
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
                station.setLongitude(stationObject.getDouble("longitude"));

                stationItem.add(station);
//                Log.d("stationItem 값 추가 ", "역 이름" + station.getName());
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

///////////////////////////////////////////////////////////////////



////    이미 선택한 관심지역
//    private String getJsonStringSelected()
//    {
//        String json = "";
//
//        try {
//            InputStream is = getAssets().open("SelectedStation.json");
//            int fileSize = is.available();
//
//            byte[] buffer = new byte[fileSize];
//            is.read(buffer);
//            is.close();
//
//            json = new String(buffer, "UTF-8");
//        }
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//        }
//
//        return json;
//    }




//
//    private void jsonParsingSeletedRenew(String json){
//        try{
//            JSONObject jsonObject = new JSONObject(json);
//
//            JSONArray stationArray = jsonObject.getJSONArray("selected_stations");
////            List<String> temp_list = new ArrayList<String>();
//            String[] temp_list = new String[stationArray.length()];
//
//            for(int i=0; i<stationArray.length(); i++)
//            {
//
//                JSONObject stationObject = stationArray.getJSONObject(i);
//                SelectedStationConstructRenew station = new SelectedStationConstructRenew();
//
//                if(!Arrays.asList(temp_list).contains(stationObject.getString("stn_name"))) {
//                    station.setStnName(stationObject.getString("stn_name"));
//                    station.setStnName(stationObject.getString("kor_stn_name"));
//                    station.setOpst_check(false);
//                    station.setApt_check(false);
//                    station.setOr_check(false);
//                    temp_list[i] = stationObject.getString("kor_stn_name");
//                    seletedStationItemRenew.add(station);
//                    Log.d("temp_list에 값추가", temp_list[i]);
//                }else{
//                    Log.d("temp_list에 값추가 안함", stationObject.getString("kor_stn_name"));
//                }
//
//            }
//
//            for(int i=0; i<stationArray.length(); i++)
//            {
//                JSONObject stationObject = stationArray.getJSONObject(i);
//
//                SelectedStationConstructRenew station = new SelectedStationConstructRenew();
//
//                for(int j=0; j<seletedStationItemRenew.size(); j++){
//                    if (seletedStationItemRenew.get(j).getStnName().equals(stationObject.getString("kor_stn_name"))){
//                        if(stationObject.getString("type").equals("OPST")){
//                            SelectedStationConstructRenew station_renew = new SelectedStationConstructRenew();
//                            station_renew.setStnName(seletedStationItemRenew.get(j).getStnName());
//                            station_renew.setOpst_check(true);
//                            station_renew.setApt_check(seletedStationItemRenew.get(j).getApt_check());
//                            station_renew.setOr_check(seletedStationItemRenew.get(j).getOr_check());
//                            seletedStationItemRenew.set(j, station_renew);
//                        }
//                        else if(stationObject.getString("type").equals("APT")){
//                            SelectedStationConstructRenew station_renew = new SelectedStationConstructRenew();
//                            station_renew.setStnName(seletedStationItemRenew.get(j).getStnName());
//                            station_renew.setOpst_check(seletedStationItemRenew.get(j).getOpst_check());
//                            station_renew.setApt_check(true);
//                            station_renew.setOr_check(seletedStationItemRenew.get(j).getOr_check());
//                            seletedStationItemRenew.set(j, station_renew);
//                        }
//                        else if(stationObject.getString("type").equals("OR")){
//                            SelectedStationConstructRenew station_renew = new SelectedStationConstructRenew();
//                            station_renew.setStnName(seletedStationItemRenew.get(j).getStnName());
//                            station_renew.setOpst_check(seletedStationItemRenew.get(j).getOpst_check());
//                            station_renew.setApt_check(seletedStationItemRenew.get(j).getApt_check());
//                            station_renew.setOr_check(true);
//                            seletedStationItemRenew.set(j, station_renew);
//                        }
//                    }
//                }
//            }
//
//            Log.d("사이즈  ", String.valueOf(seletedStationItemRenew.size()));
//
//            for(SelectedStationConstructRenew data : seletedStationItemRenew){
//                Log.d("새로운 배열의 내용", data.getStnName()+" "+data.getOpst_check()+data.getApt_check()+data.getOr_check());
//            }
//
//
//        }catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }















    //Selected station 목록에 대한 요청 결과를 파싱함
    class selectDatabaseSelectedStn extends AsyncTask<Void, Void, String> {

        private String url1;
        private ContentValues values1;
        String result1;
        // 요청 결과를 저장할 변수.

        public selectDatabaseSelectedStn(String url, ContentValues contentValues) {
            this.url1 = url;
            this.values1 = contentValues;
            Log.d("요청 주소", url1 + "    " + values1);
        }

        @Override
        protected String doInBackground(Void... params) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result1 = requestHttpURLConnection.request(url1, values1); // 해당 URL로 부터 결과물을 얻어온다.
            return result1; // 여기서 당장 실행 X, onPostExcute에서 실행
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Integer check_length;
            //txtView.setText(s); // 파서 역없이 전체 출력
            Log.d("웹과 통신 결과", s);
//            Log.d("매물 내역", s);
            try{
//                JSONObject jsonObject = new JSONObject(s);
//
//                JSONArray stationArray = jsonObject.getJSONArray("selected_stations");
////            List<String> temp_list = new ArrayList<String>();
                JSONArray stationArray = new JSONArray(s);
                String[] temp_list = new String[stationArray.length()];

//                if(only_last){
//                    check_length=3;
//                }else{
//                    check_length=stationArray.length();
//                }

                for(int i=0; i<stationArray.length(); i++)
                {

                    JSONObject stationObject = stationArray.getJSONObject(i);
                    SelectedStationConstructRenew station = new SelectedStationConstructRenew();

                    if(!Arrays.asList(temp_list).contains(stationObject.getString("kor_stn_name"))) {
//                        station.setStnName(stationObject.getString("stn_name"));
                        station.setStnName(stationObject.getString("kor_stn_name"));
                        station.setStn_id(stationObject.getString("station_id"));
                        station.setOpst_check(false);
                        station.setApt_check(false);
                        station.setOr_check(false);
                        temp_list[i] = stationObject.getString("kor_stn_name");
                        seletedStationItemRenew.add(station);
                        Log.d("temp_list에 값추가", station.getStn_id());
                        Log.d("temp_list에 값추가", temp_list[i]);
                    }else{
                        Log.d("temp_list에 값추가 안함", stationObject.getString("kor_stn_name"));
                    }

                }

                for(int i=0; i<stationArray.length(); i++)
                {
                    JSONObject stationObject = stationArray.getJSONObject(i);

                    SelectedStationConstructRenew station = new SelectedStationConstructRenew();

                    for(int j=0; j<seletedStationItemRenew.size(); j++){
                        if (seletedStationItemRenew.get(j).getStnName().equals(stationObject.getString("kor_stn_name"))){
                            if(stationObject.getString("type").equals("OPST")){
                                SelectedStationConstructRenew station_renew = new SelectedStationConstructRenew();
                                station_renew.setStnName(seletedStationItemRenew.get(j).getStnName());
                                station_renew.setStn_id(seletedStationItemRenew.get(j).getStn_id());
                                station_renew.setOpst_check(true);
                                station_renew.setApt_check(seletedStationItemRenew.get(j).getApt_check());
                                station_renew.setOr_check(seletedStationItemRenew.get(j).getOr_check());
                                seletedStationItemRenew.set(j, station_renew);
                            }
                            else if(stationObject.getString("type").equals("APT")){
                                SelectedStationConstructRenew station_renew = new SelectedStationConstructRenew();
                                station_renew.setStnName(seletedStationItemRenew.get(j).getStnName());
                                station_renew.setStn_id(seletedStationItemRenew.get(j).getStn_id());
                                station_renew.setOpst_check(seletedStationItemRenew.get(j).getOpst_check());
                                station_renew.setApt_check(true);
                                station_renew.setOr_check(seletedStationItemRenew.get(j).getOr_check());
                                seletedStationItemRenew.set(j, station_renew);
                            }
                            else if(stationObject.getString("type").equals("OR")){
                                SelectedStationConstructRenew station_renew = new SelectedStationConstructRenew();
                                station_renew.setStnName(seletedStationItemRenew.get(j).getStnName());
                                station_renew.setStn_id(seletedStationItemRenew.get(j).getStn_id());
                                station_renew.setOpst_check(seletedStationItemRenew.get(j).getOpst_check());
                                station_renew.setApt_check(seletedStationItemRenew.get(j).getApt_check());
                                station_renew.setOr_check(true);
                                seletedStationItemRenew.set(j, station_renew);
                            }
                        }
                    }
                }

                Log.d("사이즈  ", String.valueOf(seletedStationItemRenew.size()));

                adapter_selected_renew = new SelectedStationListRenewAdapter(getApplicationContext(), seletedStationItemRenew, SelectStationActivity.this::onClick);
                recyclerView_selected.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView_selected.setAdapter(adapter_selected_renew);
                pgsBar.setVisibility(View.GONE);
//                editSearch.setFocusable(true);
//                editSearch.setClickable(true);
                Log.d("psg 바의 상태는?", String.valueOf(pgsBar.getVisibility()));
                Log.d("기존 edit search의 상태는?", String.valueOf(editSearch.getFocusable()));
                editSearch.setFocusable(true);
                editSearch.setFocusableInTouchMode(true);
//                editSearch.setText("");
                Log.d("새 edit search의 상태는?", String.valueOf(editSearch.getFocusable()));


//                recyclerView_selected.onChildAttachedToWindow(this);

//                ItemClickS

                for(SelectedStationConstructRenew data : seletedStationItemRenew){
                    Log.d("새로운 배열의 내용", data.getStnName()+" "+data.getOpst_check()+data.getApt_check()+data.getOr_check());
                }


            }catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

//    public void startSelectedAdapter(){
//        adapter_selected_renew = new SelectedStationListRenewAdapter(getApplicationContext(), seletedStationItemRenew);
//        recyclerView_selected.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//        recyclerView_selected.setAdapter(adapter_selected_renew);
//    }

    class reqDatabaseSelectedStn extends AsyncTask<Void, Void, String> {

        private String url1;
        private ContentValues values1;
        String result1;
        // 요청 결과를 저장할 변수.

        public reqDatabaseSelectedStn(String url, ContentValues contentValues) {
            this.url1 = url;
            this.values1 = contentValues;
            Log.d("요청 주소", url1 + "    " + values1);
        }

        @Override
        protected String doInBackground(Void... params) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result1 = requestHttpURLConnection.request(url1, values1); // 해당 URL로 부터 결과물을 얻어온다.
            return result1; // 여기서 당장 실행 X, onPostExcute에서 실행
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //txtView.setText(s); // 파서 역없이 전체 출력
            Log.d("웹과 통신 결과", s);
        }
    }
















//
//    private void jsonParsingSelected(String json)
//    {
//        try{
//            JSONObject jsonObject = new JSONObject(json);
//
//            JSONArray stationArray = jsonObject.getJSONArray("selected_stations");
//
//            for(int i=0; i<stationArray.length(); i++)
//            {
//                JSONObject stationObject = stationArray.getJSONObject(i);
//
//                SelectedStationConstruct station = new SelectedStationConstruct();
//
//                station.setStnName(stationObject.getString("stn_name"));
//                station.setStnLine(stationObject.getString("stn_line"));
//                station.setType(stationObject.getString("type"));
//
//                seletedStationItem.add(station);
//
//                Log.d("selected 목록", "역 이름" + station.getStnName());
//            }
//        }catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
    }

    public void refrashSelectedList(){
//        pgsBar.setVisibility(View.VISIBLE);
        seletedStationItemRenew.clear();
        adapter_selected_renew = new SelectedStationListRenewAdapter(getApplicationContext(), seletedStationItemRenew, this);
        recyclerView_selected.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView_selected.setAdapter(adapter_selected_renew);

//        only_last=true;
        String url_stn_count = "http://" + IP + "/req_selected_stn_list.php?id=" + user_id;
        selectDatabaseSelectedStn selectDatabaseSelectedStn = new selectDatabaseSelectedStn(url_stn_count, null);
        selectDatabaseSelectedStn.execute(); // AsyncTask는 .excute()로 실행된다.

    }




}