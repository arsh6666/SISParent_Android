package dt.sis.parent.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ItemGalleryDetailListBinding;

public abstract class MediaSliderAdapter<T> extends PagerAdapter {

    private final List<T> tItemList;
    private T items;
    Context mContext;
    LayoutInflater mLayoutInflater;
    MediaController mediaController;
    MediaSliderVideoCallback mediaSliderVideoCallback;

    public interface MediaSliderVideoCallback{
        public void onMediaVideoCallback(PlayerView playerView, ProgressBar progressBar);
    }

    public void setMediaSliderVideoCallback(MediaSliderVideoCallback mediaSliderVideoCallback) {
        this.mediaSliderVideoCallback = mediaSliderVideoCallback;
    }

    public MediaSliderAdapter(Context context, List<T> mValues) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tItemList = mValues;
    }

    @Override
    public int getCount() {
        return tItemList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View v = mLayoutInflater.inflate(R.layout.item_gallery_detail_list, container, false);

        final ItemGalleryDetailListBinding binding = DataBindingUtil.bind(v);
        try {
            binding.playerView.setTag(position);
            binding.progressView.setTag(position);

            items = tItemList.get(position);

            String filePath = getFilePath(position,items);
            String fileExtension = getFileExtension(position,items);

            if(fileExtension.contains("mp4")){
                binding.imageView.setVisibility(View.GONE);

                binding.playerView.setVisibility(View.VISIBLE);
                binding.progressView.setVisibility(View.VISIBLE);

                if(mediaSliderVideoCallback!=null){
                    mediaSliderVideoCallback.onMediaVideoCallback(binding.playerView, binding.progressView);
                }
            }else {
                Picasso.get().load(filePath)
                        .fit()
                        .centerInside()
                        .placeholder(R.drawable.progress_placholder)
                        .error(R.mipmap.ic_round_user)
                        .into(binding.imageView);

                binding.imageView.setVisibility(View.VISIBLE);
                binding.playerView.setVisibility(View.GONE);
                binding.progressView.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public abstract String getFilePath(int position, T t);
    public abstract String getFileExtension(int position, T t);
}
