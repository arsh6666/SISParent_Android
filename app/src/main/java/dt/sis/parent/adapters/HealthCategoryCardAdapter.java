package dt.sis.parent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ItemHealthTabHostBinding;
import dt.sis.parent.databinding.ItemSubHealthCategoryBinding;

public abstract class HealthCategoryCardAdapter<T> extends RecyclerView.Adapter{
    private List<T> tItemList;
    Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public HealthCategoryCardAdapter(Context context, List<T> tItemList, ItemClickListener mClickListener) {
        this.tItemList = tItemList;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mClickListener = mClickListener;
    }

    public void updateData(List<T> updateList) {
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
            mView =(View) mInflater.inflate(R.layout.item_sub_health_category, parent, false);
            return new DataViewHolder(mView);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        ItemSubHealthCategoryBinding binding;

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

        }

        public void setData(T item, int pos) {
            try {
                this.item = item;
                String name = getHabitType(pos,item);
                String option = getHabitOption(pos,item);
                String image = getImage(pos,item);
                binding.habitType.setText(name);
                binding.habitOption.setText(option);
                if(image.isEmpty()){
                    binding.imageView.setImageResource(R.mipmap.ic_round_user);
                }else{
                    Picasso.get().load(image)
                            .placeholder(R.mipmap.ic_round_user)
                            .error(R.mipmap.ic_round_user)
                            .into(binding.imageView);
                }

                int color;
                switch (pos % 3){
                    case 1:
                        color =ContextCompat.getColor(mContext, R.color.colorDownload);
                        break;
                    case 2:
                        color =ContextCompat.getColor(mContext, R.color.colorRed);
                        break;
                    default:
                         color =ContextCompat.getColor(mContext, R.color.color_round_image_boarder);

                            break;
                }
                binding.cardItem.setCardBackgroundColor(color);


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
    public abstract String getHabitType(int position, T t);
    public abstract String getHabitOption(int position, T t);
    public abstract String getImage(int position, T t);

}