package com.lixtracking.lt.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.lixtracking.lt.R;

/**
 * Created by saiber on 01.03.14.
 */
public class Settings {
    private static final String USER_ID = "user_id";
    private static final String GROUP_BY = "group_by";
    private static final String USER_PASSWORD = "user_password";
    private static final String SAVE_LOGIN = "save_login";
    private static final String LOGIN = "u_login";
    private static final String PASSWORD = "u_password";
    private static final String ROLEID = "RoleID";

    private final SharedPreferences settings;

    public Settings(Context context) {
        settings = context.getSharedPreferences(context.getResources().getString(R.string.app_name),Context.MODE_PRIVATE);
    }
    public void setGroupBy(String groupBy) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GROUP_BY, groupBy);
        editor.commit();
    }
    public String getGroupBy() {
        return settings.getString(GROUP_BY, "Year");
    }

    public void setUserId(String userId) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_ID, userId);
        editor.commit();
    }
    public String getUserId() {
        return settings.getString(USER_ID, "");
    }

    public void setUserPassword(String password) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_PASSWORD, password);
        editor.commit();
    }
    public String getUserPassword() {
        return settings.getString(USER_PASSWORD, "");
    }

    public boolean isUserSaved() {
        return settings.getBoolean(SAVE_LOGIN, false);
    }

    public void setUserSession(boolean state) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(SAVE_LOGIN, state);
        editor.commit();
    }

    public void setUserRoleId(String roleId) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(ROLEID, roleId);
        editor.commit();
    }
    public String getUserRoleId() {
        return settings.getString(ROLEID, "");
    }

}