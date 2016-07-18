package com.csusm.incredibledata.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

/**
 * Class that contain networking util function
 */
public final class NetworkUtil {

  public static boolean isConnectedToWifi(@NonNull Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
        Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

    return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
  }
  }
