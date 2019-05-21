package hw.caching;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HashMapCacheStorage implements CacheStorage {

    private Map<String, Object> storedValues = new HashMap<>();

    @Override
    public void put(Object[] args, Object value) throws IOException {
        storedValues.put(generateKey(args), value);
    }

    @Override
    public Optional<Object> get(Object[] args) throws IOException {
        String key = generateKey(args);
        if (storedValues.containsKey(key)) {
            return Optional.of(storedValues.get(key));
        }
        return Optional.empty();
    }

    private String generateKey(Object[] args) throws IOException {
        return StringSerializer.encode(args);
    }
}
