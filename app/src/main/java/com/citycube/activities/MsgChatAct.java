package com.citycube.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.citycube.R;
import com.citycube.databinding.ActivityMsgChatBinding;
import com.citycube.retrofit.ApiClient;
import com.citycube.retrofit.CityCubeUserInterface;
import com.citycube.utility.App;
import com.citycube.utility.DataManager;
import com.citycube.utility.NetworkReceiver;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MsgChatAct extends AppCompatActivity implements View.OnClickListener {
    ActivityMsgChatBinding binding;
    DatabaseReference reference1;
    String request_id="", user_name, user_image, receiverId = "", receiverName = "", receiverImage = "";
    CityCubeUserInterface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(CityCubeUserInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_msg_chat);
        initializeViews();
        if (getIntent() != null) {
            setUserInfo(getIntent().getStringExtra("UserId"));
            receiverId = getIntent().getStringExtra("UserId");
            receiverName = getIntent().getStringExtra("UserName");
            receiverImage = getIntent().getStringExtra("UserImage");
            request_id = getIntent().getStringExtra("request_id");

            binding.tvName.setText(receiverName);
            Glide.with(getApplicationContext())
                    .load(receiverImage)
                    .apply(new RequestOptions().placeholder(R.drawable.user_default))
                    .override(300, 300)
                    .into(binding.ivPic);
        }
        if (NetworkReceiver.isConnected()) ResetChatCount();
        else App.showToast(this, getString(R.string.network_failure), Toast.LENGTH_SHORT);
    }

    private void initializeViews() {
        binding.ivBack.setOnClickListener(this);
        binding.ChatLayout.imgSendIcon.setOnClickListener(this);
        disablesend_button();

        binding.ChatLayout.tvMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.ChatLayout.imgSendIcon.setEnabled(true);
                    binding.ChatLayout.imgSendIcon.setAlpha((float) 1.0);
                } else {
                    disablesend_button();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void disablesend_button() {
        binding.ChatLayout.imgSendIcon.setEnabled(false);
        binding.ChatLayout.imgSendIcon.setAlpha((float) 0.3);
    }

    private void setUserInfo(String pid) {
        // user_id = DataManager.getInstance().getUserData(MsgChatActivity.this).result.id;
        user_name = DataManager.getInstance().getUserData(MsgChatAct.this).result.userName ;/*+ " " +
                DataManager.getInstance().getUserData(MsgChatAct.this).result.lastName;*/
        user_image = DataManager.getInstance().getUserData(MsgChatAct.this).result.image;
    /*    Glide.with(MsgChatActivity.this)
                .load(user_image)
                .apply(new RequestOptions().placeholder(R.mipmap.img))
                .into(img_user);*/
        //  tv_chat_user_name.setText(user_name);
        String self_user = DataManager.getInstance().getUserData(this).result.id;
        // String other_user = user_id;
        String other_user = pid;
        if (Double.parseDouble(self_user) > Double.parseDouble(other_user)) {
            reference1 = FirebaseDatabase.getInstance()

                    //   https://decoded-reducer-294611.firebaseio.com/
                    .getReferenceFromUrl("https://cubecity-1757c-default-rtdb.firebaseio.com/" + "messages" + "_" + self_user + "_" + other_user);
        } else {
            reference1 = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl("https://cubecity-1757c-default-rtdb.firebaseio.com/" + "messages" + "_" + other_user + "_" + self_user);
        }
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String message = map.get("message").toString();
                if (map != null) {
                    String sender_id = map.get("sender_id").toString();
                    String userName = map.get("user").toString();
                    String time = map.get("date").toString();
                    String msg_type = map.get("msg_type").toString();
                    if (userName.equals(DataManager.getInstance().getUserData(MsgChatAct.this).result.userName)
                        /* + " " + DataManager.getInstance().getUserData(MsgChatAct.this).result.lastName)*/) {
                        addMessageBox(message, 1, time, msg_type, "");
                    } else {
                        addMessageBox(message, 2, time, msg_type, "");
                    }
                }
                Log.e("mSgChat", "onChildAdded: " + message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_send_icon:
                if (binding.ChatLayout.tvMessage.getText().toString().length() > 0) {
                    String messageText = binding.ChatLayout.tvMessage.getText().toString().trim();
                    if (!messageText.equals("")) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("message", DataManager.getInstance().toBase64(messageText));
                        map.put("user", DataManager.getInstance().getUserData(MsgChatAct.this).result.userName /*+ " " +
                                DataManager.getInstance().getUserData(MsgChatAct.this).result.lastName*/);
                        map.put("date", DataManager.getInstance().getCurrent());
                        map.put("msg_type", "1");
                        map.put("sender_id", DataManager.getInstance().getUserData(MsgChatAct.this).result.id);
                        Log.e("send mdg===", map.toString());
                        reference1.push().setValue(map);

                        if (NetworkReceiver.isConnected())
                            sendPushNotifi(messageText);
                        else
                            Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                    }
                    binding.ChatLayout.tvMessage.setText("");
                } else {
                    binding.ChatLayout.tvMessage.setError("Field is Blank");
                }
                break;

            case R.id.ivBack:
                finish();
                break;

        }

    }

    public void addMessageBox(final String message, int type, String date, String msg_type, final String url) {
        TextView tv_time = null, tv_message = null;
        RelativeLayout relative_img_message = null;
        ImageView img_message = null;
        View view = null;
      /*  tv_seen_date.setText("Active " + DataManager.getInstance().getFormattedDate(ChatActivity.this,
                DataManager.getInstance().getdatemiliswith_diffrent_formate(date)));*/
        if (type == 1) {
            view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_chat_white_bg, null);
            tv_time = view.findViewById(R.id.tv_time);
            tv_message = view.findViewById(R.id.tv_message);
            relative_img_message = view.findViewById(R.id.relative_img_message);
            img_message = view.findViewById(R.id.img_message);
            tv_time.setText(DataManager.getInstance().getCurrent());//DataManager.getInstance().dateformatenew(date)
            if (msg_type.equalsIgnoreCase("1")) {
                tv_message.setVisibility(View.VISIBLE);
                relative_img_message.setVisibility(View.GONE);
                tv_message.setText(DataManager.getInstance().fromBase64(message));
            } else {
                tv_message.setVisibility(View.GONE);
                relative_img_message.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext())
                        .load(url)
                        .apply(new RequestOptions().placeholder(R.drawable.user_default))
                        .into(img_message);
                final View finalView = view;

            }
        } else {
            view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_chat_gredient_bg, null);
            tv_time = view.findViewById(R.id.tv_time);
            tv_message = view.findViewById(R.id.tv_message);
            relative_img_message = view.findViewById(R.id.relative_img_message);
            img_message = view.findViewById(R.id.img_message);
            tv_time.setText(DataManager.getInstance().getCurrent());//DataManager.getInstance().dateformatenew(date)
            if (msg_type.equalsIgnoreCase("1")) {
                tv_message.setVisibility(View.VISIBLE);
                relative_img_message.setVisibility(View.GONE);
                tv_message.setText(DataManager.getInstance().fromBase64(message));
            } else {
                tv_message.setVisibility(View.GONE);
                relative_img_message.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext())
                        .load(url)
                        .apply(new RequestOptions().placeholder(R.drawable.user_default))
                        .into(img_message);
                final View finalView1 = view;

            }
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        view.setLayoutParams(lp);

        binding.layout1.addView(view);
        binding.scrollView.post(new Runnable() {
            @Override
            public void run() {
                binding.scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        // App.activityPaused();
    }




    public void sendPushNotifi(String msg) {
        Map<String, String> map = new HashMap<>();
        map.put("sender_id", DataManager.getInstance().getUserData(MsgChatAct.this).result.id);
        map.put("receiver_id", receiverId);
        map.put("request_id", request_id);
       /* map.put("type", "user");
        map.put("sender_name",user_name);*/
        map.put("message", msg);
        Log.e("MapMap", "send msg" + map);

        Call<Map<String, String>> signupCall = apiInterface.sendPushNotification(map);
        signupCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String, String> data = response.body();
                    Log.e("send notification", data.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });
    }


    private void ResetChatCount() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(MsgChatAct.this).result.id);
        Log.e("MapMap", "RESET CHAT COUNT REQUEST" + map);
        Call<Map<String, String>> chatCount = apiInterface.resetChatCount(map);
        chatCount.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String, String> data = response.body();
                    if (data.get("status").equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "RESET CHAT COUNT RESPONSE" + dataResponse);

                    } else if (data.get("status").equals("0")) {
                        // tvCounter.setVisibility(View.GONE);
                        // Toast.makeText(TrackingActivity.this, data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });

    }

}


