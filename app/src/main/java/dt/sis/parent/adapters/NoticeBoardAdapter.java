package dt.sis.parent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ItemDashboardChildBinding;
import dt.sis.parent.databinding.ItemNoticeboardListBinding;

public abstract class NoticeBoardAdapter<T> extends RecyclerView.Adapter{
    private List<T> tItemList;
    Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onDownloadClick(View view, int position);
    }

    public NoticeBoardAdapter(Context context, List<T> tItemList, ItemClickListener mClickListener) {
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
            mView =(View) mInflater.inflate(R.layout.item_noticeboard_list, parent, false);
            return new DataViewHolder(mView);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        ItemNoticeboardListBinding binding;

        T item;

        public DataViewHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mClickListener!=null){
                        mClickListener.onItemClick(v,getAdapterPosition());
                    }
                }
            });

            binding.ivDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mClickListener!=null){
                        mClickListener.onDownloadClick(v,getAdapterPosition());
                    }

                }
            });

        }

        public void setData(T item, int pos) {
            try {
                this.item = item;
                String day = getEventDay(pos,item);
                String date = getEventDate(pos,item);
                String time = getEventTime(pos,item);
                String name = getEventName(pos,item);
                String location = getEventLocation(pos,item);
                boolean isAvailableFile = isAvailableFile(pos,item);

                binding.tvEventName.setText(name);
                binding.tvDay.setText(day);
                binding.tvDate.setText(date);
                binding.tvTime.setText(time);
                binding.tvEventLocation.setText(location);
                if (isAvailableFile){
                    binding.ivDownload.setVisibility(View.VISIBLE);
                }else{
                    binding.ivDownload.setVisibility(View.GONE);
                }

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
    public abstract String getEventDay(int position, T t);
    public abstract String getEventDate(int position, T t);
    public abstract String getEventTime(int position, T t);
    public abstract String getEventName(int position, T t);
    public abstract String getEventLocation(int position, T t);
    public abstract boolean isAvailableFile(int position, T t);

}