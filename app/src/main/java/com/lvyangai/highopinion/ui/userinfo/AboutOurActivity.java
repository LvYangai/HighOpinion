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

public class AboutOurActivity extends BaseActivity {

    private ActivityAboutBinding binding;
    private Context context;
    private String content = "\t\t\t\t本文设计的“爱share”APP，可以向用户提供全面的商品评测信息，" +
            "很好地满足了用户在生活购物中对商品真实信息的获取。该系统采用MySQL进行后台数据库设计，" +
            "用Android Studio集成开发环境完成前端App开发，用phpStorm实现系统后台管理以及后台接口数据的添加、修改、删除等。" +
            "本系统从用户的需求出发，以实用、快捷、互动性强为设计原则，实现了用户上传测评文章，浏览文章、播放视频，添加收藏，编辑个人信息等功能。" +
            " App前端采用MVP设计模式进行开发，后台使用ThinkPHP框架提高系统开发效率。";
    private String localVersionName = "111";
    private int localVersionCode = 1;

    @Override
    protected void initView() {
        context = this;
        binding = DataBindingUtil.setContentView(this,R.layout.activity_about);
        StatusBarUtil.setPaddingSmart(this,binding.aboutToolbar);
        binding.aboutToolbar.setTitle("关于作者");
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
