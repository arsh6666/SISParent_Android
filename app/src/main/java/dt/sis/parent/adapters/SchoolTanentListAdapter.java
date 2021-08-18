package dt.sis.parent.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.databinding.SchoolOptionListItemBinding;

public abstract class SchoolTanentListAdapter<T> extends RecyclerView.Adapter{
    private List<T> tItemList;
    Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int selectedIndex = -1;


    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setSelectedIndex(int selectedIndex){
        this.selectedIndex = selectedIndex;
    }

    public SchoolTanentListAdapter(Context context, List<T> tItemList, ItemClickListener mClickListener) {
        this.tItemList = tItemList;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mClickListener = mClickListener;
    }

    public void updateData(List<T> updateList) {
//        tItemList.clear();
        tItemList = new ArrayList<>();
        tItemList.addAll(updateList);
        notifyDataSetChanged();
        String updateListss  = new Gson().toJson(tItemList);
        Log.e("updateList",updateListss);
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
            mView =(View) mInflater.inflate(R.layout.school_option_list_item, parent, false);
            return new DataViewHolder(mView);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        SchoolOptionListItemBinding binding;

        T item;

        public DataViewHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);

        }

        public void setData(final T item, final int pos) {
            try {
                this.item = item;
                String keyName = getKeyName(pos,item);

                binding.tvOrganizationName.setText(keyName);
                if(selectedIndex == pos) {
                    binding.chSelected.setChecked(true);
                }else{
                    binding.chSelected.setChecked(false);
                }

                binding.rootView.setOnClickListener(new View.OnClickListener() {
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
    public abstract String getKeyName(int position, T t);

}