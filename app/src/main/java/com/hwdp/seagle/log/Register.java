package com.hwdp.seagle.log;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hwdp.seagle.R;
import com.hwdp.seagle.UserVO;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    private EditText edit_register_email;
    private EditText edit_register_verifyKey;
    private Button button_register_verifykey_send;
    private Button button_register_verifykey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edit_register_email = findViewById(R.id.edit_register_email);
        edit_register_verifyKey = findViewById(R.id.edit_register_verifyKey);
        button_register_verifykey_send = findViewById(R.id.button_register_verifyKey_send);
        button_register_verifykey = findViewById(R.id.button_register_verifyKey);


        button_register_verifykey_send.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                UserVO vo = new UserVO();
                LogDAO dao = new LogDAO();

                vo.setEmail(edit_register_email.getText().toString());

                if(vo.getEmail().equals("")){
                    Toast.makeText(getApplicationContext(), "입력되지 않은 값이 존재합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                dao.verify_email(vo, new Callback<LogResponse>() {
                    @Override
                    public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
                        String status;
                        String details;

                        if(response.code() != 200){
                            Log.d("dapea", response.toString());
                            Toast.makeText(getApplicationContext(),"다시시도 해주새요", Toast.LENGTH_LONG).show();
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            status = jsonObject.getString("status");
                            details = jsonObject.getString("details");
                            Toast.makeText(getApplicationContext(),status+details, Toast.LENGTH_LONG).show();
                            switch(status){

                                case "true" :
                                    Toast.makeText(getApplicationContext(),"이메일을 전송하였습니다.", Toast.LENGTH_LONG).show();
                                    Log.d("debug",details);
                                    edit_register_email .setClickable(false);
                                    edit_register_email.setFocusable(false);
                                    break;

                                case "false" :
                                    Toast.makeText(getApplicationContext(),"이메일 전송을 실패하였습니다.", Toast.LENGTH_LONG);
                                    Log.d("debug",details);
                                    break;
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.d("dapea", e.getMessage().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<LogResponse> call, Throwable t) {
                        Log.d("Fail", t.toString());
                    }
                });
            }
        });

        button_register_verifykey.setOnClickListener( new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
            UserVO vo = new UserVO();
            LogDAO dao = new LogDAO();

            vo.setEmail(edit_register_email.getText().toString());
            vo.setKey(edit_register_verifyKey.getText().toString());

            if(vo.getEmail().equals("") && vo.getKey().equals("")){
                Toast.makeText(getApplicationContext(), "입력되지 않은 값이 존재합니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            dao.verify_key(vo, new Callback<LogResponse>() {
                @Override
                public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
                    String status;
                    String details;
                    if(response.code() != 200){
                        Log.d("dapea", response.toString());
                        Toast.makeText(getApplicationContext(),"다시 시도 해주새요", Toast.LENGTH_LONG).show();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        status = jsonObject.getString("status");
                        details = jsonObject.getString("details");

                        switch(status){

                            case "true" :
                                //다음 register로 넘길것
                                Toast.makeText(getApplicationContext(),"검증 코드가 일치합니다..", Toast.LENGTH_LONG).show();
                                Log.d("debug", details);

                                Intent intent = new Intent(getApplicationContext(), Register_2.class);
                                intent.putExtra("email",edit_register_email.getText().toString());
                                startActivity(intent);
                                finish();

                                break;

                            case "false" :
                                Toast.makeText(getApplicationContext(),"검증 코드가 일치하지 않습니다.", Toast.LENGTH_LONG);
                                Log.d("debug", details);
                                break;
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d("dapea", e.getMessage().toString());
                    }
                }

                @Override
                public void onFailure(Call<LogResponse> call, Throwable t) {
                    Log.d("Fail", t.toString());
                }
            });
        }
    });
    }
}
