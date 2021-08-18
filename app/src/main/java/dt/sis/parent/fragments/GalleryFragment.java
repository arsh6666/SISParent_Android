package dt.sis.parent.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.activity.MediaSliderActivity;
import dt.sis.parent.adapters.GalleryGroupListAdapter;
import dt.sis.parent.adapters.GalleryGroupVerticalListAdapter;
import dt.sis.parent.databinding.FragGalleryBinding;
import dt.sis.parent.databinding.FragProfileInfoBinding;
import dt.sis.parent.helper.AppUtils;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.DashboardChildrenModel;
import dt.sis.parent.models.GalleryDateGroupModel;
import dt.sis.parent.models.GalleryGroupModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class GalleryFragment extends Fragment {

    String children_list;
    FragGalleryBinding binding;
    SessionManager sessionManager;
    Context mContext;
//    List<GalleryGroupModel.Result> galleryList;
//GalleryGroupListAdapter<GalleryGroupModel.Result> galleryGroupListAdapter;

    public static GalleryFragment getInstance() {
        return new GalleryFragment();
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_gallery, container, false);
        View mView = binding.getRoot();
        mContext = getActivity();
        sessionManager = new SessionManager(mContext);
        if(getArguments()!=null)
        children_list = getArguments().getString("children_list");

        if(children_list!=null) {
            Log.e("ChildrenList", children_list + " ");
            DashboardChildrenModel.Students childrenModel = new Gson().fromJson(children_list, new TypeToken<DashboardChildrenModel.Students>() {
            }.getType());
            int studentId = childrenModel.getId();

            getGalleryList(studentId);
        }
        return mView;
    }
    private void getGalleryList(int studentId) {
        final String tenantId = sessionManager.getTenantId();
        Call<JsonObject> call = null;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        call = webApis.getStudentGallery(studentId);

        ApiServices apiServices = new ApiServices(mContext);
        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(final String  response) {
                try{
                    GalleryGroupModel modelVal = new Gson().fromJson(response, GalleryGroupModel.class);
                    boolean status = modelVal.getSuccess();
                    if(status) {
                        List<GalleryGroupModel.Result> arrayList= new ArrayList<>(modelVal.getResult());
                        FilterGalleryList filterGalleryList = new FilterGalleryList(arrayList);
                        filterGalleryList.execute() ;

//                        galleryGroupListAdapter = new GalleryGroupListAdapter<GalleryGroupModel.Result>(mContext, galleryList, onItemClicksListener) {
//                            @Override
//                            public String getMediaUrl(int position, GalleryGroupModel.Result result) {
//                                String value = sessionManager.GALLERY_FILE_URL+"?mediaid=" + result.getMediaId() +"&Tenantid="+tenantId;
//
//                                return value;
//                            }
//
//                            @Override
//                            public String getFileExtension(int position, GalleryGroupModel.Result result) {
//                                String value = result.getFileExtension();
//
//                                return value;
//                            }
//
//                        };
//
//                        binding.rvPhotos.setLayoutManager(new GridLayoutManager(mContext, 2));
//
//                        binding.rvPhotos.setAdapter(galleryGroupListAdapter);
//                        galleryGroupListAdapter.notifyDataSetChanged();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }
    public class FilterGalleryList extends AsyncTask<Void, Void,  List<GalleryDateGroupModel> > {

        AlertDialog progressDialog;
        List<GalleryGroupModel.Result> arrayList;

        public FilterGalleryList(List<GalleryGroupModel.Result> arrayList) {
            progressDialog = sessionManager.getProgressAlert();
            progressDialog.setCancelable(false);
            progressDialog.show();
            this.arrayList = arrayList;

        }

        @Override
        protected  List<GalleryDateGroupModel> doInBackground(Void... params) {

            try {
                List<GalleryDateGroupModel> dateGroupList = new ArrayList<>();
                for(int i=0;i<arrayList.size();i++) {
                    GalleryGroupModel.Result o1 = arrayList.get(i);
                    String uniqueDate = AppUtils.getDate(o1.getMediaDate());
                    List<GalleryDateGroupModel.Result> childList = new ArrayList<>();
                    boolean isExist = false;
                    for (GalleryDateGroupModel.Result o2 : childList) {
                        String o1Date = o1.getMediaDate();
                        o1Date = AppUtils.getDate(o1Date);
                        String o2Date = o2.getMediaDate();
                        o2Date = AppUtils.getDate(o2Date);
                        if (o1Date.equals(o2Date)) {
                            isExist= true ;
                            break;
                        }
                    }
                    if(!isExist){
                        for (GalleryGroupModel.Result o2 : arrayList) {
                            String o1Date = o1.getMediaDate();
                            o1Date = AppUtils.getDate(o1Date);
//                            Log.e("o1Date",o1Date);
                            String o2Date = o2.getMediaDate();
                            o2Date = AppUtils.getDate(o2Date);
//                            Log.e("o2Date",o2Date);
                            if (o1Date.equals(o2Date)) {
                                String childString = new Gson().toJson(o2);
                                GalleryDateGroupModel.Result resultModel  = new Gson().fromJson(childString, new TypeToken<GalleryDateGroupModel.Result>(){}.getType());
                                childList.add(resultModel);

                            }
                        }
                        GalleryDateGroupModel dateGroupModel = new GalleryDateGroupModel();
                        dateGroupModel.setDate(uniqueDate);
                        dateGroupModel.setResult(childList);
                        dateGroupList.add(dateGroupModel);
                    }
                }

                String searchDate="";
                List<GalleryDateGroupModel>uniqueDateGroupList = new ArrayList<>();
                for(GalleryDateGroupModel search : dateGroupList){
                    if(!search.getDate().equalsIgnoreCase(searchDate)){
                        searchDate = search.getDate();
                        uniqueDateGroupList.add(search);
                    }
                }
                return uniqueDateGroupList;
            }catch (Exception e){
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute( List<GalleryDateGroupModel>uniqueDateGroupList ) {
            if(progressDialog.isShowing()) progressDialog.dismiss();
            if(uniqueDateGroupList!=null && uniqueDateGroupList.size()>0){
                GalleryGroupVerticalListAdapter<GalleryDateGroupModel> verticalListAdapter = new GalleryGroupVerticalListAdapter<GalleryDateGroupModel>(mContext, uniqueDateGroupList, new GalleryGroupVerticalListAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(List<GalleryDateGroupModel.Result> galleryList, int position) {
                        String mediaId  = galleryList.get(position).getMediaId();
                        String groupId  = galleryList.get(position).getGalleryGroupId();
                        String imagesList = new Gson().toJson(galleryList);
                        Intent intent = new Intent(mContext, MediaSliderActivity.class);
                        intent.putExtra("imagesList",imagesList);
                        intent.putExtra("selectedPage",position);
                        startActivity(intent);
                    }
                }) {
                    @Override
                    public String getDate(int position, GalleryDateGroupModel item) {
                        return item.getDate();
                    }

                    @Override
                    protected List<GalleryDateGroupModel.Result> geGroupGallery(int pos, GalleryDateGroupModel item) {
                        return item.getResult();
                    }
                };

                LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                mLayoutManager.setOrientation(RecyclerView.VERTICAL);
                mLayoutManager.setSmoothScrollbarEnabled(true);
                binding.rvPhotos.setLayoutManager(mLayoutManager);
                binding.rvPhotos.setAdapter(verticalListAdapter);
                verticalListAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(mContext, "Gallery is empty", Toast.LENGTH_SHORT).show();
            }

        }

    }

//    private GalleryGroupListAdapter.ItemClickListener onItemClicksListener = new GalleryGroupListAdapter.ItemClickListener() {
//        @Override
//        public void onItemClick(View view, int position) {
//            String mediaId  = galleryList.get(position).getMediaId();
//            String groupId  = galleryList.get(position).getGalleryGroupId();
//            String imagesList = new Gson().toJson(galleryList);
//            Intent intent = new Intent(mContext, MediaSliderActivity.class);
//            intent.putExtra("imagesList",imagesList);
//            intent.putExtra("selectedPage",position);
//            startActivity(intent);
//
//        }
//    };

}
