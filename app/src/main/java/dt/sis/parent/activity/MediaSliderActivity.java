package dt.sis.parent.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.adapters.MediaSliderAdapter;
import dt.sis.parent.databinding.ActivityMediaSliderBinding;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.GalleryGroupModel;
import dt.sis.parent.models.gallery.Item;

public class MediaSliderActivity extends AppCompatActivity {

    ActivityMediaSliderBinding binding ;
    Context mContext;
    SessionManager sessionManager;
    List<Item> galleryList;
    private String imagesList;
    int selectedPage = 0;
    private static final int REQUEST_WRITE_PERMISSION = 99;
    boolean isShare = false;
    SimpleExoPlayer player;
    Handler mHandler;
    Runnable mRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_media_slider);
        mContext = MediaSliderActivity.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.customToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null && actionBar.isShowing()){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            binding.customToolbar.textView.setText("Gallery");
        }

        imagesList = getIntent().getStringExtra("imagesList");
        selectedPage = getIntent().getIntExtra("selectedPage",0);

        Log.e("ImagesLIST",imagesList);

        galleryList = new Gson().fromJson(imagesList, new TypeToken<List<Item>>(){}.getType());

        final String tenantId = sessionManager.getTenantId();
        MediaSliderAdapter<Item> mediaSliderAdapter = new MediaSliderAdapter<Item>(mContext,galleryList) {
            @Override
            public String getFilePath(int position, Item result) {
                String value = sessionManager.GALLERY_FILE_URL+"?mediaid=" + result.getMediaId() +"&Tenantid="+tenantId;;
                return value;
            }

            @Override
            public String getFileExtension(int position, Item result) {
                String value = result.getFileExtension();

                return value;
            }
        };

        mediaSliderAdapter.setMediaSliderVideoCallback(new MediaSliderAdapter.MediaSliderVideoCallback() {
            @Override
            public void onMediaVideoCallback(PlayerView playerView, ProgressBar progressView) {
                int position = (int) playerView.getTag();
                if(position!=-1) {
                    String mediaId = galleryList.get(position).getMediaId();
                    String filePath = SessionManager.GALLERY_FILE_URL+"?mediaid=" + mediaId +"&Tenantid="+tenantId;;
                    String fileExtension = galleryList.get(position).getFileExtension();
                    if(fileExtension.contains("mp4")){
                        Uri videoUri = Uri.parse(filePath);
                        initializePlayer(playerView);
                        if (videoUri == null) {
                            return;
                        }
                        buildMediaSource(videoUri,progressView);

                    }
                }
            }
        });

        binding.viewPager.setAdapter(mediaSliderAdapter);

        String headeTitle = (selectedPage+1)+" of "+galleryList.size();
        binding.customToolbar.textView.setText(headeTitle);

        binding.viewPager.setCurrentItem(selectedPage);

        binding.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShare = true;
                checkPermissions();
            }
        });
        binding.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShare = false;
                checkPermissions();
            }
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                String headeTitle = (i+1)+" of "+galleryList.size();
                binding.customToolbar.textView.setText(headeTitle);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }
    private void checkPermissions(){
        ActivityCompat.requestPermissions((Activity) mContext, new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    shareImage();
                } else {
                    Toast.makeText(mContext, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            default: break;
        }
    }
    private void shareImage() {
        com.squareup.picasso.Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "IMG_"+System.currentTimeMillis()+".jpeg", null);
                Log.e("Path", path);
                Toast.makeText(mContext, "Image saved: "+path, Toast.LENGTH_LONG).show();

                if(isShare) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "Hey view/download this image");
                    Uri screenshotUri = Uri.parse(path);
                    intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                    intent.setType("image/*");
                    startActivity(Intent.createChooser(intent, "Share image via..."));
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Toast.makeText(mContext, "There is some error in download image, please try again", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        String imageID = galleryList.get(binding.viewPager.getCurrentItem()).getMediaId();
        String url =  SessionManager.GALLERY_FILE_URL+"?mediaid=" + imageID+"&Tenantid="+sessionManager.getTenantId();;;
        Picasso.get().load(url).into(target);
    }
    private void initializePlayer(PlayerView playerView) {
        if (player == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            DefaultLoadControl loadControl = new DefaultLoadControl();
            DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(mContext);
            player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            playerView.setPlayer(player);
        }
    }
    private void buildMediaSource(Uri mUri,final ProgressBar progressView) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)), bandwidthMeter);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mUri);
        // Prepare the player with the source.
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {

                    case Player.STATE_BUFFERING:
                        progressView.setVisibility(View.VISIBLE);
                        break;
                    case Player.STATE_ENDED:
                        // Activate the force enable
                        break;
                    case Player.STATE_IDLE:

                        break;
                    case Player.STATE_READY:
                        progressView.setVisibility(View.GONE);

                        break;
                    default:
                        // status = PlaybackStatus.IDLE;
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }});
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
        }
    }

    private void resumePlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlayer();
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        resumePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
