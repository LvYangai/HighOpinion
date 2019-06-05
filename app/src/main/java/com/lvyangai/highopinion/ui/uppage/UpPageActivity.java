package com.lvyangai.highopinion.ui.uppage;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingCrop;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.BoxingManager;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.bilibili.boxing.utils.ImageCompressor;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.bilibili.boxing_impl.view.SpacesItemDecoration;
import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.databinding.ActivityUpPageBinding;
import com.lvyangai.highopinion.util.BoxingPicassoLoader;
import com.lvyangai.highopinion.util.BoxingUcrop;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.lvyangai.highopinion.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UpPageActivity extends BaseActivity implements UpPageContract.upPageView{
    private static final int REQUEST_CODE = 1024;
    private static final int COMPRESS_REQUEST_CODE = 2048;
    private static final String TAG = "UpPageActivity";
    private ActivityUpPageBinding binding;
    private Context context;
    List<String> dataset = new LinkedList<>(Arrays.asList("美食","数码","家居","美妆","宠物","电影","其他"));
    private MediaResultAdapter mAdapter;
    private UpPagePresenter presenter;
    ArrayList<BaseMedia> medias;

    @Override
    protected void initView() {
        context = this;
        binding = DataBindingUtil.setContentView(this,R.layout.activity_up_page);
        StatusBarUtil.setPaddingSmart(this,binding.pageToolbar);
        presenter = new UpPagePresenter(this);

    }

    @Override
    protected void initData() {
        mAdapter = new MediaResultAdapter();
        binding.picekerRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        binding.picekerRecycler.setAdapter(mAdapter);
        binding.picekerRecycler.addItemDecoration(new SpacesItemDecoration(8));
        BoxingMediaLoader.getInstance().init(new BoxingPicassoLoader());
        BoxingCrop.getInstance().init(new BoxingUcrop());
        binding.upPageSpinner.attachDataSource(dataset);
    }

    @Override
    protected void dataProcess() {
        binding.upPicekerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPiceker();
            }
        });
        binding.pageToolbarUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submit()){
                    showProDialog();
                    String title = binding.upTitle.getText().toString();
                    String content = binding.upContent.getText().toString();
                    int userId = MyApplication.getUserid();
                    String keys = dataset.get(binding.upPageSpinner.getSelectedIndex());//关键词  类似于标签
                    startUpload(title,content,userId,keys);
                }
            }
        });
        binding.pageToolbarCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startUpload(String title, String content, int userId, String keys) {
        MultipartBody.Part[] parts = new MultipartBody.Part[medias.size()];
        for (int i=0;i<medias.size();i++) {
            File file = new File(medias.get(i).getPath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("page[]", file.getName(), requestFile);
            parts[i] = filePart;
        }
        presenter.upPage(title,content,userId,keys,parts);
    }

    private void showPiceker() {
//        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.VIDEO).withVideoDurationRes(R.mipmap.ic_boxing_play);
        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.MULTI_IMG).needCamera(R.mipmap.ic_boxing_camera_white).needGif();
        Boxing.of(config).withIntent(this, BoxingActivity.class).start(this, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (binding.picekerRecycler == null || mAdapter == null) {
                return;
            }
            binding.picekerRecycler.setVisibility(View.VISIBLE);
            medias = Boxing.getResult(data);
            if (requestCode == REQUEST_CODE) {
                mAdapter.setList(medias);
            } else if (requestCode == COMPRESS_REQUEST_CODE) {
                final List<BaseMedia> imageMedias = new ArrayList<>(1);
                BaseMedia baseMedia = medias.get(0);
                if (!(baseMedia instanceof ImageMedia)) {
                    return;
                }

                final ImageMedia imageMedia = (ImageMedia) baseMedia;
                // the compress task may need time
                if (imageMedia.compress(new ImageCompressor(this))) {
                    imageMedia.removeExif();
                    imageMedias.add(imageMedia);
                    mAdapter.setList(imageMedias);
                }

            }
        }
    }
    private boolean submit() {
        // validate
        String upTitleString = binding.upTitle.getText().toString().trim();
        if (TextUtils.isEmpty(upTitleString)) {
            Toast.makeText(this, "标题还没有写哦", Toast.LENGTH_SHORT).show();
            return false;
        }

        String upcontentString = binding.upContent.getText().toString().trim();
        if (TextUtils.isEmpty(upcontentString)) {
            Toast.makeText(this, "内容还没有写", Toast.LENGTH_SHORT).show();
            return false;
        }
//        list = pickerView.getDataList();
        if (medias == null || medias.size() == 0){
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
            return false;
        }
        // TODO validate success, do something


        return true;
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

    private class MediaResultAdapter extends RecyclerView.Adapter {
        private ArrayList<BaseMedia> mList;

        MediaResultAdapter() {
            mList = new ArrayList<>();
            BoxingMediaLoader.getInstance().init(new BoxingPicassoLoader());
        }

        void setList(List<BaseMedia> list) {
            if (list == null) {
                return;
            }
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }

        List<BaseMedia> getMedias() {
            if (mList == null || mList.size() <= 0 || !(mList.get(0) instanceof ImageMedia)) {
                return null;
            }
            return mList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_boxing_simple_media_item, parent, false);
            int height = parent.getMeasuredHeight() / 4;
            view.setMinimumHeight(height);
            return new MediaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MediaViewHolder) {
                MediaViewHolder mediaViewHolder = (MediaViewHolder) holder;
                mediaViewHolder.mImageView.setImageResource(BoxingManager.getInstance().getBoxingConfig().getMediaPlaceHolderRes());
                BaseMedia media = mList.get(position);
                String path;
                if (media instanceof ImageMedia) {
                    path = ((ImageMedia) media).getThumbnailPath();
                } else {
                    path = media.getPath();
                }
                BoxingMediaLoader.getInstance().displayThumbnail(mediaViewHolder.mImageView, path, 150, 150);
            }
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

    }

    private class MediaViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        MediaViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.media_item);
        }
    }
}
