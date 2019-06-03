package com.lvyangai.highopinion.ui.home.homefragment.home.third;


import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseLazyFragment;
import com.lvyangai.highopinion.bean.PageItemBean;
import com.lvyangai.highopinion.bean.ThirdItemBean;
import com.lvyangai.highopinion.databinding.FragmentThirdBinding;
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
 * 用途：主要用于展示电影、书籍、宠物。
 */
public class ThirdFragment extends BaseLazyFragment implements ThirdContract.thirdView{
    private static int index = 0;
    private static final int ITEM_COUNT = 20; // 每次加载多少item

    private static final String TAG = "ThirdFragment";
    private boolean isFirstData;
    private FragmentThirdBinding binding;
    private View view;
    private int pageId;
    private String pageTitle;
    private ThirdPresenter presenter;
    private Context context;
    private List<PageItemBean.PageBean> dataList;
    private MyThirdAdapter adapter;


    @SuppressLint("ValidFragment")
    public ThirdFragment(int pageId, String pageTitle) {
        this.pageId = pageId;
        this.pageTitle = pageTitle;
    }

    public ThirdFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        if (view == null){
            binding = DataBindingUtil.inflate(inflater,R.layout.fragment_third,container,false);
            view = binding.getRoot();
        }
        return view;
    }

    @Override
    protected void initData() {
        context = getActivity();
    }

    @Override
    protected void dataProcess() {
        presenter = new ThirdPresenter(this);
        dataList = new ArrayList<>();
        addDataList();
        adapter = new MyThirdAdapter(R.layout.item_third);
        adapter.replaceData(dataList);
        binding.fragThirdRecycler.setAdapter(adapter);
        binding.fragThirdRecycler.setLayoutManager(new LinearLayoutManager(context));
        binding.fragThirdRefreshLayout.setEnableAutoLoadMore(true);
        binding.fragThirdRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                presenter.getDataList(pageId);
            }
        });
        binding.fragThirdRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
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
                },2000);
            }
        });
        binding.fragThirdRefreshLayout.autoRefresh();
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
        }else {

        }
    }

    @Override
    protected void onUserInvisible() {
        if (adapter != null){
            if (binding.fragThirdRefreshLayout != null){
                binding.fragThirdRefreshLayout.finishRefresh();
                binding.fragThirdRefreshLayout.resetNoMoreData();
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
            binding.fragThirdRefreshLayout.finishRefresh();
            binding.fragThirdRefreshLayout.resetNoMoreData();
        }else {
            ToastUtil.showShortToast("暂无数据");
            binding.fragThirdRefreshLayout.finishRefresh();
            binding.fragThirdRefreshLayout.resetNoMoreData();
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
    public void retrofitFailed(String log) {
        ToastUtil.showShortToast(log);
        binding.fragThirdRefreshLayout.finishRefresh();
        binding.fragThirdRefreshLayout.resetNoMoreData();
    }

    private class MyThirdAdapter extends BaseQuickAdapter<PageItemBean.PageBean,BaseViewHolder>{
        public MyThirdAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, PageItemBean.PageBean item) {
            helper.setText(R.id.item_third_user,""+item.getPost_user())
                    .setText(R.id.item_third_date,""+item.getPage_date())
                    .setText(R.id.item_third_hot,""+(item.getPage_click()>999?"999+":""+item.getPage_click()))
                    .setText(R.id.item_third_title,""+item.getPage_title());
            Glide.with(mContext).load(item.getPage_thumb()).into((ImageView) helper.itemView.findViewById(R.id.item_third_image));
        }
    }
}
