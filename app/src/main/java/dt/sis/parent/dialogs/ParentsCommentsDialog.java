package dt.sis.parent.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import dt.sis.parent.R;
import dt.sis.parent.databinding.DialogCommentPostBinding;
import dt.sis.parent.databinding.DialogDisplayCommentBinding;

public class ParentsCommentsDialog extends AlertDialog {

    private ItemClickListener itemClickListener;
    private Context mContext;
    DialogDisplayCommentBinding binding;
    String parentComment;
    String teacherComment;
    public interface ItemClickListener {
        public void onCancelItemClick(View view);
        public void onPosItemClick(String message);
    }

    public ParentsCommentsDialog(Context mContext,String parentComment, String teacherComment, ItemClickListener itemClickListener) {
        super(mContext);
        this.mContext = mContext;
        this.parentComment = parentComment;
        this.teacherComment = teacherComment;
        this.itemClickListener = itemClickListener;
        initialize();
    }

    private void initialize() {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_display_comment,null);
        binding = DataBindingUtil.bind(mView);
        if(teacherComment!=null && !teacherComment.isEmpty()) {
            binding.teacherComment.setText(teacherComment);
        }

        if(parentComment!=null && !parentComment.isEmpty()) {
            binding.parentComment.setText(parentComment);
            binding.parentComment.setFocusableInTouchMode(false);
            binding.parentComment.clearFocus();
            binding.btnCancel.setText("Okay");
            binding.btnPost.setVisibility(View.GONE);
        }else{
            binding.parentComment.setFocusableInTouchMode(true);
            binding.parentComment.requestFocus();
            binding.btnCancel.setText("Cancel");
            binding.btnPost.setVisibility(View.VISIBLE);
        }

        binding.parentComment.setMovementMethod(new ScrollingMovementMethod());
        binding.teacherComment.setMovementMethod(new ScrollingMovementMethod());

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener!=null){
                    itemClickListener.onCancelItemClick(view);
                }

            }
        });
        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.parentComment.getText().toString().trim();
                if(itemClickListener!=null){
                    itemClickListener.onPosItemClick(message);
                }
            }
        });

        setView(mView);
    }


}