package com.townspriter.photobrowser.ui.view.page.normal;

import com.townspriter.photobrowser.ui.R;
import com.townspriter.photobrowser.ui.view.framework.widget.BrowserIndicateView;
import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.ui.ResHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/******************************************************************************
 * @Path PhotoBrowserUI:BottomBarLayout
 * @Describe 自定义底部布局
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class BottomBar extends RelativeLayout
{
    private static final String TAG="BottomBar";
    private BrowserIndicateView mBrowserIndicateView;
    private ImageView mSavePhoto;
    private IBottomBar mCallback;
    
    public BottomBar(final Context context)
    {
        super(context);
        initView();
    }
    
    public void setCallback(IBottomBar callback)
    {
        mCallback=callback;
    }
    
    public BrowserIndicateView getBrowserIndicateView()
    {
        return mBrowserIndicateView;
    }
    
    private void initView()
    {
        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(null==inflater)
        {
            Logger.w(TAG,"initView-inflater:NULL");
            return;
        }
        RelativeLayout bottomInfoLayout=(RelativeLayout)inflater.inflate(R.layout.browserxuixlayoutxbottom,null);
        mBrowserIndicateView=bottomInfoLayout.findViewById(R.id.indicateView);
        mSavePhoto=bottomInfoLayout.findViewById(R.id.saveButton);
        mSavePhoto.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Logger.d(TAG,"initView-mSavePhoto:onClick");
                if(null!=mCallback)
                {
                    mCallback.doSave();
                }
            }
        });
        LayoutParams layoutParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ResHelper.getDimenInt(R.dimen.browserxuixdpx53));
        addView(bottomInfoLayout,layoutParams);
    }
}
