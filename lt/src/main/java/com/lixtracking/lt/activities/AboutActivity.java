package com.lixtracking.lt.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.lixtracking.lt.R;

/**
 * Created by saiber on 15.04.2014.
 */
public class AboutActivity extends Activity {
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity_layout);
        context=this;
//        ActionBar ab = getActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
    }
}
