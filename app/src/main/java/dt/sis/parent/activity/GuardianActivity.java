package dt.sis.parent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.adapters.GuardianListAdapter;
import dt.sis.parent.databinding.ActivityChangePasswordBinding;
import dt.sis.parent.databinding.ActivityGuardianBinding;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.ChangePwdModel;
import dt.sis.parent.models.GuardianListModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class GuardianActivity extends AppCompatActivity {

    ActivityGuardianBinding binding;
    Context mContext;
    SessionManager sessionManager;
    List<GuardianListModel.Result> guardResultList;
    GuardianListAdapter<GuardianListModel.Result> guardianListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_guardian);
        mContext = GuardianActivity.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.customToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && actionBar.isShowing()){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            binding.customToolbar.textView.setText("Guardian List");
        }

        binding.btnAddGuardian.setOnClickListener(onClickListener);
        setGuardianAdapter();
        getGuardiansList();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_AddGuardian:
                    startActivity(new Intent(mContext, AddGuardianActivity.class));
                    break;

                default:break;
            }
        }
    };

    private void updateGuardianAdapter(List<GuardianListModel.Result> updateList){
        guardResultList = new ArrayList<>(updateList);
        guardianListAdapter.updateData(guardResultList);
        guardianListAdapter.notifyDataSetChanged();
    }

    private void setGuardianAdapter(){
        guardResultList = new ArrayList<>();
        guardianListAdapter = new GuardianListAdapter<GuardianListModel.Result>(mContext, guardResultList, new GuardianListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GuardianListModel.Result  guardian_data = guardResultList.get(position);
                String guardianData = new Gson().toJson(guardian_data);
                Intent intent = new Intent(mContext, AddGuardianActivity.class);
                intent.putExtra("guardian_data",guardianData);
                startActivityForResult(intent,99);

            }
        }) {
            @Override
            public String getName(int position, GuardianListModel.Result result) {
                String firstName = result.getFirstname();
                firstName = firstName!=null ? firstName: "";
                String lastName = result.getLastname();
                lastName = lastName!=null ? lastName: "";

                String displayName = (firstName+" "+ lastName).trim();
                return  displayName;
            }

            @Override
            public String getNumber(int position, GuardianListModel.Result result) {
                String value = result.getMobilenumber();
                value = value!=null ? value: "";
                return value;
            }

            @Override
            public String getPicture(int position, GuardianListModel.Result result) {
                int id = result.getId();
                String imagePath = SessionManager.GUARDIAN_FILE_URL+"?guardianId="+id;
                return imagePath;
            }
        };


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        binding.rvGuardianList.setLayoutManager(mLayoutManager);
        binding.rvGuardianList.setAdapter(guardianListAdapter);
    }

    private void getGuardiansList() {
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        String tenantId = sessionManager.getTenantId();
        String organizationId = sessionManager.getOrganizationId();

        call = webApis.getGuardians(tenantId,organizationId);
        new ApiServices(mContext).callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    GuardianListModel modelVal = new Gson().fromJson(response,GuardianListModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {
                        guardResultList = new ArrayList<>(modelVal.getResult());
                        if(guardResultList.size()>0){
                            updateGuardianAdapter(guardResultList);
                        }else{
                            Toast.makeText(mContext, "Guardian List empty", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(GuardianActivity.this, "Error, Please try again.", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==99 && resultCode == RESULT_OK){
            getGuardiansList();
        }
    }
}
