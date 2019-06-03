package com.lvyangai.highopinion.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.adapter.VideoDetailsAdapter;
import com.lvyangai.highopinion.bean.CommItemBean;
import com.lvyangai.highopinion.bean.VideoItemBean;
import com.lvyangai.highopinion.ui.home.homefragment.video.VideoContract;
import com.lvyangai.highopinion.ui.home.homefragment.video.VideoFragment;
import com.lvyangai.highopinion.ui.home.homefragment.video.VideoPresenter;
import com.lvyangai.highopinion.ui.web.WebActivity;
import com.lvyangai.highopinion.util.IntentUtil;
import com.lvyangai.highopinion.util.MediaPlayerTool;
import com.lvyangai.highopinion.util.MyUtil;
import com.lvyangai.highopinion.util.ToastUtil;
import com.lvyangai.highopinion.view.VideoTouchView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by zhaoshuang on 2018/11/12.
 */

public class VideoDetailsActivity extends BaseActivity implements VideoContract.videoView{
    private static final String TAG = "VideoDetailsActivity";
    private static int commIndex = 0;
    private static final int COMM_ITEM_COUNT = 25; // 每次加载多少评论item
    private RecyclerView rv_video_detail;
    private LinearLayoutManager linearLayoutManager;
    private PagerSnapHelper pagerSnapHelper;
    private MediaPlayerTool mMediaPlayerTool;
    private List<VideoItemBean.VideoBean> dataList;
    private VideoDetailsAdapter adapter;
    private ImageView iv_close;
    private VideoPresenter presenter;
    private int playPosition;
    private int appUserId = 0;
    private EditText commEdit;
    private CommQuickAdapter commAdapter;
    RefreshLayout refreshLayout;
    RecyclerView recyclerView;
    private List<CommItemBean.CommBean> commList;



    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void dataProcess() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_details);

        initIntent();
        initUI();

        mMediaPlayerTool = MediaPlayerTool.getInstance();
        rv_video_detail.post(new Runnable() {
            @Override
            public void run() {
                rv_video_detail.scrollToPosition(playPosition);
                rv_video_detail.post(new Runnable() {
                    @Override
                    public void run() {
                        playVisibleVideo(mMediaPlayerTool.isPlaying());
                    }
                });
            }
        });
    }

    private void initIntent() {

        Intent intent = getIntent();
        presenter = new VideoPresenter(this);
        appUserId = MyApplication.getUserid();
        playPosition = intent.getIntExtra(IntentUtil.INTENT_PLAY_POSITION, 0);
        dataList = (List<VideoItemBean.VideoBean>) intent.getSerializableExtra(IntentUtil.INTENT_DATA_LIST);
        //没有数据自己去获取，测试时用的 现在不用了  单独页面也要用
//        if (dataList == null || dataList.size()==0){
//            dataList  = DataUtil.createData();
//        }
    }

    private void initUI(){

        rv_video_detail = findViewById(R.id.rv_video_detail);
        iv_close = findViewById(R.id.iv_close);

        linearLayoutManager = new LinearLayoutManager(mContext);
        rv_video_detail.setLayoutManager(linearLayoutManager);

        pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rv_video_detail);
        adapter = new VideoDetailsAdapter(mContext, dataList);
        rv_video_detail.setAdapter(adapter);

        rv_video_detail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(pagerSnapHelper.findSnapView(linearLayoutManager) != playView){
                        playVisibleVideo(false);
                    }
                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    View playView;
    /**
     * @param isResumePlay 是否继续上个界面播放
     */
    private void playVisibleVideo(boolean isResumePlay){

        View snapView = pagerSnapHelper.findSnapView(linearLayoutManager);
        if(snapView == null){
            return ;
        }
        final int position = linearLayoutManager.getPosition(snapView);
        if(position < 0){
            return ;
        }

        if(!isResumePlay){
            //重置播放器要在前面
            mMediaPlayerTool.reset();
        }

        playView = snapView;
        final VideoDetailsAdapter.MyViewHolder vh = (VideoDetailsAdapter.MyViewHolder) rv_video_detail.getChildViewHolder(playView);

        if(isResumePlay){
            vh.pb_video.setVisibility(View.GONE);
            vh.iv_cover.setVisibility(View.GONE);
            vh.playTextureView.setRotation(mMediaPlayerTool.getRotation());
        }else{
            //显示正在加载的界面
            vh.pb_video.setVisibility(View.VISIBLE);
            vh.iv_cover.setVisibility(View.VISIBLE);

            mMediaPlayerTool.initMediaPLayer();
            mMediaPlayerTool.setDataSource(dataList.get(position).getVideo_url());
        }

        vh.iv_user_like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                presenter.setPageLike("video",appUserId,dataList.get(position).getVideo_page_id(),1);
                vh.iv_user_like_number.setText(""+(dataList.get(position).getVideo_like()+1));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                presenter.setPageLike("video",appUserId,dataList.get(position).getVideo_page_id(),0);
                vh.iv_user_like_number.setText(""+(dataList.get(position).getVideo_like()));
            }
        });
        vh.iv_user_comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick likedClick: "+position+"   "+dataList.get(position).getVideo_page_id() );
                showBottomDialog(dataList.get(position).getVideo_page_id());

            }
        });

        vh.videoTouchView.setOnTouchSlideListener(new VideoTouchView.OnTouchSlideListener() {
            @Override
            public void onSlide(float distant) {
                if(mMediaPlayerTool == null){
                    return ;
                }
                if(!vh.rl_change_progress.isShown()){
                    vh.rl_change_progress.setVisibility(View.VISIBLE);
                    changeProgressTime = mMediaPlayerTool.getCurrentPosition();
                }
                changeProgressText(vh, distant);
            }
            @Override
            public void onUp() {
                if(vh.rl_change_progress.isShown()){
                    vh.rl_change_progress.setVisibility(View.GONE);
                }
                mMediaPlayerTool.seekTo(changeProgressTime);
            }
            @Override
            public void onClick() {
                mContext.onBackPressed();
            }
        });

        mMediaPlayerTool.setVolume(1);
        mMediaPlayerTool.setVideoListener(new MediaPlayerTool.VideoListener() {
            @Override
            public void onStart() {
                vh.pb_video.setVisibility(View.GONE);
                vh.iv_cover.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vh.iv_cover.setVisibility(View.GONE);
                    }
                }, 300);
            }
            @Override
            public void onRotationInfo(int rotation) {
                vh.playTextureView.setRotation(rotation);
            }
            @Override
            public void onStop() {
                vh.pb_video.setVisibility(View.GONE);
                vh.iv_cover.setVisibility(View.VISIBLE);
                vh.pb_play_progress.setSecondaryProgress(0);
                vh.pb_play_progress.setProgress(0);
                vh.tv_progress.setText("");
                playView = null;
            }
            @Override
            public void onCompletion() {
                playVisibleVideo(false);
            }
            @Override
            public void onPlayProgress(long currentPosition) {
                int pro = (int) (currentPosition*1f/ mMediaPlayerTool.getDuration()*100);
                vh.pb_play_progress.setProgress(pro);

                String currentPositionStr = MyUtil.fromMMss(currentPosition);
                String videoDurationStr = MyUtil.fromMMss(mMediaPlayerTool.getDuration());
                vh.tv_progress.setText(currentPositionStr + "/" + videoDurationStr);
            }
            @Override
            public void onBufferProgress(int progress) {
                vh.pb_play_progress.setSecondaryProgress(progress);
            }
        });

        if(isResumePlay){
            vh.playTextureView.resetTextureView(mMediaPlayerTool.getAvailableSurfaceTexture());
            mMediaPlayerTool.setPlayTextureView(vh.playTextureView);
            vh.playTextureView.postInvalidate();
        }else{
            vh.playTextureView.resetTextureView();
            mMediaPlayerTool.setPlayTextureView(vh.playTextureView);
            mMediaPlayerTool.setSurfaceTexture(vh.playTextureView.getSurfaceTexture());
            mMediaPlayerTool.prepare();
        }
    }

    long changeProgressTime;
    private void changeProgressText(VideoDetailsAdapter.MyViewHolder vh, float distant){

        float radio = distant/vh.pb_play_progress.getWidth();
        changeProgressTime += mMediaPlayerTool.getDuration()*radio;

        if(changeProgressTime < 0){
            changeProgressTime = 0;
        }
        if(changeProgressTime > mMediaPlayerTool.getDuration()){
            changeProgressTime = mMediaPlayerTool.getDuration();
        }

        String changeTimeStr = MyUtil.fromMMss(changeProgressTime);
        String rawTime = MyUtil.fromMMss(mMediaPlayerTool.getDuration());
        vh.tv_change_progress.setText(changeTimeStr+" / "+rawTime);

        if(changeProgressTime > mMediaPlayerTool.getCurrentPosition()){
            vh.iv_change_progress.setImageResource(R.mipmap.video_fast_forward);
        }else{
            vh.iv_change_progress.setImageResource(R.mipmap.video_fast_back);
        }
    }

    boolean isFirst = true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(isFirst){
            isFirst = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!isFirst && mMediaPlayerTool !=null && !mMediaPlayerTool.isPlaying()){
            playVisibleVideo(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mMediaPlayerTool!=null && mMediaPlayerTool.isPlaying()) {
            if (dontPause) {
                View snapView = pagerSnapHelper.findSnapView(linearLayoutManager);
                if(snapView!=null && linearLayoutManager.getPosition(snapView)!=playPosition){
                    mMediaPlayerTool.reset();
                }
            } else {
                mMediaPlayerTool.reset();
            }
        }
    }

    boolean dontPause;
    @Override
    public void finish() {
        super.finish();

        dontPause = true;
    }


    private void showBottomDialog(final int pageId) {
        commList = new ArrayList<>();
        BottomSheetDialog dialog=new BottomSheetDialog(this);
        View dialogView = View.inflate(this, R.layout.comm_layout, null);
        commAdapter = new CommQuickAdapter(R.layout.item_comm);
        refreshLayout = dialogView.findViewById(R.id.comm_refreshLayout);
        recyclerView = dialogView.findViewById(R.id.comm_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commAdapter);
        commAdapter.replaceData(commList);
//        refreshLayout.setEnableRefresh(false);
//        refreshLayout.setEnableNestedScroll(false);
//        refreshLayout.setRefreshContent(recyclerView);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.getCommList(pageId);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (commAdapter.getItemCount() >= commList.size()) {
                           ToastUtil.showShortToast("数据加载完毕");
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        } else {
                            commAdapter.addData(getMoreCommData());
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 1000);
            }
        });
        ImageView send = dialogView.findViewById(R.id.comm_send);
        commEdit = dialogView.findViewById(R.id.comm_edit);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendStr = commEdit.getText().toString();
                if (TextUtils.isEmpty(sendStr.trim())){
                    ToastUtil.showShortToast("还没有填写评论内容哦");
                }else {
                    showProDialog();
                    presenter.upComm("video",appUserId,pageId,sendStr);
                }
            }
        });
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(MyUtil.getWindowHeight()*3/5));
        dialog.setContentView(dialogView,layoutParams);

        dialog.show();
    }

    @Override
    public void retrofitSuccess(Response<VideoItemBean> response) {
        
    }

    @Override
    public void retrofitFailed(String log) {

    }

    @Override
    public void retrofitLikeMessage(String log) {
        ToastUtil.showShortToast(log);
    }

    @Override
    public void retrofitDataSuccess(Response<CommItemBean> response) {
        if (response != null){
            commIndex = 0;
            commList = response.body().getComm();
            commAdapter.replaceData(getMoreCommData());
            refreshLayout.finishRefresh();
            refreshLayout.resetNoMoreData();
        }else {
            refreshLayout.finishRefresh();
            refreshLayout.resetNoMoreData();
        }
    }

    private List<CommItemBean.CommBean> getMoreCommData() {
        List<CommItemBean.CommBean> moreCommData = new ArrayList<>();
        for(int i=0;i<COMM_ITEM_COUNT;i++,commIndex++){
            if (commIndex == commList.size()){
                break;
            }else {
                moreCommData.add(commList.get(commIndex));
            }
        }
        return moreCommData;
    }
    @Override
    public void retrofitDataFailed(String msg) {
        ToastUtil.showShortToast(msg);
        refreshLayout.finishRefresh();
        refreshLayout.resetNoMoreData();
    }

    @Override
    public void retrofitComm(String msg) {
        ToastUtil.showShortToast(msg);
        refreshLayout.autoRefresh();
        dismissDialog();
        commEdit.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private class CommQuickAdapter extends BaseQuickAdapter<CommItemBean.CommBean,BaseViewHolder> {
        RequestOptions optionsIcon;
        public CommQuickAdapter(int layoutResId) {
            super(layoutResId);
            optionsIcon  = new RequestOptions()
                    .error(R.mipmap.icon_user_error)
                    .fitCenter();
        }

        @Override
        protected void convert(BaseViewHolder helper, CommItemBean.CommBean item) {
            helper.setText(R.id.comm_user_name,item.getComm_username())
                    .setText(R.id.comm_content,item.getComm_content())
                    .setText(R.id.comm_date,item.getComm_date());
            Glide.with(mContext).load(item.getComm_user_icon()).apply(optionsIcon).into((ImageView) helper.itemView.findViewById(R.id.comm_user_icon));
        }
    }
}
