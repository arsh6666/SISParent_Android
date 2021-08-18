package dt.sis.parent.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import androidx.core.content.ContextCompat;

import dt.sis.parent.R;

public class UpdateAppDialog {

    Context mContext;
    UpdateOnClickListnere mClickListnere;
    String title="";
    String message="";

    public void setTitle(String title, String message) {
        this.title = title;
        this.message = message;

    }

    public UpdateAppDialog(Context mContext, UpdateOnClickListnere mClickListnere) {
        this.mContext = mContext;
        this.mClickListnere = mClickListnere;
    }

    public interface UpdateOnClickListnere {
        void onClickUpdate(DialogInterface dialog);
    }
    public void showAlert(){
        String defaultTitle = "Update Available!";
        String defaultMessages = "A new version of this app is available. Please update to continue.";
        title = !title.isEmpty() ? title : defaultTitle;
        message = !message.isEmpty() ? message : defaultMessages;
        Activity activity=(Activity)mContext;
        if(activity!=null && !activity.isFinishing()){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            // Initialize a new foreground color span instance
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary));
            // Initialize a new spannable string builder instance
            SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);

            ssBuilder.setSpan(foregroundColorSpan,0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            alertDialogBuilder.setTitle(ssBuilder);
            alertDialogBuilder.setMessage(message);

            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(mClickListnere!=null){
                        mClickListnere.onClickUpdate(dialog);
                    }

                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }
}
