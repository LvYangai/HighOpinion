package com.lvyangai.highopinion.ui.upvideo;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingCrop;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.config.BoxingCropOption;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.bilibili.boxing.utils.BoxingFileHelper;
import com.bilibili.boxing.utils.ImageCompressor;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.databinding.ActivityUpVideoBinding;
import com.lvyangai.highopinion.util.BoxingGlideLoader;
import com.lvyangai.highopinion.util.BoxingPicassoLoader;
import com.lvyangai.highopinion.util.BoxingUcrop;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.lvyangai.highopinion.util.ToastUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UpVideoActivity extends BaseActivity implements UpVideoContract.upVideoView {
    private ActivityUpVideoBinding binding;
    private Context context;
    private static final String TAG = "UpVideoActivity";
    private static final int REQUEST_CODE = 1024;
    private static final int COMPRESS_REQUEST_CODE = 2048;
    private UpVideoPresenter presenter;
    private String imagePath,videoPath;
    ArrayList<BaseMedia> medias;
    private int userId = 0;

    private boolean title_page,up_video;
    @Override
    protected void initView() {
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_up_video);
        presenter = new UpVideoPresenter(this);
        StatusBarUtil.setPaddingSmart(this,binding.videoToolbar);
    }

    @Override
    protected void initData() {
        userId = MyApplication.getUserid();
        BoxingMediaLoader.getInstance().init(new BoxingPicassoLoader());
        BoxingCrop.getInstance().init(new BoxingUcrop());
    }

    @Override
    protected void dataProcess() {
        binding.upVideoPrickerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickimage();
            }
        });
        binding.upVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickVideo();
            }
        });
        binding.videoToolbarUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submit()){
                    showProDialog();
                    String title = binding.upVideoTitle.getText().toString();
                    showUploadVideo(title,userId);
                }
            }
        });
        binding.videoToolbarCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showUploadVideo(String title, int userId){
        try {
            File file = new File(imagePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            file = new File(videoPath);
            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        Log.e(TAG, "showUploadVideo: "+file.getName() );
//        String pathUrl = URLEncoder.encode(file.getName(), "utf-8");
            MultipartBody.Part videoPart = null;//URLEncoder.encode(str, "utf-8")
            videoPart = MultipartBody.Part.createFormData("video", URLEncoder.encode(file.getName(), "utf-8"), requestFile);
            presenter.upVideo(title,userId,imagePart,videoPart);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private boolean submit() {
        // validate
        String upTitleString = binding.upVideoTitle.getText().toString().trim();
        if (TextUtils.isEmpty(upTitleString)) {
            Toast.makeText(this, "标题还没有写哦", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(imagePath)){
            Toast.makeText(this, "请上传封面", Toast.LENGTH_SHORT).show();
            return false;
        }
        // TODO validate success, do something
        if (TextUtils.isEmpty(videoPath)){
            Toast.makeText(this, "请上传视频", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showPickimage() {
        String cachePath = BoxingFileHelper.getCacheDir(this);
        if (TextUtils.isEmpty(cachePath)) {
            Toast.makeText(getApplicationContext(), R.string.boxing_storage_deny, Toast.LENGTH_SHORT).show();
            return;
        }
        Uri destUri = new Uri.Builder()
                .scheme("file")
                .appendPath(cachePath)
                .appendPath(String.format(Locale.US, "%s.png", System.currentTimeMillis()))
                .build();
        BoxingConfig singleCropImgConfig = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG).withCropOption(new BoxingCropOption(destUri))
                .withMediaPlaceHolderRes(R.mipmap.ic_boxing_default_image);
        Boxing.of(singleCropImgConfig).withIntent(this, BoxingActivity.class).start(this, REQUEST_CODE);

    }

    private void showPickVideo() {
        IBoxingMediaLoader loader = new BoxingGlideLoader();
        BoxingMediaLoader.getInstance().init(loader);
        BoxingCrop.getInstance().init(new BoxingUcrop());
        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.VIDEO).withVideoDurationRes(R.mipmap.ic_boxing_play);
        Boxing.of(config).withIntent(this, BoxingActivity.class).start(this, COMPRESS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (binding.upVideoPrickerImage == null || binding.upVideo == null) {
                return;
            }

            medias = Boxing.getResult(data);
            if (requestCode == REQUEST_CODE) {
                imagePath = medias.get(0).getPath();
                BoxingMediaLoader.getInstance().displayThumbnail(binding.upTitleImage, imagePath, 750, 250);
            } else if (requestCode == COMPRESS_REQUEST_CODE) {
                videoPath = medias.get(0).getPath();
                BoxingMediaLoader.getInstance().displayThumbnail(binding.upVideoSelect,videoPath , 350, 750);

            }
        }
    }

    @Override
    public void retrofit(String response) {
        ToastUtil.showShortToast(response);
        dismissDialog();
        finish();
    }

    @Override
    public void retrofitField() {
        ToastUtil.showShortToast("上传失败");
        dismissDialog();
    }
}
