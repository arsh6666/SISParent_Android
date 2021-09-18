package dt.sis.parent.adapters.gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ItemGalleryVerticalListItemBinding;
import dt.sis.parent.fragments.GalleryFragment;
import dt.sis.parent.helper.AppUtils;
import dt.sis.parent.models.gallery.Result;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    List<Result> list;
    Activity context;
    GalleryFragment fragment;

    public GalleryAdapter(Activity context, List<Result> list, GalleryFragment fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_vertical_list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.dateTv.setText(list.get(position).getKey().equals("") ? "" : AppUtils.getDate(list.get(position).getKey()));
        holder.binding.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        holder.binding.recyclerView.setAdapter(new GalleryGroupItemAdapter(context, list.get(position).getItems()));
        holder.binding.tvSave.setOnClickListener(v -> {
            fragment.downloadPermissionCheck(list.get(position).getItems(), AppUtils.getDate(list.get(position).getKey()));
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemGalleryVerticalListItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
