package com.townspriter.android.photobrowser.ui.view.framework.widget.toolbar;

import com.townspriter.android.foundation.Foundation;
import com.townspriter.android.foundation.utils.log.Logger;
import com.townspriter.android.foundation.utils.ui.ResHelper;
import com.townspriter.android.photobrowser.ui.R;
import com.townspriter.android.photobrowser.ui.view.framework.widget.BrowserIndicateView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/******************************************************************************
 * @Path PhotoBrowserUI:Toolbar
 * @Describe 工具栏布局(评论/收藏/分享)
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class Toolbar
{
    private static final String TAG="ToolbarLayout";
    private RelativeLayout mToolbarLayout;
    /**
     * isFavorite
     * 收藏功能属于本地功能
     */
    private boolean isFavorite;
    private IToolbar mCallback;
    /** 承载工具栏图标.为了区分管理图片索引展示控件 */
    private LinearLayout mRealToolbarLayout;
    /** 进入清爽模式之后显示工具栏的图片索引控件和下载按钮 */
    private BrowserIndicateView mBrowserIndicateView;
    /** 保存图片按钮 */
    private ImageView mSaveButton;
    private RelativeLayout mNormalBottomLayout;
    /**
     * 评论输入框布局
     * 如果文章不可以评论需要隐藏此控件.评论模块有是否可以评论的监听接口
     * 点击此控件会启动评论模块
     */
    private LinearLayout mToolbarCommentEditLayout;
    private View mToolbarCommentLayout;
    /**
     * 评论按钮
     * 如果文章不可以评论需要隐藏此控件.评论模块有是否可以评论的监听接口
     */
    private ImageView mToolbarCommentIcon;
    /**
     * 评论数量
     * 如果没有评论则不会显示此控件
     */
    private TextView mToolbarCommentNumber;
    /**
     * 收藏按钮
     * 如果文章不可以收藏需要隐藏此控件
     */
    private ImageView mToolbarFavoriteIcon;
    /**
     * 分享按钮
     * 如果文章不可以分享需要隐藏此控件
     */
    private ImageView mToolbarShareIcon;
    
    public Toolbar(final Context context)
    {
        initView();
    }
    
    public void setCallback(IToolbar callback)
    {
        mCallback=callback;
    }
    
    public void updateIndicate(int current,int total)
    {
        setSaveButtonEnable(false);
        if(null==mBrowserIndicateView)
        {
            Logger.w(TAG,"updateIndicate-mIndicateView:NULL");
            return;
        }
        mBrowserIndicateView.updateIndicateView(current,total);
    }
    
    public void setSaveButtonEnable(boolean enable)
    {
        Logger.d(TAG,"setSaveButtonEnable:"+enable);
        if(null==mSaveButton)
        {
            Logger.w(TAG,"setSaveButtonEnable-mSaveButton:NULL");
            return;
        }
        if(enable)
        {
            mSaveButton.setVisibility(View.VISIBLE);
        }
        else
        {
            mSaveButton.setVisibility(View.GONE);
        }
    }
    
    public void setCommentEnable(boolean enable)
    {
        Logger.d(TAG,"setCommentEnable:"+enable);
        if(null==mToolbarCommentEditLayout)
        {
            Logger.w(TAG,"setCommentEnable-mToolbarCommentEditLayout:NULL");
            return;
        }
        if(enable)
        {
            mToolbarCommentEditLayout.setVisibility(View.VISIBLE);
            mToolbarCommentLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            mToolbarCommentEditLayout.setVisibility(View.INVISIBLE);
            mToolbarCommentLayout.setVisibility(View.INVISIBLE);
        }
    }
    
    public void setFavoriteEnable(boolean enable)
    {
        Logger.d(TAG,"setFavoriteEnable:"+enable);
        if(null==mToolbarFavoriteIcon)
        {
            Logger.w(TAG,"setFavoriteEnable-mToolbarFavoriteIcon:NULL");
            return;
        }
        if(enable)
        {
            mToolbarFavoriteIcon.setVisibility(View.VISIBLE);
        }
        else
        {
            mToolbarFavoriteIcon.setVisibility(View.GONE);
        }
    }
    
    public void setShareEnable(boolean enable)
    {
        Logger.d(TAG,"setShareEnable:"+enable);
        if(null==mToolbarShareIcon)
        {
            Logger.w(TAG,"setShareEnable-mToolbarShareIcon:NULL");
            return;
        }
        if(enable)
        {
            mToolbarShareIcon.setVisibility(View.VISIBLE);
        }
        else
        {
            mToolbarShareIcon.setVisibility(View.GONE);
        }
    }
    
    public void setCommentCount(long count)
    {
        Logger.d(TAG,"setCommentCount:"+count);
        if(null==mToolbarCommentNumber)
        {
            Logger.w(TAG,"setCommentCount-mToolbarCommentNumber:NULL");
            return;
        }
        if(count>0)
        {
            // mToolbarCommentIcon.setImageDrawable(DrawableUtils.getDyeDrawable(R.drawable.iconxcomment,ResHelper.getColor(R.color.white)));
            // mToolbarCommentNumber.setText(CommentCountUtil.convertTenThousand(count));
            mToolbarCommentNumber.setVisibility(View.VISIBLE);
        }
        else
        {
            // mToolbarCommentIcon.setImageDrawable(DrawableUtils.getDyeDrawable(R.drawable.iconxcomment,ResHelper.getColor(R.color.white)));
            mToolbarCommentNumber.setVisibility(View.GONE);
        }
        updateCommentIcon(count);
    }
    
    private void updateCommentIcon(long count)
    {
        if(count>0)
        {
            // mToolbarCommentIcon.setImageResource(R.drawable.iconxcomment);
        }
        else
        {
            // mToolbarCommentIcon.setImageResource(R.drawable.iconxcomment);
        }
    }
    
    public void setFavorite(boolean isFavorite)
    {
        this.isFavorite=isFavorite;
        if(null!=mToolbarFavoriteIcon)
        {
            Logger.d(TAG,"setFavorite-isFavorite:"+isFavorite);
            if(isFavorite)
            {
                // mToolbarFavoriteIcon.setImageDrawable(DrawableUtils.getDyeDrawable(R.drawable.iconxfavoritexfill,ResHelper.getColor(R.color.yellow)));
            }
            else
            {
                // mToolbarFavoriteIcon.setImageDrawable(DrawableUtils.getDyeDrawable(R.drawable.iconxfavorite,ResHelper.getColor(R.color.white)));
            }
        }
        else
        {
            Logger.w(TAG,"setFavorite-mToolbarFavoriteIcon:NULL");
        }
    }
    
    public void changeStyle(boolean isImmerseStyle)
    {
        if(isImmerseStyle)
        {
            mNormalBottomLayout.setVisibility(View.VISIBLE);
            mRealToolbarLayout.setVisibility(View.GONE);
        }
        else
        {
            mNormalBottomLayout.setVisibility(View.GONE);
            mRealToolbarLayout.setVisibility(View.VISIBLE);
        }
    }
    
    public RelativeLayout getToolbarLayout()
    {
        return mToolbarLayout;
    }
    
    public void changeBackgroundAlpha(boolean alpha)
    {
        if(alpha)
        {
            mRealToolbarLayout.setBackgroundColor(ResHelper.getColor(R.integer.valuexintegerxalphax75,R.color.valuexcolorxblack));
        }
        else
        {
            mRealToolbarLayout.setBackgroundColor(ResHelper.getColor(R.integer.valuexintegerxalphax60,R.color.valuexcolorxblack));
        }
    }
    
    private void initView()
    {
        LayoutInflater inflater=(LayoutInflater)Foundation.getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(null==inflater)
        {
            Logger.w(TAG,"initView-inflater:NULL");
            return;
        }
        mToolbarLayout=(RelativeLayout)inflater.inflate(R.layout.browserxuixlayoutxtoolbar,null);
        mBrowserIndicateView=mToolbarLayout.findViewById(R.id.indicateView);
        mNormalBottomLayout=mToolbarLayout.findViewById(R.id.normalBottomLayout);
        mNormalBottomLayout.setVisibility(View.GONE);
        mSaveButton=mToolbarLayout.findViewById(R.id.saveButton);
        mSaveButton.setVisibility(View.GONE);
        mSaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Logger.d(TAG,"initView-mSavePhoto:onClick");
                if(null!=mCallback)
                {
                    mCallback.doSave();
                }
            }
        });
        mRealToolbarLayout=mToolbarLayout.findViewById(R.id.realToolbarLayout);
        changeBackgroundAlpha(false);
        mToolbarCommentEditLayout=mToolbarLayout.findViewById(R.id.toolbarCommentEditLayout);
        mToolbarCommentEditLayout.setVisibility(View.INVISIBLE);
        mToolbarCommentEditLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                Logger.d(TAG,"initView:onClick-mToolbarCommentEditLayout");
                if(null!=mCallback)
                {
                    mCallback.doComment();
                }
            }
        });
        mToolbarCommentLayout=mToolbarLayout.findViewById(R.id.toolbarCommentLayout);
        mToolbarCommentLayout.setVisibility(View.GONE);
        mToolbarCommentIcon=mToolbarLayout.findViewById(R.id.browserxToolbarxCommentxIcon);
        mToolbarCommentIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                Logger.d(TAG,"initView:onClick-mToolbarCommentIcon");
                if(null!=mCallback)
                {
                    mCallback.showComment();
                }
            }
        });
        mToolbarCommentNumber=mToolbarLayout.findViewById(R.id.toolbarCommentNumber);
        mToolbarFavoriteIcon=mToolbarLayout.findViewById(R.id.toolbarFavoriteIcon);
        mToolbarFavoriteIcon.setVisibility(View.GONE);
        mToolbarFavoriteIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                Logger.d(TAG,"initView:onClick-mToolbarFavoriteIcon");
                if(null!=mCallback)
                {
                    mCallback.doFavorite(IToolbar.FavoriteType.BOTTOM,!isFavorite);
                }
            }
        });
        mToolbarShareIcon=mToolbarLayout.findViewById(R.id.toolbarShareIcon);
        mToolbarShareIcon.setVisibility(View.GONE);
        mToolbarShareIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                Logger.d(TAG,"initView:onClick-mToolbarShareIcon");
                if(null!=mCallback)
                {
                    mCallback.doShare(IToolbar.ShareType.SHARE);
                }
            }
        });
    }
}
