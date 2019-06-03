package com.lvyangai.highopinion.ui.home.homeactivity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.bean.UserLoginBean;
import com.lvyangai.highopinion.databinding.ActivityHomeBinding;
import com.lvyangai.highopinion.ui.base.BottomNavigationViewHelper;
import com.lvyangai.highopinion.ui.home.homefragment.home.HomeFragment;
import com.lvyangai.highopinion.ui.home.homefragment.like.LikeFragment;
import com.lvyangai.highopinion.ui.home.homefragment.user.UserFragment;
import com.lvyangai.highopinion.ui.home.homefragment.video.VideoFragment;
import com.lvyangai.highopinion.util.SPUtils;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.lvyangai.highopinion.util.ToastUtil;

import java.util.Map;

import retrofit2.Response;

public class HomeActivity extends BaseActivity implements HomeContract.homeView, View.OnClickListener {
    private final static String TAG = HomeActivity.class.getSimpleName();
    private ActivityHomeBinding binding;
    private HomePresenter pagePresenter;
    private Context context;
    private HomeFragment homeFragment;
    private VideoFragment videoFragment;
    private LikeFragment likeFragment;
    private UserFragment userFragment;
    private Fragment mCurFragment = new Fragment();

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.homePage:
                    switchPage(1);
                    return true;
                case R.id.videoPage:
                    switchPage(2);
                    return true;
                case R.id.empty:
                    showMenu();
                    return false;
                case R.id.favorPage:
                    switchPage(3);
                    return true;
                case R.id.userPage:
                    switchPage(4);
                    return true;

            }
            return false;
        }
    };

    private void switchPage(int index) {
        switch (index){
            case 1:
                StatusBarUtil.immersive(mContext, Color.parseColor("#455a64"),1f);
                switchFragment(homeFragment,index);
                break;
            case 2:
                StatusBarUtil.immersive(mContext, Color.BLACK,0.7f);
                switchFragment(videoFragment,index);
                break;
            case 3:
                StatusBarUtil.immersive(mContext, Color.BLACK,0.8f);
                switchFragment(likeFragment,index);
                break;
            case 4:
                StatusBarUtil.immersive(mContext, Color.parseColor("#3F3C51"),1f);
                switchFragment(userFragment,index);
                break;

        }
    }
    @Override
    protected void initView() {
        context = this;
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        StatusBarUtil.setPaddingSmart(context,binding.homeContent);
        homeFragment = new HomeFragment();
        videoFragment = new VideoFragment();
        likeFragment = new LikeFragment();
        userFragment = new UserFragment();

    }

    private void settingCache() {

        int userId = SPUtils.get("userid",0);
        String user = SPUtils.get("user","");
        String username = SPUtils.get("username","");
        String password = SPUtils.get("password","");
        String iconUrl = SPUtils.get("iconUrl","");
        boolean status = SPUtils.get("status",false);
        int userJur = SPUtils.get("userjur",7);
        String time = SPUtils.get("createTime","");
        String phone = SPUtils.get("phone","");

        if (status){
            MyApplication.setUserid(userId);
            MyApplication.setUser(user);
            MyApplication.setUsername(username);
            MyApplication.setPassword(password);
            MyApplication.setIconUrl(iconUrl);
            MyApplication.setStatus(status);
            MyApplication.setJur(userJur);
            MyApplication.setTime(time);
            MyApplication.setPhone(phone);
        }

    }

    @Override
    protected void initData() {
        pagePresenter = new HomePresenter(this);
        StatusBarUtil.immersive(mContext, Color.parseColor("#455a64"),1f);
        binding.homepageBottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(binding.homepageBottomNavigation);
        switchFragment(homeFragment,1);

    }
    @Override
    protected void dataProcess() {
        settingCache();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (7 == MyApplication.getJur()){
                    ToastUtil.showShortToast("你还没有发布的权限，赶快联系管理员吧！");
                }else {
                    //登陆
                    showMenu();
                }
            }
        });
    }

    private void showMenu() {
        pagePresenter.showMenu(this);
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void accountIsNull() {

    }

    @Override
    public void homeSuccess() {

    }

    @Override
    public void homeFailed() {

    }

    @Override
    public void autoLogin(Response<UserLoginBean> response) {

    }

    @Override
    public void autoLoginFailed(String msg) {
        ToastUtil.showShortToast(msg);
        SPUtils.clear(context);
    }


    public void switchFragment(Fragment targetFragment,int pageIndex){
        MyApplication.setNowPageIndex(pageIndex);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {//如果要显示的targetFragment没有添加过
            transaction
                    .hide(mCurFragment)//隐藏当前Fragment
                    .add(R.id.home_content, targetFragment,targetFragment.getClass().getName())//添加targetFragment
                    .commit();
        } else {//如果要显示的targetFragment已经添加过
            transaction//隐藏当前Fragment
                    .hide(mCurFragment)
                    .show(targetFragment)//显示targetFragment
                    .commit();
        }
        //更新当前Fragment为targetFragment
        mCurFragment = targetFragment;
    }
}
