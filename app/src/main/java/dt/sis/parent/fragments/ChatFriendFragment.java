package dt.sis.parent.fragments;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jackandphantom.blurimage.BlurImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.BuildConfig;
import dt.sis.parent.R;
import dt.sis.parent.activity.ChatRoomActivity;
import dt.sis.parent.activity.ChildProfileActivity;
import dt.sis.parent.activity.EditProfileActivity;
import dt.sis.parent.adapters.ChatFriendAdapter;
import dt.sis.parent.adapters.DashboardChildAdapter;
import dt.sis.parent.adapters.NoticeBoardAdapter;
import dt.sis.parent.databinding.FragmentDashboardBinding;
import dt.sis.parent.databinding.FregmentChatFriendListBinding;
import dt.sis.parent.helper.AppUtils;
import dt.sis.parent.helper.NetworkConnection;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.ChatFriendModel;
import dt.sis.parent.models.DashboardChildrenModel;
import dt.sis.parent.models.NoticeboardModel;
import dt.sis.parent.models.ProfileImageModel;
import dt.sis.parent.models.ProfileModel;
import dt.sis.parent.models.RefreshTokenModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatFriendFragment extends Fragment {
    FregmentChatFriendListBinding binding;
    Context mContext;
    SessionManager sessionManager;

    List<ChatFriendModel.Friends> chatList;
    ChatFriendAdapter<ChatFriendModel.Friends> chatListAdapter;

    public static ChatFriendFragment getInstance() {
        return new ChatFriendFragment();
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fregment_chat_friend_list, container, false);
        View mView = binding.getRoot();
        mContext = getActivity();
        sessionManager = new SessionManager(mContext);
        setChatListAdapter();
        getChat();

        return mView;
    }

    private void updateList(List<ChatFriendModel.Friends> updateList){
        chatListAdapter.updateData(updateList);
        chatListAdapter.notifyDataSetChanged();
    }
    private void setChatListAdapter(){
        chatList = new ArrayList<>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        binding.rvList.setLayoutManager(mLayoutManager);
        chatListAdapter = new ChatFriendAdapter<ChatFriendModel.Friends>(mContext, chatList, new ChatFriendAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ChatFriendModel.Friends friendObj = chatList.get(position);
                String friendsData = new Gson().toJson(friendObj);
                startActivity(new Intent(mContext, ChatRoomActivity.class)
                        .putExtra("friends_data",friendsData)
                );

            }
        }) {
            @Override
            public String geUserName(int position, ChatFriendModel.Friends friends) {
                String value = friends.getFriendname();
                value = (value!=null && !value.isEmpty()) ? value : "";
                return value;
            }

            @Override
            public String getMessage(int position, ChatFriendModel.Friends friends) {
                boolean isonline = friends.getIsonline();
                String value = isonline ? "Online" :"Offline" ;
                return value;
            }

            @Override
            public String getPicture(int position, ChatFriendModel.Friends friends) {
                int friendId = friends.getFrienduserid();
                String filePath = SessionManager.FRIEND_FILE_URL+"?friendId="+friendId+"&tenantId=" + friends.getFriendtenantid();
                return filePath;
            }
        };
        binding.rvList.setAdapter(chatListAdapter);
    }

    private void getChat() {
        String tenantId = sessionManager.getTenantId();
        String organizationId = sessionManager.getOrganizationId();
        String teacherCode = sessionManager.getTeacherCode();
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        call = webApis.getUserChatFriends(teacherCode,tenantId,organizationId);
        ApiServices apiServices = new ApiServices(mContext);
        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    ChatFriendModel modelVal = new Gson().fromJson(response, ChatFriendModel.class);
                    if(modelVal.getResult()!=null) {
                        chatList = new ArrayList<>(modelVal.getResult().getFriends());
                        updateList(chatList);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });
    }

}
