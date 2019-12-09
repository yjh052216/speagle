package com.hwdp.seagle;

import com.hwdp.seagle.log.LogResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface EndPoint {

    public static final String API_URL = "http://172.30.1.52:8000";

    @Headers({"Content-Type:application/json"})
    @POST("/accounts/jwt-login/")
    Call<LogResponse> do_login(@Body UserVO vo);

    @Headers({"Content-Type:application/json"})
    @POST("/accounts/verify-email/")
    Call<LogResponse> do_verifyEmail(@Body UserVO vo);

    @Headers({"Content-Type:application/json"})
    @POST("/accounts/verify-key/")
    Call<LogResponse> do_verifyKey(@Body UserVO vo);

    @Headers({"Content-Type:application/json"})
    @POST("/accounts/token-register/")
    Call<LogResponse> do_register(@Body UserVO vo);

}
