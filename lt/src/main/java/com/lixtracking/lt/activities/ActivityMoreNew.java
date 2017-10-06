package com.lixtracking.lt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lixtracking.lt.R;
import com.lixtracking.lt.common.Settings;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityMoreNew extends AppCompatActivity {
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String ICON = "icon";

    private ListView listView;
    ImageView ivGlobal,ivSetting,ivAlarm;
    ArrayList<HashMap<String, Object>> listObjects = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_new);
        ivGlobal=(ImageView)findViewById(R.id.ivGlobal);

        ivSetting=(ImageView)findViewById(R.id.ivSetting);
        ivAlarm=(ImageView)findViewById(R.id.ivAlarm);
        ivAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),AlarmActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ivGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),DynamicTabActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });
        listView = (ListView) findViewById(R.id.listView);
        listView.setDividerHeight(5);
        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Settings(getApplicationContext()).setUserSession(false);
                getApplicationContext().startActivity(new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
        listObjects = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> item = new HashMap<String, Object>();

        item.put(TITLE, "User account settings");
        item.put(DESCRIPTION, " allow user change password");
        item.put(ICON, R.drawable.ic_action_person_dark);
        listObjects.add(item);

        item = new HashMap<String, Object>();
        item.put(TITLE, "About");
        item.put(DESCRIPTION, " info about application");
        item.put(ICON, R.drawable.ic_action_about_dark);
        listObjects.add(item);

        item = new HashMap<String, Object>();
        item.put(TITLE, "Function");
        item.put(DESCRIPTION, " functionality of application");
        item.put(ICON, R.drawable.ic_action_view_as_grid_dark);
        listObjects.add(item);

        item = new HashMap<String, Object>();
        item.put(TITLE, "Settings");
        item.put(DESCRIPTION, " Custom setting of Application");
        item.put(ICON, R.drawable.ic_setting_light);
        listObjects.add(item);

        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), listObjects, R.layout.settings_item,
                new String[]{ICON, TITLE, DESCRIPTION},
                new int[]{R.id.icon, R.id.text1, R.id.textView});
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getApplicationContext(), AboutActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getApplicationContext(), FunctionActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
                        break;

                }
            }
        });
    }


}
