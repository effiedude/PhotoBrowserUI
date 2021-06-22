package com.townspriter.android.photobrowser.ui.view;

import com.townspriter.android.foundation.utils.log.Logger;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/******************************************************************************
 * @Path SuperScholar:AbstractDialog
 * @Describe 系统弹窗基类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-12-下午5:54
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public abstract class AbstractDialog extends DialogFragment
{
    private static final String TAG="AbstractDialog";
    
    protected abstract @NonNull @LayoutRes int layout();
    
    protected abstract void release();
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        /** 设置系统弹窗横向铺满 */
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
    {
        if(layout()==0)
        {
            return super.onCreateView(inflater,container,savedInstanceState);
        }
        return inflater.inflate(layout(),container,false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        initView(view);
    }
    
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        Logger.d(TAG,"onDestroyView");
        release();
    }
    
    protected void initView(View root)
    {
        /** 点击屏幕不消失 */
        getDialog().setCanceledOnTouchOutside(false);
    }
}
