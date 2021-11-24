package com.townspriter.photobrowser.ui.model.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;

import com.townspriter.base.foundation.utils.log.Logger;

/******************************************************************************
 * @Path PhotoBrowserUI:MockUtils
 * @Describe 加载本地模拟数据工具
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-上午10:56
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class MockUtils
{
    private static final String TAG="MockUtils";
    private static JSONObject sMockJson;
    
    public static JSONObject mockMineWeMediaData(Context context)
    {
        try
        {
            sMockJson=new JSONObject(getMockDataInner(context,"minelist.json"));
        }
        catch(JSONException jsonException)
        {
            Logger.w(TAG,"mockMineWeMediaData:JSONException",jsonException);
        }
        return sMockJson;
    }
    
    public static JSONObject mockAllQiangGuoWeMediaData(Context context)
    {
        try
        {
            sMockJson=new JSONObject(getMockDataInner(context,"alllistone.json"));
        }
        catch(JSONException jsonException)
        {
            Logger.w(TAG,"mockAllWeMediaData:JSONException",jsonException);
        }
        return sMockJson;
    }
    
    public static JSONObject mockAllWeMediaData(Context context)
    {
        try
        {
            sMockJson=new JSONObject(getMockDataInner(context,"alllisttwo.json"));
        }
        catch(JSONException jsonException)
        {
            Logger.w(TAG,"mockAllWeMediaData:JSONException",jsonException);
        }
        return sMockJson;
    }
    
    public static JSONObject mockDetailWeMediaData(Context context)
    {
        Logger.d(TAG,"mockDetailWeMediaData");
        try
        {
            sMockJson=new JSONObject(getMockDataInner(context,"detail.json"));
        }
        catch(JSONException jsonException)
        {
            Logger.w(TAG,"mockDetailWeMediaData:JSONException",jsonException);
        }
        return sMockJson;
    }
    
    private static String getMockDataInner(Context context,String jsonName)
    {
        StringBuilder data=new StringBuilder();
        InputStream openIs=null;
        BufferedReader bfReader=null;
        try
        {
            openIs=context.getAssets().open(jsonName);
            bfReader=new BufferedReader(new InputStreamReader(openIs));
            String line;
            while((line=bfReader.readLine())!=null)
            {
                data.append(line);
            }
        }
        catch(IOException ioException)
        {
            Logger.w(TAG,"getMockDataInner:IOException",ioException);
        }
        finally
        {
            if(bfReader!=null)
            {
                try
                {
                    bfReader.close();
                }
                catch(IOException ioException)
                {
                    Logger.w(TAG,"getMockDataInner:IOException",ioException);
                }
            }
            else if(openIs!=null)
            {
                try
                {
                    openIs.close();
                }
                catch(IOException ioException)
                {
                    Logger.w(TAG,"getMockDataInner:IOException",ioException);
                }
            }
        }
        return data.toString();
    }
}
