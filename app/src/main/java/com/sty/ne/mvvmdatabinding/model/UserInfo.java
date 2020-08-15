package com.sty.ne.mvvmdatabinding.model;

import com.sty.ne.mvvmdatabinding.BR;

import java.util.Observable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

/**
 * @Author: tian
 * @UpdateDate: 2020-08-14 20:08
 */
//public class UserInfo extends BaseObservable {
public class UserInfo{
    //被观察的属性（切记：必须是public修饰符，因为是DataBinding的规范 xml中要使用 标识.成员属性）
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> pwd = new ObservableField<>();

//    private String name;
//    private String pwd;
//
//    @Bindable
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//        notifyPropertyChanged(BR.name);
//    }
//
//    @Bindable
//    public String getPwd() {
//        return pwd;
//    }
//
//    public void setPwd(String pwd) {
//        this.pwd = pwd;
//        notifyPropertyChanged(BR.pwd);
//    }
}
