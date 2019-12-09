package com.hwdp.seagle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hwdp.seagle.log.Login;


public class MainActivity extends AppCompatActivity {

    private EditText edit_main_search;
    private Button button_main_search;
    private TextView text_main_logout;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_main_search = findViewById(R.id.edit_main_search);
        button_main_search = findViewById(R.id.button_main_search);
        text_main_logout = findViewById(R.id.text_main_logout);

        button_main_search.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), Room.class);
                intent.putExtra("room_name" , edit_main_search.getText().toString());
                startActivity(intent);

            }
        });

        text_main_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                pref= getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove("token");
                editor.remove("email");
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();

            }
        });

    }

}
