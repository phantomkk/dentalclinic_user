package com.dentalclinic.capstone.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.dentalclinic.capstone.models.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;


/**
 * Created by lucky on 14-Sep-17.
 */

public class Utils {

    private static final String PREF_NAME ="ACCOUNT";
    private static final String USER_KEY ="USER_KEY";
    public static boolean isEmulator(){
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86");
    }

    public static void saveUserInSharePref(Context context, User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(USER_KEY, gson.toJson(user));
        editor.apply();
    }

    public static User getUserInSharePref(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jsonUser = sharedPreferences.getString(USER_KEY, null);
        Gson gson = new Gson();
        User u = gson.fromJson(jsonUser, User.class);
        return u;
    }
    public static String getErrorMsg(ResponseBody responseBody) {
        try {
            if (responseBody != null) {
                JSONObject errorObject = new JSONObject(responseBody.string());
                return errorObject.getString("error");
            } else {
                Log.d("MYTAG", "ResponseBody or ResponseBody.string() null");
                return "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String upperCaseFirst(String value) {

        // Convert String to char array.
        char[] array = value.toCharArray();
        // Modify first element in array.
        array[0] = Character.toUpperCase(array[0]);
        // Return string.
        return new String(array);
    }
}
