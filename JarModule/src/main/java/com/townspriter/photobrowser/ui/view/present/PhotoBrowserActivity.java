package com.townspriter.photobrowser.ui.view.present;

import org.json.JSONException;
import org.json.JSONObject;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.townspriter.android.photobrowser.core.api.bean.BrowserArticleItem;
import com.townspriter.android.photobrowser.core.api.listener.UICallback;
import com.townspriter.android.photobrowser.core.api.view.IPhotoBrowserOverlay;
import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.net.NetworkUtil;
import com.townspriter.photobrowser.ui.constant.RouterConstant;
import com.townspriter.photobrowser.ui.view.page.browser.BrowserLoadFailPage;
import com.townspriter.photobrowser.ui.view.page.browser.BrowserNetworkErrorPage;
import com.townspriter.photobrowser.ui.view.page.browser.IBrowserLoadFail;
import com.townspriter.photobrowser.ui.view.page.browser.IBrowserNetworkError;
import com.townspriter.photobrowser.ui.view.page.normal.BottomBar;
import com.townspriter.photobrowser.ui.view.page.normal.IBottomBar;
import com.townspriter.photobrowser.ui.view.page.photo.IPhotoLoadFail;
import com.townspriter.photobrowser.ui.view.page.photo.PhotoLoadFailPage;
import com.townspriter.photobrowser.ui.view.page.photo.PhotoLoadingPage;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;

