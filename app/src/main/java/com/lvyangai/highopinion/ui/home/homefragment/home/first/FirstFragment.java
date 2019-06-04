package com.lvyangai.highopinion.ui.home.homefragment.home.first;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseLazyFragment;
import com.lvyangai.highopinion.bean.PageItemBean;
import com.lvyangai.highopinion.databinding.FragmentFirstBinding;
import com.lvyangai.highopinion.ui.web.WebActivity;
import com.lvyangai.highopinion.util.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends BaseLazyFragment implements FirstContract.fitstView{

    private static int index = 0;
    private static final int ITEM_COUNT = 15; // 每次加载多少item
    private MZBannerView mMZBannerView;
    private FragmentFirstBinding binding;
    private static final String TAG = "FirstFragment";
    private Context context;
    private View view;
    private boolean isFirstData;
    private List<PageItemBean.PageTopBean> itemList;
    View header;
    private List<PageItemBean.PageBean> dataList;
    private FirstAdapter adapter;
    private FirstPresenter presenter;
    private ArrayList titles;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        Log.e(TAG, "initView: ");
        if (view == null){
            binding = DataBindingUtil.inflate(inflater,R.layout.fragment_first,container,false);
            view = binding.getRoot();
        }
        return view;
    }

    @Override
    protected void initData() {
        Log.e(TAG, "initData: " );
        context = getActivity();
        presenter = new FirstPresenter(this);
        titles = new ArrayList();
    }

    @Override
    protected void dataProcess() {
        Log.e(TAG, "dataProcess: " );
        dataList = new ArrayList<>();
        itemList = new ArrayList<>();

        addDataList();
    }
    public void addDataList() {
        //预加载数据，其实我是想加个缓存的，但是时间来不及了，原本想的是判断是否有缓存，有就用缓存没有就加载新数据
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated: " );
        adapter = new FirstAdapter(R.layout.item_first);
        adapter.replaceData(dataList);
        binding.fragFirstRecycler.setLayoutManager(new LinearLayoutManager(context));
        binding.fragFirstRecycler.setAdapter(adapter);
        binding.fragFirstRefreshLayout.setEnableAutoLoadMore(true);
        binding.fragFirstRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                presenter.getDataList(0);
            }
        });
        binding.fragFirstRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getItemCount() >= dataList.size()) {
                            Toast.makeText(context, "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        } else {
                            adapter.addData(getMoreData());
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 1000);
            }
        });

        //触发自动刷新
        binding.fragFirstRefreshLayout.autoRefresh();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("intent_web_url", dataList.get(position).getPage_url());
                intent.putExtra("intent_web_title", dataList.get(position).getPage_title());
                intent.putExtra("intent_web_userid", dataList.get(position).getPage_author());
                intent.putExtra("intent_web_isLike", dataList.get(position).getPage_isLike());
                intent.putExtra("intent_web_clickLike", dataList.get(position).getPage_click());
                intent.putExtra("intent_web_pageId", dataList.get(position).getId());
                startActivity(intent);
//                IntentUtil.gotoVideoDetailsContext(context, mList, position, vh.playTextureView);
            }
        });

        header = LayoutInflater.from(context).inflate(R.layout.item_first_header,binding.fragFirstRecycler,false);
        mMZBannerView = header.findViewById(R.id.convenientBanner);
        mMZBannerView.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("intent_web_url", itemList.get(position).getTop_url());
                intent.putExtra("intent_web_title", itemList.get(position).getTop_title());
                intent.putExtra("intent_web_userid", MyApplication.getUserid());
                intent.putExtra("intent_web_isLike", false);
                intent.putExtra("intent_web_clickLike", itemList.get(position).getTop_click());
                intent.putExtra("intent_web_pageId", itemList.get(position).getTop_pageId());
                startActivity(intent);
            }
        });
        mMZBannerView.setPages(itemList, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        mMZBannerView.start();
        adapter.openLoadAnimation();

    }

    @Override
    protected void destroy() {

    }

    @Override
    protected void onFirstUserVisible() {
        if (getIsData()){
            dataProcess();
        }
    }

    @Override
    public void onUserVisible() {
        if (getIsData()){
            dataProcess();
        }else {
            mMZBannerView.start();
        }
    }

    @Override
    protected void onUserInvisible() {
        if (mMZBannerView != null){
            mMZBannerView.pause();
        }
        if (adapter != null && mMZBannerView != null){
           if (binding.fragFirstRefreshLayout != null){
               binding.fragFirstRefreshLayout.finishRefresh();
               binding.fragFirstRefreshLayout.resetNoMoreData();
           }
        }
    }

    private boolean getIsData(){
        if (!isFirstData){
            isFirstData = true;
            return true;
        }else {
            return false;
        }
    }

    public List<PageItemBean.PageBean> getMoreData() {
        List<PageItemBean.PageBean> moreData = new ArrayList<>();
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
    public void retrofitSuccess(Response<PageItemBean> response) {
        adapter.removeHeaderView(header);
        index = 0;
        if (titles != null){
            titles.clear();
        }
        dataList = response.body().getPage();
        itemList = response.body().getPage_top();
        mMZBannerView.setPages(itemList, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        mMZBannerView.start();
        adapter.addHeaderView(header);
        adapter.replaceData(getMoreData());
        binding.fragFirstRefreshLayout.finishRefresh();
        binding.fragFirstRefreshLayout.resetNoMoreData();
    }

    @Override
    public void retrofitFailed(String tag) {
        Log.e(TAG, "retrofitFailed: "+tag );
    }


    private class FirstAdapter extends BaseQuickAdapter<PageItemBean.PageBean,BaseViewHolder> {
        public FirstAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, PageItemBean.PageBean item) {
            helper.setText(R.id.item_first_title,""+item.getPage_title())
                    .setText(R.id.item_first_date,""+item.getPage_date())
                    .setText(R.id.item_first_user,""+item.getPost_user())
                    .setText(R.id.item_first_hot,""+(item.getPage_click()>999?"999+":""+item.getPage_click()));
            Glide.with(mContext).load(item.getPage_thumb()).into((ImageView) helper.itemView.findViewById(R.id.item_first_image));
        }

    }
    public static class BannerViewHolder implements MZViewHolder<PageItemBean.PageTopBean> {
        private ImageView mImageView;
        private TextView mTextView;
        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.remote_banner_item,null);
            mImageView = (ImageView) view.findViewById(R.id.remote_item_image);
            mTextView = view.findViewById(R.id.remote_item_text);
            return view;
        }

        @Override
        public void onBind(Context context, int i, PageItemBean.PageTopBean pageTopBean) {
            mTextView.setText(""+pageTopBean.getTop_title());
            Glide.with(context).load(pageTopBean.top_image).into(mImageView);
        }
    }
}
