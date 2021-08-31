package dt.sis.parent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ItemGalleryVerticalListItemBinding;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.GalleryDateGroupModel;

public abstract class GalleryGroupVerticalListAdapter<T> extends RecyclerView.Adapter {
    private List<T> tItemList = new ArrayList<>();
    Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public interface ItemClickListener {
        void onItemClick(List<GalleryDateGroupModel.Result> galleryList, int position);

        void downloadImagesDateWise(List<GalleryDateGroupModel.Result> galleryList, String date);
    }

    public GalleryGroupVerticalListAdapter(Context context, List<T> tItemList, ItemClickListener mClickListener) {
        this.tItemList = tItemList;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mClickListener = mClickListener;
    }

    public void updateData(List<T> updateList) {
        tItemList.clear();
        tItemList = new ArrayList<>();
        tItemList.addAll(updateList);
        notifyDataSetChanged();
    }

    public void updateList(List<T> tItemList){
        this.tItemList.addAll(tItemList);
        notifyItemInserted(tItemList.size()-1);
    }


    @Override
    public int getItemCount() {
        return tItemList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView;
        try {
            mView = mInflater.inflate(R.layout.item_gallery_vertical_list_item, parent, false);
            return new DataViewHolder(mView);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        ItemGalleryVerticalListItemBinding binding;

        T item;

        public DataViewHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }

        public void setData(T item, final int pos) {
            try {
                this.item = item;
                String dateVal = getDate(pos, item);
                final List<GalleryDateGroupModel.Result> galleryList = geGroupGallery(pos, item);
                binding.dateTv.setText("Date: " + dateVal);

                binding.tvSave.setOnClickListener(v -> {
                    if (mClickListener != null) {
                        mClickListener.downloadImagesDateWise(galleryList, dateVal);
                    }
                });

                GalleryGroupListAdapter groupListAdapter = new GalleryGroupListAdapter<GalleryDateGroupModel.Result>(mContext, galleryList, new GalleryGroupListAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (mClickListener != null) {
                            mClickListener.onItemClick(galleryList, position);
                        }
                    }
                }) {
                    @Override
                    public String getMediaUrl(int position, GalleryDateGroupModel.Result result) {
                        String tenantId = new SessionManager(mContext).getTenantId();

                        String value = SessionManager.GALLERY_FILE_URL + "?mediaid=" + result.getMediaId() + "&Tenantid=" + tenantId;

                        return value;
                    }

                    @Override
                    public String getFileExtension(int position, GalleryDateGroupModel.Result result) {
                        String value = result.getFileExtension();

                        return value;
                    }

                };

                LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                mLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                mLayoutManager.setSmoothScrollbarEnabled(true);
                binding.recyclerView.setLayoutManager(mLayoutManager);
                binding.recyclerView.setAdapter(groupListAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        try {
            final T tItem = tItemList.get(position);

            DataViewHolder dataViewHolder = (DataViewHolder) viewHolder;
            dataViewHolder.setData(tItem, position);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract String getDate(int position, T t);

    protected abstract List<GalleryDateGroupModel.Result> geGroupGallery(int pos, T item);
}