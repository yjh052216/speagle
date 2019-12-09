package com.hwdp.seagle.log;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hwdp.seagle.MainActivity;
import com.hwdp.seagle.R;
import com.hwdp.seagle.UserVO;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private EditText edit_login_email;
    private EditText edit_login_password;
    private Button button_login;
    private TextView text_goRegister;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getSharedPreferences("user", MODE_PRIVATE);
        String saveTokon = pref.getString("token",null);

        if(saveTokon != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }


        edit_login_email = findViewById(R.id.edit_login_email);
        edit_login_password = findViewById(R.id.edit_login_password);
        button_login = findViewById(R.id.button_login);
        text_goRegister = findViewById(R.id.text_goRegister);

        //로그인 버튼 리스너 등록
        button_login.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v){
                UserVO vo = new UserVO();
                LogDAO dao = new LogDAO();

                if(edit_login_email.getText().toString().equals("") && edit_login_password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "입력되지 않은 값이 존재합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                vo.setEmail(edit_login_email.getText().toString());
                vo.setPassword(edit_login_password.getText().toString());

                //로그인 메세지 보내기
                dao.login(vo, new Callback<LogResponse>() {

                    //성공시
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
                                    //로그인이 되었으므로 화면 전환 및 토큰 저장
                                    Toast.makeText(getApplicationContext(),"로그인 성공.", Toast.LENGTH_LONG).show();
                                    Log.d("dapea", details);

                                    //토큰 저장
                                    SharedPreferences pref= getSharedPreferences("user", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("token",details);
                                    editor.putString("email",edit_login_email.getText().toString());
                                    editor.commit();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                    break;

                                case "false" :
                                    Toast.makeText(getApplicationContext(),"회원정보가 맞지 않습니다.", Toast.LENGTH_LONG).show();
                                    Log.d("dapea", details);
                                    break;
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.d("dapea", e.getMessage().toString());
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.d("dapea", e.getMessage().toString());
                        }

                    }

                    //실패시
                    @Override
                    public void onFailure(Call<LogResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"전송 실패", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        text_goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);

            }
        });
    }



}
