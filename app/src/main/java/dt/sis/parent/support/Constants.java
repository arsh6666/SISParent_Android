package dt.sis.parent.support;

import android.annotation.SuppressLint;

import dt.sis.parent.fragments.AttendanceFragment;
import dt.sis.parent.fragments.CommentsFragment;
import dt.sis.parent.fragments.GalleryFragment;
import dt.sis.parent.fragments.HealthFragment;
import dt.sis.parent.fragments.ProfileFragment;

public class Constants {

    @SuppressLint("StaticFieldLeak")
    public static GalleryFragment galleryFragment = null;
    @SuppressLint("StaticFieldLeak")
    public static CommentsFragment commentsFragment = null;
    @SuppressLint("StaticFieldLeak")
    public static HealthFragment healthFragment = null;
    @SuppressLint("StaticFieldLeak")
    public static AttendanceFragment attendanceFragment = null;
}
