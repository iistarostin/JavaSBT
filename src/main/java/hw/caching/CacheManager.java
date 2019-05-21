package hw.caching;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class CacheManager implements InvocationHandler {

    private final Object target;
    private Map<String, CacheStorage> cacheStorages = new HashMap<>();

    public CacheManager(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Cache.class)) {
            Cache cachingParams = method.getAnnotation(Cache.class);
            return retrieveValue(method, args, cachingParams);
        } else {
            return method.invoke(target, args);
        }
    }

    private Object retrieveValue(Method method, Object[] args, Cache cachingParams) throws Throwable {
        String methodID = !"".equals(cachingParams.cacheID()) ? cachingParams.cacheID() : method.getName();
        Object[] relevantArgs = selectArgs(args, cachingParams);

        CacheStorage storage = correspondingCacheStorage(methodID, cachingParams);
        Optional<Object> valueStored = storage.get(relevantArgs);
        if (valueStored.isPresent()) {
            return valueStored.get();
        } else {
            Object value = method.invoke(target, args);
            if (needsCaching(value, cachingParams)) {
                storage.put(relevantArgs, value);
            }
            return value;
        }
    }

    private CacheStorage correspondingCacheStorage(String methodID, Cache cachingParams) throws IOException {
        if (!cacheStorages.containsKey(methodID)) {
            if (cachingParams.location() == CacheLocation.RAM) {
                cacheStorages.put(methodID, new HashMapCacheStorage());
            } else if (cachingParams.location() == CacheLocation.FILE) {
                cacheStorages.put(methodID, new DBCacheStorage("cache", target.toString() + methodID));
            }
        }
        return cacheStorages.get(methodID);
    }

    private boolean needsCaching(Object returnedValue, Cache cachingParams) {
        return cachingParams.maxListLength() < 0 ||
                !(returnedValue instanceof List<?> && ((List<?>) returnedValue).size() > cachingParams.maxListLength());
    }

    private Object[] selectArgs(Object[] args, Cache cachingParams) {
        int[] argsIndices = cachingParams.argsIndices();
        if (argsIndices.length == 0) {
            return args;
        }
        Object[] cachingArgs = new Object[argsIndices.length];
        for (int i = 0; i < cachingArgs.length; ++i) {
            cachingArgs[i] = args[argsIndices[i]];
        }
        return cachingArgs;
    }
}
