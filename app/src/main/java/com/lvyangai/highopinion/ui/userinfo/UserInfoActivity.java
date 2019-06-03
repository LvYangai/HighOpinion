package com.lvyangai.highopinion.ui.userinfo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.databinding.ActivityUserInfoBinding;
import com.lvyangai.highopinion.util.StatusBarUtil;

public class UserInfoActivity extends BaseActivity {
    ActivityUserInfoBinding binding;
    private Context context;
    private RequestOptions optionsIcon;
    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_info);
    }

    @Override
    protected void initData() {
        context = this;
    }

    @Override
    protected void dataProcess() {
        binding.userInfoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StatusBarUtil.setPaddingSmart(context,binding.userInfoToolbar);
        optionsIcon  = new RequestOptions()
                .error(R.mipmap.icon_user_error)
                .fitCenter();
        Glide.with(context).load(MyApplication.getIconUrl()).apply(optionsIcon).into(binding.myInfoIcon);
        binding.editName.setText(""+MyApplication.getUsername());
        binding.sexIncon.setText(""+MyApplication.getPhone());
        if (MyApplication.getJur() == 7){
            binding.infoSelect.setText("用户");
        }else if (MyApplication.getJur() == 6){
            binding.infoSelect.setText("作者");
        }else {
            binding.infoSelect.setText("管理员");
        }
        binding.infoJieshao.setText(""+MyApplication.getTime());

    }
}
