package com.townspriter.android.photobrowser.ui.view.framework.widget.toolbar;
/******************************************************************************
 * @Path PhotoBrowserUI:IToolbar
 * @Describe 工具栏布局监听接口
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface IToolbar
{
    /** 点击顶部或底部分享按钮回调类型 */
    enum ShareType
    {
        /** 纯分享菜单(微博或支付宝等) */
        SHARE,
        /** 纯操作菜单(复制等) */
        OPERATION,
        /** 分享和操作菜单 */
        ALL
    }
    /** 点击顶部或底部收藏按钮回调类型 */
    enum FavoriteType
    {
        /** 顶部收藏标示 */
        TOP,
        /** 底部收藏标示 */
        BOTTOM
    }
    
    /**
     * doComment
     * 启动评论模块
     */
    void doComment();
    
    /**
     * showComment
     * 展示评论模块
     */
    void showComment();
    
    /**
     * doFavorite
     * 调用收藏功能
     *
     * @param favoriteType
     * TOP:顶部 BOTTOM:底部
     * @param favorite
     * true:订阅 false:取消订阅
     */
    void doFavorite(FavoriteType favoriteType,boolean favorite);
    
    /**
     * doShare
     * 启动分享模块
     *
     * @param shareType
     * SHARE:纯分享(用于底部栏分享) ALL(用于顶部栏分享) OPERATION:纯操作(用于禁止分享之后的顶部栏分享)
     */
    void doShare(ShareType shareType);
    
    /**
     * doSave
     * 存储图片到本地相册
     */
    void doSave();
}
