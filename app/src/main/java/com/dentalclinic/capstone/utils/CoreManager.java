package com.dentalclinic.capstone.utils;

import android.content.Context;

import com.dentalclinic.capstone.models.User;


/**
 * Created by lucky on 15-Oct-17.
 */

public class CoreManager {
    private static User mUser = null;

    private CoreManager(){

    }
    public static User getUser(Context context) {
        mUser = Utils.getUserInSharePref(context);
        return mUser;
    }

    public static void setUser(Context context, User user) {
        Utils.saveUserInSharePref(context, user);
        mUser = user;
    }

}
