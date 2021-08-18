package dt.sis.parent.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jackandphantom.blurimage.BlurImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ActivityEditProfileBinding;
import dt.sis.parent.helper.CompressImage;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.ProfileImageModel;
import dt.sis.parent.models.ProfileModel;
import dt.sis.parent.models.UploadProfilePicModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class EditProfileActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 100;
    private static final int GALLERY_REQUEST = 101;
    private static final int REQUEST_WRITE_PERMISSION = 99;
    ActivityEditProfileBinding binding;
    Context mContext;
    SessionManager sessionManager;
    boolean isedit=true;
    private Uri cameraFileUri;
    private byte[] imageByteArray ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        mContext = EditProfileActivity.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.customToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && actionBar.isShowing()){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            binding.customToolbar.textView.setText("Profile");
        }

        setEnabledEdit(!isedit);

        binding.btnSave.setOnClickListener(onClickListener);
        binding.btnEdit.setOnClickListener(onClickListener);
        binding.tvEditprofile.setOnClickListener(onClickListener);

        gotoProfileImage();
    }

    private void gotoProfileImage() {

        String token = sessionManager.getToken().replace("Bearer","").trim();
        String refreshToken = sessionManager.getRefreshToken();
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        postParam.addProperty("token",token);
        postParam.addProperty("refreshToken",refreshToken);

        call = webApis.getProfileImageApi();
        ApiServices apiServices = new ApiServices(mContext);
        apiServices.setDisplayDialog(true);
        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{

                    ProfileImageModel modelVal = new Gson().fromJson(response, ProfileImageModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {

                        String imagePath = modelVal.getResult().getProfilePicture();

                        if (imagePath.isEmpty()) {
                            binding.ivProfileimage.setImageResource(R.mipmap.ic_round_user);
                        } else {

                            byte[] decodedString = Base64.decode(imagePath, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            binding.ivProfileimage.setImageBitmap(decodedByte);


                            BlurImage.with(mContext).load(decodedByte).intensity(70).Async(true).into(binding.ivProfileimageLarge);
                        }

                        getProfileForEdit();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }

    private void getProfileForEdit() {

        String tenantId = sessionManager.getTenantId();
        String organizationId = sessionManager.getOrganizationId();
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();

        call = webApis.getProfileForEdit(tenantId,organizationId);

        ApiServices apiServices = new ApiServices(mContext);
        apiServices.setDisplayDialog(true);
        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try {

                    ProfileModel modelVal = new Gson().fromJson(response,ProfileModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {

                        binding.etFirstName.setText(modelVal.getResult().getName());
                        binding.etLastName.setText(modelVal.getResult().getSurname());
                        binding.etUsername.setText(modelVal.getResult().getUserName());
                        binding.etEmail.setText(modelVal.getResult().getEmailAddress());
                        binding.etMob.setText(modelVal.getResult().getPhoneNumber());
                        binding.etPhoneNo.setText(modelVal.getResult().getPhoneNumber());
                        binding.tvName.setText(modelVal.getResult().getName()+" "+modelVal.getResult().getSurname());
                        binding.tvMail.setText(modelVal.getResult().getEmailAddress());
                        binding.tvName.setVisibility(View.VISIBLE);
                        binding.tvMail.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Toast.makeText(mContext, "Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_Save:
                    String fname  = binding.etFirstName.getText().toString().trim();
                    String lname  = binding.etLastName.getText().toString().trim();
                    String uname  = binding.etUsername.getText().toString().trim();
                    String email  = binding.etEmail.getText().toString().trim();
                    String mob  = binding.etMob.getText().toString().trim();
                    String phone  = binding.etPhoneNo.getText().toString().trim();
                    if (fname.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter first name", Toast.LENGTH_LONG).show();
                    } else if (lname.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter surname", Toast.LENGTH_LONG).show();
                    }else if (uname.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter username", Toast.LENGTH_LONG).show();
                    }else if (phone.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter Phone number", Toast.LENGTH_LONG).show();
                    }else {
                        updateProfileApi(fname,lname,uname,email,phone);
                    }

                    break;
                case R.id.btn_Edit:
                    ActivityCompat.requestPermissions((Activity) mContext, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);

                    break;

                case R.id.tv_editprofile:

                    final ProgressDialog pd= new ProgressDialog(mContext);
                    pd.setMessage("Please wait...");
                    pd.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();

                            setEnabledEdit(!isedit);


                        }
                    },300);

                    break;

                default:break;
            }
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    Toast.makeText(mContext, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void selectImage() {
        File storageFile = new File(mContext.getExternalCacheDir(),"Files");
        if (!storageFile.exists()) {
            if (storageFile.mkdir())
                Log.e("Picture", "pictureDir  Created : " + storageFile);
        }

        final CharSequence[] options = { "Camera", "Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Upload Media !");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Camera")) {
                    openCamera();

                }else if (options[item].equals("Gallery")){
                    openGallery();

                }else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void openCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String imageFileName = "IMG_" + System.currentTimeMillis() + ".jpeg";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                cameraFileUri= FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider",  new File(mContext.getExternalCacheDir().getPath(), imageFileName));
            }else{
                cameraFileUri = Uri.fromFile(new File(mContext.getExternalCacheDir().getPath(), imageFileName));
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri);
            startActivityForResult(intent, CAMERA_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
  private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // When an Image is picked
            if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK  && data!=null) {
                // Get the Image from data
                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    CompressCameraImage task = new CompressCameraImage(mImageUri);
                    task.execute();

                }

            }
            else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK ) {
                try{
                    if(cameraFileUri!=null){
                        CompressCameraImage task = new CompressCameraImage(cameraFileUri);
                        task.execute();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            else {
                Toast.makeText(mContext, "You didn't pick any image",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }
    private static byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytesArray;

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_"+System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    public class CompressCameraImage extends AsyncTask<Uri, Void, String> {

        AlertDialog progressDialog;
        Uri imageUri;

        public CompressCameraImage(Uri imageUri) {
            progressDialog = sessionManager.getProgressAlert();
            progressDialog.setCancelable(false);
            progressDialog.show();
            this.imageUri = imageUri;

        }

        @Override
        protected String doInBackground(Uri... params) {

            try {

                Bitmap photoBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
                Uri tempUri = getImageUri(getApplicationContext(), photoBitmap);

                String filePath = new CompressImage(mContext).compressImage(tempUri);


                return filePath;
            }catch (Exception e){
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String path) {
            if(progressDialog.isShowing()) progressDialog.dismiss();
            if(path!=null){
                imageByteArray = readBytesFromFile(path);
                if(imageByteArray!=null)
                    uploadFile();
            }else{
                Toast.makeText(mContext, "Error! Please try again", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void uploadFile() {
        String tenantId  = sessionManager.getTenantId();
        String userId  = sessionManager.getLinkId();

        Call<JsonObject> call = null;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);

        Map<String, RequestBody> myMap = new HashMap<>();

        List<MultipartBody.Part> uploadImagesParts = new ArrayList<>();

        if(imageByteArray!=null) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageByteArray);
            MultipartBody.Part dataPart = MultipartBody.Part.createFormData("image", "file_avatar.jpeg", imageBody);
            uploadImagesParts.add(dataPart);
        }


        call = webApis.uploadProfilePicture(uploadImagesParts);

        ApiServices apiServices = new ApiServices(mContext);
        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    UploadProfilePicModel modelVal = new Gson().fromJson(response, UploadProfilePicModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {
                        UploadProfilePicModel.Result resultData = modelVal.getResult();
                        if(resultData!=null)
                            callUpdateProfilePictureApi(resultData);

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }

    private void callUpdateProfilePictureApi(UploadProfilePicModel.Result resultData) {
        String tenantId  = sessionManager.getTenantId();
        String userId  = sessionManager.getLinkId();

        Call<JsonObject> call = null;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);


        JsonObject postParam = new JsonObject();
        postParam.addProperty("fileName",resultData.getFileName());
        postParam.addProperty("x",0);
        postParam.addProperty("y",0);
        if (resultData.getWidth() > 500) {
            postParam.addProperty("width", 500);
        } else {
            postParam.addProperty("width", resultData.getWidth());
        }
        if (resultData.getHeight() > 500) {
            postParam.addProperty("height", 500);
        } else {
            postParam.addProperty("height", resultData.getHeight());
        }


        call = webApis.updateProfilePicture(postParam);

        ApiServices apiServices = new ApiServices(mContext);
        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{

                    UploadProfilePicModel modelVal = new Gson().fromJson(response, UploadProfilePicModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {
                        Toast.makeText(mContext, "File uploaded successfully", Toast.LENGTH_SHORT).show();

                        setEnabledEdit(false);

                        gotoProfileImage();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }

    private void setEnabledEdit(boolean isEnabled){
        if(isEnabled){
            binding.tvEditprofile.setText("Cancel");
            binding.etFirstName.setEnabled(true);
            binding.etLastName.setEnabled(true);
            binding.etUsername.setEnabled(false);
            binding.etEmail.setEnabled(false);
            binding.etMob.setEnabled(true);
            binding.etPhoneNo.setEnabled(true);
            binding.btnSave.setVisibility(View.VISIBLE);
            binding.btnEdit.setVisibility(View.VISIBLE);
        }else{
            binding.tvEditprofile.setText("Edit");
            binding.etFirstName.setEnabled(false);
            binding.etLastName.setEnabled(false);
            binding.etUsername.setEnabled(false);
            binding.etEmail.setEnabled(false);
            binding.etMob.setEnabled(false);
            binding.etPhoneNo.setEnabled(false);
            binding.btnSave.setVisibility(View.GONE);
            binding.btnEdit.setVisibility(View.GONE);
        }
        isedit = !isedit;
    }

    private void updateProfileApi(String fname, String lname,String uname,String email,String phone) {
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        postParam.addProperty("name",fname);
        postParam.addProperty("surname",lname);
        postParam.addProperty("userName",uname);
        postParam.addProperty("emailAddress",email);
        postParam.addProperty("phoneNumber",phone);
        postParam.addProperty("isPhoneNumberConfirmed",true);
        postParam.addProperty("timezone","");
        postParam.addProperty("qrCodeSetupImageUrl","");
        postParam.addProperty("isGoogleAuthenticatorEnabled",false);

        call = webApis.updateProfileApi(postParam);
        new ApiServices(mContext).callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    ProfileModel modelVal = new Gson().fromJson(response,ProfileModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {
                        Toast.makeText(mContext, "Profile update Successfully", Toast.LENGTH_LONG).show();

                        setEnabledEdit(false);

                        gotoProfileImage();
                    }
                    else
                    {
                        Toast.makeText(mContext, "Invalid password, please try again", Toast.LENGTH_LONG).show();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
