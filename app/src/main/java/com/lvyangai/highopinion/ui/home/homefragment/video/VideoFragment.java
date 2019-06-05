package com.lvyangai.highopinion.ui.home.homefragment.video;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseFragment;
import com.lvyangai.highopinion.adapter.VideoDetailsAdapter;
import com.lvyangai.highopinion.bean.CommItemBean;
import com.lvyangai.highopinion.bean.VideoItemBean;
import com.lvyangai.highopinion.databinding.FragmentVideoBinding;
import com.lvyangai.highopinion.ui.web.WebActivity;
import com.lvyangai.highopinion.util.MediaPlayerTool;
import com.lvyangai.highopinion.util.MyUtil;
import com.lvyangai.highopinion.util.ToastUtil;
import com.lvyangai.highopinion.view.VideoTouchView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends BaseFragment implements VideoContract.videoView{
    private AlertDialog dialog;
    private static int index = 0;
    private static final int ITEM_COUNT = 15; // 每次加载多少视频item
    private static int commIndex = 0;
    private static final int COMM_ITEM_COUNT = 25; // 每次加载多少评论item

    private FragmentVideoBinding binding;
    private Context context;
    private View view;
    private PagerSnapHelper pagerSnapHelper;
    private static final String TAG = "VideoFragment";
    private List<VideoItemBean.VideoBean> dataList;
    private LinearLayoutManager linearLayoutManager;
    private MediaPlayerTool mMediaPlayerTool;
    private int playPosition;
    boolean isFirst = true;
    View playView;
    boolean dontPause;
    boolean isPlay;
    long changeProgressTime;
    private VideoDetailsAdapter adapter;
    private VideoPresenter presenter;
    private int appUserId = 0;
    private EditText commEdit;
    private CommQuickAdapter commAdapter;
    RefreshLayout refreshLayout;
    RecyclerView recyclerView;
    private List<CommItemBean.CommBean> commList;

    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        if (view == null){
            binding = DataBindingUtil.inflate(inflater,R.layout.fragment_video,container,false);
            view = binding.getRoot();
        }
        return view;
    }

    @Override
    protected void initData() {
        context = getActivity();
        presenter = new VideoPresenter(this);

    }

    @Override
    protected void dataProcess() {
        dataList = new ArrayList<>();
        dataList.add(new VideoItemBean.VideoBean(58,false,"admin1","http://106.14.215.7/highopinion/data/uploads/20190424/202a274feff4548308795f46a8b12192.jpg"
        ,"哪款削皮刀好一点","data/media/20190526/1558891079.mp4","http://106.14.215.7/highopinion/data/uploads/20190527/ca80c730283076c0335565effce41ec4.PNG"
        ,"05-27",2,5,5));
        linearLayoutManager = new LinearLayoutManager(context);
        binding.rvVideoDetail.setLayoutManager(linearLayoutManager);
        pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(binding.rvVideoDetail);
        adapter = new VideoDetailsAdapter(context, dataList);
        appUserId = MyApplication.getUserid();
        binding.rvVideoDetail.setAdapter(adapter);
        binding.rvVideoDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(pagerSnapHelper.findSnapView(linearLayoutManager) != playView){
                        playVisibleVideo(false);
                    }
                }
            }
        });
        binding.fragVideoRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                mMediaPlayerTool.reset();
                presenter.getDataList(MyApplication.getUserid());
            }
        });
        binding.fragVideoRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getItemCount() >= dataList.size()) {
                            Toast.makeText(context, "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            binding.fragVideoRefreshLayoutFooter.setVisibility(View.VISIBLE);
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        } else {
                            adapter.loadMore(getMoreData());
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 1000);
            }
        });
        binding.fragVideoRefreshLayout.autoRefresh();
        mMediaPlayerTool = MediaPlayerTool.getInstance();
        binding.rvVideoDetail.post(new Runnable() {
            @Override
            public void run() {
                binding.rvVideoDetail.scrollToPosition(playPosition);
                binding.rvVideoDetail.post(new Runnable() {
                    @Override
                    public void run() {
                        playVisibleVideo(mMediaPlayerTool.isPlaying());
                    }
                });
            }
        });

    }

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
        final VideoDetailsAdapter.MyViewHolder vh = (VideoDetailsAdapter.MyViewHolder) binding.rvVideoDetail.getChildViewHolder(playView);

        if(isResumePlay){
            vh.pb_video.setVisibility(View.GONE);
            vh.iv_cover.setVisibility(View.GONE);
            vh.playTextureView.setRotation(mMediaPlayerTool.getRotation());
        }else{
            //显示正在加载的界面
            isPlay = false;
            vh.iv_stopOrStart.setVisibility(View.INVISIBLE);
            vh.pb_video.setVisibility(View.VISIBLE);
            vh.iv_cover.setVisibility(View.VISIBLE);

            mMediaPlayerTool.initMediaPLayer();
            mMediaPlayerTool.setDataSource(dataList.get(position).getVideo_url());
        }
        vh.iv_user_like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                presenter.setPageLike("video",appUserId,dataList.get(position).getId(),1);
                vh.iv_user_like_number.setText(""+(dataList.get(position).getVideo_like()+1));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                presenter.setPageLike("video",appUserId,dataList.get(position).getId(),0);
                vh.iv_user_like_number.setText(""+(dataList.get(position).getVideo_like()));
            }
        });
        vh.iv_user_comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick likedClick: "+position );
                showBottomDialog(dataList.get(position).getId());

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
//                context.onBackPressed();
                if (isPlay){
                    vh.iv_stopOrStart.setVisibility(View.INVISIBLE);
                    isPlay = !isPlay;
                    if (!mMediaPlayerTool.isPlaying()){
                        mMediaPlayerTool.start();
                    }
                }else {
                    vh.iv_stopOrStart.setVisibility(View.VISIBLE);
                    isPlay = !isPlay;
                    if (mMediaPlayerTool.isPlaying()){
                        mMediaPlayerTool.pause();
                    }
                }
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
//                onStop();
                //自动切换
