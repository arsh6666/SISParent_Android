package dt.sis.parent.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ActivitySchoolBinding;
import dt.sis.parent.dialogs.OrganizationDialog;
import dt.sis.parent.dialogs.SchoolDialog;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.OrganizationModel;
import dt.sis.parent.models.SchoolTenantModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class SchoolActivity extends AppCompatActivity {

    ActivitySchoolBinding binding;
    Context mContext;
    SessionManager sessionManager;
    SchoolDialog schoolDialog;
    OrganizationDialog organizationDialog;
    List<SchoolTenantModel.Result> tanentList;
    List<OrganizationModel.Result> organizationList;
    int organizationId=-1;
    int tenantId=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_school);
        mContext = SchoolActivity.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.customToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && actionBar.isShowing()){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            binding.customToolbar.textView.setText("Select School");
        }

        binding.branchTv.setOnClickListener(onClickListener);
        binding.schoolTv.setOnClickListener(onClickListener);
        binding.getStartButton.setOnClickListener(onClickListener);

        getSchoolTenants();
//        getOraganizationBranch();

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.schoolTv:
                    if(tanentList.size()>0) {
                        openSchoolTenant();
                    }
                    break;

                case R.id.branchTv:
                    if(tenantId!=-1) {
                        openOrganization();
                    }else{
                        Toast.makeText(mContext, "Please select a tenant", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.getStartButton:
                    if (organizationId != -1 && tenantId!=-1) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("organizationId",organizationId);
                        returnIntent.putExtra("tenantId",tenantId);
                        setResult(Activity.RESULT_OK,returnIntent);
                        onBackPressed();

                    } else {
                        Toast.makeText(mContext, "Please select tenant and branch", Toast.LENGTH_LONG).show();
                    }
                    break;

                default:break;
            }
        }
    };
    private void openOrganization() {


        organizationDialog = new OrganizationDialog(mContext, organizationList, new OrganizationDialog.ItemClickListener() {
            @Override
            public void onCancelClick() {
                organizationDialog.dismiss();
            }

            @Override
            public void onSaveClick(List<OrganizationModel.Result> optionList) {
                String name ="";
                for(int i=0; i<optionList.size() ;i++){
                    if(optionList.get(i).isChecked()){
                        name = optionList.get(i).getOrganizationunitname();
                        organizationId  =optionList.get(i).getId();

                        break;
                    }
                }
                binding.branchTv.setText(name);
                organizationDialog.dismiss();

            }

        });

        organizationDialog.setCancelable(true);
        organizationDialog.show();
    }

    private void openSchoolTenant() {

        schoolDialog = new SchoolDialog(mContext, tanentList, new SchoolDialog.ItemClickListener() {
            @Override
            public void onCancelClick() {
                schoolDialog.dismiss();
            }

            @Override
            public void onSaveClick(List<SchoolTenantModel.Result> optionList) {
                String name ="";
                for(int i=0; i<optionList.size() ;i++){
                    if(optionList.get(i).isChecked()){
                        name = optionList.get(i).getName();
                        tenantId  =optionList.get(i).getTenantid();

                        break;
                    }
                }
                binding.schoolTv.setText(name);
                schoolDialog.dismiss();
                getOraganizationBranch();

            }

        });

        schoolDialog.setCancelable(true);
        schoolDialog.show();
    }

    private void getSchoolTenants() {
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();

        call = webApis.getSchoolTenants();
        new ApiServices(mContext).callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    SchoolTenantModel modelVal = new Gson().fromJson(response,SchoolTenantModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {
                        tanentList = new ArrayList<>(modelVal.getResult());
                    }
                    else {
                        Toast.makeText(SchoolActivity.this,"Error! please try again", Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }

    private void getOraganizationBranch() {
        final String organizationId = sessionManager.getOrganizationId();

        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();

        call = webApis.getOrganizationForTenants(String.valueOf(tenantId),organizationId);
        new ApiServices(mContext).callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                     OrganizationModel organizationModel = new Gson().fromJson(response,OrganizationModel.class);
                    boolean status = organizationModel.getSuccess();
                    if(status) {
                        organizationList = new ArrayList<>(organizationModel.getResult());
                    }
                    else
                    {
                        Toast.makeText(SchoolActivity.this,"Error! please try again", Toast.LENGTH_LONG).show();

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
