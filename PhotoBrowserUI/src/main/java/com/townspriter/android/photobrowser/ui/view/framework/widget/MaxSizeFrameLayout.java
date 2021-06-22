package com.townspriter.android.photobrowser.ui.view.framework.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/******************************************************************************
 * @Path PhotoBrowserUI:MaxSizeFrameLayout
 * @Describe 可以定制宽高的布局
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class MaxSizeFrameLayout extends FrameLayout
{
    private final String TAG="MaxSizeFrameLayout";
    private int mMaxWidth;
    private int mMaxHeight;
    
    public MaxSizeFrameLayout(final Context context)
    {
        super(context);
    }
    
    public MaxSizeFrameLayout(final Context context,final AttributeSet attrs)
    {
        super(context,attrs);
    }
    
    public MaxSizeFrameLayout(final Context context,final AttributeSet attrs,final int defStyleAttr)
    {
        super(context,attrs,defStyleAttr);
    }
    
    public void setMaxWidth(int maxWidth)
    {
        mMaxWidth=maxWidth;
    }
    
    public void setMaxHeight(int maxHeight)
    {
        mMaxHeight=maxHeight;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec)
    {
        if(mMaxWidth>0)
        {
            widthMeasureSpec=MeasureSpec.makeMeasureSpec(mMaxWidth,MeasureSpec.EXACTLY);
        }
        if(mMaxHeight>0)
        {
            heightMeasureSpec=MeasureSpec.makeMeasureSpec(mMaxHeight,MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }
}
