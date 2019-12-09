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

public class Register_2 extends AppCompatActivity {

    private EditText edit_register2_email;
    private EditText edit_register2_password;
    private EditText edit_register2_password2;
    private Button button_register2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_2);


        edit_register2_email = findViewById(R.id.edit_register2_email);
        edit_register2_password = findViewById(R.id.edit_register2_password);
        edit_register2_password2 = findViewById(R.id.edit_register2_password_2);

        Intent intent = getIntent();
        edit_register2_email.setText(intent.getExtras().getString("email"));
        edit_register2_email.setClickable(false);
        edit_register2_email.setFocusable(false);

        button_register2 = findViewById(R.id.button_register2);

        button_register2.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                UserVO vo = new UserVO();
                LogDAO dao = new LogDAO();

                vo.setEmail(edit_register2_email.getText().toString());
                vo.setPassword(edit_register2_password.getText().toString());
                vo.setConfirm_password(edit_register2_password2.getText().toString());

                // 공란 확인
                if(vo.getEmail().equals("") &&
                    vo.getPassword().equals("")&&
                    vo.getConfirm_password().equals("")){

                    Toast.makeText(getApplicationContext(), "입력되지 않은 값이 존재합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                //패스워드 확인
                else if(vo.getPassword().equals("") && vo.getConfirm_password().equals("")){
                    Toast.makeText(getApplicationContext(), "패스워드가 서로 동일하지 않습니다.", Toast.LENGTH_SHORT).show();
                }

                dao.register(vo, new Callback<LogResponse>() {
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

                            switch(status){

                                case "true" :
                                    //로그인 창으로 넘길것
                                    Toast.makeText(getApplicationContext(),"회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                                    Log.d("dapea", details);
                                    finish();
                                    break;

                                case "false" :
                                    Toast.makeText(getApplicationContext(),"회원가입이 실패 하였습니다.", Toast.LENGTH_LONG);
                                    Log.d("dapea", details);
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
                        Log.d("dapea", t.toString());
                    }
                });
            }
        });
    }
}
