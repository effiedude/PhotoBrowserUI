package com.townspriter.android.photobrowser.ui.model;

import java.io.File;
import com.townspriter.android.photobrowser.ui.model.base.PhotoSaveTask;
import com.townspriter.android.photobrowser.ui.model.callback.PhotoSaveCallBack;
import com.townspriter.android.foundation.utils.concurrent.ThreadManager;
import com.townspriter.android.foundation.utils.log.Logger;
import com.townspriter.android.foundation.utils.text.StringUtil;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @Path PhotoBrowserUI:PhotoSaveModel
 * @Describe
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
// TODO:工具类代码需要整理到脚手架中
public class PhotoSaveModel
{
    private static final String TAG="PhotoSaveModel";
    /** 系统相册目录 */
    private final String galleryPath=Environment.getExternalStorageDirectory()+File.separator+Environment.DIRECTORY_DCIM+File.separator+"Camera"+File.separator;
    
    /**
     * saveImageToGallery
     * 保存图片
     *
     * @param imageUrl
     * @param saveCallBack
     */
    public void saveImageToGallery(@NonNull final String imageUrl,@Nullable final PhotoSaveCallBack saveCallBack)
    {
        /** 扩展名 */
        String extension=getLastIndexStringWithSelf(imageUrl,".");
        Logger.d(TAG,"saveImageToGallery-extension:"+extension);
        /** 图片类型 */
        String mimeType=guessMediaMimeType(imageUrl);
        Logger.d(TAG,"saveImageToGallery-mimeType:"+mimeType);
        saveImageToGalleryInner(extension,mimeType,imageUrl,saveCallBack);
    }
    
    /**
     * saveImageToGalleryInner
     *
     * @param imageUrl
     * 图片下载地址
     * <p>
     * 图片文件命名有如下几种方式：
     * 1.随机命名:通过UUID生成字符串文件名：UUID.randomUUID().toString()
     * 2.时间命名:System.currentTimeMillis()
     * 3.MD5处理文件URL来命名(唯一且安全)
     * 目前采用第3种命名方式
     * @param saveCallBack
     */
    private void saveImageToGalleryInner(final String extension,final String mimeType,final String imageUrl,final PhotoSaveCallBack saveCallBack)
    {
        ThreadManager.post(ThreadManager.THREADxWORK,new PhotoSaveTask(galleryPath,extension,mimeType,imageUrl,saveCallBack));
    }
    
    /**
     * 获取字串中最后指定字符之后的内容(包含字符自身)
     *
     * @param string
     * 源字符串
     * @param character
     * 标识字符
     * @return
     */
    public @Nullable static String getLastIndexStringWithSelf(@NonNull String string,@NonNull String character)
    {
        int characterIndex=string.lastIndexOf(character);
        if(characterIndex>0&&characterIndex<string.length())
        {
            return string.substring(characterIndex);
        }
        return null;
    }
    
    public static @Nullable String guessMediaMimeType(@NonNull String filePath)
    {
        String mimeType=null;
        // 使用系统接口获取目标路径中文件的后缀名(扩展名)
        String extension=MimeTypeMap.getFileExtensionFromUrl(filePath);
        if(!StringUtil.isEmptyWithNull(extension))
        {
            // 文件扩展名与系统哈系表进行匹配
            mimeType=MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        if(extension!=null)
        {
            mimeType=MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            /** 系统方法不支持包含中文字符的名称检测.提供兜底策略.获取文件的后缀信息再次获取文件类型 */
            if(mimeType==null)
            {
                extension=filePath.substring(filePath.lastIndexOf(".")+1);
                mimeType=MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
        }
        // 系统方法不支持包含中文字符的名称检测.提供兜底策略.获取文件的信息再次获取
        /*
         * if(StringUtil.isEmptyWithNull(mimeType))
         * {
         * String result=extractMetadata(filePath,MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
         * return result;
         * }
         */
        return mimeType;
    }
}
