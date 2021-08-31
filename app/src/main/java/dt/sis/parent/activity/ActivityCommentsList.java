package dt.sis.parent.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dt.sis.parent.R;
import dt.sis.parent.adapters.CommentsListAdapter;
import dt.sis.parent.databinding.ActivityCommentsListBinding;
import dt.sis.parent.databinding.DialogCommentPostBinding;
import dt.sis.parent.dialogs.ParentsCommentsDialog;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.CommentsListModel;
import dt.sis.parent.models.PostCommentModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class ActivityCommentsList extends AppCompatActivity {

    private static final int STUDENT_REQUEST = 1;
    ActivityCommentsListBinding binding ;
    Context mContext;
    SessionManager sessionManager;
    String class_for="";
//    String  students_list;
    List<CommentsListModel.Result> commentsList;
    CommentsListAdapter<CommentsListModel.Result> commentListAdapter;
    DialogCommentPostBinding dialogBinding;
    AlertDialog postAlertDialog ;
    private int divisionId = 1;
    private int sectionId = 1;
    ParentsCommentsDialog parentsCommentsDialog;
    boolean isEdit =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comments_list);
        mContext = ActivityCommentsList.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.customToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && actionBar.isShowing()){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            binding.customToolbar.textView.setText("Comments");
        }

        getCommentList();


    }

    private void getCommentList() {

        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        postParam.addProperty("DivisionId", divisionId);
        postParam.addProperty("SectionId", sectionId);
        call = webApis.getParentsComments();
        ApiServices apiServices = new ApiServices(mContext);

        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    CommentsListModel modelVal = new Gson().fromJson(response,CommentsListModel.class);
                    if(modelVal.getResult()!=null) {
                        int totalCount = modelVal.getResult().size();
                        if(totalCount>0){

                        }else{
                            Toast.makeText(mContext, "Comments List is empty", Toast.LENGTH_SHORT).show();
                        }
                        commentsList= new ArrayList<>(modelVal.getResult());

                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        mLayoutManager.setSmoothScrollbarEnabled(true);
                        binding.rvCommentList.setLayoutManager(mLayoutManager);

                        commentListAdapter = new CommentsListAdapter<CommentsListModel.Result>(mContext, commentsList, itemClickListener) {

                            @Override
                            public String getHeader(int position, CommentsListModel.Result result) {
                                String value = result.getTeacherComment();
                                value = (value!=null && !value.isEmpty()) ? value : "No Comments";
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
                                String value = result.getFirstName()+" "+ result.getLastName();
                                value = value.replace("  "," ").trim();
                                return value;
                            }

                            @Override
                            public String getDate(int position, CommentsListModel.Result result) {
                                String value = result.getCommentDate();
                                value = (value!=null && !value.isEmpty()) ? getCalenderTime(value) : "";
                                return value;
                            }

                        };

                        binding.rvCommentList.setAdapter(commentListAdapter);

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }

    private CommentsListAdapter.ItemClickListener itemClickListener = new CommentsListAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
           final  String id = commentsList.get(position).getId();
            setParentReadComment(id);

                String parentComment =  commentsList.get(position).getParentComment();
                String teacherComment =  commentsList.get(position).getTeacherComment();
                parentComment = parentComment!=null? parentComment : "";
                teacherComment = teacherComment!=null? teacherComment : "";

                if(position%2==0){
                    parentComment = "";
                }

                parentsCommentsDialog = new ParentsCommentsDialog(mContext, parentComment, teacherComment,new ParentsCommentsDialog.ItemClickListener() {
                    @Override
                    public void onCancelItemClick(View view) {
                        parentsCommentsDialog.dismiss();
                    }

                    @Override
                    public void onPosItemClick(String message) {
                        if(!message.isEmpty()){
                            postCommentApi(message,id);
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

    private  String getCalenderTime(String dateString){
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date dateVal = input.parse(dateString.trim());         // parse input
            return  output.format(dateVal);         // format output
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void  setParentReadComment( String commentId) {

        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);

        String parentId = sessionManager.getUserId();
        JsonObject postParam = new JsonObject();
        postParam.addProperty("parentId", parentId);
        postParam.addProperty("commentId", commentId);

        call = webApis.SetParentReadComment(postParam);
        ApiServices apiServices = new ApiServices(mContext);
        apiServices.setDisplayDialog(false);
        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    PostCommentModel modelVal = new Gson().fromJson(response,PostCommentModel.class);
                    if(modelVal.getSuccess()) {

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }

    private void  postCommentApi(String postMessage, String id) {

        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);

        JsonObject postParam = new JsonObject();
        postParam.addProperty("id", id);
        postParam.addProperty("message", postMessage);

        call = webApis.ReplyToTeacherComment(postParam);
        ApiServices apiServices = new ApiServices(mContext);

        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    PostCommentModel modelVal = new Gson().fromJson(response,PostCommentModel.class);
                    if(modelVal.getSuccess()) {
                        Toast.makeText(mContext, "Comment posted successfully", Toast.LENGTH_SHORT).show();
//                        if(postAlertDialog.isShowing()) postAlertDialog.dismiss();
                        if(parentsCommentsDialog.isShowing()) parentsCommentsDialog.dismiss();
                        getCommentList();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_comment, menu);
//        return true;
//    }
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//
//        if (isEdit) {
//            menu.findItem(R.id.action_post).setTitle("Done");
//        }else{
//            menu.findItem(R.id.action_post).setTitle("Edit");
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_post) {
//            isEdit= !isEdit;
//            invalidateOptionsMenu();
//
//
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void openCommentDialog(final String id) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_comment_post,null);
        dialogBinding = DataBindingUtil.bind(dialogView);

        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        postAlertDialog= alertDialogBuilder.create();
        postAlertDialog.show();
        dialogBinding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show();
                String postMessage=  postMessage = dialogBinding.etComment.getText().toString().trim();
                if(!postMessage.isEmpty()){
                    postCommentApi(postMessage,id);


                } else {
                    Toast.makeText(mContext, "Please Enter comment", Toast.LENGTH_LONG).show();
                }

            }
        });

        if(postAlertDialog.getWindow()!=null) {
            postAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case STUDENT_REQUEST:
                if (resultCode == RESULT_OK && data!=null) {
                    int sectionId = data.getIntExtra("sectionId",-1);
                    int divisionId = data.getIntExtra("divisionId",-1);
                    String students_id_list = data.getStringExtra("students_id_list");

//                    openCommentDialog(students_id_list);

                }
                break;
                default: break;
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
