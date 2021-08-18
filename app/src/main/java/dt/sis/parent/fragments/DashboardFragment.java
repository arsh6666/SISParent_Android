package dt.sis.parent.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jackandphantom.blurimage.BlurImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.BuildConfig;
import dt.sis.parent.R;
import dt.sis.parent.activity.EditProfileActivity;
import dt.sis.parent.activity.ChildProfileActivity;
import dt.sis.parent.adapters.DashboardChildAdapter;
import dt.sis.parent.adapters.NoticeBoardAdapter;
import dt.sis.parent.databinding.FragmentDashboardBinding;
import dt.sis.parent.helper.AppUtils;
import dt.sis.parent.helper.NetworkConnection;
import dt.sis.parent.helper.SessionManager;

import dt.sis.parent.models.DashboardChildrenModel;
import dt.sis.parent.models.NoticeboardModel;
import dt.sis.parent.models.ProfileImageModel;
import dt.sis.parent.models.ProfileModel;
import dt.sis.parent.models.RefreshTokenModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Streaming;


public class DashboardFragment extends Fragment {
    private static final int REQUEST_WRITE_PERMISSION = 99;
    private static final String TAG = "DashboardFragment";

    FragmentDashboardBinding binding;
    SessionManager sessionManager;
    Context mContext;
    List<DashboardChildrenModel.Students> childrenList;
    List<NoticeboardModel.Result> noticeboardList;
    DashboardChildAdapter<DashboardChildrenModel.Students> childrenListAdapter;
    NoticeBoardAdapter<NoticeboardModel.Result> noticeboardListAdapter;
    private String downloadFileName="";

