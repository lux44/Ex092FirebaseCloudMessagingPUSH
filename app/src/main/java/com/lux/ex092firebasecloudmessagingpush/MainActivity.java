package com.lux.ex092firebasecloudmessagingpush;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.lux.ex092firebasecloudmessagingpush.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    //Firebase Cloud Messaging : FCM push 서비스 - 앱이 꺼져있어도 서버에서 클라이언트에게 메세지 전송

    //Firebase 와 이 앱을 연동 - Firebase console

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btn.setOnClickListener(view -> {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    String token=task.getResult();
                    //실무에서는 이 token 을 웹서버에 전송
                    Log.i("TOKEN",token);
                    Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.btn2.setOnClickListener(view -> {
            FirebaseMessaging.getInstance().subscribeToTopic("fcm").addOnCompleteListener(task -> {
                if (task.isSuccessful()) Toast.makeText(this, "FCM 메세지 수신을 동의하셨습니다.", Toast.LENGTH_SHORT).show();
            });
        });
        binding.btn3.setOnClickListener(view -> {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("fcm").addOnCompleteListener(task -> {
                if (task.isSuccessful()) Toast.makeText(this, "fcm 메세지 수신을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}