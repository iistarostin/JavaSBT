package hw.caching;

import java.lang.reflect.Proxy;

public class CacheProxy {

    public CacheProxy() { }

    @SuppressWarnings("unchecked")
    public <T> T cache(T target) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new CacheManager(target));
    }
}

