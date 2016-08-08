package com.csusm.incredibledata.di.component;

import com.csusm.incredibledata.activity.MainActivity;
import com.csusm.incredibledata.di.module.ApplicationModule;
import dagger.Component;
import javax.inject.Singleton;

/**
 * @author mehdichouag on 20/06/16.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
  void inject(MainActivity activity);
}
