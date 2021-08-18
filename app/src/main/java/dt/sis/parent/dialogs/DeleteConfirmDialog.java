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

public class DeleteConfirmDialog {

    Context mContext;
    DeleteOnClickListnere mClickListnere;
    String title="";
    String message="";

    public void setTitle(String title, String message) {
        this.title = title;
        this.message = message;

    }

    public DeleteConfirmDialog(Context mContext, DeleteOnClickListnere mClickListnere) {
        this.mContext = mContext;
        this.mClickListnere = mClickListnere;
    }

    public interface DeleteOnClickListnere {
        void onCLickYes(DialogInterface dialog);
        void onClickNo(DialogInterface dialog);
    }
    public void showAlert(){
        String defaultTitle = "Delete !";
        String defaultMessages = "Are you sure want to delete ?";
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

            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(mClickListnere!=null){
                        mClickListnere.onCLickYes(dialog);
                    }

                }
            });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(mClickListnere!=null){
                        mClickListnere.onClickNo(dialog);
                    }
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }
}
