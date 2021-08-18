package dt.sis.parent.adapters;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.activity.ChildProfileActivity;
import dt.sis.parent.fragments.AttendanceFragment;
import dt.sis.parent.fragments.CommentsFragment;
import dt.sis.parent.fragments.GalleryFragment;
import dt.sis.parent.fragments.HealthFragment;
import dt.sis.parent.fragments.ProfileFragment;

public class ProfileTabAdapter extends FragmentPagerAdapter {
    List<String> tabTitles;
    String children_list;

    public ProfileTabAdapter(FragmentManager fm, String children_list, List<String> tabTitles) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.children_list = children_list;
        this.tabTitles = tabTitles;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment= null;
        try {
            String titleName  = tabTitles.get(position);

            switch (titleName){
                case ChildProfileActivity.PROFILE:
                    fragment = ProfileFragment.getInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("children_list",children_list);
                    fragment.setArguments(bundle);
                    return fragment;

                case ChildProfileActivity.GALLLERY:
                    fragment = GalleryFragment.getInstance();
                    bundle = new Bundle();
                    bundle.putString("children_list",children_list);
                    fragment.setArguments(bundle);
                    return fragment;

                case ChildProfileActivity.COMMENT:
                    fragment = CommentsFragment.getInstance();
                    return fragment;

                case ChildProfileActivity.HEALTH:
                    fragment = HealthFragment.getInstance();
                    bundle = new Bundle();
                    bundle.putString("children_list",children_list);
                    fragment.setArguments(bundle);
                    return fragment;

                case ChildProfileActivity.ATTENDANCE:
                    fragment = AttendanceFragment.getInstance();
                    bundle = new Bundle();
                    bundle.putString("children_list",children_list);
                    fragment.setArguments(bundle);
                    return fragment;

                default:
                    return CommentsFragment.getInstance();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return fragment;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return  tabTitles.get(position);
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}