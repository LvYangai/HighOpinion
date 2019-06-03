package com.lvyangai.highopinion.adapter;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.like.LikeButton;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.bean.VideoItemBean;
import com.lvyangai.highopinion.ui.adapter.BaseRecyclerAdapter;
import com.lvyangai.highopinion.util.MyUtil;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.lvyangai.highopinion.view.PlayTextureView;
import com.lvyangai.highopinion.view.VideoTouchView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhaoshuang on 2018/11/12.
 */

public class VideoDetailsAdapter extends RecyclerView.Adapter {
    private static final String TAG = "VideoDetailsAdapter";

    private int mLastPosition = -1;
    private Context mContext;
    private List<VideoItemBean.VideoBean> mList;

    public VideoDetailsAdapter(Context mContext, List<VideoItemBean.VideoBean> mainVideoBeanList) {
        this.mContext = mContext;
        this.mList = mainVideoBeanList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(mContext, R.layout.item_video_details, null));
    }

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */
    public void notifyListDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }


    public boolean areAllItemsEnabled() {
        return true;
    }

    public boolean isEnabled(int position) {
        return true;
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public int getCount() {
        return mList.size();
    }
    /**
     * Notifies the attached observers that the underlying data is no longer valid
     * or available. Once invoked this adapter is no longer valid and should
     * not report further data set changes.
     */
    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    public VideoDetailsAdapter refresh(List<VideoItemBean.VideoBean> collection) {
        if (mList != null && mList.size() >0){
            mList.clear();
        }
        mList = collection;
        notifyDataSetChanged();
        notifyListDataSetChanged();
        mLastPosition = -1;
        return this;
    }

    public VideoDetailsAdapter loadMore(List<VideoItemBean.VideoBean> collection) {
        mList.addAll(collection);
        notifyDataSetChanged();
        notifyListDataSetChanged();
        return this;
    }

    public VideoDetailsAdapter insert(List<VideoItemBean.VideoBean> collection) {
        mList.addAll(0, collection);
        notifyDataSetChanged();
        notifyListDataSetChanged();
        return this;
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyViewHolder vh = (MyViewHolder) holder;
        VideoItemBean.VideoBean mainVideoBean = mList.get(position);

        vh.tv_progress.setText("");
        vh.pb_play_progress.setSecondaryProgress(0);
        vh.pb_play_progress.setProgress(0);
        vh.playTextureView.setVideoSize(0,0);

        Glide.with(mContext).load(mainVideoBean.getPost_icon()).into(vh.iv_avatar);
        Glide.with(mContext).load(mainVideoBean.getVideo_thumb()).into(vh.iv_cover);

        vh.tv_content.setText(mainVideoBean.getVideo_title());
        vh.tv_name.setText(mainVideoBean.getPost_user());
        vh.iv_user_like_number.setText(""+mainVideoBean.getVideo_like());
        vh.iv_user_comm_number.setText(""+mainVideoBean.getVideo_comment());
        Log.e(TAG, "onBindViewHolder: "+mainVideoBean.isPost_is_like()+"   "+mainVideoBean.getVideo_comment()+"    "+mainVideoBean.getVideo_like() );
        vh.iv_user_like.setLiked(mainVideoBean.isPost_is_like());
//        setVideoSize(vh,mainVideoBean.getVideoWidth(), mainVideoBean.getVideoHeight());
    }

    private void setVideoSize(MyViewHolder vh, int videoWidth, int videoHeight){

        float videoRatio = videoWidth * 1f / videoHeight;
        int windowWidth = MyUtil.getWindowWidth();
        int windowHeight = MyUtil.getWindowHeight() + StatusBarUtil.getStatusBarHeight(mContext);
        float windowRatio = MyUtil.getWindowWidth()*1f/MyUtil.getWindowHeight();
        ViewGroup.LayoutParams layoutParams = vh.videoTouchView.getLayoutParams();
        if (videoRatio >= windowRatio) {
            layoutParams.width = windowWidth;
            layoutParams.height = (int) (layoutParams.width / videoRatio);
//            layoutParams.height = windowHeight;
        } else {
            layoutParams.height = windowHeight;
            layoutParams.width = (int) (layoutParams.height * videoRatio);
        }
        vh.videoTouchView.setLayoutParams(layoutParams);
    }


    @Override
    public int getItemCount() {
        if (mList != null && mList.size() > 0){
            return mList.size();
        }else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public VideoTouchView videoTouchView;
        public ImageView iv_cover;
        public PlayTextureView playTextureView;
        public ProgressBar pb_video;
        public ProgressBar pb_play_progress;
        public TextView tv_progress;
        public RelativeLayout rl_change_progress;
        public ImageView iv_change_progress;
        public TextView tv_change_progress;
        private ImageView iv_avatar;
        private TextView tv_name;
        private TextView tv_content;
        public ImageView iv_stopOrStart;

        public LikeButton iv_user_like;
        public TextView iv_user_like_number;
        public ImageView iv_user_comm;
        public TextView iv_user_comm_number;


        public MyViewHolder(View itemView) {
            super(itemView);

            videoTouchView = itemView.findViewById(R.id.videoTouchView);
            playTextureView = itemView.findViewById(R.id.playTextureView);
            iv_cover = itemView.findViewById(R.id.iv_cover);
            pb_video = itemView.findViewById(R.id.pb_video);
            pb_play_progress = itemView.findViewById(R.id.pb_play_progress);
            tv_progress = itemView.findViewById(R.id.tv_progress);
            rl_change_progress = itemView.findViewById(R.id.rl_change_progress);
            iv_change_progress = itemView.findViewById(R.id.iv_change_progress);
            tv_change_progress = itemView.findViewById(R.id.tv_change_progress);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_content = itemView.findViewById(R.id.tv_content);
            iv_stopOrStart = itemView.findViewById(R.id.iv_stop_or_start);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            pb_play_progress.getProgressDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.white), PorterDuff.Mode.SRC_IN);

            iv_user_like = itemView.findViewById(R.id.iv_user_like);
            iv_user_like_number = itemView.findViewById(R.id.iv_user_like_number);
            iv_user_comm = itemView.findViewById(R.id.iv_user_comm);
            iv_user_comm_number = itemView.findViewById(R.id.iv_user_comm_number);
        }
    }
}

