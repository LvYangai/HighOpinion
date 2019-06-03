package com.lvyangai.highopinion.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.MainActivity;
import com.lvyangai.highopinion.bean.VideoItemBean;
import com.lvyangai.highopinion.util.IntentUtil;
import com.lvyangai.highopinion.view.PlayTextureView;

import java.util.List;

/**
 * Created by zhaoshuang on 2018/11/1.
 */

public class MainAdapter extends RecyclerView.Adapter{

    private MainActivity mContext;
    private List<VideoItemBean.VideoBean> mainVideoBeanList;

    public MainAdapter(MainActivity context, List<VideoItemBean.VideoBean> mainVideoBeanList){
        this.mContext = context;
        this.mainVideoBeanList = mainVideoBeanList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(mContext, R.layout.item_video, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final MyViewHolder vh = (MyViewHolder) holder;
        VideoItemBean.VideoBean mainVideoBean = mainVideoBeanList.get(position);

        vh.playTextureView.setVideoSize(0, 0);
        Glide.with(mContext).load(mainVideoBean.getPost_icon()).into(vh.iv_avatar);
        vh.tv_content.setText(mainVideoBean.getVideo_title());
        vh.tv_name.setText(mainVideoBean.getPost_user());

        Glide.with(mContext).load(mainVideoBean.getVideo_thumb()).into(vh.iv_cover);

        vh.playTextureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.jumpNotCloseMediaPlay(position);
                IntentUtil.gotoVideoDetailsActivity(mContext, mainVideoBeanList, position, vh.playTextureView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mainVideoBeanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout rl_video;
        public PlayTextureView playTextureView;
        public ImageView iv_cover;
        public ProgressBar pb_video;
        public ImageView iv_play_icon;
        public TextView tv_play_time;

        private TextView tv_content;
        private ImageView iv_avatar;
        private TextView tv_name;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            rl_video = itemView.findViewById(R.id.rl_video);
            playTextureView = itemView.findViewById(R.id.playTextureView);
            iv_cover = itemView.findViewById(R.id.iv_cover);
            pb_video = itemView.findViewById(R.id.pb_video);
            iv_play_icon = itemView.findViewById(R.id.iv_play_icon);
            tv_content = itemView.findViewById(R.id.tv_content);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_play_time = itemView.findViewById(R.id.tv_play_time);
        }
    }
}