/******************************************************************************
 * @Path PhotoBrowserUI:PhotoBrowserActivity
 * @Describe 图片浏览器承载类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
@Route(path=RouterConstant.AROUTERxPATHxBROWSER)
public class PhotoBrowserActivity extends BaseActivity
{
    private static final String TAG="PhotoBrowserActivity";
    private final IBrowserNetworkError mBrowserNetworkErrorCallback=new IBrowserNetworkError()
    {
        @Override
        public void onReloadPhoto()
        {
            Logger.d(TAG,"onReloadPhoto");
        }
        
        @Override
        public void onTap()
        {
            Logger.d(TAG,"onTap");
        }
        
        @Override
        public void onBack()
        {
            Logger.i(TAG,"onBack");
            finish();
        }
    };
    private final IBrowserLoadFail mBrowserLoadFailCallback=new IBrowserLoadFail()
    {
        @Override
        public void onReloadPhoto()
        {
            Logger.i(TAG,"onReloadPhoto");
        }
        
        @Override
        public void onTap()
        {
            Logger.i(TAG,"onTap");
        }
        
        @Override
        public void onBack()
        {
            Logger.i(TAG,"onBack");
            finish();
        }
    };
    private final IBottomBar mBottomBarCallback=new IBottomBar()
    {
        @Override
        public void doSave()
        {
            Logger.d(TAG,"doSave");
            savePicture(getPhotoBrowser().getCurrentPhotoUrl());
        }
    };
    @Autowired(name=RouterConstant.AROUTERxPARAMSxJSON)
    public String jsonContent;
    /** 是否加载数据失败 */
    private boolean isEmptyJson;
    private final IPhotoLoadFail mPhotoLoadFailCallback=new IPhotoLoadFail()
    {
        @Override
        public void onReloadPhoto()
        {
            Logger.d(TAG,"onReloadPhoto");
            if(!isEmptyJson)
            {
                reloadPhoto();
            }
        }
        
        @Override
        public void onTap()
        {
            Logger.d(TAG,"onTap");
            finish();
        }
    };
    private BrowserArticleItem mPhotoViewData;
    private int mPhotoViewIndex;
    private PhotoBrowserOverlayImpl photoBrowserOverlay;
    private BottomBar mBottomBar;
    private BrowserLoadFailPage mBrowserLoadFailPage;
    private BrowserNetworkErrorPage mBrowserNetworkErrorPage;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }
    
    @Override
    public void onPhotoTap(final View view,final float x,final float y)
    {
        super.onPhotoTap(view,x,y);
        finish();
    }
    
    @Override
    public void onViewTap(final View view,final float x,final float y)
    {
        super.onViewTap(view,x,y);
        finish();
    }
    
    /**************************************** 私有方法 ****************************************/
    private void initData()
    {
        ARouter.getInstance().inject(this);
        mPhotoViewIndex=0;
    }
    
    private void initView()
    {
        initPhotoWidget();
        initPhotoBrowser();
        setContentView(getPhotoBrowser());
    }
    
    private void initPhotoWidget()
    {
        mBrowserNetworkErrorPage=new BrowserNetworkErrorPage(PhotoBrowserActivity.this);
        mBrowserNetworkErrorPage.setCallback(mBrowserNetworkErrorCallback);
        mBrowserLoadFailPage=new BrowserLoadFailPage(PhotoBrowserActivity.this);
        mBrowserLoadFailPage.setCallback(mBrowserLoadFailCallback);
        mBottomBar=new BottomBar(PhotoBrowserActivity.this);
        mBottomBar.setCallback(mBottomBarCallback);
    }
    
    private void initPhotoBrowser()
    {
        photoBrowserOverlay=new PhotoBrowserOverlayImpl();
        bindView(photoBrowserOverlay);
        try
        {
            mPhotoViewData=BrowserArticleItem.parse(new JSONObject(jsonContent));
        }
        catch(JSONException jsonException)
        {
            Logger.w(TAG,"initPhotoBrowser:JSONException",jsonException);
            if(NetworkUtil.isNetworkConnected())
            {
                showLoadFailedScreen();
            }
            else
            {
                showNetworkErrorScreen(View.VISIBLE);
            }
            return;
        }
        bindData(mPhotoViewData);
        setCallback(new UICallback()
        {
            @Override
            public void onPageChanged(int total,int current)
            {
                mPhotoViewIndex=current-1;
                mBottomBar.getBrowserIndicateView().updateIndicateView(current,total);
            }
            
            @Override
            public void onPhotoSelected(Drawable drawable)
            {
                Logger.d(TAG,"initPhotoBrowser:onPhotoSelected");
            }
        });
        /** 看图模式不需要显示加载界面 */
        showLoadingScreen(View.GONE);
    }
    
    private void showLoadFailedScreen()
    {
        Logger.d(TAG,"showLoadFailedScreen");
        isEmptyJson=true;
        showLoadFailedScreen(View.VISIBLE);
    }
    
    class PhotoBrowserOverlayImpl implements IPhotoBrowserOverlay
    {
        public PhotoBrowserOverlayImpl()
        {}
        
        @SuppressWarnings("ReturnOfNull")
        @Override
        public ViewGroup createBrowserLoadingPage()
        {
            return null;
        }
        
        @Override
        public ViewGroup createBrowserLoadFailPage()
        {
            return mBrowserLoadFailPage;
        }
        
        @Override
        public ViewGroup createBrowserNetworkErrorPage()
        {
            return mBrowserNetworkErrorPage;
        }
        
        @SuppressWarnings("ReturnOfNull")
        @Override
        public ViewGroup createBrowserOfflinePage()
        {
            return null;
        }
        
        @Override
        public ViewGroup createPhotoLoadingLayout()
        {
            return new PhotoLoadingPage(PhotoBrowserActivity.this);
        }
        
        @Override
        public ViewGroup createPhotoLoadFailLayout()
        {
            /** 每次请求生成一个新的布局.防止旧的的布局同时绑定两个父节点报错 */
            PhotoLoadFailPage photoLoadFailPage=new PhotoLoadFailPage(PhotoBrowserActivity.this);
            photoLoadFailPage.setCallback(mPhotoLoadFailCallback);
            return photoLoadFailPage;
        }
        
        @SuppressWarnings("ReturnOfNull")
        @Override
        public ViewGroup createPhotoNetworkErrorPage()
        {
            return null;
        }
        
        @SuppressWarnings("ReturnOfNull")
        @Override
        public ViewGroup createTopLayout()
        {
            return null;
        }
        
        @Override
        public ViewGroup createBottomLayout()
        {
            return mBottomBar;
        }
        
        @SuppressWarnings("ReturnOfNull")
        @Override
        public ViewGroup createToolbarLayout()
        {
            return null;
        }
    }
}
