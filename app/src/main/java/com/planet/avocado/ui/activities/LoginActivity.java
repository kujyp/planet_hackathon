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
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
    private CallbackManager mFacebookCallbackManager;
    private LoginButton mFacebookLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initFacebookLogin();
        initLogoAnimation();
        checkLoginStatus();
    }

    private void initFacebookLogin() {
        Log.d(TAG, "initFacebookLogin: ");

        mFacebookLoginBtn = new LoginButton(this);
        mFacebookLoginBtn.setVisibility(View.GONE);
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginBtn.setReadPermissions("email", "public_profile");
        mFacebookLoginBtn.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                onLoginSuccess();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                onLoginFailure();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                onLoginFailure();
            }
        });

        ViewGroup layout = findViewById(R.id.layout_login);
        layout.addView(mFacebookLoginBtn);
    }

    private void onLoginFailure() {
        Log.d(TAG, "onLoginFailure: ");
        Toast.makeText(this, "Login Failure", Toast.LENGTH_SHORT).show();
    }

// ...

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
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
            }
        });

        mLayoutFacebookLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFacebookLoginBtn != null) {
                    mFacebookLoginBtn.performClick();
                }
            }
        });
    }

    private void onLoginSuccess() {
        Log.d(TAG, "onLoginSuccess: ");
        gotoMainActivity();
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
        TranslateAnimation anim = new TranslateAnimation(0,
                0,
                200,
                0);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        mLayoutFacebookLoginBtn.startAnimation(anim);
        mLayoutGoogleLoginBtn.startAnimation(anim);
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
