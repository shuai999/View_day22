package com.jackchen.day_22;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/3/8 8:32
 * Version 1.0
 * Params:
 * Description:  多条目筛选的自定义View  这里直接使用在代码中创建Tab、内容部分和阴影，而没有采用写xml文件
*/
public class ListDataScrrenView extends LinearLayout implements View.OnClickListener {


    private Context mContext ;
    // 创建LinearLayout作为头部，用来存放Tab
    private LinearLayout mMenuTabView;
    // 创建 FrameLayout 用来存放 = 阴影（View）+ 菜单内容布局（FrameLayout）
    private FrameLayout mMenuMiddleView;
    // 创建阴影 可以直接创建一个View就行 不用设置LayoutParams 默认就是MATCH_PARENT MATCH_PARENT
    private View mShadowView;
    // 阴影的颜色
    private int mShadowColor = 0x88888888 ;
    // 创建菜单 用来存放 菜单内容
    private FrameLayout mMenuContainerView;
    // 下拉的内容部分高度
    private int mMenuContainerHeight;
    // 筛选菜单的 Adapter
    private BaseMenuAdapter mAdapter ;
    // 当前打开的位置
    private int mCurrentPosition = -1 ;
    // 动画执行时长
    private long DURATION_TIME = 350;
    // 动画是否正在执行
    private boolean mAnimatorExecute ;

    public ListDataScrrenView(Context context) {
        this(context,null);
    }

