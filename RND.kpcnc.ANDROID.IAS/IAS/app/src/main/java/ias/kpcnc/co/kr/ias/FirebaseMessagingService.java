package ias.kpcnc.co.kr.ias;

/**
 * Created by Hong on 2016-10-11.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import ias.kpcnc.co.kr.ias.manager.PropertyManager;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    String imgsrc = "";
    int badgeCount;

    // [START receive_message] - FCM에서 Push 메시지를 보내면 각 단말기에서 자동으로 호출 되는 메소드
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "*************** From: " + remoteMessage);
        Log.d(TAG, "*************** Data: " + remoteMessage.getNotification());
        Log.d(TAG, "*************** Data: " + remoteMessage.getData());

        if(remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message Data PayLoad: " + remoteMessage.getData());
        }

        if(remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        imgsrc = remoteMessage.getData().get("imgsrc");
        //sendPushNotification(remoteMessage.getData().get("body") );   // "message"는 서버에 메시지를 보낼 변수명과 동일
        sendPushNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());   // "message"는 서버에 메시지를 보낼 변수명과 동일

        setAlarmBadge();
    }

    // messageBody 단말기 toast 메시지 띄움
    private void sendPushNotification(String title, String message) {
        //Log.d(TAG, "received message : " + title + "/" + message);

        // 클릭시 전달될 인텐트, 어플이 실행 된 상태에서 그 액티비티로 전달달
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ias_icon_96)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ias_icon_144))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLights(000000255,500,2000)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

      /*  if(message != null) {
            Log.d(TAG, message);
        }

        HttpURLConnection connection = null;
        try {
            // 이미지 주소를 메시지로 넘겨 받을 경우 HTTP연결하여 받은 뒤 Bitmap으로 전환 뒤, 이미지 생성
            URL url = new URL(imgsrc);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .setBigContentTitle("알림이 도착했습니다.")
                    .setSummaryText(message);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.noti)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                    .setStyle(style)
                    .setContentTitle("알림이 도착했습니다.")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setLights(000000255,500,2000)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(connection != null)
                connection.disconnect();
        }*/

//        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
//        wakelock.acquire(5000);

    }

    public void setAlarmBadge() {
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");

        // 뱃지의 카운트를 공유저장소로부터 가져옴
        badgeCount = PropertyManager.getInstance().get_badge_number();
        badgeCount++; //0으로 되어있기에 1로 만들어준다.//
        //패키지 이름과 클래그 이름설정.//

        Log.d(TAG, "########## Badge count : " + badgeCount);

        intent.putExtra("badgeCount", badgeCount);

        //문자열로 대입 가능//
        intent.putExtra("badge_count_package_name", getApplicationContext().getPackageName()); //패키지 이름//
        //배지의 적용은 맨 처음 띄우는 화면을 기준으로 한다.//
        intent.putExtra("badge_count_class_name", LoginActivity.class.getName()); //맨 처음 띄우는 화면 이름//

        //변경된 값으로 다시 공유 저장소 값 초기화.//
        PropertyManager.getInstance().setBadge_number(badgeCount);

        intent.putExtra("badge_count", badgeCount);
        sendBroadcast(intent);
    }

}

