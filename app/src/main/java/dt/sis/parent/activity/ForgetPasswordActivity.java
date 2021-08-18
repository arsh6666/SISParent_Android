package dt.sis.parent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ActivityForgetPasswordBinding;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.ResultForgotPwdModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class ForgetPasswordActivity extends AppCompatActivity {

    private static final int RESET_REQUEST = 1;
    ActivityForgetPasswordBinding binding;
    Context mContext;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        mContext = ForgetPasswordActivity.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.customToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && actionBar.isShowing()){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            binding.customToolbar.textView.setText("Forgot Password");
        }

        binding.btnResetPassword.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_resetPassword:
                    String emailAddress  = binding.etEmail.getText().toString().trim();
                    if (emailAddress.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Enter your registered email", Toast.LENGTH_LONG).show();
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                        Toast.makeText(getApplicationContext(), "Please Enter valid email address", Toast.LENGTH_LONG).show();
                    } else {
                        sendResetCode(emailAddress);
                    }
                    break;

                default:break;
            }
        }
    };

    private void sendResetCode(final String emailAddress) {
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        postParam.addProperty("emailAddress",emailAddress);

        call = webApis.sendResetCode(postParam);
        new ApiServices(mContext).callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    ResultForgotPwdModel modelVal = new Gson().fromJson(response, ResultForgotPwdModel.class);
                    boolean status = modelVal.getSuccess();

                    if(status) {
                        String user_id = modelVal.getResult();
                        Intent intent = new Intent(mContext, ResetPasswordActivity.class);
                        intent.putExtra("user_id", user_id);
                        startActivityForResult(intent,RESET_REQUEST);


                    }
                    else
                    {
                        Toast.makeText(ForgetPasswordActivity.this, "Invalid email address", Toast.LENGTH_LONG).show();

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

        switch (requestCode){
            case RESET_REQUEST:
                if (resultCode == RESULT_OK) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                    finish();
                }
                break;

            default:break;
        }


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
