package com.jackchen.day_22;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/3/8 14:06
 * Version 1.0
 * Params:
 * Description:  子类的Adapter  -  继承基类BaseMenuAdapter  来给顶部的Tab、内容区域填充数据
*/
public class ListScreenMenuAdapter extends BaseMenuAdapter {


    private Context mContext ;

    public ListScreenMenuAdapter(Context context){
        this.mContext = context ;
    }

    private String[] mItems = {"类型","品牌","价格","更多"} ;



    @Override
    public int getCount() {
        return mItems.length;
    }


    /**
     * 获取顶部菜单标签
     * @param position
     * @param parent
     * @return
     */
    @Override
    public View getTabView(int position, ViewGroup parent) {
        // 因为布局中只有一个TextView控件，所以这里可以直接强转成 TextView
        TextView tabView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.ui_list_data_screen_tab, parent, false);
        tabView.setText(mItems[position]);
        tabView.setTextColor(Color.BLACK);
        return tabView;
    }


    /**
     * 获取菜单对应的内容
     * @param position
     * @param parent
     * @return
     */
    @Override
    public View getMenuView(int position, ViewGroup parent) {
        TextView menuView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.ui_list_data_screen_menu , parent , false);
        menuView.setText(mItems[position]);
        return menuView;
    }


    /**
     * 关闭菜单时文字颜色设置为黑色
     * @param tabView
     */
    @Override
    public void menuClose(View tabView) {
        TextView tabTv = (TextView) tabView;
        tabTv.setTextColor(Color.BLACK);
    }


    /**
     * 打开菜单时文字颜色设置为红色
     * @param tabView
     */
    @Override
    public void menuOpen(View tabView) {
        TextView tabTv = (TextView) tabView;
        tabTv.setTextColor(Color.RED);
    }
}
