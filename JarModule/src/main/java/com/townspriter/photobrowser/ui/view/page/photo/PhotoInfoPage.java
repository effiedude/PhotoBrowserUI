package com.townspriter.photobrowser.ui.view.page.photo;

import com.townspriter.android.photobrowser.core.api.PhotoBrowser;
import com.townspriter.android.photobrowser.core.api.bean.BrowserArticleItem;
import com.townspriter.photobrowser.ui.R;
import com.townspriter.photobrowser.ui.view.framework.BrowserTextView;
import com.townspriter.photobrowser.ui.view.framework.IBrowserTextView;
import com.townspriter.photobrowser.ui.view.framework.widget.BrowserIndicateView;
import com.townspriter.photobrowser.ui.view.framework.widget.MaxSizeFrameLayout;
import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.text.StringUtil;
import com.townspriter.base.foundation.utils.ui.ResHelper;
import com.townspriter.base.foundation.utils.ui.ViewUtils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

/******************************************************************************
 * @Path PhotoBrowserUI:PhotoInfoPage
 * @Describe 图片文字信息展示控件(标题/详情/来源/作者/编辑)
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class PhotoInfoPage extends MaxSizeFrameLayout
{
    private static final String TAG="PhotoInfoPage";
    /** 允许首次展示详情文字的最多行数 */
    private static final int MAXxLINExLIMIT=5;
    /** 允许滑动展示详情文字的最多行数 */
    private static final int MAXxLINExSCROLL=10;
    private static final long ANIMATIONxTIME=120;
    private static final int SCROLLxSTATUSxSCROLLING=0;
    private static final int SCROLLxSTATUSxUP=1;
    private static final int SCROLLxSTATUSxDOWN=2;
    /** 控件之间的间距 */
    private static int OTHERxMARGINxHEIGHT;
    private int mScrollStatus=SCROLLxSTATUSxDOWN;
    /** 收起时的最大高度 */
    private int mLimitedHeight;
    private int mContentHeight;
    private ViewUtils.Orientation mOrientation;
    private float mLastX;
    private float mLastY;
    private boolean mCanScroll;
    private boolean mCanScrollInner;
    private BrowserIndicateView mIndicate;
    private TextView mTitle;
    private BrowserTextView mDetail;
    private final IBrowserTextView mBrowserTextViewCallback=new IBrowserTextView()
    {
        @Override
        public void onScrollText(MotionEvent motionEvent,float lastY)
        {
            Logger.d(TAG,"onScrollText-motionEvent:"+motionEvent.getAction());
            float distanceY=motionEvent.getRawY()-lastY;
            ViewUtils.Orientation orientation=ViewUtils.getScrollOrientationVertical(getContext(),distanceY);
            switch(orientation)
            {
                case TOP:
                    moveToTop(distanceY,motionEvent);
                    break;
                case BOTTOM:
                    moveToBottom(distanceY,motionEvent);
                    break;
                default:
                    break;
            }
        }
        
        @Override
        public void onKeyUp(MotionEvent motionEvent,float lastY)
        {
            Logger.d(TAG,"onKeyUp-motionEvent:"+motionEvent.getAction());
            float distanceY=motionEvent.getRawY()-lastY;
            ViewUtils.Orientation orientation=ViewUtils.getScrollOrientationVertical(getContext(),distanceY);
            Logger.d(TAG,"onKeyUp-orientation:"+orientation);
            switch(orientation)
            {
                case TOP:
                    moveToTopByTransition();
                    /** 滑动结束后已经是向上状态所以不会执行动画.需要手动设置内部文字可滑动 */
                    if(mScrollStatus==SCROLLxSTATUSxUP)
                    {
                        if(mCanScrollInner)
                        {
                            mDetail.setCanScrollInner(true);
                        }
                    }
                    break;
                case BOTTOM:
                    moveToBottomByTransition();
                    break;
                default:
                    break;
            }
        }
    };
    private TextView mTestFiveLineHeight;
    /** 是否是最后一张图片 */
    private boolean mLastPhoto;
    private IPhotoInfo mCallback;
    
    public PhotoInfoPage(Context context)
    {
        super(context);
        setOverScrollMode(OVER_SCROLL_NEVER);
        initView();
        initData();
    }
    
    public void setCallback(@Nullable IPhotoInfo callback)
    {
        mCallback=callback;
    }
    
    /**
     * changeStyle
     * 沉浸模式和普通模式切换
     *
     * @param isImmerseStyle
     * 是否处于沉浸模式
     */
    public void changeStyle(boolean isImmerseStyle)
    {
        if(isImmerseStyle)
        {
            setVisibility(GONE);
        }
        else
        {
            setVisibility(VISIBLE);
        }
    }
    
    /**
     * setContent
     * 根据文字内容自动扩展ScrollView的高度
     *
     * @param articleItem
     * 文章数据
     * @param photoIndex
     * 当前图片索引
     */
    public void setContent(@NonNull BrowserArticleItem articleItem,int photoIndex)
    {
        // 清除上次操作痕迹
        mDetail.setMovementMethod(null);
        mScrollStatus=SCROLLxSTATUSxDOWN;
        changeBackgroundAlpha(false);
        mTitle.setText("");
        mDetail.setText("");
        mLastPhoto=photoIndex==articleItem.browserImages.size();
        mIndicate.updateIndicateView(photoIndex,articleItem.browserImages.size());
        // 更新标题
        String eachTitle=articleItem.browserImages.get(photoIndex-1).getTitle();
        String totalTitle=articleItem.title;
        if(!StringUtil.isEmptyWithNull(eachTitle))
        {
            mTitle.setText(eachTitle);
            mTitle.setVisibility(VISIBLE);
        }
        else if(!StringUtil.isEmptyWithNull(totalTitle))
        {
            mTitle.setText(totalTitle);
            mTitle.setVisibility(VISIBLE);
        }
        else
        {
            Logger.w(TAG,"setContent-title:NULL");
            mTitle.setVisibility(GONE);
        }
        // 更新详情
        StringBuilder stringBuilder=new StringBuilder();
        String detail=articleItem.browserImages.get(photoIndex-1).desc;
        if(!StringUtil.isEmptyWithNull(detail))
        {
            stringBuilder.append(detail);
        }
        if(mLastPhoto)
        {
            // 更新来源
            String from=articleItem.getSource();
            if(!StringUtil.isEmptyWithNull(from))
            {
                stringBuilder.append("\n").append(ResHelper.getString(R.string.browserUIShowSource)).append(from);
            }
            // 更新作者
            String creatorName=articleItem.getCreatorName();
            if(!StringUtil.isEmptyWithNull(creatorName))
            {
                stringBuilder.append("\n").append(ResHelper.getString(R.string.browserUICreatorName)).append(creatorName);
            }
            // 更新编辑
            String editorName=articleItem.getEditorName();
            if(!StringUtil.isEmptyWithNull(editorName))
            {
                stringBuilder.append("\n").append(ResHelper.getString(R.string.browserUIEditorsName)).append(editorName);
            }
        }
        mDetail.setText(stringBuilder);
        calculateAndSetHeight();
    }
    
    private void calculateAndSetHeight()
    {
        // 索引高度
        int indicateHeight=mIndicate.getHeight();
        // 标题高度
        int titleLineCount=mTitle.getLineCount();
        int titleLineHeight=mTitle.getLineHeight();
        int titleHeightApi=titleLineCount*titleLineHeight;
        int titleHeightMeasure=(int)ViewUtils.measureViewHeight(mTitle);
        int titleHeight=Math.max(titleHeightApi,titleHeightMeasure);
        // 详情高度
        int detailLineCount=mDetail.getLineCount();
        // 通过标准的内置高度控件计算出每行高度
        // 华为(density:2.7)手机测试数据.经过比对数据发现.可以使用五行文字控件计算出来的高度
        // 一行文字计算结果:57
        // 五行文字计算结果:65
        // 十行文字计算结果:66
        float standardLineHeight=ViewUtils.measureViewHeight(mTestFiveLineHeight)/mTestFiveLineHeight.getLineCount();
        Logger.d(TAG,"calculateAndSetHeight-standardLineHeight:"+standardLineHeight);
        int detailLineHeight=(int)standardLineHeight;
        int detailHeight=detailLineHeight*mDetail.getLineCount();
        // 是否支持滑动
        mCanScroll=detailLineCount>MAXxLINExLIMIT;
        int detailHeightFiveLine=detailLineHeight*MAXxLINExLIMIT;
        mLimitedHeight=OTHERxMARGINxHEIGHT+indicateHeight+titleHeight+detailHeightFiveLine;
        mContentHeight=OTHERxMARGINxHEIGHT+indicateHeight+titleHeight+detailHeight;
        // 最大高度
        int detailHeightTenLine=OTHERxMARGINxHEIGHT+indicateHeight+titleHeight+detailLineHeight*MAXxLINExSCROLL;
        if(mContentHeight>detailHeightTenLine)
        {
            mContentHeight=detailHeightTenLine;
            mCanScrollInner=true;
        }
        else
        {
            mCanScrollInner=false;
        }
        int currentHeight=mCanScroll?mLimitedHeight:mContentHeight;
        setMaxHeight(currentHeight);
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent motionEvent)
    {
        Logger.d(TAG,"onTouchEvent:"+motionEvent.getAction());
        float currentX=motionEvent.getRawX();
        float currentY=motionEvent.getRawY();
        switch(motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mLastX=currentX;
                mLastY=currentY;
                mOrientation=ViewUtils.Orientation.NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if(PhotoBrowser.mCurrentOrientation==ViewUtils.Orientation.LEFT||PhotoBrowser.mCurrentOrientation==ViewUtils.Orientation.RIGHT)
                {
                    break;
                }
                mOrientation=ViewUtils.getScrollOrientationVertical(getContext(),currentY-mLastY);
                float dy=currentY-mLastY;
                switch(mOrientation)
                {
                    case TOP:
                        moveToTop(dy,motionEvent);
                        break;
                    case BOTTOM:
                        moveToBottom(dy,motionEvent);
                        break;
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(PhotoBrowser.mCurrentOrientation==ViewUtils.Orientation.LEFT||PhotoBrowser.mCurrentOrientation==ViewUtils.Orientation.RIGHT)
                {
                    break;
                }
                // 滑动事件和抬起事件处理逻辑相同:如果描述文字超出控件可见范围.则展示向上或向下位移动画
                switch(mOrientation)
                {
                    case TOP:
                        moveToTopByTransition();
                        break;
                    case BOTTOM:
                        moveToBottomByTransition();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return true;
    }
    
    private boolean moveToTop(float distanceY,MotionEvent motionEvent)
    {
        Logger.d(TAG,"moveToTop-mScrollStatus:"+mScrollStatus);
        if(mCanScroll&&(mScrollStatus!=SCROLLxSTATUSxUP))
        {
            // 如果在自动滑动期间手动滑动文字则停止自动滑动
            if(mScrollStatus==SCROLLxSTATUSxSCROLLING)
            {
                stopChangeHeightAnimation();
            }
            if(distanceY>0)
            {
                distanceY=0;
            }
            else if(distanceY<(mLimitedHeight-mContentHeight))
            {
                distanceY=mLimitedHeight-mContentHeight;
                mScrollStatus=SCROLLxSTATUSxUP;
                if(null!=motionEvent)
                {
                    mLastY=motionEvent.getRawY();
                }
            }
            setMaxHeight((int)(mLimitedHeight-distanceY));
            requestLayout();
            if(mScrollStatus==SCROLLxSTATUSxUP&&mCanScrollInner)
            {
                mDetail.setCanScrollInner(true);
            }
            return true;
        }
        return false;
    }
    
    private boolean moveToTopByTransition()
    {
        // 注意:自动滑动过程中不允许被中断.否则会出现抖动
        if(mCanScroll&&(mScrollStatus!=SCROLLxSTATUSxUP)&&(mScrollStatus!=SCROLLxSTATUSxSCROLLING))
        {
            startChangeHeightAnimation(true,ANIMATIONxTIME);
            setMaxHeight(mContentHeight);
            requestLayout();
            return true;
        }
        return false;
    }
    
    private boolean moveToBottom(float distanceY,MotionEvent motionEvent)
    {
        Logger.d(TAG,"moveToBottom-mScrollStatus:"+mScrollStatus);
        if(mCanScroll&&(mScrollStatus!=SCROLLxSTATUSxDOWN))
        {
            // 如果在自动滑动期间手动滑动文字则停止自动滑动
            if(mScrollStatus==SCROLLxSTATUSxSCROLLING)
            {
                stopChangeHeightAnimation();
            }
            if(distanceY<0)
            {
                distanceY=0;
            }
            else if(distanceY>(mContentHeight-mLimitedHeight))
            {
                distanceY=mContentHeight-mLimitedHeight;
                mScrollStatus=SCROLLxSTATUSxDOWN;
                if(null!=motionEvent)
                {
                    mLastY=motionEvent.getRawY();
                }
            }
            setMaxHeight((int)(mContentHeight-distanceY));
            requestLayout();
            if(mScrollStatus==SCROLLxSTATUSxDOWN&&mCanScrollInner)
            {
                mDetail.setCanScrollInner(false);
            }
            return true;
        }
        return false;
    }
    
    private boolean moveToBottomByTransition()
    {
        // 注意:自动滑动过程中不允许被中断.否则会出现抖动
        if(mCanScroll&&(mScrollStatus!=SCROLLxSTATUSxDOWN)&&(mScrollStatus!=SCROLLxSTATUSxSCROLLING))
        {
            startChangeHeightAnimation(false,ANIMATIONxTIME);
            setMaxHeight(mLimitedHeight);
            requestLayout();
            return true;
        }
        return false;
    }
    
    private void stopChangeHeightAnimation()
    {
        ViewGroup parent=(ViewGroup)getParent();
        TransitionManager.endTransitions(parent);
    }
    
    private void startChangeHeightAnimation(final boolean isUp,final long duration)
    {
        ViewGroup parent=(ViewGroup)getParent();
        Transition transition=new AutoTransition();
        if(duration>=0)
        {
            transition.setDuration(duration);
        }
        transition.addListener(new Transition.TransitionListener()
        {
            @Override
            public void onTransitionStart(@NonNull final Transition transition)
            {
                Logger.d(TAG,"onTransitionStart");
                if(duration>0)
                {
                    mScrollStatus=SCROLLxSTATUSxSCROLLING;
                }
            }
            
            @Override
            public void onTransitionEnd(@NonNull final Transition transition)
            {
                Logger.d(TAG,"onTransitionEnd");
                if(duration>0)
                {
                    if(isUp)
                    {
                        mScrollStatus=SCROLLxSTATUSxUP;
                        if(mCanScrollInner)
                        {
                            mDetail.setCanScrollInner(true);
                        }
                    }
                    else
                    {
                        mScrollStatus=SCROLLxSTATUSxDOWN;
                        if(mCanScrollInner)
                        {
                            mDetail.setCanScrollInner(false);
                        }
                    }
                }
            }
            
            @Override
            public void onTransitionCancel(@NonNull final Transition transition)
            {
                Logger.d(TAG,"onTransitionCancel");
            }
            
            @Override
            public void onTransitionPause(@NonNull final Transition transition)
            {}
            
            @Override
            public void onTransitionResume(@NonNull final Transition transition)
            {}
        });
        TransitionManager.beginDelayedTransition(parent,transition);
    }
    
    private void initData()
    {
        OTHERxMARGINxHEIGHT=ResHelper.getDimenInt(R.dimen.browserxuixdpx58);
    }
    
    private void initView()
    {
        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(null==inflater)
        {
            Logger.w(TAG,"initView-inflater:NULL");
            return;
        }
        changeBackgroundAlpha(false);
        RelativeLayout bottomInfoLayout=(RelativeLayout)inflater.inflate(R.layout.browserxuixlayoutxbottomxinfo,null);
        addView(bottomInfoLayout);
        mIndicate=bottomInfoLayout.findViewById(R.id.indicateView);
        mTitle=bottomInfoLayout.findViewById(R.id.infoTitle);
        mDetail=bottomInfoLayout.findViewById(R.id.infoDetail);
        mDetail.setCallback(mBrowserTextViewCallback);
        mTestFiveLineHeight=bottomInfoLayout.findViewById(R.id.testFiveLineHeight);
    }
    
    private void changeBackgroundAlpha(boolean alpha)
    {
        if(null!=mCallback)
        {
            mCallback.onBackgroundChanged(alpha);
        }
        if(alpha)
        {
            setBackgroundColor(ResHelper.getColor(R.integer.resxintegerxalphax75,R.color.resxcolorxblack));
        }
        else
        {
            GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{ResHelper.getColor(R.color.resxcolorxtranslate),ResHelper.getColor(R.integer.resxintegerxalphax60,R.color.resxcolorxblack),});
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
            {
                setBackground(gradientDrawable);
            }
            else
            {
                setBackgroundDrawable(gradientDrawable);
            }
        }
    }
}
