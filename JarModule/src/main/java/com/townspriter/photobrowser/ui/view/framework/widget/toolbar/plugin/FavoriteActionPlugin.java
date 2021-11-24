package com.townspriter.photobrowser.ui.view.framework.widget.toolbar.plugin;

import com.townspriter.photobrowser.ui.view.framework.widget.toolbar.IToolbar;
import com.townspriter.base.foundation.utils.log.Logger;

import androidx.annotation.NonNull;

/******************************************************************************
 * @Path PhotoBrowserUI:FavoriteActionPlugin
 * @Describe 分享界面收藏图标插件
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class FavoriteActionPlugin
{
    private static final String TAG="FavoriteActionPlugin";
    /**
     * isFavorite
     * 收藏功能属于本地功能
     */
    private boolean isFavorite;
    private IToolbar mCallback;
    
    public FavoriteActionPlugin()
    {
        this(false);
    }
    
    public FavoriteActionPlugin(boolean favorite)
    {
        updateIcon(favorite);
    }
    
    /**
     * updateIcon
     * 更新分享插件图标状态
     *
     * @param favorite
     */
    public void updateIcon(boolean favorite)
    {
        Logger.d(TAG,"updateIcon:"+favorite);
        isFavorite=favorite;
    }
    
    /**
     * setCallback
     * 监听顶部分享栏点击事件
     *
     * @param callback
     */
    public void setCallback(@NonNull IToolbar callback)
    {
        mCallback=callback;
    }
}
