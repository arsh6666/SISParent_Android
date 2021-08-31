package dt.sis.parent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ItemChatRoomListBinding;

public abstract class ChatMessageAdapter<T> extends RecyclerView.Adapter{
    private List<T> tItemList;
    Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public ChatMessageAdapter(Context context, List<T> tItemList, ItemClickListener mClickListener) {
        this.tItemList = tItemList;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mClickListener = mClickListener;
    }

    public void updateData(List<T> updateList) {
        tItemList = new ArrayList<>(updateList);
    }
    public void addData(List<T> updateList) {
        tItemList.addAll(updateList);
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
            mView =(View) mInflater.inflate(R.layout.item_chat_room_list, parent, false);
            return new DataViewHolder(mView);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        ItemChatRoomListBinding binding;
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
                String message = getMessage(pos,item);
                String pic = getPicture(pos,item);
                String creationTime = getCreationTime(pos,item);
                String creationDate = getCreationDate(pos,item);
                boolean isShowDate = getShowDate(pos,item);

                int side = getSide(pos,item);

                TextView messageTv = side==1 ? binding.tvMessageSender : binding.tvMessage;
                messageTv.setText(message);

                ImageView imageView = side==1 ? binding.ivProfileImageSender : binding.ivProfileImage;

                Picasso.get().load(pic)
                        .placeholder(R.mipmap.ic_round_user)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .error(R.mipmap.ic_round_user)
                        .into(imageView);

                binding.tvDate.setVisibility(isShowDate? View.VISIBLE : View.GONE);
                binding.tvDate.setText(creationDate);

                TextView messageTimeTv = side==1 ? binding.tvMessageDateSender : binding.tvMessageDate;
                messageTimeTv.setText(creationTime);

                int timeVisible = creationTime.isEmpty() ? View.GONE : View.VISIBLE;
                binding.tvMessageDate.setVisibility(timeVisible);

                int receiver = side==1 ? View.GONE : View.VISIBLE ;
                int sender = side==1 ? View.VISIBLE : View.GONE ;

                binding.llReceiver.setVisibility(receiver);
                binding.llSender.setVisibility(sender);

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
    public abstract int getSide(int position, T t);
    public abstract String getMessage(int position, T t);
    public abstract String getPicture(int position, T t);
    public abstract String getCreationTime(int position, T t);
    public abstract String getCreationDate(int position, T t);
    public abstract boolean getShowDate(int position, T t);

}