//                if(position+1 >= dataList.size()) {
//                    binding.rvVideoDetail.smoothScrollToPosition(0);
//                }else{
//                    binding.rvVideoDetail.smoothScrollToPosition(position+1);
//                }
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            onPause();
        }else {
            isFirst = false;
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: " );
        //判断当前页面是不是视频页
        if (2 != MyApplication.getNowPageIndex()){
            return;
        }
        if(!isFirst && mMediaPlayerTool !=null && !mMediaPlayerTool.isPlaying()){
            playVisibleVideo(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: " );
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

    @Override
    protected void destroy() {
        dontPause = true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void retrofitSuccess(Response<VideoItemBean> response) {
        index = 0;
        dataList = response.body().getVideo();
        adapter.refresh(getMoreData());
        binding.fragVideoRefreshLayout.finishRefresh();
        binding.fragVideoRefreshLayout.resetNoMoreData();
        playVisibleVideo(false);
//        binding.rvVideoDetail.smoothScrollToPosition(0);
//        mMediaPlayerTool.start();
    }

    public List<VideoItemBean.VideoBean> getMoreData() {
        List<VideoItemBean.VideoBean> moreData = new ArrayList<>();
        for(int i=0;i<ITEM_COUNT;i++,index++){
            if (index == dataList.size()){
                break;
            }else {
                moreData.add(dataList.get(index));
            }
        }
        return moreData;
    }


    private void showBottomDialog(final int pageId) {
        commList = new ArrayList<>();
        BottomSheetDialog dialog=new BottomSheetDialog(context);
        View dialogView = View.inflate(context, R.layout.comm_layout, null);
        commAdapter = new CommQuickAdapter(R.layout.item_comm);
        refreshLayout = dialogView.findViewById(R.id.comm_refreshLayout);
        recyclerView = dialogView.findViewById(R.id.comm_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(commAdapter);
        commAdapter.replaceData(commList);
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
                            Toast.makeText(context, "数据全部加载完毕", Toast.LENGTH_SHORT).show();
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
                    showProDialog(context);
                    presenter.upComm("video",appUserId,pageId,sendStr);
                }
            }
        });
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(MyUtil.getWindowHeight()*3/5));
        dialog.setContentView(dialogView,layoutParams);

        dialog.show();
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
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
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
