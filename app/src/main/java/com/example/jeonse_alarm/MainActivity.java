package com.example.jeonse_alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.util.OptionalBoolean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    ArrayList<SaleConstruct> saleItem = new ArrayList<>();

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    public static Context refrash_context;

    String strNickname, strProfile, strEmail, strGender, token, strChannel, user_id, alarm;
    RecyclerView recyclerView_sale;
    LinearLayout no_sale;
    LinearLayout no_station;
    FrameLayout frame_layout;
    SaleAdapter adapter_sale;
    ImageButton select_station_btn;
    String user_channel;
    String recent_user_view_sale_id;
    String old_first_sale_id = "";
    String new_first_sale_id = "";
    private static String IP = "3.34.189.107:80";
    SwipeRefreshLayout mSwipeRefreshLayout1;
    SwipeRefreshLayout mSwipeRefreshLayout2;
    private ProgressBar pgsBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refrash_context = this;
        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");
        strEmail = intent.getStringExtra("email");
        if (strEmail == null){
            strEmail="";
        }
//        strGender = intent.getStringExtra("gender");
        strChannel = intent.getStringExtra("channel");


        recyclerView_sale = (RecyclerView)findViewById(R.id.recylcerview_sale);
        no_sale= (LinearLayout)findViewById(R.id.no_sale);
        no_station= (LinearLayout)findViewById(R.id.no_station);

        mSwipeRefreshLayout1 = (SwipeRefreshLayout) findViewById(R.id.swipe_layout_1);
//        frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
        mSwipeRefreshLayout1.setOnRefreshListener(this);

        mSwipeRefreshLayout2 = (SwipeRefreshLayout) findViewById(R.id.swipe_layout_2);
//        frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
        mSwipeRefreshLayout2.setOnRefreshListener(this);
        pgsBar = (ProgressBar) findViewById(R.id.pBar);



        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()){
                            Log.w("FCM Log", "getInstanceId failed", task.getException());
                            return;
                        }
                        token = task.getResult().getToken();
                        Log.d("FCM LOg", "FCM 토큰 " + token);
                        ///////////////////////////
                        //json 파일들을 정리해 saleItem에 담는다.
                        ContentValues values = new ContentValues();
                        values.put("email", strEmail);
                        values.put("channel", strChannel);
                        values.put("profile_url", strProfile);
                        values.put("token_id", token);
                        String url_user = "http://" + IP + "/req_user.php";
                        selectDatabaseUser selectDatabaseUser = new selectDatabaseUser(url_user, values);
                        selectDatabaseUser.execute(); // AsyncTask는 .excute()로 실행된다.



//                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_name); //뒤로가기 버튼 이미지 지정

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        select_station_btn = (ImageButton) findViewById(R.id.select_station_btn);

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







        //json 파일들을 정리해 saleItem에 담는다.
        // String url = "http://" + IP + "/php파일명.php";
//        ContentValues values = new ContentValues();
//        values.put("id", 3);

//        Log.d("유저의 id", user_id);
//        String url = "http://" + IP + "/select_selected_station_sale_list.php?id=" + user_id;
//        selectDatabase selectDatabase = new selectDatabase(url, null);
//        selectDatabase.execute(); // AsyncTask는 .excute()로 실행된다.

