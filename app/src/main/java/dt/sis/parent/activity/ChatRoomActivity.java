package dt.sis.parent.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnection;
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnectionListener;
import com.smartarmenia.dotnetcoresignalrclientjava.HubEventListener;
import com.smartarmenia.dotnetcoresignalrclientjava.HubMessage;
import com.smartarmenia.dotnetcoresignalrclientjava.WebSocketHubConnectionP2;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.adapters.ChatMessageAdapter;
import dt.sis.parent.databinding.ActivityChatRoomBinding;
import dt.sis.parent.helper.AppUtils;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.ChatFriendModel;
import dt.sis.parent.models.ChatMessageModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class ChatRoomActivity extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    Context mContext;
    SessionManager sessionManager;
    HubConnection connection;
    List<ChatMessageModel.Items> chatList;
    ChatMessageAdapter<ChatMessageModel.Items> chatListAdapter;

    ChatFriendModel.Friends friendsObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_room);
        mContext = ChatRoomActivity.this;
        sessionManager = new SessionManager(mContext);
        setSupportActionBar(binding.customToolbar.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && actionBar.isShowing()){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        String friendsData = getIntent().getStringExtra("friends_data");
        Type type = new TypeToken<ChatFriendModel.Friends>(){}.getType();
        friendsObj = new Gson().fromJson(friendsData,type);
        binding.customToolbar.textView.setText(friendsObj!=null ? friendsObj.getFriendname() :"Chat Room");
        setChatListAdapter();
        getChat();
        setConfigureChatSetting() ;

        binding.ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.etMessage.getText().toString().trim();
                if(!message.isEmpty()){
                    sendMessage(message);

                }else{
                    Toast.makeText(mContext, "Please write something", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void sendMessage(String message) {
        try {
            JsonObject parameters = new JsonObject();
            parameters.addProperty("tenantId", friendsObj.getFriendtenantid());
            parameters.addProperty("userId", friendsObj.getFrienduserid());
            parameters.addProperty("message", message);
            parameters.addProperty("tenancyName", friendsObj.getFriendtenancyname());
            parameters.addProperty("userName", friendsObj.getFriendusername());
            connection.invoke("sendMessage", parameters);
            binding.etMessage.setText("");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setConfigureChatSetting() {
        try {
            connection = new WebSocketHubConnectionP2(SessionManager.WEBSOCKET_HUB_URL+"?enc_auth_token="+sessionManager.getEncryptedEncodedAccessToken(), sessionManager.getToken());
            connection.addListener(hubConnectionListener);
            connection.subscribeToEvent("sendMessage", hubEventListener);
            connection.connect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    HubConnectionListener hubConnectionListener = new HubConnectionListener() {
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected() {

        }

        @Override
        public void onMessage(HubMessage message) {
            try {
                if(message!=null){
                    String messageArgs = new Gson().toJson(message.getArguments());
                    Log.e("messageArgs=", message.getTarget() + "\n" + messageArgs);
                    JSONArray jsonArray = new JSONArray(messageArgs);
                    if(jsonArray.length()>0 &&  message.getTarget().equalsIgnoreCase("getChatMessage")) {
                        String chatItem = jsonArray.get(0).toString();
                        Log.e("messageARray=", message.getTarget() + "\n" + chatItem);
                        Type type = new TypeToken<ChatMessageModel.Items>(){}.getType();
                        ChatMessageModel.Items chatModel = new Gson().fromJson(chatItem, type);
                        if (chatModel != null && chatList!=null) {
                            chatList.add(chatModel);
                            updateList(chatList);
                        }
                    }
                    else {
                        chatList = new ArrayList<>();
                        updateList(chatList);

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Exception exception) {
            exception.printStackTrace();

        }
    };
    HubEventListener hubEventListener = new HubEventListener() {
        @Override
        public void onEventMessage(HubMessage message) {

        }
    };

    private void updateList(List<ChatMessageModel.Items> updateList){
        setDefaultDate(updateList);
        chatListAdapter.updateData(updateList);
        chatListAdapter.notifyDataSetChanged();
        binding.rvList.smoothScrollToPosition(updateList.size());

    }
    private void setChatListAdapter(){
        chatList = new ArrayList<>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        binding.rvList.setLayoutManager(mLayoutManager);

        chatListAdapter = new ChatMessageAdapter<ChatMessageModel.Items>(mContext,chatList,null) {
            @Override
            public int getSide(int position, ChatMessageModel.Items items) {
                return items.getSide();
            }

            @Override
            public String getMessage(int position, ChatMessageModel.Items items) {
                String value = items.getMessage();
                value = value!=null ? value : "";
                return value;
            }

            @Override
            public String getPicture(int position, ChatMessageModel.Items items) {
                int friendId = items.getSide()==1 ? items.getUserid() : friendsObj.getFrienduserid();
                int tenantId = items.getTenantid();
                String filePath = SessionManager.FRIEND_FILE_URL+"?friendId="+friendId+"&tenantId=" + tenantId;
                return filePath;
            }

            @Override
            public String getCreationTime(int position, ChatMessageModel.Items items) {
                String value = items.getCreationtime();
                value = (value!=null && !value.isEmpty()) ? AppUtils.geTimeFromUTC(value) : "";
                return value;
            }
            @Override
            public String getCreationDate(int position, ChatMessageModel.Items items) {
                String value = items.getCreationtime();
                value = (value!=null && !value.isEmpty()) ? AppUtils.getDateFromUTC(value) : "";
                return value;
            }

            @Override
            public boolean getShowDate(int position, ChatMessageModel.Items items) {
               return items.isShowDate();
            }
        };

        binding.rvList.setAdapter(chatListAdapter);
    }

    public void setDefaultDate(List<ChatMessageModel.Items> chatHistoryList) {
        String oldDate = "";
        for (int i = 0; i < chatHistoryList.size(); i++) {
            if (oldDate.equalsIgnoreCase(AppUtils.getDateFromUTC(chatHistoryList.get(i).getCreationtime()))) {
                chatHistoryList.get(i).setShowDate(false);
            } else {
                oldDate = AppUtils.getDateFromUTC(chatHistoryList.get(i).getCreationtime());
                chatHistoryList.get(i).setShowDate(true);
            }
        }
    }

    private void getChat() {
        String tenantId = sessionManager.getTenantId();
        String organizationId = sessionManager.getOrganizationId();
        String teacherCode = sessionManager.getTeacherCode();
        int friendUserId = friendsObj.getFrienduserid();
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        call = webApis.getUserChatMessage(teacherCode,tenantId,organizationId,friendUserId);
        ApiServices apiServices = new ApiServices(mContext);
        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    ChatMessageModel modelVal = new Gson().fromJson(response, ChatMessageModel.class);
                    if(modelVal.getResult()!=null) {
                        chatList = new ArrayList<>(modelVal.getResult().getItems());
                        updateList(chatList);
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
    protected void onDestroy() {
        if(connection!=null) {
            connection.disconnect();
            connection.removeListener(hubConnectionListener);
            connection.unSubscribeFromEvent("sendMessage", hubEventListener);
            connection.disconnect();
        }
        super.onDestroy();
    }
}
