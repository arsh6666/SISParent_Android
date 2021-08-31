package dt.sis.parent.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dt.sis.parent.R;
import dt.sis.parent.adapters.ProfileTabAdapter;
import dt.sis.parent.databinding.ActivityChildProfileBinding;
import dt.sis.parent.fragments.GalleryFragment;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.DashboardChildrenModel;
import dt.sis.parent.models.FeaturesModel;
import dt.sis.parent.models.LoginModel;
import dt.sis.parent.models.student_comment.CommentStudentResponse;
import dt.sis.parent.support.Constants;
import dt.sis.parent.support.Utils;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class ChildProfileActivity extends AppCompatActivity {
    ActivityChildProfileBinding binding;
    Context mContext;
    SessionManager sessionManager;
    ProfileTabAdapter profileTabAdapter;
    String children_list;

    public static final String PROFILE = "Profile";
    public static final String ATTENDANCE = "Attendance";
    public static final String GALLLERY = "Gallery";
    public static final String COMMENT = "Comment";
    public static final String FEE = "Fee";
    public static final String HEALTH = "Health";
    public static final String CHAT = "Chat";
    List<String> tabItemLIst;
    GalleryFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_child_profile);
        mContext = ChildProfileActivity.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.customToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && actionBar.isShowing()) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            binding.customToolbar.textView.setText("Profile");

            binding.customToolbar.ivDownload.setOnClickListener(v -> {
                Constants.galleryFragment.checkPermission();


            });
            binding.customToolbar.ivAdd.setOnClickListener(v -> {
                addCommentPopup();
            });
        }
        children_list = getIntent().getStringExtra("children_list");


        if (getTabItemList() != null && getTabItemList().size() > 0) {
            profileTabAdapter = new ProfileTabAdapter(getSupportFragmentManager(), children_list, getTabItemList(), this);
            binding.tabContainer.setAdapter(profileTabAdapter);
            binding.tabContainer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
            binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (Objects.equals(tab.getText(), "Gallery")) {
                        visibleDownloadButton("visible");
                        visibleAddButton("gone");
                    } else if (Objects.equals(tab.getText(), "Comment")) {
                        visibleAddButton("visible");
                        visibleDownloadButton("gone");
                    } else {
                        visibleDownloadButton("gone");
                        visibleAddButton("gone");
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            binding.tabLayout.setupWithViewPager(binding.tabContainer);
            profileTabAdapter.notifyDataSetChanged();
        }

    }

    public void visibleDownloadButton(String type) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && actionBar.isShowing()) {
            if (type.equals("visible"))
                binding.customToolbar.ivDownload.setVisibility(View.VISIBLE);
            else binding.customToolbar.ivDownload.setVisibility(View.GONE);
        }
    }

    public void visibleAddButton(String type) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && actionBar.isShowing()) {
            if (type.equals("visible"))
                binding.customToolbar.ivAdd.setVisibility(View.VISIBLE);
            else binding.customToolbar.ivAdd.setVisibility(View.GONE);
        }
    }

    private void addCommentPopup() {
        try {
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.toast_comment_popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            ImageView ivBack = dialog.findViewById(R.id.iv_back);
            ivBack.setOnClickListener(v -> dialog.dismiss());

            TextView tvPost = dialog.findViewById(R.id.tv_post);
            tvPost.setOnClickListener(v -> {
                EditText etComment = dialog.findViewById(R.id.et_comment);
                String commentStr = etComment.getText().toString();
                if (!commentStr.isEmpty()) {
                    dialog.dismiss();
                    postComment(commentStr);
                }
            });

            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postComment(String comment) {
        DashboardChildrenModel.Students childrenModel = new Gson().fromJson(children_list, new TypeToken<DashboardChildrenModel.Students>() {
        }.getType());
        int studentId = childrenModel.getId();
        String studentCode = childrenModel.getStudentcode();

        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        postParam.addProperty("parentId", sessionManager.getUserId());
        postParam.addProperty("comment", comment);
        postParam.addProperty("studentId", studentId);
        postParam.addProperty("studentCode", studentCode);

        call = webApis.sendCommentForStudent(postParam);
        new ApiServices(mContext).callWebServices(call, response -> {
            try {
                CommentStudentResponse modelResponse = (CommentStudentResponse) Utils.getObject(response, CommentStudentResponse.class);
                if (modelResponse != null && modelResponse.getSuccess() != null && modelResponse.getSuccess())
                    Toast.makeText(mContext, "Successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private List<String> getTabItemList() {
        tabItemLIst = new ArrayList<>();
        try {
            String appFeatures = sessionManager.getFeaturesModel();
            if (appFeatures != null) {
                FeaturesModel featuresModel1 = new Gson().fromJson(appFeatures, FeaturesModel.class);
                if (featuresModel1.getResult() != null &&
                        featuresModel1.getResult().getFeatures() != null &&
                        featuresModel1.getResult().getFeatures().getAllFeatures() != null) {
                    FeaturesModel.AllFeatures allFeatures = featuresModel1.getResult().getFeatures().getAllFeatures();
                    String isAttendance = allFeatures.getMobileParentAttendanceFeature().getValue();
                    String isGallery = allFeatures.getMobileParentGalleryFeature().getValue();
                    String isComment = allFeatures.getMobileParentCommentFeature().getValue();
                    String isHealth = allFeatures.getMobileParentDailyRoutineFeature().getValue();
                    String isChat = allFeatures.getMobileParentChatFeature().getValue();
                    String isFee = allFeatures.getMobileParentFeeManagmentFeature().getValue();
                    String isProfile = "true";

                    setFeaturesModel(isProfile, PROFILE);
                    setFeaturesModel(isGallery, GALLLERY);
                    setFeaturesModel(isComment, COMMENT);
                    setFeaturesModel(isHealth, HEALTH);
                    setFeaturesModel(isAttendance, ATTENDANCE);
                    //        setFeaturesModel(isChat,CHAT);
                    //        setFeaturesModel(isFee,FEE);

                    return tabItemLIst;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tabItemLIst;
    }

    ;

    public void setFeaturesModel(String checkValue, String featureTitle) {
        if (checkValue.equalsIgnoreCase("true")) {
            tabItemLIst.add(featureTitle);
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
