package com.townspriter.photobrowser.ui.view.present;

import org.json.JSONException;
import org.json.JSONObject;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.townspriter.android.photobrowser.core.api.bean.BrowserArticleItem;
import com.townspriter.android.photobrowser.core.api.listener.UICallback;
import com.townspriter.android.photobrowser.core.api.view.IPhotoBrowserOverlay;
import com.townspriter.photobrowser.ui.constant.RouterConstant;
import com.townspriter.photobrowser.ui.model.ArticleViewModel;
import com.townspriter.photobrowser.ui.model.HistoryModel;
import com.townspriter.photobrowser.ui.model.ToolbarViewModel;
import com.townspriter.photobrowser.ui.view.framework.widget.toolbar.IToolbar;
import com.townspriter.photobrowser.ui.view.framework.widget.toolbar.Toolbar;
import com.townspriter.photobrowser.ui.view.page.browser.BrowserLoadFailPage;
import com.townspriter.photobrowser.ui.view.page.browser.BrowserLoadingPage;
import com.townspriter.photobrowser.ui.view.page.browser.BrowserNetworkErrorPage;
import com.townspriter.photobrowser.ui.view.page.browser.BrowserOfflinePage;
import com.townspriter.photobrowser.ui.view.page.browser.IBrowserLoadFail;
import com.townspriter.photobrowser.ui.view.page.browser.IBrowserNetworkError;
import com.townspriter.photobrowser.ui.view.page.browser.IBrowserOffline;
import com.townspriter.photobrowser.ui.view.page.normal.ITopBar;
import com.townspriter.photobrowser.ui.view.page.normal.TopBar;
import com.townspriter.photobrowser.ui.view.page.photo.IPhotoInfo;
import com.townspriter.photobrowser.ui.view.page.photo.IPhotoLoadFail;
import com.townspriter.photobrowser.ui.view.page.photo.IPhotoNetworkError;
import com.townspriter.photobrowser.ui.view.page.photo.PhotoInfoPage;
import com.townspriter.photobrowser.ui.view.page.photo.PhotoLoadFailPage;
import com.townspriter.photobrowser.ui.view.page.photo.PhotoLoadingPage;
import com.townspriter.photobrowser.ui.view.page.photo.PhotoNetworkErrorPage;
import com.townspriter.base.foundation.utils.concurrent.ThreadManager;
import com.townspriter.base.foundation.utils.lang.AssertUtil;
import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.text.StringUtil;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/******************************************************************************
 * @Path PhotoBrowserUI:PhotoBrowserArticleActivity
 * @Describe ???????????????????????????????????????
 * @Name ??????
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-??????10:56
 * CopyRight(C)2021 ??????????????????????????????
 * *****************************************************************************
 */
