package hw.caching;

import java.io.IOException;
import java.util.*;

public class HashMapCacheStorage implements CacheStorage {

    private Map<List<Object>, Object> storedValues = new HashMap<>();

    @Override
    public void put(Object[] args, Object value) throws IOException {
        storedValues.put(generateKey(args), value);
    }

    @Override
    public Optional<Object> get(Object[] args) throws IOException {
        List<Object> key = generateKey(args);
        if (storedValues.containsKey(key)) {
            return Optional.of(storedValues.get(key));
        }
        return Optional.empty();
    }

    private List<Object> generateKey(Object[] args) {
        return Collections.unmodifiableList(Arrays.asList(args));
    }
}
