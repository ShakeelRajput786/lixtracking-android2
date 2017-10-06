package com.lixtracking.lt.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lixtracking.lt.R;
import com.lixtracking.lt.common.Settings;

public class SettingsActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioCity,radioMake,radioModel,radioYear;
    Button btnSave,btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);

        radioCity=(RadioButton)findViewById(R.id.radioCity);

        radioMake=(RadioButton)findViewById(R.id.radioMake);

        radioModel=(RadioButton)findViewById(R.id.radioModel);

        radioYear=(RadioButton)findViewById(R.id.radioYear);
        btnSave=(Button)findViewById(R.id.btnSaveGroup);
        btnCancel=(Button)findViewById(R.id.btnCancel);
        final Settings settings=new Settings(this);

        switch(settings.getGroupBy()){
            case "Year":
                radioYear.setChecked(true);break;
            case "City":
                radioCity.setChecked(true);break;
            case "Make":
                radioMake.setChecked(true);break;
            case "Model":
                radioModel.setChecked(true);break;
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                String temp=radioButton.getText().toString();
                temp=temp.replace("Group By ","");


                settings.setGroupBy(temp);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                Toast.makeText(SettingsActivity.this,
                        radioButton.getText()+" selected", Toast.LENGTH_SHORT).show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
            }
        });


    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }
}
