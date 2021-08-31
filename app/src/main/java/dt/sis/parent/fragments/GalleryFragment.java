package dt.sis.parent.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.activity.ChildProfileActivity;
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
import dt.sis.parent.support.Constants;
import dt.sis.parent.support.ProgressChangedListener;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class GalleryFragment extends Fragment implements View.OnClickListener, ProgressChangedListener {

    String children_list;
    FragGalleryBinding binding;
    SessionManager sessionManager;
    Context mContext;
    Activity mActivity;
    List<GalleryGroupModel.Result> arrayList = new ArrayList<>();
    TextView tvDownloadAll;

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
        mActivity = getActivity();
        sessionManager = new SessionManager(mContext);
        if (getArguments() != null)
            children_list = getArguments().getString("children_list");

        if (children_list != null) {
            Log.e("ChildrenList", children_list + " ");
            DashboardChildrenModel.Students childrenModel = new Gson().fromJson(children_list, new TypeToken<DashboardChildrenModel.Students>() {
            }.getType());
            int studentId = childrenModel.getId();

            getGalleryList(studentId);
        }

        tvDownloadAll = mView.findViewById(R.id.tv_download);
        tvDownloadAll.setOnClickListener(this);
        return mView;
    }


    private void getGalleryList(int studentId) {
        final String tenantId = sessionManager.getTenantId();
        Call<JsonObject> call = null;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        call = webApis.getStudentGallery(studentId);

        ApiServices apiServices = new ApiServices(mContext);
        apiServices.callWebServices(call, response -> {
            try {
                GalleryGroupModel modelVal = new Gson().fromJson(response, GalleryGroupModel.class);
                boolean status = modelVal.getSuccess();
                if (status) {
                    arrayList.addAll(modelVal.getResult());
                    FilterGalleryList filterGalleryList = new FilterGalleryList(arrayList);
                    filterGalleryList.execute();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_download) {
            checkPermission();
        }
    }

    public void checkPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(mActivity, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
                alertDialog.setTitle("Download");
                alertDialog.setMessage("Press ok for download");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> downloadAllImagesVideos());
                alertDialog.show();

            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                Toast.makeText(context, "permission denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadAllImagesVideos() {
        tvDownloadAll.setEnabled(false);
        if (arrayList != null && arrayList.size() > 0) {
            if (arrayList.size() > 20) {
                for (int i = 0; i < 20; i++) {
                    File myFile = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name) + "/" + arrayList.get(i).getFileName());
                    if (!myFile.exists())
                        downloadFile(getMediaUrl(arrayList.get(i).getMediaId()), arrayList.get(i).getFileName(), this, i);
                }
            }
        }
    }

    private void downloadAccToDate(List<GalleryDateGroupModel.Result> galleryList, String date) {
        if (galleryList != null && galleryList.size() > 0) {
            for (int i = 0; i < galleryList.size(); i++) {
                File myFile = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name) + "/" + galleryList.get(i).getFileName());
                if (!myFile.exists())
                    downloadFileAccToDate(getMediaUrl(galleryList.get(i).getMediaId()), galleryList.get(i).getFileName(), this, i, date);
            }
        }
    }

    private String getMediaUrl(String mediaId) {
        String tenantId = new SessionManager(mContext).getTenantId();
        return SessionManager.GALLERY_FILE_URL + "?mediaid=" + mediaId + "&Tenantid=" + tenantId;
    }

    public void downloadFile(String uRl, String fileName, ProgressChangedListener progressChangeListener, int index) {
        File myDir = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name) + "/");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        DownloadManager mgr = (DownloadManager) mActivity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE).setAllowedOverMetered(true)
                .setAllowedOverRoaming(true).setTitle(getString(R.string.app_name) + " - " + "Downloading " + fileName)
                /*setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)*/
                .setDestinationInExternalPublicDir(getString(R.string.app_name) + "/", fileName);
        progressChangeListener.onProgressChanged(index, "");
        mgr.enqueue(request);
    }

    public void downloadFileAccToDate(String uRl, String fileName, ProgressChangedListener progressChangeListener, int index, String date) {
        File myDir = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name) + "/");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        DownloadManager mgr = (DownloadManager) mActivity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE).setAllowedOverMetered(true)
                .setAllowedOverRoaming(true).setTitle(getString(R.string.app_name) + " - " + "Downloading " + fileName)
                /*setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)*/
                .setDestinationInExternalPublicDir(getString(R.string.app_name) + "/", fileName);
        progressChangeListener.onProgressChanged(index, date);
        mgr.enqueue(request);
    }


    @Override
    public void onProgressChanged(int percent, String date) {
        tvDownloadAll.setEnabled(true);
        Log.e("Progress", "total:" + arrayList.size() + " Downloaded : " + percent);
    }

    @SuppressLint("StaticFieldLeak")
    public class FilterGalleryList extends AsyncTask<Void, Void, List<GalleryDateGroupModel>> {
        AlertDialog progressDialog;
        List<GalleryGroupModel.Result> arrayList;

        public FilterGalleryList(List<GalleryGroupModel.Result> arrayList) {
            progressDialog = sessionManager.getProgressAlert();
            progressDialog.setCancelable(false);
            progressDialog.show();
            this.arrayList = arrayList;
        }

        @Override
        protected List<GalleryDateGroupModel> doInBackground(Void... params) {
            try {
                List<GalleryDateGroupModel> dateGroupList = new ArrayList<>();
                if (arrayList.size() > 20) {
                    for (int i = 0; i < 20; i++) {
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
                                isExist = true;
                                break;
                            }
                        }
                        if (!isExist) {
                            for (GalleryGroupModel.Result o2 : arrayList) {
                                String o1Date = o1.getMediaDate();
                                o1Date = AppUtils.getDate(o1Date);
//                            Log.e("o1Date",o1Date);
                                String o2Date = o2.getMediaDate();
                                o2Date = AppUtils.getDate(o2Date);
//                            Log.e("o2Date",o2Date);
                                if (o1Date.equals(o2Date)) {
                                    String childString = new Gson().toJson(o2);
                                    GalleryDateGroupModel.Result resultModel = new Gson().fromJson(childString, new TypeToken<GalleryDateGroupModel.Result>() {
                                    }.getType());
                                    childList.add(resultModel);

                                }
                            }
                            GalleryDateGroupModel dateGroupModel = new GalleryDateGroupModel();
                            dateGroupModel.setDate(uniqueDate);
                            dateGroupModel.setResult(childList);
                            dateGroupList.add(dateGroupModel);
                        }
                    }
                }

                String searchDate = "";
                List<GalleryDateGroupModel> uniqueDateGroupList = new ArrayList<>();
                for (GalleryDateGroupModel search : dateGroupList) {
                    if (!search.getDate().equalsIgnoreCase(searchDate)) {
                        searchDate = search.getDate();
                        uniqueDateGroupList.add(search);
                    }
                }
                return uniqueDateGroupList;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<GalleryDateGroupModel> uniqueDateGroupList) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (uniqueDateGroupList != null && uniqueDateGroupList.size() > 0) {
                GalleryGroupVerticalListAdapter<GalleryDateGroupModel> verticalListAdapter = new GalleryGroupVerticalListAdapter<GalleryDateGroupModel>(mContext, uniqueDateGroupList, new GalleryGroupVerticalListAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(List<GalleryDateGroupModel.Result> galleryList, int position) {
                        String mediaId = galleryList.get(position).getMediaId();
                        String groupId = galleryList.get(position).getGalleryGroupId();
                        String imagesList = new Gson().toJson(galleryList);
                        Intent intent = new Intent(mContext, MediaSliderActivity.class);
                        intent.putExtra("imagesList", imagesList);
                        intent.putExtra("selectedPage", position);
                        startActivity(intent);
                    }

                    @Override
                    public void downloadImagesDateWise(List<GalleryDateGroupModel.Result> galleryList, String date) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        Permissions.check(mActivity, permissions, null, null, new PermissionHandler() {
                            @Override
                            public void onGranted() {
                                AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
                                alertDialog.setTitle("Download");
                                alertDialog.setMessage("Press ok for download");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        (dialog, which) -> downloadAccToDate(galleryList, date));
                                alertDialog.show();

                            }

                            @Override
                            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                                super.onDenied(context, deniedPermissions);
                                Toast.makeText(context, "permission denied", Toast.LENGTH_SHORT).show();
                            }
                        });
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
            } else {
                Toast.makeText(mContext, "Gallery is empty", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
