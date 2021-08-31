package dt.sis.parent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ActivityLoginBinding;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.FeaturesModel;
import dt.sis.parent.models.LoginModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity  {
    Context mContext;
    SessionManager sessionManager;
    ActivityLoginBinding binding;
    private static final int RESET_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mContext = LoginActivity.this;
        sessionManager = new SessionManager(mContext);

        if (sessionManager.getRememberMe().equalsIgnoreCase("1")) {
            binding.etUsername.setText("" + sessionManager.getUserName());
            binding.checkbox.setChecked(true);
        } else {
            binding.etUsername.setText("");
            binding.etPassword.setText("");

            binding.checkbox.setChecked(false);
        }

        binding.btnLogin.setOnClickListener(onClickListener);
        binding.tvForgotpassword.setOnClickListener(onClickListener);
        getAppFeatures();

    }
    private void getAppFeatures() {
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
                            sessionManager.setFeaturesModel(response);
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_Login:
                    String userName  = binding.etUsername.getText().toString().trim();
                    String userPassword  = binding.etPassword.getText().toString().trim();
                    if (userName.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Enter email id", Toast.LENGTH_LONG).show();
                    } else if (userPassword.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Enter password", Toast.LENGTH_LONG).show();
                    } else {
                        if (binding.checkbox.isChecked()) {
                            sessionManager.setRememberMe("1");
                            sessionManager.setUserName(userName);
                        } else {
                            sessionManager.setRememberMe( "0");
                        }
                        gotoAddLoginAPI(userName,userPassword);
                    }
                    break;
                case R.id.tv_forgotpassword:
                    startActivity(new Intent(mContext,ForgetPasswordActivity.class));

                    break;
                    default:break;
            }
        }
    };

    private void gotoAddLoginAPI(final String userName, String password) {
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        postParam.addProperty("userNameOrEmailAddress",userName);
        postParam.addProperty("password",password);

        call = webApis.loginAuthenticate(postParam);
        new ApiServices(mContext).callWebServices(call, response -> {
            try{
                LoginModel modelVal = new Gson().fromJson(response,LoginModel.class);
                boolean status = modelVal.getSuccess();
                if(status) {

                    boolean resetPwd = modelVal.getResult().getShouldResetPassword();
                    List<String> rolesArray = modelVal.getResult().getRoles();

                    int user_id = modelVal.getResult().getUserId();

                    if(resetPwd){
                        sessionManager.setTeacherCode(userName);
                        sessionManager.setUserid(String.valueOf(user_id));
                        String resetCode = modelVal.getResult().getPasswordResetCode();

                        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("user_id", String.valueOf(user_id));
                        intent.putExtra("reset_code", resetCode);
                        startActivityForResult(intent,RESET_REQUEST);
                    }else if(!rolesArray.contains("Parent")){
                        sessionManager.createMessageAlertDialog("","Required permission are not assigned.");
                    }else{
                        String token = modelVal.getResult().getAccessToken();
                        String encodedAccessToken = modelVal.getResult().getEncryptedEncodedAccessToken();
                        String linkId = modelVal.getResult().getLinkId();
                        String refreshToken = modelVal.getResult().getRefreshClientToken();
                        String teacherCode = userName;
                        sessionManager.setToken("Bearer "+token);
                        sessionManager.setEncryptedEncodedAccessToken(encodedAccessToken);
                        sessionManager.setTeacherCode(teacherCode);
                        sessionManager.setUserid(String.valueOf(user_id));
                        sessionManager.setLinkId(linkId);
                        sessionManager.setRefreshToken(refreshToken);
                        sessionManager.setEmail(userName);

                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RESET_REQUEST:
                if (resultCode == RESULT_OK) {
                    binding.etUsername.setText("");
                    binding.etPassword.setText("");
                    binding.checkbox.setChecked(false);
                }
                break;

            default:break;
        }


    }

    @Override
    public void onBackPressed() {
        finish();

    }
}