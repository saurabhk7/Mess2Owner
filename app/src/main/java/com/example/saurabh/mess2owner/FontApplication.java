package com.example.saurabh.mess2owner;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by saurabh on 4/4/17.
 */

public class FontApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Questrial-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        //....
    }

}
