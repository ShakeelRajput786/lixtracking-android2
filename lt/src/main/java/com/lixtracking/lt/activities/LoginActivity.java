package com.lixtracking.lt.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.lixtracking.lt.MainActivity;
import com.lixtracking.lt.R;
import com.lixtracking.lt.common.Settings;
import com.lixtracking.lt.common.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kbushko on 03/22/14.
 */

public class LoginActivity extends Activity implements View.OnClickListener{

    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";

    EditText eUserName = null;
    EditText ePassword = null;
    TextView tvSignInTitle=null;
    private String message = "";
    Context context = null;
    CheckBox checkBox;
    Settings settings;
    RoleIdTask getRoleIdTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        context = this;

        eUserName = (EditText)findViewById(R.id.editText);
        ePassword = (EditText)findViewById(R.id.editText2);
        Button SigIn = (Button)findViewById(R.id.button);
        SigIn.setOnClickListener(this);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/myfont.ttf");
        tvSignInTitle=(TextView)findViewById(R.id.tvSignInTitle);
        tvSignInTitle.setTypeface(typeface);
        settings = new Settings(context);
        if(settings.isUserSaved()) {
            eUserName.setText(settings.getUserId());
            ePassword.setText(settings.getUserPassword());
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()) {
                    checkBox.setChecked(true);
                    settings.setUserSession(true);
                }else {
                    checkBox.setChecked(false);
                    settings.setUserSession(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                if(eUserName.length() == 0 || ePassword.length() == 0){
                    message = "Please complete fields";
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Warning");
                    builder.setMessage(message);
                    builder.setCancelable(true);
                    builder.setPositiveButton("cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    new LoginTask().execute();
                }
                break;
            default: break;
        }
    }
    /**********************************************************************************************/
    /* Login async task */
    /**********************************************************************************************/
    class LoginTask extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog = null;
        HttpResponse httpResponse;
        boolean r = false;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoginActivity.this,""," please wait...",true);
        }

        @Override
        protected String doInBackground(String... url) {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
            HttpConnectionParams.setSoTimeout(httpParams, 2000);

            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

            HttpPost httpPost = new HttpPost(URL.verifyUserLogin);

            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> nameValuePairsList = new ArrayList<NameValuePair>(2);
            nameValuePairsList.add(new BasicNameValuePair("user_id", eUserName.getText().toString()));
            nameValuePairsList.add(new BasicNameValuePair("password",ePassword.getText().toString()));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairsList));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                message = EntityUtils.toString(httpEntity);
                //result = message//VerifyUserLogin(message);
                r = true;
            } catch (IOException e) {
                message = "Server doesn't response";
                e.printStackTrace();
                r = false;
            }
            return message;
        }

        @Override
        protected void onPostExecute(String resultMessage) {
            progressDialog.dismiss();
            progressDialog.cancel();
            boolean result = false;

            if(r == false ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Error");
                builder.setMessage(message);
                builder.setIcon(R.drawable.ic_action_warning_dark);
                builder.setCancelable(true);
                builder.setPositiveButton("cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
            result = VerifyUserLogin(message);
            if (result == true) {
                if(checkBox.isChecked()) {
                    settings.setUserSession(true);
                }
                settings.setUserId(eUserName.getText().toString());
                settings.setUserPassword(ePassword.getText().toString());
                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();*/
                getRoleIdTask = new RoleIdTask();
                getRoleIdTask.execute();
            }else{
                // Error login
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Error");
                builder.setMessage("Incorrect username or password");
                builder.setIcon(R.drawable.ic_action_warning_dark);
                builder.setCancelable(true);
                builder.setPositiveButton("cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
    /**********************************************************************************************/
    private boolean VerifyUserLogin(String xml) {
        boolean result = false;
        String tmp = "";
        try {
            XmlPullParser xpp = prepareXpp(xml);
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        tmp = "";
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            tmp = tmp + xpp.getAttributeName(i) + " = " + xpp.getAttributeValue(i) + ", ";
                        }
                        if (!TextUtils.isEmpty(tmp))
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        result = Boolean.parseBoolean(xpp.getText());

                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    XmlPullParser prepareXpp(String data) throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(data));
        return xpp;
    }



    /**********************************************************************************************/
    /* RoleId async task */
    /**********************************************************************************************/
    class RoleIdTask extends AsyncTask<String, Void, String> {
        private String resultString = "";
        private ProgressDialog progressDialog = null;
        HttpResponse httpResponse;
        boolean r = false;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoginActivity.this,""," please wait...",true);
        }

        @Override
        protected String doInBackground(String... url) {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
            HttpConnectionParams.setSoTimeout(httpParams, 2000);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(URL.getUserRoleId);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(1);
            nameValuePairList.add(new BasicNameValuePair("user_id", settings.getUserId()));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                resultString = EntityUtils.toString(httpEntity);
                r = true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                r = false;
            } catch (ClientProtocolException e) {
                resultString = "Server does not respond";
                e.printStackTrace();
                r=false;
            } catch (IOException e) {
                r=false;
                e.printStackTrace();
            }
            return resultString;
        }

        @Override
        protected void onPostExecute(String resultMessage) {
            progressDialog.dismiss();
            progressDialog.cancel();

            if(r == false ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Error");
                builder.setMessage(resultMessage);
                builder.setIcon(R.drawable.ic_action_warning_dark);
                builder.setCancelable(true);
                builder.setPositiveButton("cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
            String result = VerifyUserRoleId(resultMessage);
            Log.d("Android:","result RoleId:" +result);

            if (!"".equalsIgnoreCase(result)) {
                if(checkBox.isChecked()) {
                    settings.setUserSession(true);
                }
                settings.setUserRoleId(result);

               Intent intent = new Intent(getApplicationContext(), DynamicTabActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
            }else{
                // Error RoleId
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Error");
                builder.setMessage("Unable to get role Id, Try again later");
                builder.setIcon(R.drawable.ic_action_warning_dark);
                builder.setCancelable(true);
                builder.setPositiveButton("cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }


    /**********************************************************************************************/
    private String VerifyUserRoleId(String xml) {
        String result ="";
        String tmp = "";
        try {
            XmlPullParser xpp = prepareXpp(xml);
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        tmp = "";
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            tmp = tmp + xpp.getAttributeName(i) + " = " + xpp.getAttributeValue(i) + ", ";
                        }
                        if (!TextUtils.isEmpty(tmp))
                            break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        result = xpp.getText();
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