//        navigationView.Lis


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
//                    intent.putExtra("channel", ch)
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
//        Toast.makeText(MainActivity.this, "닉네임: " + strNickname + "이메일: " + strEmail + "fcm토큰: " + token, Toast.LENGTH_SHORT).show();



    }
    @Override
    public void onRefresh() {
        refrashSelectedList();
        mSwipeRefreshLayout1.setRefreshing(false);
        mSwipeRefreshLayout2.setRefreshing(false);

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


//SALE 목록에 대한 요청 결과를 파싱함
   class selectDatabaseSale extends AsyncTask<Void, Void, String> {

        private String url1;
        private ContentValues values1;
        String result1;
        // 요청 결과를 저장할 변수.

        public selectDatabaseSale(String url, ContentValues contentValues) {
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
//            Log.d("매물 내역", s);
            pgsBar.setVisibility(View.VISIBLE);

            try{
//            JSONObject jsonObject = new JSONObject(json);
//            Log.d("최종 매물 확인", String.valueOf(jsonObject));
//            JSONArray stationArray = jsonObject.getJSONArray("datas");
                JSONArray stationArray = new JSONArray(s);
                for(int i=0; i<stationArray.length(); i++)
                {
                    JSONObject stationObject = stationArray.getJSONObject(i);

                    SaleConstruct sale = new SaleConstruct();

                    sale.setId(stationObject.getString("id"));
                    sale.setName(stationObject.getString("name"));
                    sale.setNaverId(stationObject.getString("naver_id"));
                    sale.setPrice(stationObject.getString("price"));
                    sale.setImgLink(stationObject.getString("img_link"));
                    sale.setNaverLink(stationObject.getString("naver_link"));
                    sale.setType(stationObject.getString("type"));
                    sale.setStation(stationObject.getString("kor_stn_name"));
                    sale.setCreateDt(stationObject.getString("create_dt"));
                    if (stationObject.getString("naver_id").equals(recent_user_view_sale_id) ){
                        sale.setRecentSale("true");
                        Log.d("setRecentSale 값은 ", "true");
                    }else{
                        sale.setRecentSale("false");
                        Log.d("setRecentSale 값은 ", "false");
                    }

                    saleItem.add(sale);

//                Log.d("어댑터에 넣기전, 데이터 보기 ", String.valueOf(saleItem));
                    Log.d("saleItem 값 추가 ", "매물 명: " + sale.getName());
                }
                if(saleItem.size()==0){
                    no_station.setVisibility(View.GONE);
                    recyclerView_sale.setVisibility(View.GONE);
                    no_sale.setVisibility(View.VISIBLE);
//                    no_station.setClickable(false);
//                    no_sale.setClickable(true);
//                    recyclerView_sale.setClickable(false);
                }else{
                    no_station.setVisibility(View.GONE);
                    recyclerView_sale.setVisibility(View.VISIBLE);
                    no_sale.setVisibility(View.GONE);


                    adapter_sale = new SaleAdapter(getApplicationContext(), saleItem);
                    recyclerView_sale.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView_sale.setAdapter(adapter_sale);

                    Log.d("실제 true false 값을 비교합니다.", String.valueOf(!saleItem.get(0).getNaverId().equals(recent_user_view_sale_id)));
                    Log.d("최신매물의 id를 보셨으므로 db에 넣습니다  ", saleItem.get(0).getNaverId()+ " " + recent_user_view_sale_id);
//                    no_station.setVisibility(View.GONE);
//                    recyclerView_sale.setVisibility(View.VISIBLE);
//                    no_sale.setVisibility(View.GONE);

//                    no_station.setClickable(false);
//                    no_sale.setClickable(false);
//                    recyclerView_sale.setClickable(true);
                    if(!saleItem.get(0).getNaverId().equals(recent_user_view_sale_id)){
                        ContentValues values = new ContentValues();
                        values.put("email", strEmail);
                        values.put("channel", strChannel);
                        values.put("profile_url", strProfile);
                        values.put("token_id", token);
                        values.put("rec_sale_id", saleItem.get(0).getNaverId());
                        Log.d("최신매물의 id를 보셨으므로 db에 넣습니다  ", saleItem.get(0).getNaverId());
                        String url_user = "http://" + IP + "/req_user.php";
                        DatabaseUserRecSale DatabaseUserRecSale = new DatabaseUserRecSale(url_user, values);
                        DatabaseUserRecSale.execute();
                    }
                    Log.d("첫 매물의 id ", saleItem.get(0).getNaverId());
                }
                pgsBar.setVisibility(View.GONE);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //USER 정보에 대한 요청 결과를 파싱함
    class selectDatabaseUser extends AsyncTask<Void, Void, String> {

        private String url1;
        private ContentValues values1;
        String result1;
        // 요청 결과를 저장할 변수.

        public selectDatabaseUser(String url, ContentValues contentValues) {
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
            pgsBar.setVisibility(View.VISIBLE);

            try{
                JSONObject jsonObject = new JSONObject(s);

                JSONArray stationArray = jsonObject.getJSONArray("datas");

                for(int i=0; i<stationArray.length(); i++)
                {
                    JSONObject stationObject = stationArray.getJSONObject(i);
                    user_id = stationObject.getString("id");
                    recent_user_view_sale_id =stationObject.getString("rec_sale_id");
                    alarm =stationObject.getString("alarm");
                    Log.d("유저 id 는", user_id);
                }

                Log.d("유저의 id", user_id);

                String url_stn_count = "http://" + IP + "/select_selected_station_count.php?id=" + user_id;
                selectDatabaseStnCnt selectDatabaseStnCnt = new selectDatabaseStnCnt(url_stn_count, null);
                selectDatabaseStnCnt.execute(); // AsyncTask는 .excute()로 실행된다.



            }catch (JSONException e) {
                e.printStackTrace();
            }
            pgsBar.setVisibility(View.GONE);



        }
    }





//    //관심역의 갯수를 불러오는 쿼리
//    class DatabaseUserRecSale extends AsyncTask<Void, Void, String> {
//
//        private String url1;
//        private ContentValues values1;
//        String result1;
//        // 요청 결과를 저장할 변수.
//
//        public DatabaseUserRecSale(String url, ContentValues contentValues) {
//            this.url1 = url;
//            this.values1 = contentValues;
//            Log.d("요청 주소", url1 + "    " + values1);
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
//            result1 = requestHttpURLConnection.request(url1, values1); // 해당 URL로 부터 결과물을 얻어온다.
//            return result1; // 여기서 당장 실행 X, onPostExcute에서 실행
//        }


    //최근 봤던 매물을 등록하는 변수
    class DatabaseUserRecSale extends AsyncTask<Void, Void, String> {

        private String url1;
        private ContentValues values1;
        String result1;
        // 요청 결과를 저장할 변수.

        public DatabaseUserRecSale(String url, ContentValues contentValues) {
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



















    //관심역의 갯수를 불러오는 쿼리
    class selectDatabaseStnCnt extends AsyncTask<Void, Void, String> {

        private String url1;
        private ContentValues values1;
        String result1;
        // 요청 결과를 저장할 변수.


        public selectDatabaseStnCnt(String url, ContentValues contentValues) {
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
            pgsBar.setVisibility(View.VISIBLE);
            //txtView.setText(s); // 파서 역없이 전체 출력
            String count = new String(s);
            Log.d("유저가 저장한 관심 역 갯수", count);
            if(s.equals("0")){
                no_station.setVisibility(View.VISIBLE);
                no_sale.setVisibility(View.GONE);
                recyclerView_sale.setVisibility(View.GONE);

//                frame_layout.setClickable(false);
//                no_station.setClickable(false);
//
//                no_sale.setClickable(false);
//
//                recyclerView_sale.setClickable(false);
//
//                select_station_btn.setClickable(true);

                select_station_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                });

//                View.OnClickListener clickListener = new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        switch (v.getId()){
//                            case R.id.no_station :
//                                Log.d("log01","click layout01");
//                                break;
//                        }
//                    }
//                };

            }else{
                String url_sale = "http://" + IP + "/select_selected_station_sale_list.php?id=" + user_id;
                selectDatabaseSale selectDatabaseSale = new selectDatabaseSale(url_sale, null);
                selectDatabaseSale.execute(); // AsyncTask는 .excute()로 실행된다.
            }
            pgsBar.setVisibility(View.GONE);
        }
    }

    public void refrashSelectedList(){
//        refrash_context = this;
        saleItem.clear();

//        seletedStationItemRenew.clear();
        adapter_sale = new SaleAdapter(getApplicationContext(), saleItem);
        recyclerView_sale.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView_sale.setAdapter(adapter_sale);

//        only_last=true;
//        String url_stn_count = "http://" + IP + "/req_selected_stn_list.php?id=" + user_id;
//        SelectStationActivity.selectDatabaseSelectedStn selectDatabaseSelectedStn = new SelectStationActivity.selectDatabaseSelectedStn(url_stn_count, null);
//        selectDatabaseSelectedStn.execute(); // AsyncTask는 .excute()로 실행된다.


        ContentValues values = new ContentValues();
        values.put("email", strEmail);
        values.put("channel", strChannel);
        values.put("profile_url", strProfile);
        values.put("token_id", token);
        String url_user = "http://" + IP + "/req_user.php";
        selectDatabaseUser selectDatabaseUser = new selectDatabaseUser(url_user, values);
        selectDatabaseUser.execute(); // AsyncTask는 .excute()로 실행된다.
//        String url_stn_count = "http://" + IP + "/select_selected_station_count.php?id=" + user_id;
//        selectDatabaseStnCnt selectDatabaseStnCnt = new selectDatabaseStnCnt(url_stn_count, null);
//        selectDatabaseStnCnt.execute(); // AsyncTask는 .excute()로 실행된다.
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refrashSelectedList();
    }
}