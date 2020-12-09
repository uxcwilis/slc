package suska.uin.tif.smartlivingcost.Util;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import suska.uin.tif.smartlivingcost.Activity.MainActivity;
import suska.uin.tif.smartlivingcost.R;

public class AppReceiver extends BroadcastReceiver {
    private PendingIntent pendingIntent;
    private static final int ALARM_REQUEST_CODE  = 134;
    //set interval notifikasi 3600 detik = 1 jam
    private int interval_hour = 1;
    private NotificationManager alarmNotificationManager;
    String NOTIFICATION_CHANNEL_ID = "smart_living_cost_channel_id";
    String NOTIFICATION_CHANNEL_NAME = "Smart Living Cost";
    private int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmIntent = new Intent(context, AppReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, alarmIntent, 0);
        //set waktu sekarang berdasarkan interval
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        //cal.add untuk sifat interval perhitungan jam, contoh 1 jam + 1 jam
        //cal.set untuk set waktu spesifik, tidak perhitungan
//        cal.add(Calendar.HOUR, interval_hour);
        cal.set(Calendar.HOUR_OF_DAY, 13);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        cal2.set(Calendar.HOUR_OF_DAY, 20);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //set alarm manager dengan memasukkan waktu yang telah dikonversi menjadi milliseconds
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), pendingIntent);
        } else if (android.os.Build.VERSION.SDK_INT >= 19) {
            manager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            manager.setExact(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), pendingIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            manager.set(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), pendingIntent);
        }
        //kirim notifikasi
        sendNotification(context, intent);
    }

    //handle notification
    private void sendNotification(Context context, Intent itn) {
        String notif_title = "Smart Living Cost";
        String notif_content = "Baru melakukan transaksi? Mari Catat dulu";
        alarmNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent newIntent = new Intent(context, MainActivity.class);
        newIntent.putExtra("frgToLoad", "Input");
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //cek jika OS android Oreo atau lebih baru
        //kalau tidak di set maka notifikasi tidak akan muncul di OS tersebut
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            alarmNotificationManager.createNotificationChannel(mChannel);
        }

        //Buat notification
        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        alamNotificationBuilder.setContentTitle(notif_title);
        alamNotificationBuilder.setSmallIcon(R.mipmap.icon);
        alamNotificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        alamNotificationBuilder.setContentText(notif_content);
        alamNotificationBuilder.setAutoCancel(true);
        alamNotificationBuilder.setContentIntent(contentIntent);
        //Tampilkan notifikasi
        alarmNotificationManager.notify(NOTIFICATION_ID, alamNotificationBuilder.build());
    }
}