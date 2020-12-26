package com.example.jeonse_alarm;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class LoginActivity2 extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Context context = this;
    String strNickname, strProfile, strEmail, strGender, strChannel, user_id, alarm;
    CheckBox alarm_checkbox;
    private static String IP = "3.34.189.107:80";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_2);
        TextView tvNickname = findViewById(R.id.tvNickname);
        ImageView ivProfile = findViewById(R.id.ivProfile);
        TextView tvEmail = findViewById(R.id.tvEmail);
//        TextView tvGender = findViewById(R.id.tvGender);
        Button btnLogout = findViewById(R.id.btnLogout);
        alarm_checkbox = findViewById(R.id.alarm);
        Intent intent = getIntent();
        alarm = intent.getStringExtra("alarm");
        user_id = intent.getStringExtra("user_id");
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");
        strEmail = intent.getStringExtra("email");
//        strGender = intent.getStringExtra("gender");
        strChannel = intent.getStringExtra("channel");

        tvNickname.setText(strNickname);
        tvEmail.setText(strEmail);
//        tvGender.setText(strGender);

        Glide.with(this).load(strProfile).into(ivProfile); //프로필 사진 url을 사진으로 보여줌
        btnLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
                if(strChannel.equals("kakao")){
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            Intent intent = new Intent(LoginActivity2.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                }else if(strChannel.equals("google")){
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(LoginActivity2.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        if(alarm.equals("1")){
            alarm_checkbox.setChecked(true);
        }

        alarm_checkbox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    String url ="";
                    url = "http://" + IP + "/req_alarm_configure.php?user_id="+ user_id +"&select=1";
                    reqDatabaseSelectedStn reqDatabaseSelectedStn = new reqDatabaseSelectedStn(url, null);
                    reqDatabaseSelectedStn.execute();
                    alarm_checkbox.setChecked(true);
                } else {
                    String url ="";
                    url = "http://" + IP + "/req_alarm_configure.php?user_id="+ user_id +"&select=0";
                    reqDatabaseSelectedStn reqDatabaseSelectedStn = new reqDatabaseSelectedStn(url, null);
                    reqDatabaseSelectedStn.execute();
                    alarm_checkbox.setChecked(false);
                }
            }
        }) ;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                    Toast.makeText(context, title + ": 계정 정보를 확인합니다.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, title + ": 로그아웃 시도중", Toast.LENGTH_SHORT).show();
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
}
