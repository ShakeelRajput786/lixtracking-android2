package com.lixtracking.lt.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.lixtracking.lt.R;

/**
 * Created by saiber on 08.04.2014.
 */
public class ChangePasswordActivity extends Activity{

    Typeface typeface;
    TextView tvChangePasswordTitle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/myfont.ttf");
        tvChangePasswordTitle=(TextView)findViewById(R.id.tvChangePasswordTitle);
        tvChangePasswordTitle.setTypeface(typeface);
    }
}
