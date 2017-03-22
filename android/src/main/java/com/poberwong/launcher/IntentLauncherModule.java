package com.poberwong.launcher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.content.pm.PackageManager;
import android.content.Context;
import android.app.Fragment;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.io.Console;

/**
 * Created by poberwong on 16/6/30.
 */
public class IntentLauncherModule extends ReactContextBaseJavaModule {
    private static final String ATTR_ACTION = "action";
    private static final String ATTR_CATEGORY = "category";
    private static final String TAG_EXTRA = "extra";
    private static final String ATTR_DATA = "data";
    private static final String ATTR_FLAGS = "flags";

    public IntentLauncherModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "IntentLauncher";
    }

    /**
     * 选用方案
     * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     * getReactApplicationContext().startActivity(intent);
     */
    @ReactMethod
    public void startActivity(ReadableMap params){

        PackageManager manager = getReactApplicationContext().getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(params.getString(ATTR_DATA));
            if (i == null) {
                //return false;
                throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            getReactApplicationContext().startActivity(i);
            //return true;
        } catch (PackageManager.NameNotFoundException e) {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + params.getString(ATTR_DATA)));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getReactApplicationContext().startActivity(i);
            } catch (android.content.ActivityNotFoundException anfe) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + params.getString(ATTR_DATA)));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getReactApplicationContext().startActivity(i);
            }
        }
    }
}
