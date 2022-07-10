package com.lux.ex092firebasecloudmessagingpush;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFCMService extends FirebaseMessagingService {

    //** 앱이 설치된 후 적어도 한 번은 앱 런처화면(앱 목록 화면)에서 사용자가 직접 아이콘을
    //클릭하여 실행한 앱만 push 메세지를 받을 수 있음. **

    //FCM 메세지 유형 2가지 : 알림, 데이터
    //1. 앱이 Foreground 일때 - 무조건 onMessageReceived() 라는 메소드가 발동함.
    //2. 앱이 Background 일때 - 알림 메세지 유형일때는 기본 알림이 발생.
    //          ;;          - 데이터 유형일때는 onMessageReceived()메소드 발동
    //###########################################
    //** 작업표시줄에 표시되는 기본알림은 커스텀이 다소 제한됨.
    //** 그래서 실무에서는 기본알림 사용을 기피함.
    // 무조건 메세지를 이 onMessageReceived() 메소드에서 받아서 처리하는 방법을 선호함.
    //이 곳에서 받으면 개발자가 작성한 Notification 으로 보여줄 수 있어서 더 선호함.

    //메세지 유형이 알림 메세지 타입이면 백그라운드일 때  기본 알림이 보이기에
    //실무에서는 데이터 메세지 타입만을 사용함.


    //앱상태	          알림	              데이터	                  모두
    //포그라운드	onMessageReceived	onMessageReceived	    onMessageReceived
    //백그라운드	    작업 표시줄	    onMessageReceived 	    알림: 작업 표시줄
    //                                                      데이터: 인텐트 부가 정보

    //firebase console 에 알림메세지 수신을 연습할 수 있도록 [알림 작성기]가 존재함
    //[알림메세지유형]만 가능함, 애석하게 [데이터메세지유형]은 테스트 불가
    //대신 알림메세지유형+데이터메세지유형(모두)은 테스트 가능함.

    //FCM 메세지를 받았을때 자동으로 발동하는 메소드 - 앱이 켜져있으면 무조건 발동
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Log.i("TAG","onMessageReceived....");

        //혹시 알림메세지 유형이라면 [이 메소드의 파라미터로 전달된 RemoteMessage message 에 알림 정보가 있음.]
        RemoteMessage.Notification notiMsg =message.getNotification();  //원격지에서 날라온 notification 정보
        if(notiMsg!=null){
            String title=notiMsg.getTitle();
            String text=notiMsg.getBody();

            Log.i("NOTI",title+",,,"+text);
        }
        //알림 메세지 유형은 백그라운드일때 기본 알림으로 발동하기에 잘 사용하지 않음
        //데이터 메세지 유형을 더 많이 사용함.
        //[알림작성기]라는 테스트 push 서비스는 알림 + 데이터 메세지 유형이 가능함.
        //만약, 데이터 메세지가 있다면
        Map<String ,String> data =message.getData();
        if (data.size()>0){
            String name=data.get("name");
            String msg=data.get("msg");

            //사용자에게 push 메세지의 수신을 알려주기 위해 알림(notification)을 보이기
            NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
            NotificationChannelCompat channelCompat=new NotificationChannelCompat.Builder("fcm_channel",NotificationManagerCompat.IMPORTANCE_HIGH)
                    .setName("Ex92 fcm channel").build();
            notificationManagerCompat.createNotificationChannel(channelCompat);
            NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"fcm_channel");
            builder.setSmallIcon(R.drawable.ic_fcm_noti);
            builder.setContentTitle(name);
            builder.setContentText(msg);

            notificationManagerCompat.notify(0,builder.build());
        }


    }

    //fcm 서버에서 디바이스의 고유 등록 id(token)이 발급되었을때 자동으로 발동하는 메소드
    //Manifests 에 이 서비스가 등록되어 있다면 앱을 실행하기만 하면 자동으로 실행됨. [처음 한번 설치할때만 발동함]
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //발급된 토큰 확인해보기 - 실무에서는 이 token 값을 third-party server(dothome 같은 호스팅 웹서버)의 db에 저장하여 FCM 전송할떄 디바이스 식별용으로 사용됨.
        Log.i("TOKEN",token);
    }
}
