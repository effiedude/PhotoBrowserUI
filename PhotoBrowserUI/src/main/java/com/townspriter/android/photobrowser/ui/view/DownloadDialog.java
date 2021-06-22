package com.townspriter.android.photobrowser.ui.view;

import com.townspriter.android.foundation.utils.ui.ResHelper;
import com.townspriter.android.photobrowser.ui.R;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @Path PhotoBrowserUI:DownloadDialog
 * @Describe 发布评论弹窗
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:43
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class DownloadDialog extends AbstractDialog
{
    public static final String TAG="DownloadDialog";
    private TextView tvConfirmButton;
    private TextView tvCancelButton;
    private View.OnClickListener mClickListener;
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        WindowManager.LayoutParams layoutParams=getDialog().getWindow().getAttributes();
        layoutParams.width=ResHelper.dpToPxI(300);
        layoutParams.height=WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity=Gravity.CENTER_HORIZONTAL;
        getDialog().getWindow().setAttributes(layoutParams);
    }
    
    @NonNull
    @Override
    public int layout()
    {
        return R.layout.browserxuixlayoutxdialogxdownload;
    }
    
    @Override
    protected void initView(View root)
    {
        super.initView(root);
        tvConfirmButton=root.findViewById(R.id.browserDownDialogConfirmButton);
        tvConfirmButton.setOnClickListener(mClickListener);
        tvCancelButton=root.findViewById(R.id.browserDownDialogCancelButton);
        tvCancelButton.setOnClickListener(mClickListener);
    }
    
    @Override
    protected void release()
    {}
    
    public void setOnClickListener(View.OnClickListener listener)
    {
        mClickListener=listener;
    }
}
