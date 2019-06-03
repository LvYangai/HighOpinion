package com.lvyangai.highopinion.ui.home.homefragment.home.second;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseLazyFragment;
import com.lvyangai.highopinion.bean.PageItemBean;
import com.lvyangai.highopinion.databinding.FragmentSecondBinding;
import com.lvyangai.highopinion.ui.web.WebActivity;
import com.lvyangai.highopinion.util.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * 用途：主要展示网页的电器类、生活类、等
 */
public class SecondFragment extends BaseLazyFragment implements SecondContract.secondView{
    private static int index = 0;
    private static final int ITEM_COUNT = 25; // 每次加载多少item
    private static final String TAG = "SecondFragment";
    private boolean isFirstData;
    private int pageId;
    private String pageTitle;
    private View view;
    private Context context;
    private List<PageItemBean.PageBean> dataList;
    private MySecondAdapter adapter;
    private SecondPresenter presenter;

    private FragmentSecondBinding binding;

    @SuppressLint("ValidFragment")
    public SecondFragment(int pageId, String pageTitle) {
        this.pageId = pageId;
        this.pageTitle = pageTitle;
    }

    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        Log.e(TAG, "initView: ");
        if (view == null){
            binding = DataBindingUtil.inflate(inflater,R.layout.fragment_second,container,false);
            view = binding.getRoot();
        }
        return view;
    }

    @Override
    protected void initData() {
        Log.e(TAG, "initData: " );
        context = getActivity();
    }

    @Override
    protected void dataProcess() {
        Log.e(TAG, "dataProcess: " );
        presenter = new SecondPresenter(this);
        dataList = new ArrayList<>();
        addDataList();
        adapter = new MySecondAdapter(R.layout.item_second);
        adapter.replaceData(dataList);
        binding.fragSecondRecycler.setAdapter(adapter);
        binding.fragSecondRecycler.setLayoutManager(new LinearLayoutManager(context));
        binding.fragSecondRefreshLayout.setEnableAutoLoadMore(true);
        binding.fragSecondRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                presenter.getDataList(pageId);
            }
        });
        binding.fragSecondRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
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
                },1000);
            }
        });
        binding.fragSecondRefreshLayout.autoRefresh();
        adapter.openLoadAnimation();
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
            }
        });
    }

    private void addDataList() {

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
        }
    }

    @Override
    protected void onUserInvisible() {
        if (adapter != null){
            if (binding.fragSecondRefreshLayout != null){
                binding.fragSecondRefreshLayout.finishRefresh();
                binding.fragSecondRefreshLayout.resetNoMoreData();
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


    @Override
    public void retrofitSuccess(Response<PageItemBean> response) {
        index = 0;
        dataList = response.body().getPage();
        if (dataList.size()>0){
            adapter.replaceData(getMoreData());
            binding.fragSecondRefreshLayout.finishRefresh();
            binding.fragSecondRefreshLayout.resetNoMoreData();
        }else {
            ToastUtil.showShortToast("暂无数据");
            binding.fragSecondRefreshLayout.finishRefresh();
            binding.fragSecondRefreshLayout.resetNoMoreData();
        }

    }

    @Override
    public void retrofitFailed(String log) {
        ToastUtil.showShortToast(log);
        binding.fragSecondRefreshLayout.finishRefresh();
        binding.fragSecondRefreshLayout.resetNoMoreData();
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

    private class MySecondAdapter extends BaseQuickAdapter<PageItemBean.PageBean,BaseViewHolder>{
        RequestOptions optionsImage,optionsIcon;
        public MySecondAdapter(int layoutResId) {
            super(layoutResId);
            optionsImage  = new RequestOptions()
                    .error(R.mipmap.loading_image_error)
                    .fitCenter();

            optionsIcon  = new RequestOptions()
                    .error(R.mipmap.icon_user_error)
                    .fitCenter();
        }

        @Override
        protected void convert(BaseViewHolder helper, PageItemBean.PageBean item) {
            helper.setText(R.id.item_second_user,""+item.getPost_user())
                    .setText(R.id.item_second_title,""+item.getPage_title())
                    .setText(R.id.item_second_date,""+item.getPage_date())
                    .setText(R.id.item_second_hot,""+(item.getPage_click()>999?"999+":""+item.getPage_click()));

            Glide.with(mContext).load(item.getPage_thumb()).apply(optionsImage).into((ImageView) helper.itemView.findViewById(R.id.item_second_image));
            Glide.with(mContext).load(item.getPost_icon()).apply(optionsIcon).into((ImageView) helper.itemView.findViewById(R.id.item_second_icon));
        }
    }
}
