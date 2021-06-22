package com.townspriter.android.photobrowser.ui.model.base;

import androidx.annotation.NonNull;

/******************************************************************************
 * @Path PhotoBrowserUI:ArticleService
 * @Describe 正文页数据请求服务接口
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class ArticleService
{
    private static final String TAG="ArticleService";
    private String mApiVersion="1.0.0";
    
    private static class InstanceHolder
    {
        private static final ArticleService INSTANCE=new ArticleService();
    }
    
    private ArticleService()
    {}
    
    public static ArticleService instance()
    {
        return InstanceHolder.INSTANCE;
    }
    
    public void setDefaultApiVersion(@NonNull String apiVersion)
    {
        mApiVersion=apiVersion;
    }
}