    public ListDataScrrenView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs ,0);
    }

    public ListDataScrrenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context ;
        initLayout() ;
    }


    /**
     * 1. 将布局实例化号（组合控件）
     */
    private void initLayout() {
        // 在这里实例化布局文件有2种方式：第一种：创建xml文件 然后加载，再findviewbyid  第二种：直接使用代码创建，这里直接采用第二种

        // 整体是垂直的线性布局
        setOrientation(VERTICAL);

        // 1.1 创建LinearLayout作为头部，用来存放Tab
        mMenuTabView = new LinearLayout(mContext);
        // 设置布局参数
        mMenuTabView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mMenuTabView);


        // 1.2  创建 FrameLayout 用来存放 = 阴影（View）+ 菜单内容布局（FrameLayout）
        mMenuMiddleView = new FrameLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT , 0) ;
        params.weight = 1 ;
        mMenuMiddleView.setLayoutParams(params);
        addView(mMenuMiddleView);

        // 1.3  创建阴影 可以直接创建一个View就行 不用设置LayoutParams 默认就是MATCH_PARENT MATCH_PARENT
        mShadowView = new View(mContext);
        // 阴影背景颜色
        mShadowView.setBackgroundColor(mShadowColor);
        // 透明度
        mShadowView.setAlpha(0f);
        // 点击事件
        mShadowView.setOnClickListener(this);
        // 刚开始让阴影的 View 隐藏
        mShadowView.setVisibility(View.GONE);
        // 把阴影添加到上边创建的 FrameLayout，即就是mMenuMiddleView
        mMenuMiddleView.addView(mShadowView);

        // 1.4  创建菜单 用来存放 菜单内容
        mMenuContainerView = new FrameLayout(mContext);
        // 设置菜单背景颜色
        mMenuContainerView.setBackgroundColor(Color.WHITE);
        // 把菜单添加到上边创建的 FrameLayout，即就是mMenuMiddleView
        mMenuMiddleView.addView(mMenuContainerView);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获取整体高度
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (mMenuContainerHeight ==0 && height > 0) {
            // 下拉的内容部分高度不是整体高度 应该整个 View的高度的 75%
            mMenuContainerHeight = (int) (height * 75f / 100);
            // 给菜单内容设置布局参数
            ViewGroup.LayoutParams params = mMenuContainerView.getLayoutParams();
            params.height = mMenuContainerHeight;
            mMenuContainerView.setLayoutParams(params);
            // 刚进来的时候阴影不显示 ，下拉部分的内容也是不显示的（把下拉部分的高度移上去就行）
            mMenuContainerView.setTranslationY(-mMenuContainerHeight);  // 这里是位移动画，Y方向上的
        }
    }


    /**
     * 给 Tab 和 每一个 Tab 对应的内容区域setAdapter  设置内容
     * @param adapter
     */
    public void setAdapter(BaseMenuAdapter adapter){
        this.mAdapter = adapter ;
        // 获取有多少上边条菜单
        int count = mAdapter.getCount() ;
        for (int i = 0; i < count; i++) {
            // 根据角标 和 存放所有Tab 的 LinearLayout 来获取对应菜单的 Tab
            View tabView = mAdapter.getTabView(i, mMenuTabView);
            // 把获取到的每一个tabView 标签添加到 存放所有Tab 的 LinearLayout
            mMenuTabView.addView(tabView);

            // 给每一个Tab 设置布局参数
            LinearLayout.LayoutParams params = (LayoutParams) tabView.getLayoutParams();
            // 使用权重
            params.weight = 1 ;
            tabView.setLayoutParams(params);

            // 设置 Tab 的点击事件
            setTabClick(tabView , i) ;

            // 获取菜单内容
            View menuView = mAdapter.getMenuView(i, mMenuContainerView);
            // 刚开始让菜单内容隐藏
            menuView.setVisibility(View.GONE);
            // 把获取到的每一个菜单内容 添加到 存放菜单内容的 mMenuContainerView 中
            mMenuContainerView.addView(menuView);

        }
    }


    /**
     * 给每个Tab 设置点击事件
     * @param tabView
     * @param position
     */
    private void setTabClick(final View tabView, final int position) {
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // 表示当前没有打开菜单，点击后就打开
                if (mCurrentPosition == -1){
                    openMenu(position , tabView) ;
                }else{
                    // 表示菜单已经打开，点击后就关闭
                    if (mCurrentPosition == position){
                        closeMenu() ;
                    }else{
                        // 切换一下显示
                        // 根据当前位置获取当前的菜单，让其隐藏，然后关闭，然后记录位置
                        View currentMenu = mMenuContainerView.getChildAt(mCurrentPosition) ;
                        currentMenu.setVisibility(View.GONE);
                        mAdapter.menuClose(mMenuTabView.getChildAt(mCurrentPosition));
                        mCurrentPosition = position ;


                        // 再去根据 新纪录的位置，获取当前的Tab，然后让其显示，然后再重新打开
                        currentMenu = mMenuContainerView.getChildAt(mCurrentPosition) ;
                        currentMenu.setVisibility(VISIBLE);
                        mAdapter.menuOpen(mMenuContainerView.getChildAt(mCurrentPosition));
                    }
                }
            }
        });
    }


    /**
     * 关闭菜单
     *     1.  开启位移动画
     *     2.
     */
    private void closeMenu() {
        if (mAnimatorExecute){
            return;
        }
        // 关闭动画  此处涉及到  translationY位移动画 竖直的Y方向  透明度动画
        // 此处直接使用属性动画

        // translationY位移动画 竖直的Y方向
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mMenuContainerView, "translationY", 0, -mMenuContainerHeight);
        translationAnimator.setDuration(DURATION_TIME) ;
        translationAnimator.start();
        mShadowView.setVisibility(VISIBLE);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView, "alpha", 1f, 0f);
        alphaAnimator.setDuration(DURATION_TIME) ;
        // 等关闭动画执行完才能去隐藏当前菜单
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 根据位置获取当前具体菜单的内容，然后隐藏，并且把当前位置置为-1
                View menuView = mMenuContainerView.getChildAt(mCurrentPosition);
                menuView.setVisibility(View.GONE);
                mCurrentPosition = -1 ;

                mShadowView.setVisibility(View.GONE);
                mAnimatorExecute = false ;
            }


            @Override
            public void onAnimationStart(Animator animation) {
                // 根据当前位置获取对应Tab
                mAdapter.menuClose(mMenuTabView.getChildAt(mCurrentPosition));
                mAnimatorExecute = true ;

            }
        });
        alphaAnimator.start();
    }


    /**
     * 打开菜单
     * @param position
     * @param tabView
     */
    private void openMenu(final int position, final View tabView) {

        if (mAnimatorExecute){
            return;
        }
        mShadowView.setVisibility(View.VISIBLE);
        // 根据当前位置显示当前菜单，菜单加到了菜单容器
        View menuView = mMenuContainerView.getChildAt(position);
        menuView.setVisibility(VISIBLE);

        // 打开菜单 开启动画  位移动画 透明度动画
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mMenuContainerView, "translationY", -mMenuContainerHeight, 0);
        translationAnimator.setDuration(DURATION_TIME) ;
        translationAnimator.start();


        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView, "alpha", 0f, 1f);
        alphaAnimator.setDuration(DURATION_TIME) ;

        // 透明动画执行完毕 记录当前位置
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorExecute = false ;
                mCurrentPosition = position ;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                // 把当前 Tab 传递到 外面
                mAdapter.menuOpen(tabView);
                mAnimatorExecute = true ;
            }
        });

        alphaAnimator.start();
    }

    @Override
    public void onClick(View v) {
        closeMenu();
    }
}
