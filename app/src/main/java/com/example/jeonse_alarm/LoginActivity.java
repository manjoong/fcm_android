package com.example.jeonse_alarm;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

import com.kakao.usermgmt.LoginButton;

import java.security.MessageDigest;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback sessionCallback;
    private Context mContext;
    private Button btn_custom_login;
    private Button btn_custom_google_login;
    private SignInButton signInButton;
    private LoginButton btn_kakao_login;
    private static String IP = "3.34.189.107:80";

    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mContext = getApplicationContext();

        //구글 로그인 실행을 위한 코드
        signInButton = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            Log.d("그냥 넘어옴", mAuth.getCurrentUser().getDisplayName() + mAuth.getCurrentUser().getEmail() + mAuth.getCurrentUser().getPhotoUrl());

            intent.putExtra("channel", "google");
            intent.putExtra("name", mAuth.getCurrentUser().getDisplayName());
            intent.putExtra("profile", mAuth.getCurrentUser().getPhotoUrl().toString());
            if(mAuth.getCurrentUser().getEmail().length() !=0)
                intent.putExtra("email", mAuth.getCurrentUser().getEmail());
            else
                intent.putExtra("email", "none");
//            intent.putExtra("gender", "none");

            startActivity(intent);
            finish();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signIn();
//            }
//        });
        btn_custom_google_login =(Button) findViewById(R.id.btn_custom_google_login);
        btn_custom_google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
//                signInButton.performClick();
            }
        });


//        카카오 로그인을 대리실행하는 코드
        btn_custom_login = (Button) findViewById(R.id.btn_custom_login);
        btn_custom_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_kakao_login.performClick();
            }
        });
        btn_kakao_login = (LoginButton) findViewById(R.id.btn_kakao_login);

        //////


        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
        getAppKeyHash();
    }

    /////////////////////////////////구글 로그인//////////////////////////
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("리퀘스트 코드", String.valueOf(requestCode));
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Snackbar.make(findViewById(R.id.layout_main), "Authentication Successed.", Snackbar.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("유저 정보", user.getDisplayName() + user.getEmail()+ user.getPhotoUrl());
                            updateUI(user);
                        } else {
                            Snackbar.make(findViewById(R.id.layout_main), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            Log.d("계정값 출력", user.getDisplayName()+" "+user.getPhotoUrl()+" "+user.getEmail());
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("channel", "google");
            intent.putExtra("name", user.getDisplayName());
            intent.putExtra("profile", user.getPhotoUrl().toString());
            if(user.getEmail().length() !=0)
                intent.putExtra("email", user.getEmail());
            else
                intent.putExtra("email", "none");
//            intent.putExtra("gender", "none");
            startActivity(intent);
            finish();
        }
    }
    /////////////////////////////////구글 로그인//////////////////////////




    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("channel", "kakao");
                    intent.putExtra("name", result.getNickname());
                    intent.putExtra("profile", result.getProfileImagePath());
                    if(result.getKakaoAccount().hasEmail() == OptionalBoolean.TRUE){
                        intent.putExtra("email", result.getKakaoAccount().getEmail());
//                        Log.d("이메일은 ", result.getKakaoAccount().getEmail());
                    }
                    else{
                        intent.putExtra("email", "none");
//                        Log.d("이메일은 ", result.getKakaoAccount().getEmail());
                    }



//                    if(result.getKakaoAccount().hasGender() == OptionalBoolean.TRUE)
//                        intent.putExtra("gender", result.getKakaoAccount().getGender().getValue());
//                    else
//                        intent.putExtra("gender", "none");


                    Toast.makeText(getApplicationContext(),"안녕하세요. "+result.getNickname() + "님!",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

}
