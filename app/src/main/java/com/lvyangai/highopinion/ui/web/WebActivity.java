package com.lvyangai.highopinion.ui.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.bean.ClickLikeBean;
import com.lvyangai.highopinion.bean.CommItemBean;
import com.lvyangai.highopinion.data.App;
import com.lvyangai.highopinion.databinding.ActivityWebBinding;
import com.lvyangai.highopinion.util.MyUtil;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.lvyangai.highopinion.util.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class WebActivity extends BaseActivity implements WebContract.webView{

    private static int index = 0;
    private static final int ITEM_COUNT = 25; // 每次加载多少item

    private ActivityWebBinding binding;
    private Context context;
    private static final String TAG = "WebActivity";
    private Intent intent;
    private String url;
    private String title;
    private int userId;
    private boolean isLike;
    private WebPresenter presenter;
    private int clickLike;
    private int pageId;
    private int appUserId = 0;
    private EditText commEdit;
    private CommQuickAdapter adapter;
    RefreshLayout refreshLayout;
    RecyclerView recyclerView;
    private List<CommItemBean.CommBean> dataList;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        context = this;
        binding = DataBindingUtil.setContentView(this,R.layout.activity_web);
        StatusBarUtil.setPaddingSmart(this,binding.webToolbar);
        intent = getIntent();
        url = App.APP_PAGE_URL+intent.getExtras().getString("intent_web_url","");
        title = intent.getExtras().getString("intent_web_title","");
        userId = intent.getExtras().getInt("intent_web_userid",0);
        isLike = intent.getExtras().getBoolean("intent_web_isLike",false);
        clickLike = intent.getExtras().getInt("intent_web_clickLike",0);
        pageId = intent.getExtras().getInt("intent_web_pageId",0);
        appUserId = MyApplication.getUserid();
        binding.webToolbar.setTitle(""+title);
        binding.webClickNumber.setText(""+clickLike);
    }

    @Override
    protected void initData() {
        presenter = new WebPresenter(this);
        binding.webToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.webView.loadUrl(url);
        binding.webView.getSettings().setJavaScriptEnabled(true);

    }

    @Override
    protected void dataProcess() {
        binding.webView.setWebViewClient(new WebViewClient(){

            @Override
            @SuppressWarnings("deprecation")
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.webLoading.showContent();
            }
        });
        binding.webRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                binding.webLoading.showLoading();
                binding.webView.loadUrl(url);
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.webLoading.showContent();
                        refreshLayout.finishRefresh();
                        refreshLayout.resetNoMoreData();
                    }
                },2000);
            }
        });
        presenter.getInitLike(pageId,appUserId );
        binding.webBtnFavour.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                presenter.setPageLike("text",appUserId,pageId,1);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                presenter.setPageLike("text",appUserId,pageId,0);
            }
        });
        binding.webBtnComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });
    }

    private void showBottomDialog() {
        dataList = new ArrayList<>();
        BottomSheetDialog dialog=new BottomSheetDialog(this);
        View dialogView = View.inflate(context, R.layout.comm_layout, null);
        adapter = new CommQuickAdapter(R.layout.item_comm);
        refreshLayout = dialogView.findViewById(R.id.comm_refreshLayout);
        recyclerView = dialogView.findViewById(R.id.comm_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        adapter.replaceData(dataList);
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
        ImageView send = dialogView.findViewById(R.id.comm_send);
        commEdit = dialogView.findViewById(R.id.comm_edit);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendStr = commEdit.getText().toString();
                if (TextUtils.isEmpty(sendStr.trim())){
                    ToastUtil.showShortToast("还没有填写评论内容哦");
                }else {
                    showProDialog("正在评论");
                    presenter.upComm("text",userId,pageId,sendStr);

                }
            }
        });
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(MyUtil.getWindowHeight()*3/5));
        dialog.setContentView(dialogView,layoutParams);

        dialog.show();
    }

    private List<CommItemBean.CommBean> getMoreData() {
        List<CommItemBean.CommBean> moreData = new ArrayList<>();
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
    public void initIsLike(boolean flag) {
        binding.webBtnFavour.setLiked(flag);
    }

    @Override
    public void retrofitLikeSuccess(String msg) {
        ToastUtil.showShortToast(msg);
    }

    @Override
    public void retrofitLikeFailed(String log) {
        ToastUtil.showShortToast(log);
    }


    @Override
    public void retrofitDataSuccess(Response<CommItemBean> response) {
        if (response != null){
            index = 0;
            dataList = response.body().getComm();
            adapter.replaceData(getMoreData());
            refreshLayout.finishRefresh();
            refreshLayout.resetNoMoreData();
        }else {
            refreshLayout.finishRefresh();
            refreshLayout.resetNoMoreData();
        }
    }

    @Override
    public void retrofitDataFailed(String msg) {
        ToastUtil.showShortToast(msg);
        refreshLayout.finishRefresh();
        refreshLayout.resetNoMoreData();
        refreshLayout.autoRefresh();
    }

    @Override
    public void retrofitComm(String msg) {
        ToastUtil.showShortToast(msg);
        dismissDialog();
        refreshLayout.autoRefresh();
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
