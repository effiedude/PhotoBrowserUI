package com.townspriter.android.photobrowser.ui.view.present;

import com.townspriter.android.foundation.utils.log.Logger;
import com.townspriter.android.foundation.utils.toast.ToastManager;
import com.townspriter.android.foundation.utils.ui.ResHelper;
import com.townspriter.android.photobrowser.core.api.PhotoBrowser;
import com.townspriter.android.photobrowser.core.api.bean.BrowserArticleItem;
import com.townspriter.android.photobrowser.core.api.listener.OnGestureListener;
import com.townspriter.android.photobrowser.core.api.listener.OnPhotoTapListener;
import com.townspriter.android.photobrowser.core.api.listener.OnViewTapListener;
import com.townspriter.android.photobrowser.core.api.listener.UICallback;
import com.townspriter.android.photobrowser.core.api.view.IPhotoBrowserOverlay;
import com.townspriter.android.photobrowser.core.model.listener.OnScrollListener;
import com.townspriter.android.photobrowser.ui.R;
import com.townspriter.android.photobrowser.ui.model.PhotoSaveModel;
import com.townspriter.android.photobrowser.ui.model.callback.PhotoSaveCallBack;
import com.townspriter.android.photobrowser.ui.view.DownloadDialog;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/******************************************************************************
 * @Path PhotoBrowserUI:BaseActivity
 * @Describe 抽离图片浏览器基础模块公用
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public abstract class BaseActivity extends FragmentActivity
{
    private static final String TAG="BaseActivity";
    /** 监听图片保存状态 */
    private final PhotoSaveCallBack mPhotoSaveCallBack=new PhotoSaveCallBack()
    {
        /** 保存成功 */
        @Override
        public void savePhotoSuccess()
        {
            ToastManager.getInstance().showToast(ResHelper.getString(R.string.browserUISavePicSucceed),ToastManager.LENGTHxSHORT);
        }
        
        /** 保持失败 */
        @Override
        public void savePhotoFailure()
        {
            ToastManager.getInstance().showToast(ResHelper.getString(R.string.browserUISavePicFailed),ToastManager.LENGTHxSHORT);
        }
    };
    /** 监听图片轻点事件 */
    private final OnPhotoTapListener mPhotoTapListener=new OnPhotoTapListener()
    {
        @Override
        public void onPhotoTap(final View view,final float x,final float y)
        {
            Logger.d(TAG,"onPhotoTap");
            BaseActivity.this.onPhotoTap(view,x,y);
        }
    };
    /** 监听图片承载控件轻点事件 */
    private final OnViewTapListener mViewTapListener=new OnViewTapListener()
    {
        @Override
        public void onViewTap(final View view,final float x,final float y)
        {
            Logger.d(TAG,"onViewTap");
            BaseActivity.this.onViewTap(view,x,y);
        }
    };
    /** 监听图片手势事件 */
    private final OnGestureListener mGestureListener=new OnGestureListener()
    {
        @Override
        public void onDrag(final float movedDistanceY,final float dx,final float dy)
        {}
        
        @Override
        public void onDragRelease(final boolean willExit,final float dragDistanceY,final float dragDistanceX)
        {}
        
        @Override
        public void onFling(final float startX,final float startY,final float velocityX,final float velocityY)
        {}
        
        @Override
        public void onScale(final float scaleFactor,final float focusX,final float focusY)
        {}
        
        @Override
        public int getBackgroundAlphaByGesture()
        {
            return 255;
        }
        
        @Override
        public void onBackgroundAlphaChangingByGesture(final int alphaValue)
        {}
        
        @Override
        public void onExit()
        {
            Logger.d(TAG,"onExit");
            finish();
        }
    };
    /** 保存图片窗口 */
    private DownloadDialog mDownloadDialog;
    /** 监听图片长按事件 */
    private final View.OnLongClickListener mLongClickListener=new View.OnLongClickListener()
    {
        @Override
        public boolean onLongClick(final View view)
        {
            Logger.d(TAG,"onLongClick");
            if(!isFinishing()&&!isDestroyed())
            {
                mDownloadDialog.show(getSupportFragmentManager(),DownloadDialog.TAG);
            }
            return false;
        }
    };
    private PhotoSaveModel mPhotoSaveModel;
    private PhotoBrowser mPhotoBrowser;
    /** 监听图片保存对话框点击事件 */
    private final View.OnClickListener mDownDialogClickListener=new View.OnClickListener()
    {
        @Override
        public void onClick(final View view)
        {
            int id=view.getId();
            if(id==R.id.browserDownDialogCancelButton)
            {
                mDownloadDialog.dismiss();
            }
            else if(id==R.id.browserDownDialogConfirmButton)
            {
                mDownloadDialog.dismiss();
                savePicture(mPhotoBrowser.getCurrentPhotoUrl());
            }
        }
    };
    /** 监听图片滑动.动态修改背景颜色 */
    private final OnScrollListener mScrollListener=new OnScrollListener()
    {
        @Override
        public void onFlingUp(float velocityX,float velocityY)
        {}
        
        @Override
        public void onFlingDown(float velocityX,float velocityY)
        {}
        
        @Override
        public int getBackgroundAlphaByScroll()
        {
            return 255;
        }
        
        @Override
        public void onBackgroundAlphaChangingByScroll(int alpha)
        {
            mPhotoBrowser.setAlpha(alpha/255.0f);
        }
        
        @Override
        public void onScrollExit()
        {}
    };
    
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        Logger.d(TAG,"onWindowFocusChanged:"+hasFocus);
        if(hasFocus)
        {
            hideSystemUI();
        }
    }
    
    /**
     * onPhotoTap
     * 监听图片轻点事件.选择性重写此方法
     *
     * @param view
     * 图片
     * @param x
     * 轻点位置
     * @param y
     * 轻点位置
     */
    public void onPhotoTap(final View view,final float x,final float y)
    {}
    
    /**
     * onViewTap
     * 监听图片承载控件轻点事件.选择性重写此方法
     *
     * @param view
     * 图片
     * @param x
     * 轻点位置
     * @param y
     * 轻点位置
     */
    public void onViewTap(final View view,final float x,final float y)
    {}
    
    protected void bindData(@Nullable BrowserArticleItem photoViewData)
    {
        mPhotoBrowser.bindData(photoViewData);
    }
    
    protected void bindView(IPhotoBrowserOverlay overlay,UICallback callback)
    {
        mPhotoBrowser.bindView(overlay,callback);
    }
    
    protected void bindView(IPhotoBrowserOverlay overlay)
    {
        mPhotoBrowser.bindView(overlay);
    }
    
    protected void setCallback(UICallback callback)
    {
        mPhotoBrowser.setCallback(callback);
    }
    
    protected PhotoBrowser getPhotoBrowser()
    {
        return mPhotoBrowser;
    }
    
    protected void showLoadingScreen(int visibility)
    {
        mPhotoBrowser.showLoadingView(visibility);
    }
    
    protected void showLoadFailedScreen(int visibility)
    {
        mPhotoBrowser.showLoadFailedView(visibility);
    }
    
    protected void showOfflineScreen(int visibility)
    {
        mPhotoBrowser.showOfflinePage(visibility);
    }
    
    protected void showNetworkErrorScreen(int visibility)
    {
        mPhotoBrowser.showNetworkErrorPage(visibility);
    }
    
    protected void savePicture(@NonNull final String imageUrl)
    {
        mPhotoSaveModel.saveImageToGallery(imageUrl,mPhotoSaveCallBack);
    }
    
    protected void reloadPhoto()
    {
        mPhotoBrowser.reloadPhoto();
    }
    
    private void initData()
    {
        adaptTranslucentTheme();
        mPhotoSaveModel=new PhotoSaveModel();
    }
    
    private void initView()
    {
        mPhotoBrowser=new PhotoBrowser(this);
        /** 监听图片长按 */
        mPhotoBrowser.setLongClickListener(mLongClickListener);
        /** 监听图片轻点 */
        mPhotoBrowser.setPhotoTapListener(mPhotoTapListener);
        /** 监听图片控件轻点 */
        mPhotoBrowser.setViewTapListener(mViewTapListener);
        /** 监听手势 */
        mPhotoBrowser.setGestureListener(mGestureListener);
        mPhotoBrowser.setScrollListener(mScrollListener);
        mDownloadDialog=new DownloadDialog();
        mDownloadDialog.setOnClickListener(mDownDialogClickListener);
    }
    
    /**
     * adaptTranslucentTheme
     * Android8.0中Activity实现透明采用动态的设置方向
     */
    @SuppressLint("SourceLockedOrientationActivity")
    private void adaptTranslucentTheme()
    {
        if(android.os.Build.VERSION.SDK_INT!=Build.VERSION_CODES.O)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
    
    private void hideSystemUI()
    {
        View decorView=getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE|View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
