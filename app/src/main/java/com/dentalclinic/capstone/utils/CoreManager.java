package com.dentalclinic.capstone.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.User;

import java.util.List;


/**
 * Created by lucky on 15-Oct-17.
 */

public class CoreManager {
    private static User mUser = null;
    private static Patient mCurrentPatient = null;

    private CoreManager() {

    }

    public static User getUser(Context context) {
        mUser = Utils.getUserInSharePref(context);
        if (mUser == null) {
            Log.d(AppConst.DEBUG_CORE_TAG, "get User from share prefef Null");
        }
        return mUser;
    }

    public static void setUser(Context context, User user) {
        Utils.saveUserInSharePref(context, user);
        if (user != null) {
            List<Patient> list = user.getPatients();
            mUser = user;
            if (list != null && list.size() > 0) {
                setCurrentPatient(list.get(0));
            } else {
                Log.d(AppConst.DEBUG_TAG, "class CoreManager.setUser(): LIST PATIENT IS NULL OR EMPTY PLEASE CHECK");
            }
        } else {
            Log.d(AppConst.DEBUG_TAG, "class CoreManager.setUser(): USER IS NULL");
        }
    }

    public static void clearUser(Context context){
        Utils.saveUserInSharePref(context, null);
         setCurrentPatient(null);
         mUser=null;
    }

    public static void setCurrentPatient(Patient patient) {
        mCurrentPatient = patient;
    }

    public static Patient getCurrentPatient() {
        return mCurrentPatient;
    }
}
