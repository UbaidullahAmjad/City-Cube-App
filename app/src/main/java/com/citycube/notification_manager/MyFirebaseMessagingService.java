package com.citycube.notification_manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.citycube.R;
import com.citycube.activities.HandlingServiceActivity;
import com.citycube.activities.HomeActivity;
import com.citycube.retrofit.Constant;
import com.citycube.utility.SessionManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private NotificationChannel mChannel;
    private NotificationManager notifManager;
    JSONObject object;
    String work_image="",https = "",driver_img="",httpsss="";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "Notification_Data:" + remoteMessage.getData());


        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            try {
                String title = "", key = "", status = "";

                 object = new JSONObject(data.get("message"));
                 status = object.getString("status");
                if (status.equals("Accept")) {

                    SessionManager.writeString(getApplicationContext(), Constant.driver_id, object.getString("driver_id"));
                    SessionManager.writeString(getApplicationContext(), Constant.request_id, object.getString("request_id"));
                    SessionManager.writeString(getApplicationContext(), Constant.user_name, object.getString("user_name"));
                    title = getString(R.string.booking_accept_by_driver);
                    Intent intent1 = new Intent("Job_Status_Action");
                    intent1.putExtra("request_id", object.getString("request_id"));
                    intent1.putExtra("status", status);
                    sendBroadcast(intent1);
                }

                else if (status.equals("chat")) {
                    key = object.getString("message");
                    title = getString(R.string.new_chat_msg_provider);
                    Intent intent1 = new Intent("Job_Status_Action");
                    intent1.putExtra("request_id", object.getString("request_id"));
                    intent1.putExtra("status", status);
                    sendBroadcast(intent1);
                }

                else if (status.equals("Arrived")) {
                    title = getString(R.string.driver_arrived_driver_location);
                    SessionManager.writeString(getApplicationContext(), Constant.driver_id, object.getString("driver_id"));
                    SessionManager.writeString(getApplicationContext(), Constant.request_id, object.getString("request_id"));
                    SessionManager.writeString(getApplicationContext(), Constant.user_name, object.getString("user_name"));
                    Intent intent1 = new Intent("Job_Status_Action");
                    intent1.putExtra("request_id", object.getString("request_id"));
                    intent1.putExtra("status", status);
                    sendBroadcast(intent1);
                }

                else if (status.equals("Start")) {
                    title = getString(R.string.driver_start_the_run);
                    SessionManager.writeString(getApplicationContext(), Constant.driver_id, object.getString("driver_id"));
                    SessionManager.writeString(getApplicationContext(), Constant.request_id, object.getString("request_id"));
                    SessionManager.writeString(getApplicationContext(), Constant.user_name, object.getString("user_name"));
                    Intent intent1 = new Intent("Job_Status_Action");
                    intent1.putExtra("request_id", object.getString("request_id"));
                    intent1.putExtra("status", status);
                    sendBroadcast(intent1);
                }

                else if (status.equals("Loading")) {
                    driver_img = object.getString("driver_img");
                    key = object.getString("message");
                    title = getString(R.string.new_chat_msg_provider);
                  //  Intent intent1 = new Intent("Job_Status_Action");
                  //  intent1.putExtra("request_id", object.getString("request_id"));
                 //   intent1.putExtra("status", status);
                   // sendBroadcast(intent1);
                }




                else if (status.equals("Finish")) {
                    title = getString(R.string.run_finish);
                    work_image = object.getString("booking_image");
                    SessionManager.writeString(getApplicationContext(), Constant.driver_id, object.getString("driver_id"));
                    SessionManager.writeString(getApplicationContext(), Constant.request_id, object.getString("request_id"));
                    SessionManager.writeString(getApplicationContext(), Constant.user_name, object.getString("user_name"));
                    Intent intent1 = new Intent("Job_Status_Action");
                    intent1.putExtra("request_id", object.getString("request_id"));
                    intent1.putExtra("status", status);
                    sendBroadcast(intent1);
                }


                 else if (status.equals("Cancel")) {
                    key = "Booking Cancel";
                    title = getString(R.string.booking_cancel_by_driver);
                    Intent intent1 = new Intent("Job_Status_Action");
                    intent1.putExtra("request_id", object.getString("request_id"));
                    intent1.putExtra("status", status);
                    intent1.putExtra("object", object.toString());
                    sendBroadcast(intent1);
                }



                if (!SessionManager.readString(getApplicationContext(), Constant.USER_INFO, "").equals("")){
                    wakeUpScreen();
                    displayCustomNotificationForOrders(status, title, key);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void displayCustomNotificationForOrders(String status, String title, String msg) {
        // SessionManager.writeString(getApplicationContext(),"provider_id",provider_id);
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService
                    (Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder;
            Intent intent = null;
            intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent;
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel == null) {
                mChannel = new NotificationChannel
                        ("0", title, importance);
                mChannel.setDescription((String) msg);
                mChannel.enableVibration(true);
                mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE),attributes);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, "0");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentTitle(title)
                    .setSmallIcon(R.drawable.logo) // required
                    .setContentText(msg)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_RINGTONE));


            if (status.equals("Loading")) {
                try {
                    Bitmap bitmap = getBitmapfromUrl(driver_img);
                    // notificationBuilder.setLargeIcon(bitmap);
                    //BigPicture Style
                    builder.setStyle(new NotificationCompat.BigPictureStyle()
                            //This one is same as large icon but it wont show when its expanded that's why we again setting
                            .bigLargeIcon(bitmap)
                            //This is Big Banner image
                            .bigPicture(bitmap));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (status.equals("Finish")) {
                try {
                    Bitmap bitmap = getBitmapfromUrl(work_image);
                    // notificationBuilder.setLargeIcon(bitmap);
                    //BigPicture Style
                    builder.setStyle(new NotificationCompat.BigPictureStyle()
                            //This one is same as large icon but it wont show when its expanded that's why we again setting
                            .bigLargeIcon(bitmap)
                            //This is Big Banner image
                            .bigPicture(bitmap));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Notification notification = builder.build();
            notifManager.notify(0, notification);
        } else {
            Intent intent = null;
            intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setSmallIcon(R.drawable.logo)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(msg));


            if (status.equals("Loading")) {
                try {
                    httpsss = driver_img;
                    // String picture = "http://i.stack.imgur.com/CE5lz.png";
                    Picasso.with(getApplicationContext()).load(httpsss).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                                    //This one is same as large icon but it wont show when its expanded that's why we again setting
                                    .bigLargeIcon(bitmap)
                                    //This is Big Banner image
                                    .bigPicture(bitmap));


                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (status.equals("Finish")) {
                try {
                    https = work_image;
                    // String picture = "http://i.stack.imgur.com/CE5lz.png";
                    Picasso.with(getApplicationContext()).load(https).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                                    //This one is same as large icon but it wont show when its expanded that's why we again setting
                                    .bigLargeIcon(bitmap)
                                    //This is Big Banner image
                                    .bigPicture(bitmap));


                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1251, notificationBuilder.build());
        }
    }


    private void wakeUpScreen() {
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();

        Log.e("screen on......", "" + isScreenOn);
        if (isScreenOn == false) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");
            wl_cpu.acquire(10000);
        }
    }



    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }


}
