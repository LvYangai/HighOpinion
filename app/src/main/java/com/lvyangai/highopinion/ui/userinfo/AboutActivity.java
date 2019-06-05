package com.lvyangai.highopinion.ui.userinfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.databinding.ActivityAboutBinding;
import com.lvyangai.highopinion.util.StatusBarUtil;

public class AboutActivity extends BaseActivity {

    private ActivityAboutBinding binding;
    private Context context;
    private String content = "\t\t\t\t\t\t\t\t此款App为本人2019.6毕业设计\n\t\t\t\t首先感谢下载此App，此App功能相对完整，" +
            "但在使用过程中仍会存在不可预测性问题，希望能够给予指证。\n" +
            "\t\t\t\tApp为基于Android的网上评测系统的开发与实现";
    private String localVersionName = "";
    private int localVersionCode = 1;

    @Override
    protected void initView() {
        context = this;
        binding = DataBindingUtil.setContentView(this,R.layout.activity_about);
        StatusBarUtil.setPaddingSmart(this,binding.aboutToolbar);
        binding.aboutToolbar.setTitle("关于软件");
    }

    @Override
    protected void initData() {
        binding.aboutToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.aboutContent.setText(""+content);
        getAPPLocalVersion(context);
    }

    @Override
    protected void dataProcess() {
        binding.aboutAppEdition.setText("v"+localVersionName);
    }
    //获取本地版本号
    private  void getAPPLocalVersion(Context ctx) {
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            localVersionName = info.versionName; // 版本名
            localVersionCode = info.versionCode; // 版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
