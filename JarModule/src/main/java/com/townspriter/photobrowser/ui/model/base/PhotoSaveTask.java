package com.townspriter.photobrowser.ui.model.base;

import java.io.File;
import java.io.IOException;
import com.townspriter.photobrowser.ui.model.callback.PhotoSaveCallBack;
import com.townspriter.base.foundation.Foundation;
import com.townspriter.base.foundation.utils.concurrent.ThreadManager;
import com.townspriter.base.foundation.utils.io.FileUtils;
import com.townspriter.base.foundation.utils.io.MD5Utils;
import com.townspriter.base.foundation.utils.lang.AssertUtil;
import com.townspriter.base.foundation.utils.log.Logger;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/******************************************************************************
 * @Path PhotoBrowserUI:PhotoSaveTask
 * @Describe 工具栏数据交互接口
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class PhotoSaveTask implements Runnable
{
    private static final String TAG="PhotoSaveTask";
    private final String galleryPath;
    private final String extension;
    private final String mimeType;
    private final String imageUrl;
    private final PhotoSaveCallBack saveCallBack;
    
    public PhotoSaveTask(String galleryPath,String extension,String mimeType,String imageUrl,PhotoSaveCallBack saveCallBack)
    {
        this.galleryPath=galleryPath;
        this.extension=extension;
        this.mimeType=mimeType;
        this.imageUrl=imageUrl;
        this.saveCallBack=saveCallBack;
    }
    
    @Override
    public void run()
    {
        AssertUtil.mustInNonUiThread();
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(imageUrl).build();
        Call call=client.newCall(request);
        try
        {
            Response response=call.execute();
            Logger.d(TAG,"run-response.isSuccessful():"+response.isSuccessful());
            if(response.isSuccessful())
            {
                ResponseBody responseBody=response.body();
                if(null!=responseBody)
                {
                    Logger.d(TAG,"run-responseBody.contentLength():"+responseBody.contentLength());
                    if(responseBody.contentLength()>0)
                    {
                        byte[] bytes=responseBody.bytes();
                        save(extension,mimeType,imageUrl,bytes,saveCallBack);
                    }
                    else
                    {
                        ThreadManager.post(ThreadManager.THREADxUI,new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                AssertUtil.mustInUiThread();
                                /** 保存失败回调 */
                                if(null!=saveCallBack)
                                {
                                    saveCallBack.savePhotoFailure();
                                }
                            }
                        });
                    }
                }
                else
                {
                    Logger.w(TAG,"run-responseBody:NULL");
                    ThreadManager.post(ThreadManager.THREADxUI,new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            AssertUtil.mustInUiThread();
                            /** 保存失败回调 */
                            if(null!=saveCallBack)
                            {
                                saveCallBack.savePhotoFailure();
                            }
                        }
                    });
                }
            }
            else
            {
                ThreadManager.post(ThreadManager.THREADxUI,new Runnable()
                {
                    @Override
                    public void run()
                    {
                        AssertUtil.mustInUiThread();
                        /** 保存失败回调 */
                        if(null!=saveCallBack)
                        {
                            saveCallBack.savePhotoFailure();
                        }
                    }
                });
            }
        }
        catch(IOException ioException)
        {
            Logger.w(TAG,"run:IOException",ioException);
            ThreadManager.post(ThreadManager.THREADxUI,new Runnable()
            {
                @Override
                public void run()
                {
                    AssertUtil.mustInUiThread();
                    /** 保存失败回调 */
                    if(null!=saveCallBack)
                    {
                        saveCallBack.savePhotoFailure();
                    }
                }
            });
        }
    }
    
    private void save(String extension,String mimeType,String imageUrl,byte[] bytes,@Nullable final PhotoSaveCallBack saveCallBack)
    {
        String picNameMd5=MD5Utils.getMD5(imageUrl);
        /** 声明文件对象 */
        File file;
        /** 如果有目标文件直接获得文件对象.否则创建一个新的的文件 */
        file=new File(galleryPath);
        if(!file.exists())
        {
            file.mkdir();
        }
        file=new File(galleryPath,picNameMd5+extension);
        /** 写入到文件中 */
        boolean result=FileUtils.writeBytes(galleryPath,picNameMd5+extension,bytes);
        if(result)
        {
            /** 通知相册更新 */
            ContentValues values=new ContentValues();
            values.put(MediaStore.Images.Media.DATA,file.getAbsolutePath());
            values.put(MediaStore.Images.Media.MIME_TYPE,mimeType);
            Foundation.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
            Foundation.getApplication().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("file://"+file.getAbsolutePath())));
            ThreadManager.post(ThreadManager.THREADxUI,new Runnable()
            {
                @Override
                public void run()
                {
                    AssertUtil.mustInUiThread();
                    /** 保存成功回调 */
                    if(null!=saveCallBack)
                    {
                        saveCallBack.savePhotoSuccess();
                    }
                }
            });
        }
        else
        {
            ThreadManager.post(ThreadManager.THREADxUI,new Runnable()
            {
                @Override
                public void run()
                {
                    AssertUtil.mustInUiThread();
                    /** 保存失败回调 */
                    if(null!=saveCallBack)
                    {
                        saveCallBack.savePhotoFailure();
                    }
                }
            });
        }
    }
}
