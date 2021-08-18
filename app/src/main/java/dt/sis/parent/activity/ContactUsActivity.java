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
import dt.sis.parent.databinding.ActivityContactUsBinding;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.ContactUsModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class ContactUsActivity extends AppCompatActivity {

    ActivityContactUsBinding binding;
    Context mContext;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        mContext = ContactUsActivity.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.customToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && actionBar.isShowing()){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            binding.customToolbar.textView.setText("Contact Us");
        }

        binding.btnSend.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_Send:
                    String stname  = binding.etName.getText().toString().trim();
                    String stemail  = binding.etEmail.getText().toString().trim();
                    String stphone  = binding.etPhoneNo.getText().toString().trim();
                    String stmsg  = binding.etMessage.getText().toString().trim();
                    if (stname.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter Your Name", Toast.LENGTH_LONG).show();
                    } else if (stemail.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter your email address", Toast.LENGTH_LONG).show();
                    }else if (!checkValidEmail(stemail)) {
                        Toast.makeText(getApplicationContext(), "Please enter your valid email", Toast.LENGTH_LONG).show();
                    }else if (stphone.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter your phone number", Toast.LENGTH_LONG).show();
                    }else if (stmsg.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter your message", Toast.LENGTH_LONG).show();
                    }else {
                        contactUsAPI(stname,stphone,stemail,stmsg);
                    }
                    break;

                default:break;
            }
        }
    };



    private boolean checkValidEmail(String data) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(data).matches()) {
            return true;
        }
        return false;
    }

    private void contactUsAPI(String stname, String stphone,String stemail,String stmsg) {
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        postParam.addProperty("name",stname);
        postParam.addProperty("contactNumber",stphone);
        postParam.addProperty("email",stemail);
        postParam.addProperty("message",stmsg);

        call = webApis.contactusApi(postParam);
        new ApiServices(mContext).callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    ContactUsModel modelVal = new Gson().fromJson(response,ContactUsModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {
                        Toast.makeText(ContactUsActivity.this, "Request submitted successfully.", Toast.LENGTH_LONG).show();
                       finish();

                    }
                    else
                    {
                        Toast.makeText(ContactUsActivity.this, "Something went wrong! Try Again", Toast.LENGTH_LONG).show();

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