@Route(path=RouterConstant.AROUTERxPATHxATLAS)
public class PhotoBrowserArticleActivity extends BaseActivity
{
    private static final String TAG="PhotoBrowserArticleActivity";
    /** ?????????????????????????????? */
    private final IBrowserOffline mBrowserOfflineCallback=new IBrowserOffline()
    {
        @Override
        public void onBack()
        {
            Logger.d(TAG,"onBack");
            finish();
        }
    };
    @Autowired(name=RouterConstant.AROUTERxPARAMSxJSON)
    public String jsonContent;
    @Autowired(name=RouterConstant.AROUTERxPARAMSxARTICLExID)
    public String articleId;
    public String sourceFrom;
    @Autowired(name=RouterConstant.AROUTERxPARAMSxCHANNELxID)
    public String channelId;
    private ArticleViewModel mArticleViewModel;
    private ToolbarViewModel mToolbarViewModel;
    private HistoryModel mHistoryModel;
    private BrowserArticleItem mArticleItem;
    /**
     * ?????????????????????????????????
     * ??????:??????????????????????????????????????????(?????????????????????).???????????????????????????????????????????????????????????????
     */
    private final ITopBar mTopBarCallback=new ITopBar()
    {
        @Override
        public void doBack()
        {
            Logger.d(TAG,"doBack");
            finish();
        }
        
        @Override
        public void doShare(final IToolbar.ShareType shareType)
        {
            mToolbarViewModel.doShare(PhotoBrowserArticleActivity.this,PhotoBrowserArticleActivity.this,mArticleItem,getSupportFragmentManager(),mArticleItem.shareEnable?IToolbar.ShareType.ALL:shareType);
            /** ???????????? */
            switch(shareType)
            {
                case OPERATION:
                case ALL:
                case SHARE:
                    break;
            }
        }
    };
    private final IToolbar mToolbarCallback=new IToolbar()
    {
        @Override
        public void doComment()
        {
            mToolbarViewModel.doComment(PhotoBrowserArticleActivity.this,getSupportFragmentManager(),mArticleItem.id,mArticleItem.getCategoryId());
        }
        
        @Override
        public void showComment()
        {
            mToolbarViewModel.showComment(PhotoBrowserArticleActivity.this,getSupportFragmentManager(),mArticleItem.id,mArticleItem.getCategoryId());
        }
        
        @Override
        public void doFavorite(final FavoriteType favoriteType,final boolean favorite)
        {
            Logger.d(TAG,"doFavorite["+favoriteType+"]:"+favorite);
            mToolbarViewModel.doFavorite(favorite,mArticleItem);
            /** ???????????? */
            switch(favoriteType)
            {
                case TOP:
                case BOTTOM:
                    break;
            }
        }
        
        @Override
        public void doShare(final ShareType shareType)
        {
            mToolbarViewModel.doShare(PhotoBrowserArticleActivity.this,PhotoBrowserArticleActivity.this,mArticleItem,getSupportFragmentManager(),shareType);
            /** ???????????? */
            switch(shareType)
            {
                case OPERATION:
                case ALL:
                case SHARE:
                    break;
            }
        }
        
        @Override
        public void doSave()
        {
            Logger.d(TAG,"doSave");
        }
    };
    private int mArticleItemIndex;
    private PhotoBrowserOverlayImpl photoBrowserOverlay;
    private TopBar mTopBar;
    private Toolbar mToolbar;
    private final IPhotoInfo mPhotoInfoCallback=new IPhotoInfo()
    {
        @Override
        public void onBackgroundChanged(final boolean alpha)
        {
            mToolbar.changeBackgroundAlpha(alpha);
        }
    };
    private PhotoInfoPage mPhotoInfoPage;
    private BrowserLoadingPage mBrowserLoadingPage;
    private BrowserLoadFailPage mBrowserLoadFailPage;
    private BrowserNetworkErrorPage mBrowserNetworkErrorPage;
    private BrowserOfflinePage mBrowserOfflinePage;
    /** ????????????????????????????????? */
    private boolean isImmerseStyle;
    /** ???????????????????????? */
    private boolean isLoadError;
    private final IPhotoLoadFail mPhotoLoadFailCallback=new IPhotoLoadFail()
    {
        @Override
        public void onReloadPhoto()
        {
            Logger.d(TAG,"onReloadPhoto");
            if(!showReloadPage())
            {
                reloadPhoto();
            }
        }
        
        @Override
        public void onTap()
        {
            Logger.d(TAG,"onTap");
        }
    };
    private final IPhotoNetworkError mPhotoNetworkErrorCallback=new IPhotoNetworkError()
    {
        @Override
        public void onReloadPhoto()
        {
            Logger.d(TAG,"onReloadPhoto");
            if(!showReloadPage())
            {
                reloadPhoto();
            }
        }
        
        @Override
        public void onTap()
        {
            Logger.d(TAG,"onTap");
        }
    };
    private final IBrowserLoadFail mBrowserLoadFailCallback=new IBrowserLoadFail()
    {
        @Override
        public void onReloadPhoto()
        {
            Logger.d(TAG,"onReloadPhoto");
            showReloadPage();
        }
        
        @Override
        public void onTap()
        {
            Logger.d(TAG,"onTap");
            changeStyle();
        }
        
        @Override
        public void onBack()
        {
            Logger.d(TAG,"onBack");
            finish();
        }
    };
    private final IBrowserNetworkError mBrowserNetworkErrorCallback=new IBrowserNetworkError()
    {
        @Override
        public void onReloadPhoto()
        {
            Logger.d(TAG,"onReloadPhoto");
            showReloadPage();
        }
        
        @Override
        public void onTap()
        {
            Logger.d(TAG,"onTap");
        }
        
        @Override
        public void onBack()
        {
            Logger.d(TAG,"onBack");
            finish();
        }
    };
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }
    
    @Override
    public void onPhotoTap(final View view,final float x,final float y)
    {
        super.onPhotoTap(view,x,y);
        changeStyle();
    }
    
    @Override
    public void onViewTap(final View view,final float x,final float y)
    {
        super.onViewTap(view,x,y);
        changeStyle();
    }
    
    /**************************************** ???????????? ****************************************/
    private void initData()
    {
        ARouter.getInstance().inject(this);
        mHistoryModel=new HistoryModel();
        mToolbarViewModel=new ViewModelProvider(this).get(ToolbarViewModel.class);
        if(null!=mToolbarViewModel.getFavoriteActionPlugin())
        {
            mToolbarViewModel.getFavoriteActionPlugin().setCallback(mToolbarCallback);
        }
        mArticleViewModel=new ViewModelProvider(this).get(ArticleViewModel.class);
        mToolbarViewModel.getFavoriteLiveData().observe(this,new Observer<Boolean>()
        {
            @Override
            public void onChanged(final Boolean favorite)
            {
                if(null!=mToolbar)
                {
                    ThreadManager.post(ThreadManager.THREADxUI,new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mToolbar.setFavorite(favorite);
                        }
                    });
                }
            }
        });
        mToolbarViewModel.getFavoriteLiveDataThrow().observe(this,new Observer<Throwable>()
        {
            @Override
            public void onChanged(final Throwable throwable)
            {
                Logger.w(TAG,"getFavoriteLiveDataThrow:Throwable",throwable);
            }
        });
        if(null!=mToolbarViewModel.getFavoriteActionPlugin())
        {
            mToolbarViewModel.getFavoriteActionPlugin().setCallback(mToolbarCallback);
        }
        mToolbarViewModel.getCommentCount(articleId);
        mToolbarViewModel.getCommentLiveData().observe(this,new Observer<Long>()
        {
            @Override
            public void onChanged(final Long count)
            {
                Logger.d(TAG,"initData:getCommentLiveData-count:"+count);
                AssertUtil.mustInUiThread();
            }
        });
        mToolbarViewModel.getCommentLiveDataThrow().observe(this,new Observer<Throwable>()
        {
            @Override
            public void onChanged(final Throwable throwable)
            {
                Logger.w(TAG,"initData:getCommentLiveDataThrow:Throwable",throwable);
            }
        });
        mArticleViewModel=new ViewModelProvider(this).get(ArticleViewModel.class);
        mArticleViewModel.getArticleLiveData().observe(this,new Observer<String>()
        {
            @Override
            public void onChanged(final String data)
            {
                BrowserArticleItem articleItem;
                try
                {
                    articleItem=BrowserArticleItem.parse(new JSONObject(data));
                }
                catch(JSONException jsonException)
                {
                    showLoadFailedPage();
                    return;
                }
                mArticleItem=articleItem;
                bindData(articleItem);
                updatePhotoWidget(articleItem,1);
                mToolbarViewModel.syncFavorite(articleItem.id);
                /** ?????????????????? */
                mHistoryModel.addHistory();
            }
        });
        mArticleViewModel.getArticleLiveDataThrow().observe(this,new Observer<Throwable>()
        {
            @Override
            public void onChanged(final Throwable throwable)
            {
                showLoadFailedPage();
            }
        });
        mArticleItemIndex=0;
        if(StringUtil.isEmptyWithNull(articleId))
        {
            showLoadFailedPage();
            return;
        }
        /** ?????????????????????????????????(??????:????????????????????????????????????????????????.???????????????????????????getArticle????????????????????????) */
        mToolbarViewModel.getCommentCount(articleId);
        mArticleViewModel.getArticle(articleId,channelId);
    }
    
    private void initView()
    {
        initPhotoWidget();
        initPhotoBrowser();
    }
    
    private void initPhotoWidget()
    {
        mTopBar=new TopBar(this);
        mTopBar.setCallback(mTopBarCallback);
        mToolbar=new Toolbar(this);
        mToolbar.setCallback(mToolbarCallback);
        mPhotoInfoPage=new PhotoInfoPage(this);
        mPhotoInfoPage.setCallback(mPhotoInfoCallback);
        mBrowserLoadingPage=new BrowserLoadingPage(PhotoBrowserArticleActivity.this);
        mBrowserLoadFailPage=new BrowserLoadFailPage(PhotoBrowserArticleActivity.this);
        mBrowserLoadFailPage.setCallback(mBrowserLoadFailCallback);
        mBrowserNetworkErrorPage=new BrowserNetworkErrorPage(PhotoBrowserArticleActivity.this);
        mBrowserNetworkErrorPage.setCallback(mBrowserNetworkErrorCallback);
        mBrowserOfflinePage=new BrowserOfflinePage(PhotoBrowserArticleActivity.this);
        mBrowserOfflinePage.setCallback(mBrowserOfflineCallback);
    }
    
    private void initPhotoBrowser()
    {
        photoBrowserOverlay=new PhotoBrowserOverlayImpl();
        bindView(photoBrowserOverlay,new UICallback()
        {
            @Override
            public void onPageChanged(int total,int current)
            {
                mArticleItemIndex=current-1;
                updatePhotoWidget(mArticleItem,current);
            }
            
            @Override
            public void onPhotoSelected(Drawable drawable)
            {
                Logger.d(TAG,"initPhotoBrowser:onPhotoSelected");
                if(null!=mToolbar)
                {
                    if(null!=drawable)
                    {
                        mToolbar.setSaveButtonEnable(true);
                    }
                }
            }
        });
        setContentView(getPhotoBrowser());
    }
    
    private void updatePhotoWidget(BrowserArticleItem articleItem,int photoIndex)
    {
        if(null!=articleItem)
        {
            updateTopLayout(articleItem);
            updatePhotoBrowserInfoView(articleItem,photoIndex);
            updateToolbarLayout(articleItem,photoIndex);
        }
        else
        {
            Logger.w(TAG,"updatePhotoWidget-mArticleItem:NULL");
        }
    }
    
    private void updateTopLayout(BrowserArticleItem articleItem)
    {
        if(null!=mTopBar)
        {
            mTopBar.setTitle(articleItem.getSource());
            // ?????????????????????????????????
            mTopBar.getTitle().setVisibility(View.GONE);
            // ??????????????????????????????????????????????????????????????????
            if(!articleItem.isShareEnable()&&!articleItem.isFavoriteEnable())
            {
                mTopBar.setMoreLayoutVisible(View.GONE);
            }
        }
        else
        {
            Logger.w(TAG,"updateTopLayout-mTopLayout:NULL");
        }
    }
    
    private void updatePhotoBrowserInfoView(@NonNull BrowserArticleItem articleItem,int photoIndex)
    {
        if(null!=mPhotoInfoPage)
        {
            mPhotoInfoPage.setContent(articleItem,photoIndex);
        }
        else
        {
            Logger.w(TAG,"updatePhotoBrowserInfoView-mPhotoBrowserInfoView:NULL");
        }
    }
    
    private void updateToolbarLayout(BrowserArticleItem articleItem,int photoIndex)
    {
        mToolbarViewModel.setFavoriteEnable(articleItem.isFavoriteEnable());
        if(null!=mToolbar)
        {
            mToolbar.updateIndicate(photoIndex,articleItem.browserImages.size());
            /** ?????????????????????????????????(??????:????????????????????????????????????????????????.?????????????????????????????????????????????????????????????????????) */
            mToolbar.setCommentEnable(articleItem.isCommentEnable());
            mToolbar.setFavoriteEnable(articleItem.isFavoriteEnable());
            mToolbar.setShareEnable(articleItem.isShareEnable());
        }
        else
        {
            Logger.w(TAG,"updateToolbarLayout-mToolbarLayout:NULL");
        }
    }
    
    private void changeStyle()
    {
        isImmerseStyle=!isImmerseStyle;
        if(null!=mPhotoInfoPage)
        {
            mPhotoInfoPage.changeStyle(isImmerseStyle);
        }
        else
        {
            Logger.w(TAG,"changeStyle-mPhotoBrowserInfoView:NULL");
        }
        if(null!=mToolbar)
        {
            mToolbar.changeStyle(isImmerseStyle);
        }
        else
        {
            Logger.w(TAG,"changeStyle-mToolbarLayout:NULL");
        }
    }
    
    private void showLoadFailedPage()
    {
        Logger.d(TAG,"showLoadFailedPage");
        isLoadError=true;
        showLoadingScreen(View.GONE);
        showNetworkErrorScreen(View.GONE);
        showOfflineScreen(View.GONE);
        showLoadFailedScreen(View.VISIBLE);
    }
    
    private void showNetworkErrorPage()
    {
        Logger.d(TAG,"showNetworkErrorPage");
        isLoadError=true;
        showLoadingScreen(View.GONE);
        showLoadFailedScreen(View.GONE);
        showOfflineScreen(View.GONE);
        showNetworkErrorScreen(View.VISIBLE);
    }
    
    private boolean showReloadPage()
    {
        Logger.d(TAG,"showReloadPage");
        if(isLoadError)
        {
            isLoadError=false;
            showLoadingScreen(View.VISIBLE);
            showLoadFailedScreen(View.GONE);
            showNetworkErrorScreen(View.GONE);
            mArticleViewModel.getArticle(articleId,channelId);
            // ?????????????????????????????????(??????:????????????????????????????????????????????????.???????????????????????????getArticle????????????????????????)
            mToolbarViewModel.getCommentCount(articleId);
            return true;
        }
        return false;
    }
    
    private void showBrowserOfflinePage()
    {
        Logger.d(TAG,"showBrowserOfflinePage");
        showLoadingScreen(View.GONE);
        showLoadFailedScreen(View.GONE);
        showNetworkErrorScreen(View.GONE);
        showOfflineScreen(View.VISIBLE);
    }
    
    class PhotoBrowserOverlayImpl implements IPhotoBrowserOverlay
    {
        public PhotoBrowserOverlayImpl()
        {}
        
        @Override
        public ViewGroup createBrowserLoadingPage()
        {
            return mBrowserLoadingPage;
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
        
        @Override
        public ViewGroup createBrowserOfflinePage()
        {
            return mBrowserOfflinePage;
        }
        
        @Override
        public ViewGroup createPhotoLoadingLayout()
        {
            return new PhotoLoadingPage(PhotoBrowserArticleActivity.this);
        }
        
        @Override
        public ViewGroup createPhotoNetworkErrorPage()
        {
            PhotoNetworkErrorPage photoNetworkErrorPage=new PhotoNetworkErrorPage(PhotoBrowserArticleActivity.this);
            photoNetworkErrorPage.setCallback(mPhotoNetworkErrorCallback);
            return photoNetworkErrorPage;
        }
        
        @Override
        public ViewGroup createPhotoLoadFailLayout()
        {
            /** ????????????????????????????????????.?????????????????????????????????????????????????????? */
            PhotoLoadFailPage photoLoadFailPage=new PhotoLoadFailPage(PhotoBrowserArticleActivity.this);
            photoLoadFailPage.setCallback(mPhotoLoadFailCallback);
            return photoLoadFailPage;
        }
        
        @Override
        public ViewGroup createTopLayout()
        {
            return mTopBar;
        }
        
        @Override
        public ViewGroup createBottomLayout()
        {
            return mPhotoInfoPage;
        }
        
        @Override
        public ViewGroup createToolbarLayout()
        {
            return mToolbar.getToolbarLayout();
        }
    }
}
