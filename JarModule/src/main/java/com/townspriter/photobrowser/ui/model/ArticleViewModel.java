package com.townspriter.photobrowser.ui.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/******************************************************************************
 * @Path PhotoBrowserUI:ArticleViewModel
 * @Describe 文章详情数据获取接口
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class ArticleViewModel extends AndroidViewModel
{
    private static final String TAG="ArticleViewModel";
    private MutableLiveData<String> mArticleLiveData;
    private MutableLiveData<Throwable> mArticleLiveDataThrow;
    
    public ArticleViewModel(@NonNull final Application application)
    {
        super(application);
        init();
    }
    
    public LiveData<String> getArticleLiveData()
    {
        return mArticleLiveData;
    }
    
    public LiveData<Throwable> getArticleLiveDataThrow()
    {
        return mArticleLiveDataThrow;
    }
    
    /**
     * getArticle
     * 根据文章标题标示请求文章数据
     *
     * @param articleId
     * 文章标题内容请求标示
     * @param channelId
     * 数据上层来源
     */
    public void getArticle(final String articleId,String channelId)
    {}
    
    private void init()
    {
        mArticleLiveData=new MutableLiveData<>();
        mArticleLiveDataThrow=new MutableLiveData<>();
    }
}
