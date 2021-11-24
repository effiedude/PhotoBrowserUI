package com.townspriter.photobrowser.ui.view.framework;

import android.view.MotionEvent;

/******************************************************************************
 * @Path PhotoBrowserUI:IBrowserTextView
 * @Describe
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface IBrowserTextView
{
    /**
     * onScrollText
     * 匀速滑动文字
     *
     * @param motionEvent
     * @param downY
     */
    void onScrollText(MotionEvent motionEvent,float downY);
    
    /**
     * onKeyUp
     * 快速滑动文字
     *
     * @param motionEvent
     * @param downY
     */
    void onKeyUp(MotionEvent motionEvent,float downY);
}
