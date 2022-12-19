package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.citycube.BuildConfig;
import com.citycube.R;
import com.citycube.databinding.ActivityEditProfileBinding;
import com.citycube.model.SignupModel;
import com.citycube.retrofit.ApiClient;
import com.citycube.retrofit.CityCubeUserInterface;
import com.citycube.retrofit.Constant;
import com.citycube.utility.App;
import com.citycube.utility.DataManager;
import com.citycube.utility.NetworkReceiver;
import com.citycube.utility.SessionManager;
import com.google.gson.Gson;

import java.io.File;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    String str_image_path = "";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private Uri uriSavedImage;
    CityCubeUserInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile);
        apiInterface = ApiClient.getClient().create(CityCubeUserInterface.class);
        initViews();
    }

    private void initViews() {
        binding.toolbar.ivBack.setOnClickListener(v -> { finish(); });
        binding.toolbar.tvTitle.setText(getString(R.string.edit_profile));

        setUserInfo();

        binding.ivProfile.setOnClickListener(v -> {
            if(checkPermisssionForReadStorage())
                showImageSelection();
        });


        binding.tvChangePassword.setOnClickListener(v -> { startActivity(new Intent(EditProfileActivity.this,ChangePasswordActivity.class)); });

        binding.btnUpdate.setOnClickListener(v -> {validation();});

    }

    private void validation() {
        if (binding.etUserName.getText().toString().equals("")) {
            binding.etUserName.setError(getString(R.string.please_enter_username));
            binding.etUserName.setFocusable(true);
        }  else if (binding.etMobile.getText().toString().equals("")) {
            binding.etMobile.setError(getString(R.string.please_enter_mobile_number));
            binding.etMobile.setFocusable(true);
        }
        else {
            if(NetworkReceiver.isConnected()) updateProfile();
            else App.showToast(EditProfileActivity.this,getString(R.string.network_failure), Toast.LENGTH_LONG);
        }
    }

    public void setUserInfo(){
        binding.etUserName.setText(DataManager.getInstance().getUserData(EditProfileActivity.this).result.userName);
        binding.etEmail.setText(DataManager.getInstance().getUserData(EditProfileActivity.this).result.email);
        binding.etMobile.setText(DataManager.getInstance().getUserData(EditProfileActivity.this).result.mobile);
        binding.ccp.setCountryForPhoneCode(Integer.parseInt(DataManager.getInstance().getUserData(EditProfileActivity.this).result.phoneCode));

        Glide.with(EditProfileActivity.this)
                .load(DataManager.getInstance().getUserData(EditProfileActivity.this).result.image)
                .override(80,80)
                .apply(new RequestOptions().placeholder(R.drawable.user_default))
                .into(binding.ivProfile);

    }

    public void showImageSelection() {

        final Dialog dialog = new Dialog(EditProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);
        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                openCamera();
            }
        });
        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                getPhotoFromGallary();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private void getPhotoFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);

    }

    private void openCamera() {

        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/CityCubeUser/Images/");

        if (!dirtostoreFile.exists()) {
            dirtostoreFile.mkdirs();
        }

        String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());

        File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/CityCubeUser/Images/" + "IMG_" + timestr + ".jpg");

        str_image_path = tostoreFile.getPath();

        uriSavedImage = FileProvider.getUriForFile(EditProfileActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                tostoreFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        startActivityForResult(intent, REQUEST_CAMERA);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_FILE) {
                str_image_path = DataManager.getInstance().getRealPathFromURI(EditProfileActivity.this, data.getData());
                Glide.with(EditProfileActivity.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivProfile);

            } else if (requestCode == REQUEST_CAMERA) {
                Glide.with(EditProfileActivity.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivProfile);
            }

        }
    }


    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(EditProfileActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(EditProfileActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(EditProfileActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(EditProfileActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(EditProfileActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(EditProfileActivity.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }


    private void updateProfile() {
        DataManager.getInstance().showProgressMessage(EditProfileActivity.this, getString(R.string.please_wait));
        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }
        RequestBody f_name = RequestBody.create(MediaType.parse("text/plain"), binding.etUserName.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), binding.etEmail.getText().toString());
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), binding.etMobile.getText().toString());
        RequestBody country_code = RequestBody.create(MediaType.parse("text/plain"), binding.ccp.getSelectedCountryCode());
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(EditProfileActivity.this).result.id);


        Call<SignupModel> signupCall = apiInterface.profileUpdate(f_name, email, mobile, country_code,user_id, filePart);
        signupCall.enqueue(new Callback<SignupModel>() {
            @Override
            public void onResponse(Call<SignupModel> call, Response<SignupModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SignupModel data = response.body();
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SessionManager.writeString(EditProfileActivity.this, Constant.USER_INFO, dataResponse);
                        finish();
                    } else if (data.status.equals("0")) {
                        App.showToast(EditProfileActivity.this, data.message, Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SignupModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });
    }


}