    public static DashboardFragment getInstance() {
        return new DashboardFragment();
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        View mView = binding.getRoot();
        mContext = getActivity();
        sessionManager = new SessionManager(mContext);

        binding.btnEdit.setOnClickListener(onClickListener);

        refreshToken();

        return mView;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.btn_Edit:
                    startActivity(new Intent(mContext, EditProfileActivity.class));
                    break;

                default:
                    break;
            }
        }
    };


    private void refreshToken() {

        String token = sessionManager.getToken().replace("Bearer","").trim();
        String refreshToken = sessionManager.getRefreshToken();
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        postParam.addProperty("token",token);
        postParam.addProperty("refreshToken",refreshToken);

        call = webApis.refreshToken(postParam);
        ApiServices apiServices = new ApiServices(mContext);
        apiServices.setDisplayDialog(true);
        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    RefreshTokenModel modelVal = new Gson().fromJson(response,RefreshTokenModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {

                        String token = modelVal.getResult().getToken();
                        String refreshToken = modelVal.getResult().getRefreshToken();
                        sessionManager.setToken("Bearer "+token);
                        sessionManager.setRefreshToken(refreshToken);
                        gotoProfileImage();


                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }
//
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

//                            Picasso.get()
//                                    .load(imagePath)
//                                    .transform(new BlurTransformation(mContext, 25, 1))
//                                    .into(binding.ivProfileimageLarge);
//
//                            Picasso.get()
//                                    .load(imagePath)
//                                    .error(R.mipmap.ic_round_user)
//                                    .placeholder(R.mipmap.ic_round_user)
//                                    .into(binding.ivProfileimageLarge);

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

                        String name = modelVal.getResult().getName()+" "+ modelVal.getResult().getSurname();
                        binding.tvName.setText(name);
                        String email = modelVal.getResult().getEmailAddress();
                        binding.tvMail.setText(email);

                        getChildren();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }

    private void getChildren() {

        String tenantId = sessionManager.getTenantId();
        String organizationId = sessionManager.getOrganizationId();
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();

        call = webApis.getChildren(tenantId,organizationId);

        ApiServices apiServices = new ApiServices(mContext);
        apiServices.setDisplayDialog(true);
        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    DashboardChildrenModel modelVal = new Gson().fromJson(response,DashboardChildrenModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {

                        if(modelVal.getResult()!=null){

                            childrenList= new ArrayList<>(modelVal.getResult().getStudents());

                            if(childrenList.size()>0){
                                binding.tvNoChild.setVisibility(View.GONE);
                            }else{
                                binding.tvNoChild.setVisibility(View.VISIBLE);
                            }
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            mLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                            mLayoutManager.setSmoothScrollbarEnabled(true);
                            binding.listView.setLayoutManager(mLayoutManager);

                            childrenListAdapter = new DashboardChildAdapter<DashboardChildrenModel.Students>(mContext,childrenList,new DashboardChildAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                    String children_list = new Gson().toJson(childrenList.get(position));

                                    startActivity(new Intent(mContext, ChildProfileActivity.class)
                                            .putExtra("children_list",children_list)
                                    );

                                }
                            }) {
                                @Override
                                public String getPicture(int position, DashboardChildrenModel.Students students) {
                                    String value = students.getProfilePictureId();
                                    value = value!=null  ? value : "";
                                    value = SessionManager.IMAGE_FILE_URL + "?pictureId=" + value + "&tenantId=" + sessionManager.getTenantId();
                                    return value;
                                }

                                @Override
                                public String getSection(int position, DashboardChildrenModel.Students students) {
                                    String value = students.getSectionname();
                                    value = value!=null  ? value : "";
                                    return value;
                                }

                                @Override
                                public String getName(int position, DashboardChildrenModel.Students students) {
                                    String firstName = students.getFirstname();
                                    firstName = firstName!=null ? firstName :"";
                                    String lastName = students.getLastname();
                                    lastName = lastName!=null ? lastName :"";
                                    String value = firstName+" "+lastName;
                                    return value;
                                }

                                @Override
                                public String getGrade(int position, DashboardChildrenModel.Students students) {
                                    String value = students.getSectionname();
                                    value = value!=null ? value : "";
                                    return value;
                                }
                            };
                            binding.listView.setAdapter(childrenListAdapter);
                        }

                        getNoticeboard();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }

    private void downloadManager(Uri uri,String fileName, String contentType){
        try{
            Toast.makeText(mContext, "Downloading has started...", Toast.LENGTH_SHORT).show();
            mContext.registerReceiver(attachmentDownloadCompleteReceive, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            DownloadManager downloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                    DownloadManager.Request.NETWORK_MOBILE);

            request.setTitle(fileName);
//            request.setDescription("");

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

//set the local destination for download file to a path within the application's external files directory
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);

            request.setMimeType(contentType);
            downloadManager.enqueue(request);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    BroadcastReceiver attachmentDownloadCompleteReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                openDownloadedAttachment(context, downloadId);
            }
        }
    };
    private void openDownloadedAttachment(final Context context, final long downloadId) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String downloadLocalUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            String downloadMimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
            if ((downloadStatus == DownloadManager.STATUS_SUCCESSFUL) && downloadLocalUri != null) {
                openDownloadedAttachment(context, Uri.parse(downloadLocalUri), downloadMimeType);
            }
        }
        cursor.close();
    }
    private void openDownloadedAttachment(final Context context, Uri attachmentUri, final String attachmentMimeType) {
        if(attachmentUri!=null) {
            // Get Content Uri.
            if (ContentResolver.SCHEME_FILE.equals(attachmentUri.getScheme())) {
                // FileUri - Convert it to contentUri.
                if(attachmentUri.getPath()!=null) {
                    File file = new File(attachmentUri.getPath());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        attachmentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file);
                    } else {
                        attachmentUri = Uri.fromFile(file);
                    }
                }
            }

            Intent openAttachmentIntent = new Intent(Intent.ACTION_VIEW);
            openAttachmentIntent.setDataAndType(attachmentUri, attachmentMimeType);
            openAttachmentIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.startActivity(openAttachmentIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Unable to open file", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void getNoticeboard() {
        final String tenantId = sessionManager.getTenantId();
        String organizationId = sessionManager.getOrganizationId();
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();

        call = webApis.getNoticeboard(tenantId,organizationId);

        ApiServices apiServices = new ApiServices(mContext);
        apiServices.setDisplayDialog(false);
        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(final String  response) {
                try{
                    NoticeboardModel modelVal = new Gson().fromJson(response,NoticeboardModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {

                        if(modelVal.getResult()!=null){

                            noticeboardList= new ArrayList<>(modelVal.getResult());

                            if(noticeboardList.size()>0){
                                binding.tvNoResultAvailable.setVisibility(View.GONE);
                                binding.rvNoticBoard.setVisibility(View.VISIBLE);
                            }else{
                                binding.tvNoResultAvailable.setVisibility(View.VISIBLE);
                                binding.rvNoticBoard.setVisibility(View.GONE);
                            }
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            mLayoutManager.setOrientation(RecyclerView.VERTICAL);
                            mLayoutManager.setSmoothScrollbarEnabled(true);
                            binding.rvNoticBoard.setLayoutManager(mLayoutManager);

                            noticeboardListAdapter = new NoticeBoardAdapter<NoticeboardModel.Result>(mContext, noticeboardList, new NoticeBoardAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                }

                                @Override
                                public void onDownloadClick(View view, int position) {

                                    downloadFileName = noticeboardList.get(position).getFileId();
                                    String tempFileName = noticeboardList.get(position).getTempFileName();
                                    String contentType = noticeboardList.get(position).getContentType();
                                    contentType = contentType!=null ? contentType : "";
                                    tempFileName = tempFileName!=null ? tempFileName : "";
                                    downloadFileName = downloadFileName!=null ? downloadFileName : "";
                                    boolean isFileAvailable = noticeboardList.get(position).isFileAvailable();
                                    if(isFileAvailable && !downloadFileName.isEmpty()){
//                                        String downloadFileURL = SessionManager.BASE_URL+"File/DownloadFile?"+"fileId="+downloadFileName+"&tenantId="+tenantId;

                                        String downloadFileURL = SessionManager.BASE_URL+"NoticeBoard/"+tempFileName;
                                        try {
//                                            Uri uri = Uri.parse(downloadFileURL);
//                                            downloadManager(uri,tempFileName,contentType);
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadFileURL));
                                            startActivity(browserIntent);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                       // requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,  Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
                                    }
                                }
                            }) {
                                @Override
                                public String getEventDay(int position, NoticeboardModel.Result result) {
                                    String value = result.getEventdate();
                                    value = value!=null  ? AppUtils.getDayFromUTC(value) : "";
                                    return value;
                                }

                                @Override
                                public String getEventDate(int position, NoticeboardModel.Result result) {
                                    String value = result.getEventdate();
                                    value = value!=null && !value.isEmpty() ? AppUtils.getDateFromUTC(value) : "";
                                    return value;
                                }

                                @Override
                                public String getEventTime(int position, NoticeboardModel.Result result) {
                                    String value = result.getEventdate();
                                    value = value!=null  ? AppUtils.getTimeFromUTC(value) : "";
                                    return value;
                                }

                                @Override
                                public String getEventName(int position, NoticeboardModel.Result result) {
                                    String value = result.getName();
                                    value = value!=null  ? value : "";
                                    return value;
                                }

                                @Override
                                public String getEventLocation(int position, NoticeboardModel.Result result) {
                                    String value = result.getLocationname();
                                    value = value!=null  ? value : "";
                                    return value;
                                }

                                @Override
                                public boolean isAvailableFile(int position, NoticeboardModel.Result result) {
                                    return result.isFileAvailable();
                                }
                            };
                            binding.rvNoticBoard.setAdapter(noticeboardListAdapter);
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    downloadFileServices(downloadFileName);
                } else {
                    Toast.makeText(mContext, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void downloadFileServices(String fileName) {
       final  AlertDialog progressDialog = sessionManager.getProgressAlert();
        String tenantId = sessionManager.getTenantId();
        Call<ResponseBody> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();


        call = webApis.downloadFile(fileName,tenantId);
//        call = webApis.downloadFileSample("http://www.africau.edu/images/default/sample.pdf");
        SessionManager.displayResonoseBody(call);

        if(new NetworkConnection(mContext).isConnected()){
            progressDialog.show();

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    try{

                        progressDialog.dismiss();
                        long rT = response.raw().receivedResponseAtMillis();
                        long sT = response.raw().sentRequestAtMillis();
                        Log.e("RESPONSE", response.toString());
                        Log.e("RESPONSE_HEADER",response.raw().headers().toString());
                        Log.e("RESPONSE_TIME", String.valueOf(rT-sT));
                        SessionManager.largeLog("RESPONSE_RESULT",response.body()!=null ?response.body().toString():"");
                        String fileUrl  = call.request().url().toString();

                        if (response.isSuccessful() && response.code()==200 && response.body()!=null) {
                            String fileName = response.headers().get("Content-Disposition");
                            Log.e("Content-Disposition", fileName + " ");
                            if(fileName!=null) {
                                fileName = fileName.substring(fileName.lastIndexOf("filename=")+9);
                                Log.e("fileName", fileName + " ");
                                new DownloadZipFileTask(fileName).execute(response.body());  }
                        }
                        else {
                            String titleVal = "Error: "+response.code();
                            String messageVal = response.message();
                            sessionManager.createMessageAlertDialog(titleVal, messageVal);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                    progressDialog.dismiss();
                    String titleVal = mContext.getString(R.string.error);
                    String messageVal = mContext.getString(R.string.error_message);
                    Log.e("Error", t.toString());
                    sessionManager.createMessageAlertDialog(titleVal, messageVal);

                }
            });

        }else{
            String titleVal = mContext.getString(R.string.no_connection);
            String messageVal = mContext.getString(R.string.no_connection_message);
            sessionManager.createMessageAlertDialog(titleVal, messageVal);
        }

    }

    private class DownloadZipFileTask extends AsyncTask<ResponseBody, Void, Boolean> {
        AlertDialog progressDialog;
        File saveFilePath;
        String fileName;

        public DownloadZipFileTask(String fileName) {
            this.fileName = fileName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = sessionManager.getProgressAlert();
            progressDialog.show();
            saveFilePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

        }

        @Override
        protected Boolean doInBackground(ResponseBody... urls) {
             boolean writtenToDisk = writeResponseBodyToDisk(saveFilePath,urls[0]);
            return writtenToDisk;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(saveFilePath.exists()){
                            progressDialog.dismiss();
                            Toast.makeText(mContext, "File saved: "+saveFilePath.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                            openFilePath(saveFilePath);
                        }
                    }
                },2000);

            }

        }
    }

    public Uri getFileUri( File file) {
        return FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file);
    }
    public static String getMimeType(String filePathOrURL) { //file path or whatever suitable URL you want.
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePathOrURL);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        Log.e("fileMimeType",type+" ");
        return type;
    }
    private void openFilePath(File file) {
        try {
            String fileName = file.getName();
            String extenstion = fileName.substring(fileName.lastIndexOf(".")+1);
            Log.d(TAG, "openFilePath: ."+extenstion);
            String fileMimeType =getMimeType(file.getAbsolutePath());
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(getFileUri(file), fileMimeType);
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Intent intent = Intent.createChooser(target, "Open File");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void saveToDisk(ResponseBody body, String filename) {
        try {

            File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "+Shashank1_"+ System.currentTimeMillis()+".pdf");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);
                byte data[] = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                Log.d(TAG, "File Size=" + fileSize);
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    progress += count;
                }

                outputStream.flush();

                return;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed to save the file!");
                return;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        }
    }
    private boolean writeResponseBodyToDisk(File futureStudioIconFile, ResponseBody body) {
        try {

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

//                    Log.e("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}
