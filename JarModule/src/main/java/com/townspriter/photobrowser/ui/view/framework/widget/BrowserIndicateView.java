package com.townspriter.photobrowser.ui.view.framework.widget;

import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.photobrowser.ui.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @Path PhotoBrowserUI:BrowserIndicateView
 * @Describe 图片索引显示控件
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class BrowserIndicateView extends FrameLayout
{
    private static final String TAG="IndicateView";
    /** 左侧索引显示控件(对应1/5中1) */
    private TextView mIndicateLeftText;
    private View mLine;
    /** 右侧索引显示控件(对应1/5中的5) */
    private TextView mIndicateRightText;
    
    public BrowserIndicateView(@NonNull final Context context)
    {
        super(context);
        initView();
    }
    
    public BrowserIndicateView(@NonNull final Context context,@Nullable final AttributeSet attrs)
    {
        super(context,attrs);
        initView();
    }
    
    public void updateIndicateView(int index,int total)
    {
        if(index>=0&&total>0&&total>=index)
        {
            mIndicateLeftText.setText(String.valueOf(index));
            mIndicateRightText.setText(String.valueOf(total));
            mLine.setVisibility(VISIBLE);
        }
        else
        {
            Logger.w(TAG,"updateIndicateView:FAIL:"+index+"/"+total);
        }
    }
    
    private void initView()
    {
        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(null==inflater)
        {
            return;
        }
        LinearLayout indicateLayout=(LinearLayout)inflater.inflate(R.layout.browserxuixlayoutxindicate,null);
        mIndicateLeftText=indicateLayout.findViewById(R.id.indicateLeftText);
        mLine=indicateLayout.findViewById(R.id.indicateMiddleLine);
        mIndicateRightText=indicateLayout.findViewById(R.id.indicateRightText);
        addView(indicateLayout);
    }
}
