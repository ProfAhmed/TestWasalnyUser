package com.example.testwasalnyuser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.testwasalnyuser.trip_node.TripNode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MyTestService extends IntentService {
    Context mContext;
    final boolean[] first = {true};
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    public static final String ACTION = "com.example.testwasalnyuser.MyTestService";


    public MyTestService() {
        super("test");
    }

    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
        mContext = getApplicationContext();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.v("FOO", intent.getStringExtra("foo"));
        Toast.makeText(mContext, "start service", Toast.LENGTH_SHORT).show();
        JSONObject jsonBody = new JSONObject();
        try {

            jsonBody.put("customerLat", intent.getStringExtra("customerLat"));
            jsonBody.put("customerLng", intent.getStringExtra("customerLng"));
            jsonBody.put("customerAddress", intent.getStringExtra("customerAddress"));
            jsonBody.put("distinationLat", intent.getStringExtra("distinationLat"));
            jsonBody.put("distinationLng", intent.getStringExtra("distinationLng"));
            jsonBody.put("distinationAddresss", intent.getStringExtra("distinationAddresss"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        pickRequest(jsonBody);
    }

    void pickRequest(JSONObject jsonBody) {
        String url = "https://wasalny2020.herokuapp.com/trip/request";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        boolean successful = response.getBoolean("successful");
                        if (successful) {
                            Type dataType = new TypeToken<PickModel>() {
                            }.getType();
                            PickModel data = new Gson().fromJson(response.toString(), dataType);
                            Toast.makeText(mContext, data.getMessage(), Toast.LENGTH_SHORT).show();
                            DatabaseReference reference1 = FirebaseDatabase.getInstance("https://wasalny-6139e.firebaseio.com/").getReference("/trips/"
                                    + data.getData().getTripFireBase());

                            displayNotification();
                            reference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    TripNode tripNode = dataSnapshot.getValue(TripNode.class);
                                    String value = null;
                                    if (tripNode != null) {
                                        value = tripNode.getCurrentStatus();

                                        Intent in = new Intent(ACTION);
                                        if (value != null) {
                                            switch (value) {
                                                case "NotFound":
                                                       if (first[0]) {
                                                        first[0] = false;
                                                        pickRequest(jsonBody);
                                                    } else {
                                                        Toast.makeText(mContext, "Not drvers ", Toast.LENGTH_SHORT).show();
                                                        Log.v("FOO", "Not drvers");
                                                        notificationBuilder.setOngoing(false);
                                                        notificationBuilder.setContentText("لا يوج ساشق");
                                                        notificationManager.notify(101, notificationBuilder.build());
                                                        // Put extras into the intent as usual
                                                        in.putExtra("resultCode", Activity.RESULT_OK);
                                                        in.putExtra("resultValue", "My Result Value. Passed in: " + "NotFound");
                                                        in.putExtra("tripNode", tripNode);
                                                        // Fire the broadcast with intent packaged
                                                        LocalBroadcastManager.getInstance(MyTestService.this).sendBroadcast(in);
                                                    }

                                                    break;
                                                case "Accept":
                                                    Toast.makeText(mContext, "Driver Accept", Toast.LENGTH_SHORT).show();
                                                    Log.v("FOO", "Driver Accept");
                                                    notificationBuilder.setOngoing(false);
                                                    notificationBuilder.setContentText("الكابتن فى طريقه اليك عربيه برتقالى حلو وملونه ");
                                                    notificationManager.notify(101, notificationBuilder.build());
                                                    // Put extras into the intent as usual
                                                    in.putExtra("resultCode", Activity.RESULT_OK);
                                                    in.putExtra("resultValue", "My Result Value. Passed in: " + "Accept");
                                                    in.putExtra("tripNode", tripNode);
                                                    // Fire the broadcast with intent packaged
                                                    LocalBroadcastManager.getInstance(MyTestService.this).sendBroadcast(in);
                                                    // or sendBroadcast(in) for a normal broadcast;
                                                    break;
                                                case "Waiting":
                                                    notificationBuilder.setOngoing(false);
                                                    notificationBuilder.setContentText("السائق منتظرك");
                                                    notificationManager.notify(101, notificationBuilder.build());
                                                    // Put extras into the intent as usual
                                                    in.putExtra("resultCode", Activity.RESULT_OK);
                                                    in.putExtra("resultValue", "My Result Value. Passed in: " + "Waiting");
                                                    in.putExtra("tripNode", tripNode);
                                                    // Fire the broadcast with intent packaged
                                                    LocalBroadcastManager.getInstance(MyTestService.this).sendBroadcast(in);
                                                    break;
                                                case "DriverCancel":
                                                    pickRequest(jsonBody);
                                                    Log.v("FOO", "DriverCancel");
                                                    // Put extras into the intent as usual
                                                    in.putExtra("resultCode", Activity.RESULT_OK);
                                                    in.putExtra("resultValue", "My Result Value. Passed in: " + "Accept");
                                                    in.putExtra("tripNode", tripNode);
                                                    // Fire the broadcast with intent packaged
                                                    LocalBroadcastManager.getInstance(MyTestService.this).sendBroadcast(in);
                                                    break;
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
                                }
                            });
                        } else {
                            ErrorsMessages error = new Gson().fromJson(response.toString(), ErrorsMessages.class);
                            Toast.makeText(mContext, error.getErrorMessages().get(0), Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Log.v("volley_error", error.toString());
            error.printStackTrace();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IkNVOTc3MjgxNDkzMjE0IiwiZW1haWwiOiJhaG1lZDQ0MUBnbWFpbC5jb20iLCJ0eXBlIjoiQ3VzdG9tZXIiLCJpYXQiOjE1NzY4NDY3ODEsImV4cCI6MTU3OTQzODc4MX0.kmJz2CZg3i8ZUmC_X1qFPTNc5n0tKEa2sCCQi1PITWE");
//                headers.put("lang", PreferenceProcessor.getInstance(mContext).getStr(MyConfig.MyPrefs.LANG, "en"));
                return headers;

            }
        };

        //handle timeout error
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Add the request to the RequestQueue.
        VolleySingleTone.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void displayNotification() {


//        style.bigPicture(bitmap);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        stackBuilder.addNextIntentWithParentStack(intent);

//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("Game Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText("جارى البحث عن سائق ")
                .setContentIntent(pendingIntent)
//                .setStyle(style)
//                .setLargeIcon()
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .setOngoing(true);

        notificationManager.notify(101, notificationBuilder.build());

    }

    public static void clearNotification(Context mContext) {
        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(101);
        }
    }
}
