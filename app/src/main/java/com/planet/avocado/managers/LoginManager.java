package com.planet.avocado.managers;


import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.subjects.SingleSubject;

public class LoginManager {
    private static LoginManager sInstance = null;

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        if (sInstance == null) {
            synchronized (LoginManager.class) {
                if (sInstance == null) {
                    sInstance = new LoginManager();
                }
            }
        }

        return sInstance;
    }

    public Single<Boolean> isLoggedIn() {
        SingleSubject<Boolean> result = SingleSubject.create();

        result.onSuccess(false);

        return result.delay(2, TimeUnit.SECONDS);
    }
}
