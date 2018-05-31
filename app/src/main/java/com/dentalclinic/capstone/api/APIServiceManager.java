package com.dentalclinic.capstone.api;

import com.dentalclinic.capstone.api.services.UserService;
import com.dentalclinic.capstone.utils.AppConst;

/**
 * Created by lucky on 13-Sep-17.
 */

public class APIServiceManager {
    private static final String getURL(){
//        if(Utils.isEmulator()){
//            return "http://10.0.2.2:51390/";
//        }else{
            return AppConst.SERVER_NAME;
//        }
    }
    public static UserService getUserService(){
        return RetrofitClient.getClient(getURL()).create(UserService.class);
    }

}
