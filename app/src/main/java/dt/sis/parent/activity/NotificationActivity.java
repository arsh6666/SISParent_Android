package dt.sis.parent.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import dt.sis.parent.adapters.NotificationListAdapter;
import dt.sis.parent.databinding.ActivityNotificationListBinding;
import dt.sis.parent.helper.AppUtils;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.NotifcationModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class NotificationActivity extends AppCompatActivity {

    ActivityNotificationListBinding binding;
    Context mContext;
    SessionManager sessionManager;

    List<NotifcationModel.Items> notificationList;
    NotificationListAdapter<NotifcationModel.Items> notificationListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_list);
        mContext = NotificationActivity.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.customToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && actionBar.isShowing()){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            binding.customToolbar.textView.setText("Notifications");
        }

        getNotificationData();
    }

    private void getNotificationData() {

        String state="1"; //0,1

        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        call = webApis.getNotification(state);
        ApiServices apiServices = new ApiServices(mContext);

        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    NotifcationModel modelVal = new Gson().fromJson(response,NotifcationModel.class);
                    if(modelVal.getResult()!=null) {

                        notificationList= new ArrayList<>(modelVal.getResult().getItems());

                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        mLayoutManager.setSmoothScrollbarEnabled(true);
                        binding.rvCommentList.setLayoutManager(mLayoutManager);
                        notificationListAdapter = new NotificationListAdapter<NotifcationModel.Items>(mContext, notificationList, new NotificationListAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }
                        }) {
                            @Override
                            public String getName(int position, NotifcationModel.Items result) {
                                String value = result.getNotification().getData().getMessage();
                                value = (value!=null && !value.isEmpty()) ? value : "";
                                return value;
                            }

                            @Override
                            public String getDate(int position, NotifcationModel.Items result) {
                                String value = result.getNotification().getCreationtime();
                                value = (value != null && !value.isEmpty()) ? AppUtils.getDate(value) : "";
                                return value;
                            }
                        };

                        binding.rvCommentList.setAdapter(notificationListAdapter);

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
