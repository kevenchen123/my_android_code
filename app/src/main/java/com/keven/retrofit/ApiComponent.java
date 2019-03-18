package com.keven.retrofit;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by Mohsen on 20/10/2016.
 */

@Singleton
@Component(modules = {
        ConstantModule.class,
        ApiModule.class,
})
public interface ApiComponent {
    void inject(DataService main);
}