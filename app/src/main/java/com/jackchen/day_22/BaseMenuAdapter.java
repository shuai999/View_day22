package com.jackchen.day_22;

import android.view.View;
import android.view.ViewGroup;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/3/8 9:10
 * Version 1.0
 * Params:
 * Description:  Adapter设计模式使用，将方法放到基类中，让子类继承然后实现方法即可
*/
public abstract class BaseMenuAdapter {

    // 获取总共有多少条 就是上边的筛选菜单
    public abstract int getCount() ;
    // 获取当前的TabView
    public abstract View getTabView(int position , ViewGroup parent) ;
    // 获取当前的菜单内容
    public abstract View getMenuView(int position , ViewGroup parent) ;


    /**
     * 菜单打开
     * @param tabView
     */
    public void menuOpen(View tabView){

    }


    /**
     * 菜单关闭
     * @param tabView
     */
    public void menuClose(View tabView){

    }

}
