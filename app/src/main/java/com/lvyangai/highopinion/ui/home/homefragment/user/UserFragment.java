package com.lvyangai.highopinion.ui.home.homefragment.user;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseFragment;
import com.lvyangai.highopinion.bean.UserItemBean;
import com.lvyangai.highopinion.databinding.FragmentUserBinding;
import com.lvyangai.highopinion.ui.adapter.BaseRecyclerAdapter;
import com.lvyangai.highopinion.ui.adapter.SmartViewHolder;
import com.lvyangai.highopinion.ui.base.AppBarStateChangeListener;
import com.lvyangai.highopinion.ui.home.homeactivity.HomeActivity;
import com.lvyangai.highopinion.ui.login.LoginActivity;
import com.lvyangai.highopinion.ui.register.RegisterActivity;
import com.lvyangai.highopinion.ui.reset.ResetActivity;
import com.lvyangai.highopinion.ui.userinfo.PhoneActivity;
import com.lvyangai.highopinion.ui.userinfo.UserInfoActivity;
import com.lvyangai.highopinion.ui.userinfo.UserResetActivity;
import com.lvyangai.highopinion.util.SPUtils;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.lvyangai.highopinion.util.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends BaseFragment {

    private FragmentUserBinding binding;
    private Context context;
    private static final String TAG = "UserFragment";
    private View view;
    private List<UserItemBean> dataList;
    private RequestOptions optionsIcon;
    private BaseRecyclerAdapter<UserItemBean> adapter;
    Intent intent;
    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        if (view == null){
            binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user,container,false);
            view = binding.getRoot();
        }
        return view;
    }

    @Override
    protected void initData() {
        context = getActivity();
        dataList = getDataList();
        optionsIcon  = new RequestOptions()
                .error(R.mipmap.icon_user_error)
                .fitCenter();
//        StatusBarUtil.setPaddingSmart(context,binding.coordinator);
    }

    @Override
    protected void dataProcess() {
        binding.fragUserToolbar.setTitle("");
        binding.fragUserAppbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {
                    //展开状态
                    binding.fragUserToolbar.setTitle("");
                    binding.fragUserToolbar.setVisibility(View.INVISIBLE);

                }else if(state == State.COLLAPSED){
                    Log.e(TAG,"折叠");
                    //折叠状态
                    binding.fragUserToolbar.setVisibility(View.VISIBLE);

                }else {
                    Log.e(TAG,"中间");
                    //中间状态
                    binding.fragUserToolbar.setVisibility(View.VISIBLE);
                }
            }
        });
        initRecycleView();
        binding.fragUserExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle("退出当前账户")
                        .setMessage("是否退出账户")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                userExit();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        }).create();
                alertDialog.show();
            }
        });
    }

    private void userExit() {
        SPUtils.clear(context);
        Intent intentRegister = new Intent();
        intentRegister.setClass(context, LoginActivity.class);
        startActivity(intentRegister);
        getActivity().finish();
    }

    private void initRecycleView() {
        adapter = new BaseRecyclerAdapter<UserItemBean>(dataList,R.layout.item_user) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, UserItemBean model, int position) {
                holder.text(R.id.item_user_name,""+model.getItemName());
                holder.image(R.id.item_user_icon,model.getIcon());
            }
        };
        binding.fragUserRvMine.setItemAnimator(new DefaultItemAnimator());
        binding.fragUserRvMine.setLayoutManager(new LinearLayoutManager(context));
        binding.fragUserRvMine.setAdapter(adapter);
        if (MyApplication.getStatus()){
            binding.fragUserName.setText(""+MyApplication.getUsername());
            Glide.with(context).load(MyApplication.getIconUrl()).into(binding.fragUserHeadView);
        }
        intent = new Intent();
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        intent.setClass(context, UserInfoActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(context, UserResetActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(context, PhoneActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
        }else {
            if (MyApplication.getStatus()){
                binding.fragUserName.setText(""+MyApplication.getUsername());
                Glide.with(context).load(MyApplication.getIconUrl()).apply(optionsIcon).into(binding.fragUserHeadView);
            }
        }
    }

    @Override
    protected void destroy() {

    }


    @Override
    public void onClick(View v) {

    }

    public List<UserItemBean> getDataList() {
        dataList = new ArrayList<>();
        dataList.add(new UserItemBean("我的信息",R.mipmap.user_mess));
        dataList.add(new UserItemBean("修改密码",R.mipmap.reset_pass));
        dataList.add(new UserItemBean("修改手机号",R.mipmap.user_phone_icon));
        dataList.add(new UserItemBean("关于软件",R.mipmap.user_about));
        dataList.add(new UserItemBean("关于我们",R.mipmap.user_about_our));
        dataList.add(new UserItemBean("支持作者",R.mipmap.user_zhichi));
        return dataList;
    }
}
