package com.dentalclinic.capstone.api;

import com.dentalclinic.capstone.api.services.UserService;
import com.dentalclinic.capstone.utils.AppConst;

/**
 * Created by lucky on 13-Sep-17.
 */

public class APIServiceManager {

    public static <T> T getService(final Class<T> tClass) {
        return RetrofitClient.getClient().create(tClass);
    }
    public static <T> T getCurencyService(final Class<T> tClass) {
        return CurrencyConverterClient.getRetrofitInstance().create(tClass);
    }

}
