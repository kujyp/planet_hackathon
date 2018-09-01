package com.planet.avocado.ui.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.planet.avocado.R;
import com.planet.avocado.managers.LoginManager;
import com.planet.avocado.ui.base.BaseActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private CompositeDisposable mDisposables = new CompositeDisposable();
    private View mLayoutGoogleLoginBtn;
    private View mLayoutFacebookLoginBtn;
    private SimpleDraweeView mDraweeLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initLogoAnimation();
        checkLoginStatus();
    }

    private void initLogoAnimation() {
        Log.d(TAG, "initLogoAnimation: ");

        Context ctx = this;
        Resources resources = ctx.getResources();
        int resId = R.drawable.avocado;
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId));
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        mDraweeLogo.setController(controller);
    }

    private void initViews() {
        Log.d(TAG, "initViews: ");

        mLayoutGoogleLoginBtn = findViewById(R.id.layout_googleLoginBtn);
        mLayoutFacebookLoginBtn = findViewById(R.id.layout_facebookLoginBtn);
        mDraweeLogo = findViewById(R.id.drawee_logo);

        mLayoutGoogleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplication(), "google", Toast.LENGTH_SHORT).show();
                gotoMainActivity();
            }
        });

        mLayoutFacebookLoginBtn.setOnClickListener(new View.OnClickListener() {
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

        mLayoutFacebookLoginBtn.setVisibility(View.VISIBLE);
        mLayoutGoogleLoginBtn.setVisibility(View.VISIBLE);
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
