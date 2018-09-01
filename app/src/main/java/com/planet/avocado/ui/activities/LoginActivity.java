package com.planet.avocado.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.planet.avocado.R;
import com.planet.avocado.managers.LoginManager;
import com.planet.avocado.ui.base.BaseActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private CompositeDisposable mDisposables = new CompositeDisposable();
    private Button mBtnGoogle;
    private Button mBtnFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initButtons();
        checkLoginStatus();
    }

    private void initButtons() {
        Log.d(TAG, "initButtons: ");

        mBtnGoogle = findViewById(R.id.btn_googleLogin);
        mBtnFacebook = findViewById(R.id.btn_facebookLogin);

        mBtnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplication(), "google", Toast.LENGTH_SHORT).show();
                gotoMainActivity();
            }
        });

        mBtnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplication(), "facebook", Toast.LENGTH_SHORT).show();
                gotoMainActivity();
            }
        });
    }

    private void checkLoginStatus() {
        Log.d(TAG, "checkLoginStatus: ");

        mDisposables.add(
                LoginManager.getInstance()
                        .isLoggedIn()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isLoggedIn -> {
                            if (isLoggedIn) {
                                gotoMainActivity();
                            } else {
                                showLoginButtons();
                            }
                        })
        );
    }

    private void showLoginButtons() {
        Log.d(TAG, "showLoginButtons: ");

        mBtnFacebook.setVisibility(View.VISIBLE);
        mBtnGoogle.setVisibility(View.VISIBLE);
    }

    private void gotoMainActivity() {
        Log.d(TAG, "gotoMainActivity: ");

        ContextCompat.startActivity(this,
                new Intent(this, MainActivity.class),
                null);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

        mDisposables.dispose();
    }
}
