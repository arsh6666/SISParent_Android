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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ActivityAddGuardianBinding;
import dt.sis.parent.databinding.ActivityGuardianBinding;
import dt.sis.parent.dialogs.DeleteConfirmDialog;
import dt.sis.parent.dialogs.GuardianTypeListDialog;
import dt.sis.parent.dialogs.ParentsCommentsDialog;
import dt.sis.parent.helper.CompressImage;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.GuardianListModel;
import dt.sis.parent.models.GuardianTypeModel;
import dt.sis.parent.models.ResultModel;
import dt.sis.parent.models.UploadProfilePicModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AddGuardianActivity extends AppCompatActivity {

    ActivityAddGuardianBinding binding;
    Context mContext;
    SessionManager sessionManager;
    private static final int CAMERA_REQUEST = 100;
    private static final int GALLERY_REQUEST = 101;
    private static final int REQUEST_WRITE_PERMISSION = 99;

    private String firstName="";
    private String lastName="";
    private String address="";
    private String mobileNumber="";
    private String guardianTypeName="";
    private Uri cameraFileUri;
    private byte[] imageByteArray ;

    private int guardianTypeId=-1;
    boolean isedit=true;
    GuardianTypeListDialog guardianTypeListDialog;
    List <GuardianTypeModel.Result> guardianTypeList ;
    GuardianListModel.Result guardianData ;
    private String guardian_data="";
    private int guardianId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_guardian);
        mContext = AddGuardianActivity.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.customToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && actionBar.isShowing()){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            binding.customToolbar.textView.setText("Guardian Detail");
        }

        guardian_data = getIntent().getStringExtra("guardian_data");
        if(guardian_data!=null){
            guardianData = new Gson().fromJson(guardian_data, GuardianListModel.Result.class);
            setGuardianData();
            setEnabledEdit(!isedit);
            binding.btnEdit.setVisibility(View.VISIBLE);
        }else {
            binding.btnEdit.setVisibility(View.GONE);
            setEnabledEdit(isedit);
        }

        binding.btnSave.setOnClickListener(onClickListener);
        binding.btnEdit.setOnClickListener(onClickListener);
        binding.btnCamera.setOnClickListener(onClickListener);
        binding.btnDelete.setOnClickListener(onClickListener);
        binding.etGuardianType.setOnClickListener(onClickListener);

        getGuardianTypes();

    }
    private void setEnabledEdit(boolean isEnabled){
        if(isEnabled){
            binding.btnEdit.setText("Cancel");
            binding.etFirstName.setEnabled(true);
            binding.etLastName.setEnabled(true);
            binding.etGuardianType.setEnabled(true);
            binding.etPhoneNo.setEnabled(true);
            binding.etAddress.setEnabled(true);
            binding.btnDelete.setVisibility(View.VISIBLE);
            binding.btnSave.setVisibility(View.VISIBLE);
            binding.btnCamera.setVisibility(View.VISIBLE);

        }else{
            binding.btnEdit.setText("Edit");
            binding.etFirstName.setEnabled(false);
            binding.etLastName.setEnabled(false);
            binding.etAddress.setEnabled(false);
            binding.etPhoneNo.setEnabled(false);
            binding.etGuardianType.setEnabled(false);
            binding.btnDelete.setVisibility(View.GONE);
            binding.btnSave.setVisibility(View.GONE);
            binding.btnCamera.setVisibility(View.GONE);
        }
        if(guardian_data==null){
            binding.btnDelete.setVisibility(View.GONE);
        }
        isedit = !isedit;
    }

    private void setGuardianData() {
        guardianId = guardianData.getId();
        firstName = guardianData.getFirstname();
        lastName = guardianData.getLastname();
        mobileNumber = guardianData.getMobilenumber();
        address = guardianData.getAddress();
        guardianTypeName = guardianData.getGuardiantype();
        guardianTypeId = guardianData.getGuardiantypeid();
        String picture = SessionManager.GUARDIAN_FILE_URL+"?guardianId="+guardianId;
        binding.etGuardianType.setText(guardianTypeName);
        binding.etAddress.setText(address);
        binding.etPhoneNo.setText(mobileNumber);
        binding.etLastName.setText(lastName);
        binding.etFirstName.setText(firstName);
        Picasso.get().load(picture)
                .placeholder(R.mipmap.ic_round_user)
                .error(R.mipmap.ic_round_user)
                .into(binding.ivProfileimage);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.et_GuardianType:
                    guardianTypeListDialog = new GuardianTypeListDialog(mContext,guardianTypeId, guardianTypeList ,new GuardianTypeListDialog.ItemClickListener() {
                        @Override
                        public void onCancelItemClick(View view) {
                            guardianTypeListDialog.dismiss();
                        }

                        @Override
                        public void onSelectItemClick(int id, String name)  {
                            guardianTypeId = id;
                            binding.etGuardianType.setText(name);
                            guardianTypeListDialog.dismiss();
//                            if(!message.isEmpty()){
//                                postCommentApi(message,id);
//                            } else {
//                                Toast.makeText(mContext, "Please Enter comment", Toast.LENGTH_LONG).show();
//                            }
                        }
                    });
                    guardianTypeListDialog.setCancelable(true);
                    guardianTypeListDialog.show();

                    break;

                case R.id.btn_Delete:
                    DeleteConfirmDialog deleteConfirmDialog = new DeleteConfirmDialog(mContext, new DeleteConfirmDialog.DeleteOnClickListnere() {
                        @Override
                        public void onCLickYes(DialogInterface dialog) {
                            dialog.dismiss();
                            deleteGuardian();
                        }

                        @Override
                        public void onClickNo(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });
                    deleteConfirmDialog.showAlert();

                    break;

                case R.id.btn_Edit:

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

                case R.id.btn_camera:
                    ActivityCompat.requestPermissions((Activity) mContext, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);

                    break;

                case R.id.btn_Save:
                    firstName = binding.etFirstName.getText().toString().trim();
                    lastName = binding.etLastName.getText().toString().trim();
                    mobileNumber = binding.etPhoneNo.getText().toString().trim();
                    address = binding.etAddress.getText().toString().trim();
                    if(firstName.isEmpty()){
                        Toast.makeText(mContext, "Please enter your first name", Toast.LENGTH_SHORT).show();
                    }else if(lastName.isEmpty()){
                        Toast.makeText(mContext, "Please enter your last name", Toast.LENGTH_SHORT).show();
                    }else if(mobileNumber.isEmpty()){
                        Toast.makeText(mContext, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                    }else if(guardianTypeId==-1){
                        Toast.makeText(mContext, "Please select guardian", Toast.LENGTH_SHORT).show();
                    }else if(address.isEmpty()) {
                        Toast.makeText(mContext, "Please enter your address", Toast.LENGTH_SHORT).show();
                    }else {
                        addGuardian();
                    }
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
                Picasso.get().load(new File(path)).error(R.mipmap.ic_round_user).into(binding.ivProfileimage);

            }else{
                Toast.makeText(mContext, "Error! Please try again", Toast.LENGTH_SHORT).show();
            }

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
        postParam.addProperty("guardianId",guardianId);
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


        call = webApis.updateGuardianPicture(postParam);

        ApiServices apiServices = new ApiServices(mContext);
        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{

                    UploadProfilePicModel modelVal = new Gson().fromJson(response, UploadProfilePicModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {
                        Toast.makeText(mContext, "Guardian Saved Successfully", Toast.LENGTH_LONG).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });
    }

    private void getGuardianTypes() {
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        String tenantId = sessionManager.getTenantId();
        String organizationId = sessionManager.getOrganizationId();

        call = webApis.getGuardianTypes();
        new ApiServices(mContext).callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    GuardianTypeModel modelVal = new Gson().fromJson(response,GuardianTypeModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {
                        guardianTypeList = new ArrayList<>(modelVal.getResult());
                    }
                    else {
                        Toast.makeText(mContext, "Error ! Please try again", Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }

    private void addGuardian() {
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        postParam.addProperty("id", guardianId);
        postParam.addProperty("firstName",firstName);
        postParam.addProperty("lastName",lastName);
        postParam.addProperty("address",address);
        postParam.addProperty("mobileNumber",mobileNumber);
        postParam.addProperty("guardianTypeId",guardianTypeId);
        String tenantId = sessionManager.getTenantId();
        String organizationId = sessionManager.getOrganizationId();

        call = webApis.createOrUpdateGuardian(postParam);
        new ApiServices(mContext).callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    ResultModel modelVal = new Gson().fromJson(response,ResultModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {
                        if(imageByteArray!=null) {
                            uploadFile();
                        }else {
                            Toast.makeText(mContext, "Guardian Saved Successfully", Toast.LENGTH_LONG).show();
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }

                    }
                    else
                    {
                        Toast.makeText(mContext, "Error! Please Try again", Toast.LENGTH_LONG).show();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }

    private void deleteGuardian() {
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();

        call = webApis.deleteGuardian(guardianId);
        new ApiServices(mContext).callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    ResultModel modelVal = new Gson().fromJson(response,ResultModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {
                        Toast.makeText(mContext, "Deleted Successfully", Toast.LENGTH_LONG).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(mContext, "Error! Please Try again", Toast.LENGTH_LONG).show();

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
