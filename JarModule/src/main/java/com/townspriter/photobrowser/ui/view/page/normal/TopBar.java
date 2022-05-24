package com.townspriter.photobrowser.ui.view.page.normal;

import com.townspriter.photobrowser.ui.R;
import com.townspriter.photobrowser.ui.view.framework.widget.toolbar.IToolbar;
import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.system.SystemInfo;
import com.townspriter.base.foundation.utils.ui.ResHelper;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/******************************************************************************
 * @Path PhotoBrowserUI:TopBarLayout
 * @Describe 顶部标题栏布局
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class TopBar extends FrameLayout
{
    private final String TAG="TopLayout";
    private TextView mTitle;
    private FrameLayout mMoreLayout;
    private ITopBar mCallback;
    
    public TopBar(Context context)
    {
        super(context);
        GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{ResHelper.getColor(R.color.resxcolorxtranslate),ResHelper.getColor(R.integer.resxintegerxalphax60,R.color.resxcolorxblack),});
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
        {
            setBackground(gradientDrawable);
        }
        else
        {
            setBackgroundDrawable(gradientDrawable);
        }
        setClickable(true);
        initTopLayout();
    }
    
    public TextView getTitle()
    {
        return mTitle;
    }
    
    public void setTitle(String title)
    {
        if(null!=mTitle)
        {
            mTitle.setText(title);
        }
        else
        {
            Logger.w(TAG,"setTitle-mTitle:NULL");
        }
    }
    
    /**
     * setMoreLayoutVisible
     * 如果同时关闭分享和收藏功能则不再显示更多按钮
     *
     * @param visibility
     * View.GONE View.VISIBLE
     */
    public void setMoreLayoutVisible(int visibility)
    {
        mMoreLayout.setVisibility(visibility);
    }
    
    public void setCallback(ITopBar callback)
    {
        mCallback=callback;
    }
    
    private void initTopLayout()
    {
        LayoutParams topLayoutParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        setLayoutParams(topLayoutParams);
        FrameLayout innerLayout=new FrameLayout(getContext());
        FrameLayout.LayoutParams innerLayoutParams=new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,ResHelper.getDimenInt(R.dimen.browserxuixdpx44));
        innerLayoutParams.topMargin=SystemInfo.INSTANCE.getStatusBarHeight();
        addView(innerLayout,innerLayoutParams);
        innerLayout.addView(createBackView());
        innerLayout.addView(createTitleView(""));
        mMoreLayout=createMoreView();
        innerLayout.addView(mMoreLayout);
    }
    
    private FrameLayout createBackView()
    {
        FrameLayout backLayout=new FrameLayout(getContext());
        backLayout.setClickable(true);
        ImageView backIcon=new ImageView(getContext());
        // Drawable drawable=DrawableUtils.getDyeDrawable(R.drawable.iconxback,ResHelper.getColor(R.color.white));
        // backIcon.setImageDrawable(drawable);
        int backIconSize=ResHelper.getDimenInt(R.dimen.browserxuixdpx24);
        LayoutParams backIconParams=new LayoutParams(backIconSize,backIconSize);
        backLayout.addView(backIcon,backIconParams);
        backLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Logger.d(TAG,"createBackButton:onClick");
                if(null!=mCallback)
                {
                    mCallback.doBack();
                }
            }
        });
        LayoutParams backLayoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        backLayoutParams.leftMargin=ResHelper.getDimenInt(R.dimen.browserxuixdpx12);
        backLayoutParams.gravity=Gravity.LEFT|Gravity.CENTER_VERTICAL;
        backLayout.setLayoutParams(backLayoutParams);
        return backLayout;
    }
    
    private FrameLayout createTitleView(String title)
    {
        FrameLayout titleLayout=new FrameLayout(getContext());
        mTitle=new TextView(getContext());
        mTitle.setText(title);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,ResHelper.getDimenInt(R.dimen.browserxuixdpx16));
        mTitle.setTextColor(ResHelper.getColor(R.color.resxcolorxred));
        mTitle.setMaxLines(1);
        titleLayout.addView(mTitle);
        LayoutParams titleLayoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        titleLayoutParams.leftMargin=ResHelper.getDimenInt(R.dimen.browserxuixdpx44);
        titleLayoutParams.gravity=Gravity.CENTER_VERTICAL;
        titleLayout.setLayoutParams(titleLayoutParams);
        return titleLayout;
    }
    
    private FrameLayout createMoreView()
    {
        FrameLayout moreLayout=new FrameLayout(getContext());
        moreLayout.setClickable(true);
        ImageView moreIcon=new ImageView(getContext());
        // Drawable drawable=DrawableUtils.getDyeMutableDrawable(R.drawable.iconxmore,ResHelper.getColor(R.color.white));
        // moreIcon.setImageDrawable(drawable);
        int moreIconSize=ResHelper.getDimenInt(R.dimen.browserxuixdpx24);
        LayoutParams moreIconParams=new LayoutParams(moreIconSize,moreIconSize);
        moreLayout.addView(moreIcon,moreIconParams);
        moreIcon.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Logger.d(TAG,"createMoreView:onClick");
                if(null!=mCallback)
                {
                    mCallback.doShare(IToolbar.ShareType.OPERATION);
                }
            }
        });
        LayoutParams moreLayoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        moreLayoutParams.rightMargin=ResHelper.getDimenInt(R.dimen.browserxuixdpx16);
        moreLayoutParams.gravity=Gravity.RIGHT|Gravity.CENTER_VERTICAL;
        moreLayout.setLayoutParams(moreLayoutParams);
        return moreLayout;
    }
}
