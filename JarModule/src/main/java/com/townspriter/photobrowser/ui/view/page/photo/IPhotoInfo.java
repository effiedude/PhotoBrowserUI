package com.townspriter.photobrowser.ui.view.page.photo;
/******************************************************************************
 * @Path PhotoBrowserUI:IPhotoBrowserInfo
 * @Describe 图集底部布局监听接口
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface IPhotoInfo
{
    /**
     * onBackgroundChanged
     * 切换背景状态时调用此方法
     *
     * @param alpha
     * true:使用75%透明度黑色背景 false:使用0%-60%渐变黑色背景
     */
    void onBackgroundChanged(boolean alpha);
}
