package dt.sis.parent.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;

import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.activity.ChildProfileActivity;
import dt.sis.parent.fragments.AttendanceFragment;
import dt.sis.parent.fragments.CommentsFragment;
import dt.sis.parent.fragments.GalleryFragment;
import dt.sis.parent.fragments.HealthFragment;
import dt.sis.parent.fragments.ProfileFragment;
import dt.sis.parent.support.Constants;

public class ProfileTabAdapter extends FragmentPagerAdapter {
    List<String> tabTitles;
    String children_list;
    Activity context;
    FragmentManager fragmentManager;

    public ProfileTabAdapter(FragmentManager fm, String children_list, List<String> tabTitles, Activity context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.children_list = children_list;
        fragmentManager = fm;
        this.tabTitles = tabTitles;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        try {
            String titleName = tabTitles.get(position);

            switch (titleName) {
                case ChildProfileActivity.PROFILE:
                    fragment = ProfileFragment.getInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("children_list", children_list);
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().setMaxLifecycle(fragment, Lifecycle.State.STARTED);
                    return fragment;

                case ChildProfileActivity.GALLLERY:
                    if (Constants.galleryFragment == null) {
                        Constants.galleryFragment = GalleryFragment.getInstance();
                        fragment = Constants.galleryFragment;
                    } else fragment = Constants.galleryFragment;
                    bundle = new Bundle();
                    bundle.putString("children_list", children_list);
                    fragment.setArguments(bundle);
                    return fragment;

                case ChildProfileActivity.COMMENT:
                    if (Constants.commentsFragment == null) {
                        Constants.commentsFragment = CommentsFragment.getInstance();
                        fragment = Constants.commentsFragment;
                    } else fragment = Constants.commentsFragment;
                    return fragment;

                case ChildProfileActivity.HEALTH:
                    if (Constants.healthFragment == null) {
                        Constants.healthFragment = HealthFragment.getInstance();
                        fragment = Constants.healthFragment;
                    } else fragment = Constants.healthFragment;
                    bundle = new Bundle();
                    bundle.putString("children_list", children_list);
                    fragment.setArguments(bundle);
                    return fragment;

                case ChildProfileActivity.ATTENDANCE:
                    if (Constants.attendanceFragment == null) {
                        Constants.attendanceFragment = AttendanceFragment.getInstance();
                        fragment = Constants.attendanceFragment;
                    } else fragment = Constants.attendanceFragment;
                    bundle = new Bundle();
                    bundle.putString("children_list", children_list);
                    fragment.setArguments(bundle);
                    return fragment;

                default:
                    if (Constants.commentsFragment == null) {
                        Constants.commentsFragment = CommentsFragment.getInstance();
                        fragment = Constants.commentsFragment;
                    } else fragment = Constants.commentsFragment;
                    return fragment;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
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