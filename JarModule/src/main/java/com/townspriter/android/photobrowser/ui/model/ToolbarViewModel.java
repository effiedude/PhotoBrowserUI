package com.townspriter.android.photobrowser.ui.model;

import com.townspriter.android.photobrowser.ui.view.framework.widget.toolbar.IToolbar;
import com.townspriter.android.photobrowser.ui.view.framework.widget.toolbar.plugin.FavoriteActionPlugin;
import com.townspriter.android.foundation.utils.log.Logger;
import com.townspriter.android.photobrowser.core.api.bean.ArticleItem;
import com.townspriter.android.photobrowser.core.api.bean.BrowserArticleItem;

import android.app.Activity;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import rx.Observable;

/******************************************************************************
 * @Path PhotoBrowserUI:ToolbarViewModel
 * @Describe 工具栏数据交互接口
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class ToolbarViewModel extends AndroidViewModel
{
    private final String TAG="ToolbarViewModel";
    @Nullable
    private FavoriteActionPlugin mFavoriteActionPlugin;
    private MutableLiveData<Boolean> mFavoriteLiveData;
    private MutableLiveData<Throwable> mFavoriteLiveDataThrow;
    private MutableLiveData<Long> mCommentLiveData;
    private MutableLiveData<Throwable> mCommentLiveDataThrow;
    
    public ToolbarViewModel(Application application)
    {
        super(application);
        initData();
    }
    
    /**
     * setFavoriteEnable
     * 设置是否在顶部栏分享面板中展示收藏按钮.根据接口下发字段决定.保证顶部和底部分享面板收藏图标状态展示一致
     *
     * @param enable
     * true:展示 false:隐藏
     */
    public void setFavoriteEnable(boolean enable)
    {
        Logger.d(TAG,"setFavoriteEnable:"+enable);
    }
    
    /**
     * getFavoriteActionPlugin
     * 获取顶部收藏按钮插件
     *
     * @return FavoriteActionPlugin
     */
    public @Nullable FavoriteActionPlugin getFavoriteActionPlugin()
    {
        return mFavoriteActionPlugin;
    }
    
    /**
     * getFavoriteLiveData
     * 获取收藏模块数据LiveData
     *
     * @return LiveData<Boolean>
     */
    public LiveData<Boolean> getFavoriteLiveData()
    {
        return mFavoriteLiveData;
    }
    
    /**
     * getFavoriteLiveDataThrow
     * 获取收藏模块异常LiveData
     *
     * @return LiveData<Throwable>
     */
    public LiveData<Throwable> getFavoriteLiveDataThrow()
    {
        return mFavoriteLiveDataThrow;
    }
    
    /**
     * getCommentLiveData
     * 获取评论模块数据LiveData
     *
     * @return LiveData<Long>
     */
    public LiveData<Long> getCommentLiveData()
    {
        return mCommentLiveData;
    }
    
    /**
     * getCommentLiveDataThrow
     * 获取评论模块异常LiveData
     *
     * @return LiveData<Throwable>
     */
    public LiveData<Throwable> getCommentLiveDataThrow()
    {
        return mCommentLiveDataThrow;
    }
    
    /**
     * doComment
     * 启动评论模块
     *
     * @param fragmentManager
     * 用于承载评论列表的FragmentManager
     * @param articleId
     * 文章标示
     */
    public void doComment(LifecycleOwner lifecycleOwner,FragmentManager fragmentManager,@NonNull String articleId,@NonNull String categoryId)
    {
        Logger.d(TAG,"doComment-articleId:"+articleId);
        Logger.d(TAG,"doComment-categoryId:"+categoryId);
    }
    
    /**
     * showComment
     * 展示评论模块
     *
     * @param fragmentManager
     * 用于承载评论列表的FragmentManager
     * @param articleId
     * 文章标示
     */
    public void showComment(LifecycleOwner lifecycleOwner,FragmentManager fragmentManager,@NonNull String articleId,@NonNull String categoryId)
    {
        Logger.d(TAG,"showComment-articleId:"+articleId);
        Logger.d(TAG,"showComment-categoryId:"+categoryId);
    }
    
    /**
     * doShare
     * 启动分享模块
     *
     * @param shareType
     * TOP:顶部分享 BOTTOM:底部分享
     */
    public void doShare(LifecycleOwner lifecycleOwner,Activity activity,@NonNull ArticleItem articleItem,FragmentManager fragmentManager,IToolbar.ShareType shareType)
    {}
    
    private Observable<String> login()
    {
        return null;
    }
    
    /**
     * doFavorite
     * 更改收藏状态.需要用户登陆才能更新收藏状态
     *
     * @param isFavorite
     * 当前收藏状态
     */
    public void doFavorite(final boolean isFavorite,@NonNull final BrowserArticleItem articleItem)
    {}
    
    /**
     * syncFavorite
     * 查询当前文章的订阅状态.查询的是本地数据库
     */
    public void syncFavorite(@NonNull String articleId)
    {}
    
    /**
     * getCommentCount
     * 获取当前文章的用户全部的评论数量(包含未过审评论)
     * 获取文章的评论数据成功之后.通过LiveData通知关注者当前文章的评论数量
     *
     * @param articleId
     * 正文页标示
     */
    public void getCommentCount(@NonNull String articleId)
    {}
    
    /**
     * updateFavoriteIcon
     * 根据收藏状态更新分享面板收藏图标
     *
     * @param favorite
     */
    private void updateFavoriteIcon(final boolean favorite)
    {
        if(null!=mFavoriteActionPlugin)
        {
            mFavoriteActionPlugin.updateIcon(favorite);
        }
    }
    
    private void initData()
    {
        mFavoriteActionPlugin=new FavoriteActionPlugin();
        mFavoriteLiveData=new MutableLiveData<>();
        mFavoriteLiveDataThrow=new MutableLiveData<>();
        mCommentLiveData=new MutableLiveData<>();
        mCommentLiveDataThrow=new MutableLiveData<>();
    }
}
