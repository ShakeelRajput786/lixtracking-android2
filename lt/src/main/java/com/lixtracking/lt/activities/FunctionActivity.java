package com.lixtracking.lt.activities;

import android.app.Activity;
import android.os.Bundle;

import com.lixtracking.lt.R;

/**
 * Created by saiber on 17.04.2014.
 */
public class FunctionActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function_activity);
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }
}
