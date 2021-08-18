package dt.sis.parent.webservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;


import dt.sis.parent.R;
import dt.sis.parent.helper.NetworkConnection;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.APIError;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Er. Shashank on 21 Sep 2019.
 */

public class ApiServices {
    private Context mContext;
    private SessionManager myUtility;
    private Activity activity;
    boolean isDisplayDialog = true;
    AlertDialog progressDialog;

    public ApiServices(Context mContext) {
        this.mContext = mContext;
        myUtility = new SessionManager(mContext);
    }

    public void setDisplayDialog(boolean displayDialog) {
       this.isDisplayDialog = displayDialog;
    }

    public void callWebServices(Call<JsonObject> call, final ServiceCallBack serviceCallBack) {
        if(isDisplayDialog) {
            progressDialog = myUtility.getProgressAlert();
        }else{
            progressDialog = null;
        }

        SessionManager.displayURL(call);
        activity =  mContext instanceof Activity ? ((Activity) mContext) : null;

        if(new NetworkConnection(mContext).isConnected()){
            if(activity!=null && !activity.isFinishing() && progressDialog!=null)
                progressDialog.show();

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    try{
                        if(activity!=null && !activity.isFinishing() && progressDialog!=null)
                            progressDialog.dismiss();
                        long rT = response.raw().receivedResponseAtMillis();
                        long sT = response.raw().sentRequestAtMillis();
                        Log.e("RESPONSE", response.toString());
                        Log.e("RESPONSE_HEADER",response.raw().headers().toString());
                        Log.e("RESPONSE_TIME", String.valueOf(rT-sT));
                        SessionManager.largeLog("RESPONSE_RESULT",response.body()!=null ?response.body().toString():"");

                         if (response.isSuccessful() && response.code()==200 && response.body()!=null) {
                            String returnData ="";
                             returnData = response.body().toString();
                             serviceCallBack.success(returnData);
                         }else {
                             APIError error = ErrorUtils.parseError(mContext,response);
                             // … and use it to show error information
                             // … or just log the issue like we’re doing :)
                             if(!error.getSuccess()) {
                                 String titleVal = "Oops !";
                                 String messageVal = error.getError().getMessage()!=null ? error.getError().getMessage() : "";
                                 myUtility.createMessageAlertDialog(titleVal, messageVal);
                             }
                         }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                    if(activity!=null && !activity.isFinishing() && progressDialog!=null)
                        progressDialog.dismiss();
                    String titleVal = mContext.getString(R.string.error);
                    String messageVal = mContext.getString(R.string.error_message);
                    Log.e("Error", t.toString());
                    myUtility.createMessageAlertDialog(titleVal, messageVal);

                }
            });

        }else{
            String titleVal = mContext.getString(R.string.no_connection);
            String messageVal = mContext.getString(R.string.no_connection_message);
            myUtility.createMessageAlertDialog(titleVal, messageVal);
        }

    }

    public void downloadFileServices(Call<ResponseBody> call, final ServiceCallBack serviceCallBack) {
        if(isDisplayDialog) {
            progressDialog = myUtility.getProgressAlert();
        }else{
            progressDialog = null;
        }
        SessionManager.displayResonoseBody(call);
        activity =  mContext instanceof Activity ? ((Activity) mContext) : null;

        if(new NetworkConnection(mContext).isConnected()){
            if(activity!=null && !activity.isFinishing() && progressDialog!=null)
                progressDialog.show();

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    try{
                        if(activity!=null && !activity.isFinishing() && progressDialog!=null)
                            progressDialog.dismiss();
                        long rT = response.raw().receivedResponseAtMillis();
                        long sT = response.raw().sentRequestAtMillis();
                        Log.e("RESPONSE", response.toString());
                        Log.e("RESPONSE_HEADER",response.raw().headers().toString());
                        Log.e("RESPONSE_TIME", String.valueOf(rT-sT));
                        SessionManager.largeLog("RESPONSE_RESULT",response.body()!=null ?response.body().toString():"");

                        if (response.isSuccessful() && response.code()==200 && response.body()!=null) {
                            String returnData ="";
                            returnData = response.body().toString();
                            serviceCallBack.success(returnData);

//                        } else if(response.code()==500){
//
//                             String titleVal = mContext.getString(R.string.session_expired);
//                             String messageVal = mContext.getString(R.string.session_expired_message);
//                             seesionExpired(titleVal,messageVal);
//

                        }
                        else {
                            String titleVal = "Error: "+response.code();
                            String messageVal = response.message();
                            myUtility.createMessageAlertDialog(titleVal, messageVal);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                    if(activity!=null && !activity.isFinishing() && progressDialog!=null)
                        progressDialog.dismiss();
                    String titleVal = mContext.getString(R.string.error);
                    String messageVal = mContext.getString(R.string.error_message);
                    Log.e("Error", t.toString());
                    myUtility.createMessageAlertDialog(titleVal, messageVal);

                }
            });

        }else{
            String titleVal = mContext.getString(R.string.no_connection);
            String messageVal = mContext.getString(R.string.no_connection_message);
            myUtility.createMessageAlertDialog(titleVal, messageVal);
        }

    }

    private void seesionExpired(String title, String message) {
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
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new SessionManager(mContext).logout();

                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }

    public interface ServiceCallBack {
        void success(String response);
    }


}