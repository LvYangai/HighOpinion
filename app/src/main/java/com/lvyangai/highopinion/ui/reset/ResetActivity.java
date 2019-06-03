package com.lvyangai.highopinion.ui.reset;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.databinding.ActivityResetBinding;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.lvyangai.highopinion.util.ToastUtil;

public class ResetActivity extends BaseActivity implements ResetContract.resetView ,View.OnClickListener{
    private static final String TAG = "ResetActivity";
    private String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$(?!.*\\s)";//密码验证
    private ActivityResetBinding binding;
    private Context context;
    private ResetPresenter presenter;
    @Override
    protected void initView() {
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset);
    }

    @Override
    protected void initData() {
        presenter = new ResetPresenter(this);
        binding.resetBtn.setOnClickListener(this);

    }


    @Override
    protected void dataProcess() {
        binding.resetToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StatusBarUtil.setPaddingSmart(this,binding.resetToolbar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reset_btn:
                    if (submit()){
                        showProDialog();
                        String pass1 = binding.resetPassword.getText().toString();
                        presenter.reset(MyApplication.getForgetUser(),pass1);
                    }
                break;
        }
    }

    private boolean submit() {
        String pass1 = binding.resetPassword.getText().toString().trim();

        if (TextUtils.isEmpty(pass1)){
            ToastUtil.showShortToast("请输入新密码");
            return false;
        }
        String pass2 = binding.resetPassword2.getText().toString().trim();
        if (TextUtils.isEmpty(pass2)){
            ToastUtil.showShortToast("请再次输入新密码");
            return false;
        }
        if (!pass1.equals(pass2)){
            ToastUtil.showShortToast("两次密码输入不一致");
            return false;
        }
        if(!pass1.matches(regex)){
            ToastUtil.showShortToast("密码为6-16数字、字母组合");
            return false;
        }

        return true;
    }

    @Override
    public void inputIsNull(String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void resetSuccess(String msg) {
        dismissDialog();
        if ("密码重置成功".equals(msg)){
            ToastUtil.showShortToast(msg);
            finish();
        }
    }

    @Override
    public void resetFailed(String msg) {
        dismissDialog();
        ToastUtil.showShortToast(msg);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (context != null)context = null;
        if (presenter != null){
            presenter = null;
        }
        if (binding != null) binding = null;
    }
}
