package dt.sis.parent.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ActivityChangePasswordBinding;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.ChangePwdModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class ChangePasswordActivity extends AppCompatActivity {

    ActivityChangePasswordBinding binding;
    Context mContext;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        mContext = ChangePasswordActivity.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.customToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && actionBar.isShowing()){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            binding.customToolbar.textView.setText("Change Password");
        }

        binding.btnChangePassword.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_ChangePassword:
                    String oldPwd  = binding.etOldPassword.getText().toString().trim();
                    String newPwd  = binding.etNewPassword.getText().toString().trim();
                    String cnfPwd  = binding.etConfirmPassword.getText().toString().trim();
                    if (oldPwd.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Enter current password", Toast.LENGTH_LONG).show();
                    } else if (newPwd.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Enter new password", Toast.LENGTH_LONG).show();
                    }else if (newPwd.length()<6) {
                        Toast.makeText(getApplicationContext(), "Password must 6 characters", Toast.LENGTH_LONG).show();
                    } else if (!cnfPwd.equals(newPwd)) {
                        Toast.makeText(getApplicationContext(), "Password do not match", Toast.LENGTH_LONG).show();
                    } else {
                        changePwdAPI(oldPwd,newPwd);
                    }
                    break;

                default:break;
            }
        }
    };

    private void changePwdAPI(final String oldPwd, String newPwd) {
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        postParam.addProperty("currentPassword",oldPwd);
        postParam.addProperty("newPassword",newPwd);

        call = webApis.changePasswordApi(postParam);
        new ApiServices(mContext).callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    ChangePwdModel modelVal = new Gson().fromJson(response,ChangePwdModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {
                        Toast.makeText(ChangePasswordActivity.this, "Password changed Successful", Toast.LENGTH_LONG).show();
                       finish();

                    }
                    else
                    {
                        Toast.makeText(ChangePasswordActivity.this, "Invalid password, please try again", Toast.LENGTH_LONG).show();

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
