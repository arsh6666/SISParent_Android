package dt.sis.parent.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ActivityAboutUsBinding;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.AboutUsModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class AboutUsActivity extends AppCompatActivity {

    ActivityAboutUsBinding binding;
    Context mContext;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        mContext = AboutUsActivity.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.customToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && actionBar.isShowing()){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            binding.customToolbar.textView.setText("About Us");
        }

        getAboutUsData();
    }

    private void getAboutUsData() {

        String tenantId = sessionManager.getTenantId();
        String organizationId = sessionManager.getOrganizationId();
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();

        call = webApis.getAboutUs(tenantId,organizationId);

        ApiServices apiServices = new ApiServices(mContext);
        apiServices.setDisplayDialog(true);
        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try {

                    AboutUsModel modelVal = new Gson().fromJson(response,AboutUsModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            binding.tvMessge.setText(Html.fromHtml(modelVal.getResult().getAboutUsInformation(), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            binding.tvMessge.setText(Html.fromHtml(modelVal.getResult().getAboutUsInformation()));
                        }

                    }
                    else
                    {
                        Toast.makeText(mContext, getString(R.string.something_went_wrong_msg), Toast.LENGTH_SHORT).show();
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
