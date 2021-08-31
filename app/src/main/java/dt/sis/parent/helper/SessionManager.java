package dt.sis.parent.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;

import java.io.IOException;

import dt.sis.parent.R;
import dt.sis.parent.activity.LoginActivity;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;

public class SessionManager {

    Context mContext;
    private static final String STATE = "STATE";
    private static final String USER_ID = "USER_ID";
    private static final String FIRST_TIME_OPEN = "FIRST_TIME_OPEN";
    private static final String TOKEN = "TOKEN";
    private static final String LINKID = "LINKID";
    private static final String PROFILE_PICTURE  = "PROFILE_PICTURE";
    private static final String REMEMBER_ME  = "REMEMBER_ME";
    private static final String USER_NAME  = "USER_NAME";
    private static final String ENCRYPTED_ENCODED_ACCESS_TOKEN  = "ENCRYPTED_ENCODED_ACCESS_TOKEN";
    private static final String REFRESH_TOKEN  = "REFRESH_TOKEN";
    private static final String TEACHER_CODE  = "TEACHER_CODE";
    private static final String S_NAME  = "S_NAME";
    private static final String EMAIL  = "EMAIL";

    private static final String ORGANIZATION_ID  = "ORGANIZATION_ID";
    private static final String TENANT_ID  = "TENANT_ID";
    private static final String S_CLASS  = "S_CLASS";
    private static final String SECTION  = "SECTION";
    private static final String STUDENT_ID  = "STUDENT_ID";
    private static final String FIREBASE_TOKEN  = "FIREBASE_TOKEN";
    private static final String FEATURES_MODEL  = "FEATURES_MODEL";

    public static final String BASE_URL = "http://86.96.201.117:8080/";
//    public static final String IMAGE_FILE_URL = BASE_URL + "file/getstudentimage";
    public static final String IMAGE_FILE_URL = BASE_URL + "file/GetImageByPictureId";
    public static final String GALLERY_FILE_URL = BASE_URL + "Gallery/Getmediafile";
    public static final String DOWNLOAD_NOTICE_FILE_URL = BASE_URL + "File/DownloadFile";
    public static final String GUARDIAN_FILE_URL = BASE_URL + "file/getguardianimage";
    public static final String FRIEND_FILE_URL = BASE_URL + "file/GetFriendsProfilePictureById";
    public static final String WEBSOCKET_HUB_URL = BASE_URL + "signalr-chat";



    SharedPreferences sharedPreferences;

    public SessionManager() {

    }

    public SessionManager(Context mContext){
        this.mContext = mContext;
    }

