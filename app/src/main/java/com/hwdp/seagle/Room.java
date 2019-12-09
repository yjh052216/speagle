package com.hwdp.seagle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hwdp.seagle.chat.ChatMessage;
import com.hwdp.seagle.chat.RecycleAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import tech.gusavila92.websocketclient.WebSocketClient;


public class Room extends AppCompatActivity {

    RecyclerView mRecyclerView = null;
    RecycleAdapter mAdapter = null;
    ArrayList<ChatMessage> mList = new ArrayList<ChatMessage>();
    private URI uri;
    private WebSocketClient webSocketClient;
    private EditText edit_message;
    private Button button_message_send;
    SharedPreferences pref;
    String token;
    MediaRecorder mRecorder;

    private void createWebSocketClient(String room_name) {
        URI uri;
        try {
            uri = new URI("ws://172.30.1.52:8000/ws/chat/"+room_name +"/?token="+token);
            Log.d("dapea", uri.toString());
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                System.out.println("onOpen");
            }

            @Override
            public void onTextReceived(final String message) {
                System.out.println("onTextReceived");
                //파싱하고
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        viewMessage(message);
                    }
                });

            }

            @Override
            public void onBinaryReceived(byte[] data) {
                System.out.println("onBinaryReceived");
            }

            @Override
            public void onPingReceived(byte[] data) {
                System.out.println("onPingReceived");
            }

            @Override
            public void onPongReceived(byte[] data) {
                System.out.println("onPongReceived");
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onCloseReceived() {
                System.out.println("onCloseReceived");
            }

        };

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
//        webSocketClient.addHeader("Origin", "http://developer.example.com");
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        //오디오 설정
        mRecorder = new MediaRecorder();


        //저장
        pref = getSharedPreferences("user", MODE_PRIVATE);
        token = pref.getString("token",null);

        Intent intent = getIntent();

        createWebSocketClient(intent.getExtras().getString("room_name"));

        edit_message = findViewById(R.id.edit_message);
        button_message_send = findViewById(R.id.button_message_send);

        mRecyclerView = findViewById(R.id.message_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this)) ;
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        mAdapter = new RecycleAdapter(mList);
        mAdapter.setContext(this);
        mRecyclerView.setAdapter(mAdapter);




        button_message_send.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                JSONObject jsonObject = new JSONObject();
                String message = edit_message.getText().toString();
                try {
                    jsonObject.put("message", message);
                }catch (Exception e){

                }
                edit_message.setText("");
                webSocketClient.send(jsonObject.toString());
            }
        });
    }



    public void viewMessage(String message){
        ChatMessage cm = new ChatMessage();
        String strMessage;
        String strSender;
        try {
            JSONObject jsonObject = new JSONObject(message);
            strMessage = jsonObject.getString("message");
            strSender = jsonObject.getString("sender");

        }catch (JSONException e) {
            Log.d("dapea",e.getMessage().toString());
            strMessage = "json 파싱 오류";
            strSender = "json";
        }catch (Exception e){
            e.printStackTrace();
            Log.d("dapea", e.getMessage().toString());
            strMessage = "Log확인";
            strSender="exception";
        }


        cm.setMessage(strMessage);
        cm.setSender(strSender);
        mList.add(cm);

        mAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
    }

    public void initAudioRecorder() {
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        String mPath;

        mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/record.aac";
        Log.d("dapea", "file path is " + mPath);
        mRecorder.setOutputFile(mPath);

        try {
            mRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
