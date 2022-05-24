package com.townspriter.photobrowser.ui.view.page.browser;
/******************************************************************************
 * @Path PhotoBrowserUI:IBrowserLoadFail
 * @Describe 图集加载失败监听接口
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface IBrowserLoadFail
{
    /**
     * onReloadPhoto
     * 点击重新加载按钮
     */
    void onReloadPhoto();
    
    /**
     * onTap
     * 轻点加载失败布局
     */
    void onTap();
    
    /**
     * onBack
     * 点击返回按钮
     */
    void onBack();
}
