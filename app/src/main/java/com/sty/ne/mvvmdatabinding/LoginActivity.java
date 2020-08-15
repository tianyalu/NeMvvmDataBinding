package com.sty.ne.mvvmdatabinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.sty.ne.mvvmdatabinding.databinding.ActivityLoginBinding;
import com.sty.ne.mvvmdatabinding.model.UserInfo;
import com.sty.ne.mvvmdatabinding.vm.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private LoginViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1、必须先rebuilder
        //2、书写绑定
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mViewModel = new LoginViewModel(binding);

        //单向绑定第一种方式
        mViewModel.userInfo.name.set("天涯路");
        mViewModel.userInfo.pwd.set("123");
        //刷新数据？无效果
        Log.e(TAG, mViewModel.userInfo.name.get() + " / " + mViewModel.userInfo.pwd.get());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewModel.userInfo.name.set("哈哈哈");
                mViewModel.userInfo.pwd.set("456");
                Log.e(TAG, mViewModel.userInfo.name.get() + " / " + mViewModel.userInfo.pwd.get());
            }
        }, 3000);
    }
}
