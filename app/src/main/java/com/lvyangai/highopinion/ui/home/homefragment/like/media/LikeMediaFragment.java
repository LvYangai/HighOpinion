package com.lvyangai.highopinion.ui.home.homefragment.like.media;


import android.Manifest;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseLazyFragment;
import com.lvyangai.highopinion.adapter.VideoDetailsAdapter;
import com.lvyangai.highopinion.bean.VideoItemBean;
import com.lvyangai.highopinion.databinding.FragmentLikeMediaBinding;
import com.lvyangai.highopinion.util.IntentUtil;
import com.lvyangai.highopinion.util.MediaPlayerTool;
import com.lvyangai.highopinion.util.MyUtil;
import com.lvyangai.highopinion.util.ToastUtil;
import com.lvyangai.highopinion.view.PlayTextureView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikeMediaFragment extends BaseLazyFragment implements LikeMediaContract.likeMediaView{

    private static int index = 0;
    private static final int ITEM_COUNT = 40; // 每次加载多少item

    private FragmentLikeMediaBinding binding;
    private Context context;
    private FragmentAdapter adapter;
    private View view;
    private static final String TAG = "LikeMediaFragment";
    private MediaPlayerTool mMediaPlayerTool;
    private List<VideoItemBean.VideoBean> dataList;
    private LikeMediaPresenter presenter;
    MediaPlayerTool.VideoListener myVideoListener;
    //当前播放的视频角标
    int currentPlayIndex;
    private int appUserId = 0;//用户id
    //可以播放的视频集合
    ArrayList<Integer> videoPositionList = new ArrayList<>();
    View currentPlayView;
    boolean isFirst = true;

    public LikeMediaFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        if (view == null){
            binding = DataBindingUtil.inflate(inflater,R.layout.fragment_like_media,container,false);
            view = binding.getRoot();
        }
        return view;
    }

    @Override
    protected void initData() {
    }
    //跳转页面时是否关闭播放器
    private int jumpVideoPosition = -1;
    public void jumpNotCloseMediaPlay(int position){
        jumpVideoPosition = position;
    }

    @Override
    protected void dataProcess() {
        appUserId = MyApplication.getUserid();
        presenter = new LikeMediaPresenter(this);
        binding.fragLikeRvVideo.setLayoutManager(new LinearLayoutManager(context));
        adapter = new FragmentAdapter(context, dataList);
        binding.fragLikeRvVideo.setAdapter(adapter);
        binding.fragLikeRvVideo.addItemDecoration(new RecyclerView.ItemDecoration(){
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                int position = parent.getChildAdapterPosition(view);
                if (position != 0) {
                    outRect.top = (int) getResources().getDimension(R.dimen.activity_margin2);
                }
            }
        });

        binding.fragLikeMediaRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.getDataList("video",appUserId);
            }
        });
        binding.fragLikeMediaRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getItemCount() >= dataList.size()) {
                            Toast.makeText(context, "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        } else {
                            adapter.loadMore(getMoreData());
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 1000);
            }
        });
        binding.fragLikeMediaRefreshLayout.autoRefresh();

        binding.fragLikeRvVideo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(currentPlayView!=null){
                    boolean playRange = isPlayRange(currentPlayView, recyclerView);
                    if(!playRange){
                        mMediaPlayerTool.reset();
                    }
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //检测播放视频
                    checkPlayVideo();
                    if(currentPlayView == null){
                        playVideoByPosition(-1);
                    }
                }
            }
        });
        mMediaPlayerTool = MediaPlayerTool.getInstance();

        AndPermission.with(context).permission(Manifest.permission.WRITE_EXTERNAL_STORAGE).callback(new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            }
            @Override
            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

            }
        }).start();

    }
    //检查子view是否在父view显示布局里面
    private boolean isPlayRange(View childView, View parentView){

        if(childView==null || parentView==null){
            return false;
        }

        int[] childLocal = new int[2];
        childView.getLocationOnScreen(childLocal);

        int[] parentLocal = new int[2];
        parentView.getLocationOnScreen(parentLocal);

        boolean playRange = childLocal[1]>=parentLocal[1] &&
                childLocal[1]<=parentLocal[1]+parentView.getHeight()-childView.getHeight();

        return playRange;
    }

    //检测是否播放视频
    private void checkPlayVideo(){

        currentPlayIndex = 0;
        videoPositionList.clear();

        int childCount = binding.fragLikeRvVideo.getChildCount();
        for (int x = 0; x < childCount; x++) {
            View childView = binding.fragLikeRvVideo.getChildAt(x);
            boolean playRange = isPlayRange(childView.findViewById(R.id.rl_video), binding.fragLikeRvVideo);
            if(playRange){
                int position = binding.fragLikeRvVideo.getChildAdapterPosition(childView);
                if(position>=0 && !videoPositionList.contains(position)){
                    videoPositionList.add(position);
                }
            }
        }
    }

    private void playVideoByPosition(int resumePosition){

        boolean isResumePlay = resumePosition >= 0;

        if(!isResumePlay && (videoPositionList.size()==0 || mMediaPlayerTool ==null)){
            return ;
        }

        if(!isResumePlay){
            //一定要先重置播放器
            mMediaPlayerTool.reset();
        }

        int playPosition = 0;
        if(isResumePlay){
            playPosition = resumePosition;
        }else{
            if(currentPlayIndex >= videoPositionList.size()){
                currentPlayIndex = 0;
            }
            playPosition = videoPositionList.get(currentPlayIndex);
        }

        //根据传进来的position找到对应的ViewHolder
        final FragmentAdapter.MyViewHolder vh = (FragmentAdapter.MyViewHolder) binding.fragLikeRvVideo.findViewHolderForAdapterPosition(playPosition);
        if(vh == null){
            return ;
        }

        currentPlayView = vh.rl_video;

        //初始化一些播放状态, 如进度条,播放按钮,加载框等
        if(isResumePlay){
            vh.pb_video.setVisibility(View.GONE);
            vh.iv_play_icon.setVisibility(View.GONE);
            vh.iv_cover.setVisibility(View.GONE);
        }else{
            //显示正在加载的界面
            vh.iv_play_icon.setVisibility(View.GONE);
            vh.pb_video.setVisibility(View.VISIBLE);
            vh.iv_cover.setVisibility(View.VISIBLE);
            vh.tv_play_time.setText("");

            mMediaPlayerTool.initMediaPLayer();

            String videoUrl = dataList.get(playPosition).getVideo_url();
            mMediaPlayerTool.setDataSource(videoUrl);
        }

        mMediaPlayerTool.setVolume(0);
        myVideoListener = new MediaPlayerTool.VideoListener() {
            @Override
            public void onStart() {
                vh.iv_play_icon.setVisibility(View.GONE);
                vh.pb_video.setVisibility(View.GONE);
                //防止闪屏
                vh.iv_cover.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vh.iv_cover.setVisibility(View.GONE);
                    }
                }, 300);
            }
            @Override
            public void onStop() {
                vh.pb_video.setVisibility(View.GONE);
                vh.iv_cover.setVisibility(View.VISIBLE);
                vh.iv_play_icon.setVisibility(View.VISIBLE);
                vh.tv_play_time.setText("");
                currentPlayView = null;
            }
            @Override
            public void onCompletion() {
                currentPlayIndex++;
                playVideoByPosition(-1);
            }
            @Override
            public void onRotationInfo(int rotation) {
                vh.playTextureView.setRotation(rotation);
            }
            @Override
            public void onPlayProgress(long currentPosition) {
                String date = MyUtil.fromMMss(mMediaPlayerTool.getDuration() - currentPosition);
                vh.tv_play_time.setText(date);
            }
        };
        mMediaPlayerTool.setVideoListener(myVideoListener);

        if(isResumePlay){
            //把播放器当前绑定的SurfaceTexture取出起来, 设置给当前界面的TextureView
            vh.playTextureView.resetTextureView(mMediaPlayerTool.getAvailableSurfaceTexture());
            mMediaPlayerTool.setPlayTextureView(vh.playTextureView);
            vh.playTextureView.postInvalidate();
        }else {
            vh.playTextureView.resetTextureView();
            mMediaPlayerTool.setPlayTextureView(vh.playTextureView);
            mMediaPlayerTool.setSurfaceTexture(vh.playTextureView.getSurfaceTexture());
            mMediaPlayerTool.prepare();
        }
    }


    @Override
    protected void destroy() {
        if(mMediaPlayerTool != null) {
            //关闭播放器
            mMediaPlayerTool.reset();
            if (!videoPositionList.contains(jumpVideoPosition)) {
                videoPositionList.add(jumpVideoPosition);
            }
            currentPlayIndex = videoPositionList.indexOf(jumpVideoPosition);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMediaPlayerTool != null){
            mMediaPlayerTool.reset();
        }

    }


    @Override
    protected void onFirstUserVisible() {
        context = getActivity();
//        dataList = DataUtil.createData();
        dataProcess();
    }

    @Override
    public void onUserVisible() {
        isFirst = false;
        this.onResume();
    }

    @Override
    protected void onUserInvisible() {
        this.onPause();
    }



    public void refreshVideo(){

        if(mMediaPlayerTool !=null) {
            mMediaPlayerTool.reset();
            checkPlayVideo();
            playVideoByPosition(-1);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //检测是否继续播放视频
        if(jumpVideoPosition!=-1 &&
                (videoPositionList.size()>currentPlayIndex && jumpVideoPosition==videoPositionList.get(currentPlayIndex))
                && mMediaPlayerTool!=null && mMediaPlayerTool.isPlaying()){
            playVideoByPosition(jumpVideoPosition);
        }else{
            refreshVideo();
        }
        jumpVideoPosition = -1;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mMediaPlayerTool != null) {
            //如果要跳转播放, 那么不关闭播放器
            if (videoPositionList.size()>currentPlayIndex && jumpVideoPosition==videoPositionList.get(currentPlayIndex)) {
                binding.fragLikeRvVideo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (myVideoListener != null) {
                            myVideoListener.onStop();
                        }
                    }
                }, 300);
            } else {
                mMediaPlayerTool.reset();
                if (!videoPositionList.contains(jumpVideoPosition)) {
                    videoPositionList.add(jumpVideoPosition);
                }
                currentPlayIndex = videoPositionList.indexOf(jumpVideoPosition);
            }
        }
    }

    @Override
    public void retrofitSuccess(Response<VideoItemBean> response) {
        index = 0;
        dataList = response.body().getVideo();
        if (dataList.size()!=0){
            adapter.refresh(getMoreData());
        }else {
            ToastUtil.showShortToast("暂时没有收藏呢");
        }
        binding.fragLikeMediaRefreshLayout.finishRefresh();
        binding.fragLikeMediaRefreshLayout.resetNoMoreData();
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

    @Override
    public void retrofitFailed(String log) {
        binding.fragLikeMediaRefreshLayout.finishRefresh();
        binding.fragLikeMediaRefreshLayout.resetNoMoreData();
        ToastUtil.showShortToast(log);
    }

    public class FragmentAdapter extends RecyclerView.Adapter {
        private Context mContext;
        private List<VideoItemBean.VideoBean> mList;
        private int mLastPosition = -1;

        public FragmentAdapter(Context context, List<VideoItemBean.VideoBean> mainVideoBeanList) {
            this.mContext = context;
            this.mList = mainVideoBeanList;
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

        public FragmentAdapter refresh(List<VideoItemBean.VideoBean> collection) {
            if (mList != null && mList.size() >0){
                mList.clear();
            }
            mList = collection;
            notifyDataSetChanged();
            notifyListDataSetChanged();
            mLastPosition = -1;
            return this;
        }

        public FragmentAdapter loadMore(List<VideoItemBean.VideoBean> collection) {
            mList.addAll(collection);
            notifyDataSetChanged();
            notifyListDataSetChanged();
            return this;
        }

        public FragmentAdapter insert(List<VideoItemBean.VideoBean> collection) {
            mList.addAll(0, collection);
            notifyDataSetChanged();
            notifyListDataSetChanged();
            return this;
        }




        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_video,null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

            final MyViewHolder vh = (MyViewHolder) holder;
            VideoItemBean.VideoBean mainVideoBean = mList.get(position);
            vh.playTextureView.setVideoSize(1, 1);
            Glide.with(mContext).load(mainVideoBean.getPost_icon()).into(vh.iv_avatar);
            vh.tv_content.setText(mainVideoBean.getVideo_title());
            vh.tv_name.setText(mainVideoBean.getPost_user());

            Glide.with(mContext).load(mainVideoBean.getVideo_thumb()).into(vh.iv_cover);

            vh.playTextureView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jumpNotCloseMediaPlay(position);
                    IntentUtil.gotoVideoDetailsContext(mContext, mList, position, vh.playTextureView);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mList != null && mList.size()>0){
                return mList.size();
            }else {
                return 0;
            }
        }



        public class MyViewHolder extends RecyclerView.ViewHolder {

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
}
