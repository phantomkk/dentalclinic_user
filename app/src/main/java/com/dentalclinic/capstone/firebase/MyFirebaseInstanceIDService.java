package com.dentalclinic.capstone.firebase;

import android.util.Log;

import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.services.UserService;
import com.dentalclinic.capstone.models.User;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.CoreManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {

        //For registration of token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //To displaying token on logcat
        Log.d("TOKEN: ", refreshedToken);
        User user = CoreManager.getUser(this);
        if (user != null) {
            UserService userService = APIServiceManager.getService(UserService.class);
            userService.updateNotifyFirebaseToken(refreshedToken, user.getPhone()).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread()).subscribe(new SingleObserver<Response<String>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    d.dispose();
                }

                @Override
                public void onSuccess(Response<String> stringResponse) {
                    if (stringResponse.isSuccessful()) {
                        Log.d(AppConst.DEBUG_TAG, stringResponse.body());
                    } else {
                        try {
                            String s = stringResponse.errorBody() != null ?
                                    stringResponse.errorBody().string() : "error null in firebaseInstanceIdService";
                            Log.d(AppConst.DEBUG_TAG, s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }
            });

        }
    }
}
