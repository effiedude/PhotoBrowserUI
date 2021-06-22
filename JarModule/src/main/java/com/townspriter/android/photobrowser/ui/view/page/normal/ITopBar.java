package com.townspriter.android.photobrowser.ui.view.page.normal;

import com.townspriter.android.photobrowser.ui.view.framework.widget.toolbar.IToolbar;

/******************************************************************************
 * @Path PhotoBrowserUI:ITopBarLayout
 * @Describe 标题栏布局监听接口
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface ITopBar
{
    /**
     * doBack
     * 点击返回按钮
     */
    void doBack();
    
    /**
     * doShare
     * 启动分享模块
     *
     * @param shareType
     * SHARE:纯分享(用于底部栏分享) ALL(用于顶部栏分享) OPERATION:纯操作(用于禁止分享之后的顶部栏分享)
     */
    void doShare(IToolbar.ShareType shareType);
}
