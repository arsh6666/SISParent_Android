package dt.sis.parent.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import androidx.core.content.ContextCompat;

import com.applandeo.materialcalendarview.CalendarUtils;

import dt.sis.parent.R;

public class DrawableUtils {

    private DrawableUtils() {
    }

    public static Drawable getCircleDrawableWithText(Context context, String string) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.sample_circle_green);
        Drawable text = CalendarUtils.getDrawableText(context, string, null, android.R.color.white, 12);

        Drawable[] layers = {background, text};
        return new LayerDrawable(layers);
    }

    public static Drawable getThreeDots(Context context, String string) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.sample_circle_red);
        Drawable text = CalendarUtils.getDrawableText(context, string, null, android.R.color.white, 12);

        Drawable[] layers = {background, text};
        return new LayerDrawable(layers);
    }

    public static Drawable getGerThreeDots(Context context, String string) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.sample_circle_grye);
        Drawable text = CalendarUtils.getDrawableText(context, string, null, android.R.color.white, 12);

        Drawable[] layers = {background, text};
        return new LayerDrawable(layers);
    }


}
