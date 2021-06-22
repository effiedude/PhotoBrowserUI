package com.townspriter.android.photobrowser.ui.view.page.browser;

import com.townspriter.android.foundation.utils.log.Logger;
import com.townspriter.android.foundation.utils.system.SystemInfo;
import com.townspriter.android.foundation.utils.ui.ResHelper;
import com.townspriter.android.photobrowser.ui.R;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @Path PhotoBrowserUI:BrowserOfflinePage
 * @Describe 图集下线页面
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class BrowserOfflinePage extends RelativeLayout
{
    private static final String TAG="BrowserOfflinePage";
    private @Nullable IBrowserOffline mCallback;
    
    public BrowserOfflinePage(@NonNull Context context)
    {
        super(context);
        initView();
    }
    
    /**
     * setCallback
     * 设置下线页面监听接口
     *
     * @param callback
     * 监听器
     */
    public void setCallback(@Nullable IBrowserOffline callback)
    {
        mCallback=callback;
    }
    
    private void initView()
    {
        setTranslationY(SystemInfo.INSTANCE.getStatusBarHeight());
        setBackgroundColor(ResHelper.getColor(R.color.valuexcolorxblack));
        addView(createBackView());
        addView(createInfoView());
    }
    
    private TextView createInfoView()
    {
        TextView infoText=new TextView(getContext());
        infoText.setText(ResHelper.getString(R.string.browserUIPhotoOffline));
        infoText.setTextSize(ResHelper.getDimenInt(R.dimen.browserxuixdpx16));
        infoText.setTextColor(ResHelper.getColor(R.color.valuexcolorxwhitex50));
        LayoutParams infoTextParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        infoTextParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        infoText.setLayoutParams(infoTextParams);
        return infoText;
    }
    
    private FrameLayout createBackView()
    {
        FrameLayout backIconLayout=new FrameLayout(getContext());
        backIconLayout.setClickable(true);
        ImageView backIcon=new ImageView(getContext());
        // Drawable drawable=DrawableUtils.getDyeDrawable(R.drawable.iconxback,ResHelper.getColor(R.color.white));
        // backIcon.setImageDrawable(drawable);
        int backIconSize=ResHelper.getDimenInt(R.dimen.browserxuixdpx24);
        FrameLayout.LayoutParams backIconParams=new FrameLayout.LayoutParams(backIconSize,backIconSize);
        backIconParams.leftMargin=ResHelper.getDimenInt(R.dimen.browserxuixdpx12);
        backIconParams.gravity=Gravity.LEFT|Gravity.CENTER_VERTICAL;
        backIconLayout.addView(backIcon,backIconParams);
        backIconLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Logger.d(TAG,"createBackButton:onClick");
                if(null!=mCallback)
                {
                    mCallback.onBack();
                }
            }
        });
        FrameLayout.LayoutParams backLayoutParams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,ResHelper.getDimenInt(R.dimen.browserxuixdpx44));
        backIconLayout.setLayoutParams(backLayoutParams);
        return backIconLayout;
    }
}
