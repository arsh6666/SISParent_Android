package dt.sis.parent.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dt.sis.parent.BuildConfig;
import dt.sis.parent.R;
import dt.sis.parent.databinding.ActivitySplashBinding;
import dt.sis.parent.dialogs.UpdateAppDialog;
import dt.sis.parent.helper.AppUtils;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.FeaturesModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

import static com.google.firebase.iid.FirebaseInstanceId.getInstance;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    Context mContext;
    SessionManager sessionManager;
    String branchId ;
    String tenantId;
    final static int REQUEST_SCHOOL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        mContext = SplashActivity.this;
        sessionManager = new SessionManager(mContext);
        branchId = sessionManager.getOrganizationId();
        tenantId = sessionManager.getTenantId();
        if(BuildConfig.FLAVOR == AppUtils.SIS_MAIN){
            if (tenantId.isEmpty() || branchId.isEmpty()) {
                startActivityForResult(new Intent(mContext, SchoolActivity.class), REQUEST_SCHOOL);
            }else{
                getAppConfig();
            }
        }else{
            if (tenantId.isEmpty() || branchId.isEmpty()) {
                switch (BuildConfig.FLAVOR) {
                    case AppUtils.KIDS_PALACE:
                        branchId = "1";
                        tenantId = BuildConfig.DEBUG ? "10" : "8";
                        break;
                    case AppUtils.THE_PALACE:
                        branchId = "1";
                        tenantId = BuildConfig.DEBUG ? "10" : "9";
                        break;
                    default:
                        break;
                }
                sessionManager.setTenantId(tenantId);
                sessionManager.setOrganizationId(String.valueOf(branchId));
            }
            getAppConfig();
        }
    }
    private void getAppConfig() {
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();

        call = webApis.geEnableAppFeatures();
        new ApiServices(mContext).callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    FeaturesModel modelVal = new Gson().fromJson(response,FeaturesModel.class);
                    if(modelVal.getResult()!=null) {
                        boolean status = modelVal.getSuccess();
                        if (status) {
                            FeaturesModel.Values values = modelVal.getResult().getSetting().getValues();
                            Log.e("values",new Gson().toJson(values));
                            String validation = values.getMobileParentAppValidation();
                            String version = values.getMobileAndroidParentAppVersion();
                            validation = validation!=null ? validation :"";
                            version = version!=null ? version:"";
                            String currentVersion = String.valueOf(BuildConfig.VERSION_CODE);
                            Log.e("versionCode", version + " ");
                            if(validation.equalsIgnoreCase("true") && !version.equalsIgnoreCase(currentVersion)){
                                sessionManager.clearCacheData();
                                UpdateAppDialog updateAppDialog = new UpdateAppDialog(mContext, new UpdateAppDialog.UpdateOnClickListnere() {
                                    @Override
                                    public void onClickUpdate(DialogInterface dialog) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mContext.startActivity(intent);
                                        ((Activity)mContext).finish();
                                    }

                                });
                                updateAppDialog.showAlert();
                            }else {
                                doFurther();
                            }
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_SCHOOL:
                if (resultCode == RESULT_OK && data != null) {
                    int organizationId = data.getIntExtra("organizationId", -1);
                    int tenantId = data.getIntExtra("tenantId", -1);
                    sessionManager.setTenantId(String.valueOf(tenantId));
                    sessionManager.setOrganizationId(String.valueOf(organizationId));
                    getAppConfig();
                }
                break;
            default: finish();
                break;
        }
    }

    private void doFurther() {
        branchId = sessionManager.getOrganizationId();
        tenantId = sessionManager.getTenantId();
        Log.e("branchId",branchId+" ");
        Log.e("tenantId",tenantId+" ");
        if (!branchId.isEmpty() && (!tenantId.isEmpty())) {
            if (sessionManager.getToken().isEmpty()) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }
}
