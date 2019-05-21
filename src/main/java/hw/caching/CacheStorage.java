package hw.caching;

import java.io.IOException;
import java.util.Optional;

public interface CacheStorage {
    void put(Object[] args, Object value) throws IOException;
    Optional<Object> get(Object[] args) throws IOException;
}
