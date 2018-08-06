package com.caile.user.file;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

public class HGFileOpenerPlugin extends CordovaPlugin {

    private static String TAG = "HGFileOpenerPlugin";
    
    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
    }
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("install")) {
            String pathName = args.getString(0);    // 指下载的apk文件的路径
            install(pathName, callbackContext);
            return true;
        }

        return false;
    }

    private void install(String pathName, CallbackContext callbackContext) {
        Context context = cordova.getActivity().getApplicationContext();

        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.NO_RESULT));

        try {

            if (pathName.startsWith("file://")) {
                pathName = pathName.substring(7);
            }

            if (pathName != null && pathName.endsWith(".apk")) {
                if (Build.VERSION.SDK_INT >= 24) {
                    File file = new File(pathName);
                    Uri apkUri = FileProvider.getUriForFile(context, "com.caile.user.file.fileprovider", file);     // 提供者的id需唯一,并与AndroidManifest.xml中的provider保持一致，最好选用应用包名
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);    // 添加这一句表示对目标应用临时授权该Uri所代表的文件
                    install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    context.startActivity(install);
                } else {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setDataAndType(Uri.fromFile(new File(pathName)), "application/vnd.android.package-archive");
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(install);
                }
            }

        } catch (Exception e) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "安装失败"));
        }

    }

}