    private SharedPreferences getRef(){
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }
    public String getFeaturesModel(){
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(FEATURES_MODEL, "");

    }
    public void setFeaturesModel(String featuresModel){
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FEATURES_MODEL, featuresModel);
        editor.apply();
        editor.commit();

    }
    public String getState(){
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(STATE, "");

    }
    public void setState(String state){
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(STATE, state);
        editor.apply();
        editor.commit();

    }
    public String getFirebaseToken(){
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(FIREBASE_TOKEN, "");

    }
    public void setFirebaseToken(String state){
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIREBASE_TOKEN, state);
        editor.apply();
        editor.commit();

    }

    public boolean getAppFirstOpen() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getBoolean(FIRST_TIME_OPEN, true);

    }

    public void setAppFirstOpen(boolean firstTimeAppOpen) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIRST_TIME_OPEN, firstTimeAppOpen);
        editor.apply();
        editor.commit();
    }


    public String getUserId() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(USER_ID, "");
    }

    public void setUserid(String id) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, id);
        editor.apply();
        editor.commit();
    }

    public String getToken() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(TOKEN, "");
    }

    public void setToken(String token) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN, token);
        editor.apply();
        editor.commit();
    }

    public String getLinkId() {
        SharedPreferences sharedPrefs = getRef();

        return sharedPrefs.getString(LINKID, "");
    }

    public void setLinkId(String linkid) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LINKID, linkid);
        editor.apply();
        editor.commit();
    }
   public String getProfilePcture() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(PROFILE_PICTURE, "");
    }

    public void setProfilePicture(String profile_picture) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROFILE_PICTURE, profile_picture);
        editor.apply();
        editor.commit();
    }
    public String getRememberMe() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(REMEMBER_ME, "");
    }

    public void setRememberMe(String rememberMe) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REMEMBER_ME, rememberMe);
        editor.apply();
        editor.commit();
    }
    public String getUserName() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(USER_NAME, "");
    }

    public void setUserName(String username) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, username);
        editor.apply();
        editor.commit();
    }
    public String getEmail() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(EMAIL, "");
    }

    public void setEmail(String username) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, username);
        editor.apply();
        editor.commit();
    }
    public String getEncryptedEncodedAccessToken() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(ENCRYPTED_ENCODED_ACCESS_TOKEN, "");
    }

    public void setEncryptedEncodedAccessToken(String encryptedEncodedAccessToken) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ENCRYPTED_ENCODED_ACCESS_TOKEN, encryptedEncodedAccessToken);
        editor.apply();
        editor.commit();
    }

    public String getRefreshToken() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(REFRESH_TOKEN, "");
    }

    public void setRefreshToken(String refreshToken) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REFRESH_TOKEN, refreshToken);
        editor.apply();
        editor.commit();
    }
    public String getTeacherCode() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(TEACHER_CODE, "");
    }

    public void setTeacherCode(String teacherCode) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEACHER_CODE, teacherCode);
        editor.apply();
        editor.commit();
    }


    public String getSname() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(S_NAME, "");
    }

    public void setSname(String sname) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(S_NAME, sname);
        editor.apply();
        editor.commit();
    }

    public String getSclass() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(S_CLASS, "");
    }

    public void setSclass(String sclass) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(S_CLASS, sclass);
        editor.apply();
        editor.commit();
    }



    public String getSection() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(SECTION, "");
    }

    public void setSection(String section) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SECTION, section);
        editor.apply();
        editor.commit();
    }
  public String getStudentId() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(STUDENT_ID, "");
    }

    public void setStudentId(String student_id) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(STUDENT_ID, student_id);
        editor.apply();
        editor.commit();
    }
    public String getTenantId() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(TENANT_ID, "");
    }

    public void setTenantId(String tenantId) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TENANT_ID, tenantId);
        editor.apply();
        editor.commit();
    }
 public String getOrganizationId() {
        SharedPreferences sharedPrefs = getRef();
        return sharedPrefs.getString(ORGANIZATION_ID, "");
    }

    public void setOrganizationId(String organizationId) {
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ORGANIZATION_ID, organizationId);
        editor.apply();
        editor.commit();
    }

    public static void displayURL(retrofit2.Call<JsonObject> call){
        Log.e("RequestURL",call.request().url().toString());
        Log.e("RequestMethod",call.request().method());
        Log.e("RequestHeader",call.request().headers().toString()+" ");
        Log.e("RequestParams",bodyToString(call.request().body())+" ");

    }
    public static void displayResonoseBody(retrofit2.Call<ResponseBody> call){
        Log.e("RequestURL",call.request().url().toString());
        Log.e("RequestMethod",call.request().method());
        Log.e("RequestHeader",call.request().headers().toString()+" ");
        Log.e("RequestParams",bodyToString(call.request().body())+" ");

    }

    public static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static void largeLog(String title, String message){
//        if (message.length() > 4000) {
//            Log.d("", message.substring(0, 4000));
//            largeLog(message.substring(4000));
//        } else
//            return message;
//        return "";

        int maxLogSize = 10000;
        for(int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            Log.e(title, message.substring(start, end));
        }
    }

    public AlertDialog getProgressAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custom_progress_dialog,null);
        builder.setView(dialogView);

        builder.setCancelable(false); // if you want user to wait for some process to finish,
        AlertDialog dialog = builder.create();
        if(dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    public void createMessageAlertDialog(String title, String message){
        Activity activity=(Activity)mContext;
        if(activity!=null && !activity.isFinishing()){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            if(title!=null && !title.isEmpty()){
                // Initialize a new foreground color span instance
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.colorPrimary));
                // Initialize a new spannable string builder instance
                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);

                ssBuilder.setSpan(foregroundColorSpan,0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                alertDialogBuilder.setTitle(ssBuilder);

            }
            if(message!=null && !message.isEmpty()) {
                alertDialogBuilder.setMessage(message);
            }
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

    }
    public void clearCacheData(){
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN,"");
        editor.apply();
        editor.commit();
    }

    public void logout(){
        SharedPreferences sharedPreferences = getRef();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN,"");
        editor.apply();
        editor.commit();
        ActivityCompat.finishAffinity((Activity)mContext);
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        ((Activity)mContext).finish();
    }

}