package dt.sis.parent.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dt.sis.parent.R;
import dt.sis.parent.adapters.CommentsListAdapter;
import dt.sis.parent.databinding.ActivityCommentsListBinding;
import dt.sis.parent.databinding.DialogCommentPostBinding;
import dt.sis.parent.databinding.FragProfileInfoBinding;
import dt.sis.parent.databinding.FragmentCommentsBinding;
import dt.sis.parent.dialogs.ParentsCommentsDialog;
import dt.sis.parent.helper.AppUtils;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.CommentsListModel;
import dt.sis.parent.models.DashboardChildrenModel;
import dt.sis.parent.models.PostCommentModel;
import dt.sis.parent.support.Utils;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class CommentsFragment extends Fragment {

    FragmentCommentsBinding binding;
    SessionManager sessionManager;
    Context mContext;
    List<CommentsListModel.Result> commentsList;
    CommentsListAdapter<CommentsListModel.Result> commentListAdapter;
    private int divisionId = 1;
    private int sectionId = 1;
    ParentsCommentsDialog parentsCommentsDialog;

    public static CommentsFragment getInstance() {
        return new CommentsFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comments, container, false);
        View mView = binding.getRoot();
        mContext = getActivity();
        sessionManager = new SessionManager(mContext);
        return mView;
    }

    public void getCommentList() {

        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        postParam.addProperty("DivisionId", divisionId);
        postParam.addProperty("SectionId", sectionId);
        call = webApis.getParentsComments();
        ApiServices apiServices = new ApiServices(mContext);
        apiServices.callWebServices(call, response -> {
            try {
                CommentsListModel modelVal = new Gson().fromJson(response, CommentsListModel.class);
                if (modelVal.getResult() != null) {
                    int totalCount = modelVal.getResult().size();
                    if (totalCount > 0) {

                    } else {
                        Toast.makeText(mContext, "Comments List is empty", Toast.LENGTH_SHORT).show();
                    }
                    commentsList = new ArrayList<>(modelVal.getResult());

                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    mLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    mLayoutManager.setSmoothScrollbarEnabled(true);
                    binding.rvCommentList.setLayoutManager(mLayoutManager);

                    commentListAdapter = new CommentsListAdapter<CommentsListModel.Result>(mContext, commentsList, itemClickListener) {

                        @Override
                        public String getHeader(int position, CommentsListModel.Result result) {
                            String value = result.getTeacherComment();
                            value = (value != null && !value.isEmpty()) ? value : "No Comments";
                            return value;
                        }

                        @Override
                        public String getContent(int position, CommentsListModel.Result result) {
                            String value = result.getParentComment();
                            value = (value != null && !value.isEmpty()) ? value : "No Comments";
                            return value;
                        }

                        @Override
                        public String getName(int position, CommentsListModel.Result result) {
                            String value = result.getFirstName() + " " + result.getLastName();
                            value = value.replace("  ", " ").trim();
                            return value;
                        }

                        @Override
                        public String getDate(int position, CommentsListModel.Result result) {
                            String value = result.getCommentDate();
                            value = (value != null && !value.isEmpty()) ? getCalenderTime(value) : "";
                            return value;
                        }

                    };

                    binding.rvCommentList.setAdapter(commentListAdapter);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private CommentsListAdapter.ItemClickListener itemClickListener = new CommentsListAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            final String id = commentsList.get(position).getId();
            setParentReadComment(id);

            String parentComment = commentsList.get(position).getParentComment();
            String teacherComment = commentsList.get(position).getTeacherComment();
            parentComment = parentComment != null ? parentComment : "";
            teacherComment = teacherComment != null ? teacherComment : "";

            parentsCommentsDialog = new ParentsCommentsDialog(mContext, parentComment, teacherComment, new ParentsCommentsDialog.ItemClickListener() {
                @Override
                public void onCancelItemClick(View view) {
                    parentsCommentsDialog.dismiss();
                }

                @Override
                public void onPosItemClick(String message) {
                    if (!message.isEmpty()) {
                        if (Utils.isNetworkAvailable(mContext)) {
                            Utils.hideKeyboard(getActivity());
                            postCommentApi(message, id);
                        } else
                            Toast.makeText(mContext, "Internet required", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "Please Enter comment", Toast.LENGTH_LONG).show();
                    }
                }
            });
            parentsCommentsDialog.setCancelable(true);
            parentsCommentsDialog.show();
//               }

        }
    };

    private String getCalenderTime(String dateString) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date dateVal = input.parse(dateString.trim());         // parse input
            return output.format(dateVal);         // format output
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void setParentReadComment(String commentId) {

        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);

        String parentId = sessionManager.getUserId();
        JsonObject postParam = new JsonObject();
        postParam.addProperty("parentId", parentId);
        postParam.addProperty("commentId", commentId);

        call = webApis.SetParentReadComment(postParam);
        ApiServices apiServices = new ApiServices(mContext);
        apiServices.setDisplayDialog(false);
        apiServices.callWebServices(call, new ApiServices.ServiceCallBack() {
            @Override
            public void success(String response) {
                try {
                    PostCommentModel modelVal = new Gson().fromJson(response, PostCommentModel.class);
                    if (modelVal.getSuccess()) {

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }

    private void postCommentApi(String postMessage, String id) {

        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);

        JsonObject postParam = new JsonObject();
        postParam.addProperty("id", id);
        postParam.addProperty("message", postMessage);

        call = webApis.ReplyToTeacherComment(postParam);
        ApiServices apiServices = new ApiServices(mContext);

        apiServices.callWebServices(call, new ApiServices.ServiceCallBack() {
            @Override
            public void success(String response) {
                try {
                    PostCommentModel modelVal = new Gson().fromJson(response, PostCommentModel.class);
                    if (modelVal.getSuccess()) {
                        Toast.makeText(mContext, "Comment posted successfully", Toast.LENGTH_SHORT).show();
//                        if(postAlertDialog.isShowing()) postAlertDialog.dismiss();
                        if (parentsCommentsDialog.isShowing()) parentsCommentsDialog.dismiss();
                        getCommentList();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }

}
