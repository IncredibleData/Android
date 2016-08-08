package com.csusm.incredibledata;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.csusm.incredibledata.di.component.ApplicationComponent;
import com.csusm.incredibledata.di.component.DaggerApplicationComponent;
import com.csusm.incredibledata.di.module.ApplicationModule;

/**
 * Application class.
 */
public class IDApplication extends Application {

  private ApplicationComponent mApplicationComponent;

  @NonNull
  public static IDApplication get(@NonNull Context context) {
    return (IDApplication) context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    super.onCreate();

    initializeInjector();
  }

  protected void initializeInjector() {
    mApplicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(getApplicationModule())
        .build();
  }

  public ApplicationComponent getApplicationComponent() {
    return mApplicationComponent;
  }

  protected ApplicationModule getApplicationModule() {
    return new ApplicationModule(this);
  }
}
