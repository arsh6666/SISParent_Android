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

import dt.sis.parent.R;
import dt.sis.parent.databinding.ActivityResetPasswordBinding;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.ResetPwdModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class ResetPasswordActivity extends AppCompatActivity {

    ActivityResetPasswordBinding binding;
    Context mContext;
    SessionManager sessionManager;

    String user_id ;

    String reset_code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        mContext = ResetPasswordActivity.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.customToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && actionBar.isShowing()){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            binding.customToolbar.textView.setText("Reset Password");
        }
        user_id = getIntent().getStringExtra("user_id");
        reset_code = getIntent().getStringExtra("reset_code");

        if(reset_code!=null && !reset_code.isEmpty()){
            binding.etResetCode.setText(reset_code);
            binding.etResetCode.setEnabled(false);
            binding.etNewPassword.setFocusable(true);
        }else{
            binding.etResetCode.setEnabled(true);
            binding.etResetCode.setText("");
        }
        binding.btnResetPassword.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.btn_ResetPassword:
                    String resetCode  = binding.etResetCode.getText().toString().trim();
                    String newPwd  = binding.etNewPassword.getText().toString().trim();
                    String cnfPwd  = binding.etConfirmPassword.getText().toString().trim();
                    if (resetCode.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter reset code that sent you on registered email address", Toast.LENGTH_LONG).show();
                    } else if (newPwd.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter new password", Toast.LENGTH_LONG).show();
                    }else if (newPwd.length()<6) {
                        Toast.makeText(getApplicationContext(), "Password must 6 characters", Toast.LENGTH_LONG).show();
                    } else if (!cnfPwd.equals(newPwd)) {
                        Toast.makeText(getApplicationContext(), "New password does't match with confirm password", Toast.LENGTH_LONG).show();
                    } else {
                        changePwdAPI(resetCode,newPwd);
                    }

                    break;

                default:break;
            }
        }
    };

    private void changePwdAPI(final String resetCode, String password) {
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        postParam.addProperty("userId",user_id);
        postParam.addProperty("resetCode",resetCode);
        postParam.addProperty("password",password);

        call = webApis.resetPassword(postParam);
        new ApiServices(mContext).callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    ResetPwdModel modelVal = new Gson().fromJson(response, ResetPwdModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {
                        Toast.makeText(mContext, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();

                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this,"Error! please try again", Toast.LENGTH_LONG).show();

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
