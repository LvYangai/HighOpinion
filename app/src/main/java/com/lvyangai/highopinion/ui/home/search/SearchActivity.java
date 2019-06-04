package com.lvyangai.highopinion.ui.home.search;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.activity.VideoDetailsActivity;
import com.lvyangai.highopinion.activity.VideoSearchActivity;
import com.lvyangai.highopinion.adapter.VideoDetailsAdapter;
import com.lvyangai.highopinion.bean.PageItemBean;
import com.lvyangai.highopinion.bean.VideoItemBean;
import com.lvyangai.highopinion.databinding.ActivitySearchBinding;
import com.lvyangai.highopinion.ui.home.homeactivity.HomeActivity;
import com.lvyangai.highopinion.ui.home.homefragment.home.second.SecondPresenter;
import com.lvyangai.highopinion.ui.login.LoginActivity;
import com.lvyangai.highopinion.ui.web.WebActivity;
import com.lvyangai.highopinion.util.IntentUtil;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.lvyangai.highopinion.util.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class SearchActivity extends BaseActivity implements SearchContract.searchView {

    private ActivitySearchBinding binding;
    private Context context;
    private static final String TAG = "SearchActivity";
    private Intent intent;
    private int appUserId = 0;
    private String keywords;
    private SearchDataAdapter adapter;
    private SearchPresenter presenter;
    private List<PageItemBean.PageBean> dataList;
    private static int index = 0;
    private static final int ITEM_COUNT = 15; // 每次加载多少item
    private View header;
    private TextView header_textView;

    @Override
    protected void initView() {
        context = this;
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search);
    }

    @Override
    protected void initData() {
        binding.searchToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StatusBarUtil.setPaddingSmart(this,binding.searchToolbar);
        intent = getIntent();
        keywords = intent.getExtras().getString("INTENT_KEY_WORD","");
//        keywords = "一";
    }

    @Override
    protected void dataProcess() {
        appUserId = MyApplication.getUserid();
        presenter = new SearchPresenter(this);
        dataList = new ArrayList<>();
        adapter = new SearchDataAdapter(R.layout.item_search);
        adapter.replaceData(dataList);
        binding.searchDetail.setLayoutManager(new LinearLayoutManager(context));
        binding.searchDetail.setAdapter(adapter);
        binding.searchRefreshLayout.setEnableAutoLoadMore(true);
        binding.searchRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                presenter.getDataList(keywords, appUserId);
            }
        });
        binding.searchRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
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
        binding.searchRefreshLayout.autoRefresh();
        adapter.openLoadAnimation();
        header = LayoutInflater.from(context).inflate(R.layout.layout_header,binding.searchDetail,false);
        header_textView = header.findViewById(R.id.header_date);
        header_textView.setVisibility(View.GONE);
        adapter.addHeaderView(header);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if ("文章".equals(dataList.get(position).getPage_type())){
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("intent_web_url", dataList.get(position).getPage_url());
                    intent.putExtra("intent_web_title", dataList.get(position).getPage_title());
                    intent.putExtra("intent_web_userid", dataList.get(position).getPage_author());
                    intent.putExtra("intent_web_isLike", dataList.get(position).getPage_isLike());
                    intent.putExtra("intent_web_clickLike", dataList.get(position).getPage_click());
                    intent.putExtra("intent_web_pageId", dataList.get(position).getId());
                    startActivity(intent);
                }else {
                    List<VideoItemBean.VideoBean> mList = new ArrayList<>();
                    mList.add(new VideoItemBean.VideoBean(dataList.get(position).getId(),dataList.get(position).getPage_isLike()
                            ,dataList.get(position).getPost_user(),dataList.get(position).getPost_icon(),dataList.get(position).getPage_title()
                            ,dataList.get(position).getPage_url(),dataList.get(position).getPage_thumb(),dataList.get(position).getPage_date()
                            ,dataList.get(position).getPage_like(),dataList.get(position).getPage_comment(),dataList.get(position).getId()));
                    Intent intentRegister = new Intent();
                    intentRegister.setClass(SearchActivity.this, VideoSearchActivity.class);
                    intentRegister.putExtra("intent_data_list", (Serializable) mList);
                    intentRegister.putExtra("intent_play_position", 0);
                    startActivity(intentRegister);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }
            }
        });
    }
    @Override
    public void retrofitSuccess(Response<PageItemBean> response) {
        index = 0;
        dataList = response.body().getPage();
        if (dataList.size()>0){
            adapter.replaceData(getMoreData());
            header_textView.setVisibility(View.GONE);
        }else {
            header_textView.setVisibility(View.VISIBLE);
        }
        binding.searchRefreshLayout.finishRefresh();
        binding.searchRefreshLayout.resetNoMoreData();
    }

    @Override
    public void retrofitFailed(String log) {
        ToastUtil.showShortToast(log);
        header_textView.setVisibility(View.VISIBLE);
        binding.searchRefreshLayout.finishRefresh();
        binding.searchRefreshLayout.resetNoMoreData();
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

    private class SearchDataAdapter extends BaseQuickAdapter<PageItemBean.PageBean,BaseViewHolder> {
        public SearchDataAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, PageItemBean.PageBean item) {
            helper.setText(R.id.item_search_title,""+item.getPage_title())
                    .setText(R.id.item_search_lable,""+item.getPage_type())
                    .setText(R.id.item_search_username,""+item.getPost_user())
                    .setText(R.id.item_search_like,""+item.getPage_like()+" 收藏");
        }
    }
}
