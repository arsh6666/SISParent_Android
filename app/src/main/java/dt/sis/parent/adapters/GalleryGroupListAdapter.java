package dt.sis.parent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ItemGalleryGroupListBinding;

public abstract class GalleryGroupListAdapter<T> extends RecyclerView.Adapter{
    private List<T> tItemList;
    Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public GalleryGroupListAdapter(Context context, List<T> tItemList, ItemClickListener mClickListener) {
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


    @Override
    public int getItemCount() {
        return tItemList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView ;
        try{
            mView =(View) mInflater.inflate(R.layout.item_gallery_group_list, parent, false);
            return new DataViewHolder(mView);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        ItemGalleryGroupListBinding binding;

        T item;

        public DataViewHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }

        public void setData(T item, int pos) {
            try {
                this.item = item;
                String mediaURL = getMediaUrl(pos,item);
                String fileExtension = getFileExtension(pos,item);

                if(fileExtension.contains("mp4")){
                    binding.ivImage.setImageResource(R.mipmap.video_thumb);

                }else{
                    Picasso.get().load(mediaURL)
//                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .fit()
                            .centerInside()
                            .placeholder(R.drawable.progress_placholder)
                            .error(R.mipmap.ic_round_user)
                            .into(binding.ivImage);

                }
                binding.ivImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mClickListener!=null){
                            mClickListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });



            }catch (Exception e){
                e.printStackTrace();
            }


        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        try {
            final T tItem = tItemList.get(position);

            DataViewHolder dataViewHolder = (DataViewHolder) viewHolder;
            dataViewHolder.setData(tItem,position);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public abstract String getMediaUrl(int position, T t);
    public abstract String getFileExtension(int position, T t);

}