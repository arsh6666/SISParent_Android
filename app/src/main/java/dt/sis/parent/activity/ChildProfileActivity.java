package dt.sis.parent.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.adapters.ProfileTabAdapter;
import dt.sis.parent.databinding.ActivityChildProfileBinding;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.FeaturesModel;

public class ChildProfileActivity extends AppCompatActivity {
    ActivityChildProfileBinding binding;
    Context mContext;
    SessionManager sessionManager;
    ProfileTabAdapter profileTabAdapter;
    String children_list;

    public static  final String PROFILE = "Profile";
    public static  final String ATTENDANCE = "Attendance";
    public static  final String GALLLERY = "Gallery";
    public static  final String COMMENT = "Comment";
    public static  final String FEE = "Fee";
    public static  final String HEALTH = "Health";
    public static  final String CHAT = "Chat";
    List<String> tabItemLIst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_child_profile);
        mContext = ChildProfileActivity.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.customToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && actionBar.isShowing()){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            binding.customToolbar.textView.setText("Profile");
        }
        children_list = getIntent().getStringExtra("children_list");


        if(getTabItemList()!=null && getTabItemList().size()>0) {
            profileTabAdapter = new ProfileTabAdapter(getSupportFragmentManager(), children_list, getTabItemList());
            binding.tabContainer.setAdapter(profileTabAdapter);
            binding.tabContainer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
            binding.tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(binding.tabContainer));
            binding.tabLayout.setupWithViewPager(binding.tabContainer);
            profileTabAdapter.notifyDataSetChanged();

        }

    }

    private List<String> getTabItemList (){
        tabItemLIst = new ArrayList<>();
        try{
            String appFeatures = sessionManager.getFeaturesModel();
            if(appFeatures!=null){
                FeaturesModel featuresModel1 = new Gson().fromJson(appFeatures,FeaturesModel.class);
                if(featuresModel1.getResult()!=null &&
                        featuresModel1.getResult().getFeatures()!=null &&
                        featuresModel1.getResult().getFeatures().getAllFeatures()!=null) {
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
                    setFeaturesModel(isAttendance,ATTENDANCE);
            //        setFeaturesModel(isChat,CHAT);
            //        setFeaturesModel(isFee,FEE);

                    return tabItemLIst;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
      return  tabItemLIst;
    };

    public void setFeaturesModel(String checkValue, String featureTitle){
        if(checkValue.equalsIgnoreCase("true")){
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
