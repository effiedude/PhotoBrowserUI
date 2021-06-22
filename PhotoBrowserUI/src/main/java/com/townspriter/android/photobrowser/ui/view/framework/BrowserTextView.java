package com.townspriter.android.photobrowser.ui.view.framework;

import com.townspriter.android.foundation.utils.log.Logger;
import com.townspriter.android.foundation.utils.ui.ViewUtils;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/******************************************************************************
 * @Path PhotoBrowserUI:BrowserTextView
 * @Describe
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class BrowserTextView extends AppCompatTextView
{
    private static final String TAG="BrowserTextView";
    private @Nullable IBrowserTextView mCallback;
    private float mLastY;
    private boolean mCanScrollInner;
    private boolean mHadInit;
    private ViewUtils.Orientation mLastOrientation=ViewUtils.Orientation.NONE;
    private boolean isScrolling;
    
    public BrowserTextView(@NonNull Context context)
    {
        this(context,null,0);
    }
    
    public BrowserTextView(@NonNull Context context,@Nullable AttributeSet attributeSet)
    {
        this(context,attributeSet,0);
    }
    
    public BrowserTextView(@NonNull Context context,@Nullable AttributeSet attributeSet,int defStyleAttr)
    {
        super(context,attributeSet,defStyleAttr);
    }
    
    public void setCallback(@Nullable IBrowserTextView callback)
    {
        mCallback=callback;
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent motionEvent)
    {
        Logger.d(TAG,"onTouchEvent:"+motionEvent.getAction());
        processTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }
    
    public void setCanScrollInner(boolean canScrollInner)
    {
        Logger.d(TAG,"setCanScrollInner-isScrolling:"+isScrolling);
        if(isScrolling)
        {
            return;
        }
        Logger.d(TAG,"setCanScrollInner-canScrollInner:"+canScrollInner);
        mCanScrollInner=canScrollInner;
        /** 如果允许内部文字滑动则设置文字滑动器 */
        if(mCanScrollInner)
        {
            setMovementMethod(ScrollingMovementMethod.getInstance());
        }
        else
        {
            /** 如果用户没有手动滑动内部文字到底部.程序自动设置内部文字滑动到底部 */
            // setScrollY(0);
            setMovementMethod(null);
        }
    }
    
    private void processTouchEvent(final MotionEvent motionEvent)
    {
        float currentY=motionEvent.getRawY();
        switch(motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mLastY=currentY;
                mHadInit=false;
                break;
            case MotionEvent.ACTION_MOVE:
                isScrolling=true;
                if(getScrollY()==0)
                {
                    float distanceY=currentY-mLastY;
                    ViewUtils.Orientation moveOrientation=ViewUtils.getScrollOrientationVertical(getContext(),distanceY);
                    if(mLastOrientation!=moveOrientation)
                    {
                        Logger.d(TAG,"processTouchEvent:RESET");
                        mLastOrientation=moveOrientation;
                        mLastY=currentY;
                    }
                    switch(moveOrientation)
                    {
                        case BOTTOM:
                            if(!mHadInit)
                            {
                                mHadInit=true;
                                /** 如果在文字滑动到顶部之后继续滑动.则需要重新计算滑动起始位置并将新计算出来的相对距离交给父节点 */
                                setMovementMethod(null);
                            }
                            break;
                        case TOP:
                        default:
                            break;
                    }
                    if(null!=mCallback)
                    {
                        mCallback.onScrollText(motionEvent,mLastY);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isScrolling=false;
                /** 滑动事件和抬起事件处理逻辑相同:如果描述文字超出控件可见范围.则展示向上或向下位移动画 */
                if(getScrollY()==0)
                {
                    // setMovementMethod(null);
                    if(null!=mCallback)
                    {
                        mCallback.onKeyUp(motionEvent,mLastY);
                    }
                }
                break;
            default:
                break;
        }
    }
}
