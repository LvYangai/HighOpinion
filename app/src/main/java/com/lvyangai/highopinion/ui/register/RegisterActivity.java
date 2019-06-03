package com.lvyangai.highopinion.ui.register;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.databinding.ActivityRegisterBinding;
import com.lvyangai.highopinion.ui.login.LoginActivity;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.lvyangai.highopinion.util.ToastUtil;


public class RegisterActivity extends BaseActivity implements RegisterContract.registerView, View.OnClickListener {
    private final static String TAG = RegisterActivity.class.getSimpleName();
    private Context mContext;
    private ActivityRegisterBinding binding;
    private RegisterPresenter presenter;
    private String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$(?!.*\\s)";//密码验证
    private String regexUser = "^[0-9A-Za-z]{6,16}$(?!.*\\s)";//用户名验证
    private String regphone="^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\d]{8}$";
    @Override
    protected void initView() {
        mContext = RegisterActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
    }

    @Override
    protected void initData() {
        presenter = new RegisterPresenter(this);
        binding.registerBtnPhone.setOnClickListener(this);
    }

    @Override
    protected void dataProcess() {
        StatusBarUtil.setPaddingSmart(this,binding.registerToolbar);
        binding.registerToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_btn_phone:
                if (submit()){
                    showProDialog();
                    String user = binding.registerAccount.getText().toString();
                    String pass = binding.registerPass1.getText().toString();
                    String phone = binding.registerNumber.getText().toString();
                    presenter.register(user,pass,phone);
                }
            break;
        }
    }

    private boolean submit() {
        String user = binding.registerAccount.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            ToastUtil.showShortToast("请输入用户名");
            return false;
        }
        String pass = binding.registerPass1.getText().toString();
        String pass2 = binding.registerPass2.getText().toString();
        if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(pass2)) {
            ToastUtil.showShortToast("请输入密码");
            return false;
        }

        String phone = binding.registerNumber.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShortToast("请输入手机号");
            return false;
        }
        if (!pass.equals(pass2)) {
            ToastUtil.showShortToast("两次密码输入不一致");
            return false;
        }
        if(!user.matches(regexUser)){
            ToastUtil.showShortToast("用户名为6-16纯数字或字母");
            return false;
        }
        if(!pass.matches(regex)){
            ToastUtil.showShortToast("密码为6-16数字、字母组合");
            return false;
        }
        if(!phone.matches(regphone)){
            ToastUtil.showShortToast("手机号格式不正确");
            return false;
        }
        return true;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void resisterSuccess(String msg) {
        dismissDialog();
        ToastUtil.showShortToast(msg);
        if ("注册成功".equals(msg)){
            finish();
        }
    }

    @Override
    public void registerFailed(String msg) {
        dismissDialog();
        ToastUtil.showShortToast(msg);
    }
}
