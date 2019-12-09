package com.hwdp.seagle.log;

import com.hwdp.seagle.EndPoint;
import com.hwdp.seagle.UserVO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogDAO {

    private Retrofit retrofit;
    private EndPoint endPoint;

    public LogDAO() {

        retrofit = new Retrofit.Builder().baseUrl(endPoint.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        ;
        endPoint = retrofit.create(EndPoint.class);

    }


    public void login(UserVO vo, Callback callback) {
        Call<LogResponse> doLogin = endPoint.do_login(vo);
        doLogin.enqueue(callback);
    }


    // 이메일 검증 키 전송
    public void verify_email(UserVO vo, Callback callback) {
        Call<LogResponse> doVerifyEmail = endPoint.do_verifyEmail(vo);
        doVerifyEmail.enqueue(callback);
    }


    // 이메일 코드 검증
    public void verify_key(UserVO vo, Callback callback) {
        Call<LogResponse> doVerifyKey = endPoint.do_verifyKey(vo);
        doVerifyKey.enqueue(callback);
    }


    // 회원가입
    public void register(UserVO vo, Callback callback) {
        Call<LogResponse> doRegister = endPoint.do_register(vo);
        doRegister.enqueue(callback);
    }
}
