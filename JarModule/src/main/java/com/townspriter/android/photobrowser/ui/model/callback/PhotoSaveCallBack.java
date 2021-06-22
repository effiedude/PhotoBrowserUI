package com.townspriter.android.photobrowser.ui.model.callback;
/******************************************************************************
 * @Path PhotoBrowserUI:PhotoSaveCallBack
 * @Describe 抽离图片浏览器基础模块公用
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface PhotoSaveCallBack
{
    /** 保存成功 */
    void savePhotoSuccess();
    
    /** 保持失败 */
    void savePhotoFailure();
}
