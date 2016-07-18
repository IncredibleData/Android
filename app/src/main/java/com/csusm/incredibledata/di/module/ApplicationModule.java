package com.csusm.incredibledata.di.module;

import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.csusm.incredibledata.IDApplication;
import com.csusm.incredibledata.util.NetworkUtil;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import dagger.Module;
import dagger.Provides;
import java.net.URISyntaxException;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Dagger's application module.
 */
@Module
public class ApplicationModule {

  private final IDApplication mApplication;

  public ApplicationModule(@NonNull IDApplication application) {
    mApplication = application;
  }

  @NonNull
  @Provides
  @Singleton
  public Context provideApplicationContext() {
    return mApplication;
  }

  @Nullable
  @Provides
  @Singleton
  public Socket provideSocket() {
    try {
      return IO.socket("http://incredibledata.herokuapp.com");
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return null;
  }

  @NonNull
  @Provides
  @Singleton
  @Named("uniqueId")
  public String provideUniqueId() {
    final ContentResolver resolver = mApplication.getContentResolver();
    return Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID);
  }

  @NonNull
  @Provides
  @Singleton
  public GoogleApiClient.Builder provideGoogleApiBuilder() {
    return new GoogleApiClient.Builder(mApplication).addApi(LocationServices.API);
  }

  @NonNull
  @Provides
  @Singleton
  public WifiInfo provideWifiInfo() {
    final WifiManager manager = (WifiManager) mApplication.getSystemService(Context.WIFI_SERVICE);
    return manager.getConnectionInfo();
  }

  @Provides
  @Singleton
  public boolean provideWifiConnection() {
    return NetworkUtil.isConnectedToWifi(mApplication);
  }
}
