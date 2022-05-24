package com.townspriter.photobrowser.ui.view.page.photo;

import com.townspriter.base.foundation.utils.ui.ResHelper;
import com.townspriter.base.foundation.utils.ui.ViewUtils;
import com.townspriter.photobrowser.ui.R;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;

/******************************************************************************
 * @Path PhotoBrowserUI:PhotoNetworkErrorPage
 * @Describe 图片网络错误页面
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class PhotoNetworkErrorPage extends RelativeLayout implements GestureDetector.OnDoubleTapListener,View.OnTouchListener
{
    private static final String TAG="PhotoNetworkErrorPage";
    private static final int IDxVIEWxERRORxIMAGE=ViewUtils.generateViewId();
    private static final int IDxVIEWxERRORxTEXT=ViewUtils.generateViewId();
    private GestureDetector mGestureDetector;
    private IPhotoNetworkError mCallback;
    
    public PhotoNetworkErrorPage(@NonNull Context context)
    {
        super(context);
        initView();
    }
    
    /**
     * setCallback
     * 设置布局监听
     *
     * @param callback
     * 监听器
     */
    public void setCallback(@NonNull IPhotoNetworkError callback)
    {
        mCallback=callback;
    }
    
    @Override
    public boolean onSingleTapConfirmed(final MotionEvent motionEvent)
    {
        if(null!=mCallback)
        {
            mCallback.onTap();
        }
        return false;
    }
    
    @Override
    public boolean onDoubleTap(final MotionEvent motionEvent)
    {
        return false;
    }
    
    @Override
    public boolean onDoubleTapEvent(final MotionEvent motionEvent)
    {
        return false;
    }
    
    @Override
    public boolean onTouch(final View view,final MotionEvent event)
    {
        return mGestureDetector.onTouchEvent(event);
    }
    
    private void initView()
    {
        setBackgroundColor(ResHelper.getColor(R.color.resxcolorxblack));
        setClickable(true);
        setOnTouchListener(this);
        mGestureDetector=new GestureDetector(new GestureDetector.SimpleOnGestureListener());
        mGestureDetector.setOnDoubleTapListener(this);
        RelativeLayout innerLayout=new RelativeLayout(getContext());
        innerLayout.setTranslationY(-ResHelper.getDimenInt(R.dimen.browserxuixdpx80));
        LayoutParams innerLayoutParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        addView(innerLayout,innerLayoutParams);
        ImageView errorImage=new ImageView(getContext());
        errorImage.setId(IDxVIEWxERRORxIMAGE);
        LayoutParams errorImageParams=new LayoutParams(ResHelper.getDimenInt(R.dimen.browserxuixdpx56),ResHelper.getDimenInt(R.dimen.browserxuixdpx56));
        errorImageParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        errorImage.setImageResource(R.drawable.browserxuixiconxbroken);
        innerLayout.addView(errorImage,errorImageParams);
        TextView errorText=new TextView(getContext());
        errorText.setId(IDxVIEWxERRORxTEXT);
        errorText.setGravity(Gravity.CENTER);
        LayoutParams errorTextParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        errorTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        errorTextParams.addRule(RelativeLayout.BELOW,errorImage.getId());
        errorTextParams.topMargin=ResHelper.getDimenInt(R.dimen.browserxuixdpx16);
        errorText.setText(ResHelper.getString(R.string.browserUINetworkError));
        errorText.setTextSize(ResHelper.getDimenInt(R.dimen.browserxuixdpx16));
        errorText.setTextColor(ResHelper.getColor(R.color.resxcolorxwhitex50));
        innerLayout.addView(errorText,errorTextParams);
        LinearLayout mReloadLayout=new LinearLayout(getContext());
        mReloadLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(null!=mCallback)
                {
                    mCallback.onReloadPhoto();
                }
            }
        });
        LayoutParams reloadParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        reloadParams.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        reloadParams.addRule(RelativeLayout.BELOW,errorText.getId());
        reloadParams.topMargin=ResHelper.getDimenInt(R.dimen.browserxuixdpx20);
        mReloadLayout.setOrientation(LinearLayout.HORIZONTAL);
        mReloadLayout.setGravity(Gravity.CENTER_VERTICAL);
        mReloadLayout.setPadding(ResHelper.getDimenInt(R.dimen.browserxuixdpx8),ResHelper.getDimenInt(R.dimen.browserxuixdpx8),ResHelper.getDimenInt(R.dimen.browserxuixdpx8),ResHelper.getDimenInt(R.dimen.browserxuixdpx8));
        innerLayout.addView(mReloadLayout,reloadParams);
        ImageView reloadImage=new ImageView(getContext());
        LayoutParams reloadImageParams=new LayoutParams(ResHelper.getDimenInt(R.dimen.browserxuixdpx13),ResHelper.getDimenInt(R.dimen.browserxuixdpx13));
        reloadImage.setLayoutParams(reloadImageParams);
        reloadImage.setColorFilter(ResHelper.getColor(R.color.resxcolorxwhite),Mode.SRC_IN);
        // reloadImage.setImageResource(R.drawable.iconxrefresh);
        TextView reloadText=new TextView(getContext());
        reloadText.setText(ResHelper.getString(R.string.browserUINetworkLoadRetry));
        reloadText.setTextSize(ResHelper.getDimenInt(R.dimen.browserxuixdpx14));
        reloadText.setTextColor(ResHelper.getColor(R.color.resxcolorxwhite));
        mReloadLayout.addView(reloadImage);
        mReloadLayout.addView(reloadText);
    }
}
