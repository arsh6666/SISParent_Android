package dt.sis.parent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ItemCommentListBinding;
import dt.sis.parent.databinding.ItemGuardianTypeListBinding;

public abstract class GuardianTypeListAdapter<T> extends RecyclerView.Adapter{
    private List<T> tItemList;
    Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    int selectedPos = -1;

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public GuardianTypeListAdapter(Context context, List<T> tItemList, ItemClickListener mClickListener) {
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
            mView =(View) mInflater.inflate(R.layout.item_guardian_type_list, parent, false);
            return new DataViewHolder(mView);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        ItemGuardianTypeListBinding binding;

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
                String organizationName = getOrganizationName(pos,item);
                binding.tvOrganizationName.setText(organizationName);
                binding.chSelected.setChecked(selectedPos==pos);

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
    public abstract String getOrganizationName(int position, T t);

}