package dt.sis.parent.adapters.gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.activity.MediaSliderActivity;
import dt.sis.parent.databinding.ItemGalleryGroupListBinding;
import dt.sis.parent.databinding.ItemGalleryVerticalListItemBinding;
import dt.sis.parent.helper.AppUtils;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.gallery.Item;
import dt.sis.parent.models.gallery.Result;

public class GalleryGroupItemAdapter extends RecyclerView.Adapter<GalleryGroupItemAdapter.ViewHolder> {

    List<Item> list;
    Activity context;

    public GalleryGroupItemAdapter(Activity context, List<Item> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_group_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).getFileExtension().equals("mp4"))
            holder.binding.ivImage.setImageResource(R.mipmap.video_thumb);
        else {
            String url = SessionManager.GALLERY_FILE_URL + "?mediaid=" + list.get(position).getMediaId() + "&Tenantid=" + new SessionManager(context).getTenantId();
            Picasso.get().load(url)
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.progress_placholder)
                    .error(R.mipmap.ic_round_user)
                    .into(holder.binding.ivImage);
        }

        holder.binding.ivImage.setOnClickListener(v -> {
            String imagesList = new Gson().toJson(list);
            Intent intent = new Intent(context, MediaSliderActivity.class);
            intent.putExtra("imagesList", imagesList);
            intent.putExtra("selectedPage", position);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemGalleryGroupListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
