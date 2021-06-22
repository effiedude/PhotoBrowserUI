package com.townspriter.android.photobrowser.ui.view.page.photo;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.townspriter.android.foundation.utils.ui.ResHelper;
import com.townspriter.android.foundation.utils.ui.ViewUtils;
import com.townspriter.android.photobrowser.ui.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;

/******************************************************************************
 * @Path PhotoBrowserUI:PhotoLoadingPage
 * @Describe 图片加载中页面
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class PhotoLoadingPage extends RelativeLayout
{
    private static final String TAG="PhotoLoadingPage";
    private static final String LOTTIExFOLDERxNAME="images";
    private static final String LOTTIExANIMATIONxNAME="loading.json";
    private static final int IDxVIEWxANIMATION=ViewUtils.generateViewId();
    private LottieAnimationView mLoadingAnimationView;
    
    public PhotoLoadingPage(Context context)
    {
        super(context);
        initView();
    }
    
    private void initView()
    {
        setBackgroundColor(ResHelper.getColor(R.color.valuexcolorxblack));
        RelativeLayout innerLayout=new RelativeLayout(getContext());
        innerLayout.setTranslationY(-ResHelper.getDimenInt(R.dimen.browserxuixdpx80));
        LayoutParams innerLayoutParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        addView(innerLayout,innerLayoutParams);
        mLoadingAnimationView=new LottieAnimationView(getContext());
        mLoadingAnimationView.setId(IDxVIEWxANIMATION);
        mLoadingAnimationView.setImageAssetsFolder(LOTTIExFOLDERxNAME);
        mLoadingAnimationView.setAnimation(LOTTIExANIMATIONxNAME);
        mLoadingAnimationView.setRepeatCount(LottieDrawable.INFINITE);
        LayoutParams animationLayoutParams=new LayoutParams(ResHelper.getDimenInt(R.dimen.browserxuixdpx56),ResHelper.getDimenInt(R.dimen.browserxuixdpx56));
        animationLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        animationLayoutParams.topMargin=-ResHelper.getDimenInt(R.dimen.browserxuixdpx80);
        innerLayout.addView(mLoadingAnimationView,animationLayoutParams);
        TextView tipView=new TextView(getContext());
        tipView.setTextSize(ResHelper.getDimenInt(R.dimen.browserxuixdpx16));
        tipView.setTextColor(ResHelper.getColor(R.color.valuexcolorxwhitex50));
        tipView.setText(R.string.browserUILoadingText);
        LayoutParams layoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.BELOW,mLoadingAnimationView.getId());
        layoutParams.topMargin=ResHelper.getDimenInt(R.dimen.browserxuixdpx18);
        innerLayout.addView(tipView,layoutParams);
    }
    
    @Override
    protected void onVisibilityChanged(@NonNull final View changedView,final int visibility)
    {
        super.onVisibilityChanged(changedView,visibility);
        if(visibility==VISIBLE&&!mLoadingAnimationView.isAnimating())
        {
            mLoadingAnimationView.playAnimation();
        }
        else
        {
            mLoadingAnimationView.cancelAnimation();
        }
    }
}
