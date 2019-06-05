package com.lvyangai.highopinion.ui.userinfo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.databinding.ActivityHoldBinding;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class HoldActivity extends BaseActivity {

    private ActivityHoldBinding binding;
    private Context context;
    private static final String TAG = "HoldActivity";
    private static final String url = "https://github.com/LvYangai/HighOpinion";

    @Override
    protected void initView() {
        context = this;
        binding = DataBindingUtil.setContentView(this,R.layout.activity_hold);
        StatusBarUtil.setPaddingSmart(this,binding.webToolbar);
        binding.webToolbar.setTitle("支持作者");
    }

    @Override
    protected void initData() {
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
    }
}
