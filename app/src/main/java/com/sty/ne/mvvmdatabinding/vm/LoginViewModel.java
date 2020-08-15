package com.sty.ne.mvvmdatabinding.vm;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.sty.ne.mvvmdatabinding.databinding.ActivityLoginBinding;
import com.sty.ne.mvvmdatabinding.model.UserInfo;

/**
 * @Author: tian
 * @UpdateDate: 2020-08-14 20:11
 */
public class LoginViewModel {
    private static final String TAG = LoginViewModel.class.getSimpleName();

    public UserInfo userInfo;

    public LoginViewModel(ActivityLoginBinding binding) {
        userInfo = new UserInfo();
        //将ViewModel和View进行绑定，通过DataBinding工具
        binding.setLoginViewModel(this);
    }

    public TextWatcher nameInputListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //View层接收到用户的输入，改变Model层的javabean属性
            userInfo.name.set(String.valueOf(s));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher pwdInputListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //View层接收到用户的输入，改变Model层的javabean属性
            userInfo.pwd.set(String.valueOf(s));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //模拟网络请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Model层属性的变更，改变View层的显示
//                    userInfo.name.set("哈哈哈");
                    SystemClock.sleep(2000);
                    if("sty".equals(userInfo.name.get()) && "123".equals(userInfo.pwd.get())) {
                        Log.e(TAG, "登录成功");
                    }else {
                        Log.e(TAG, "登录失败");
                    }
                }
            }).start();
        }
    };


}
