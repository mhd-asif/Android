package com.bscheme.linkkin.utils;

import android.app.Application;

/**
 * Created by Android on 8/18/2015.
 */
public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        TypefaceUtil.overrideFont(getApplicationContext(),"SERIF","fonts/Roboto-Thin.ttf");
        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/ProximaNovaCond-Regular.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );*/
    }
}
