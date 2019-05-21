package hw.caching;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

interface Service {
    int doWork();
    @Cache(location = CacheLocation.RAM, argsIndices = {0}, maxListLength = 100)
    List<Integer> doHardWork(int n, String s);
    @Cache(location = CacheLocation.FILE)
    List<Integer> doReallyHardWork(int n, int m);
}

class ServiceImpl implements Service {

    public int workDone = 0;
    public int hardWorkDone = 0;
    public int reallyHardWorkDone = 0;
    @Override
    public int doWork() {
        ++workDone;
        return 0;
    }

    @Override
    public List<Integer> doHardWork(int n, String s) {
        ++hardWorkDone;
        return IntStream.range(0, n).boxed().collect(Collectors.toList());
    }

    @Override
    public List<Integer> doReallyHardWork(int m, int n) {
        ++reallyHardWorkDone;
        return IntStream.range(0, n * m).boxed().collect(Collectors.toList());
    }
}

class CacheProxyTest {

    @Test
    void cacheRAM() {
        CacheProxy proxy = new CacheProxy();
        ServiceImpl cacheTarget = new ServiceImpl();
        Service service = proxy.cache(cacheTarget);

        service.doWork();
        assert cacheTarget.workDone == 1;

        service.doHardWork(10, "11");
        assert cacheTarget.hardWorkDone == 1;

        service.doHardWork(10, "22");
        assert cacheTarget.hardWorkDone == 1;

        service.doHardWork(1000, "11");
        assert cacheTarget.hardWorkDone == 2;

        service.doHardWork(1000, "11");
        assert cacheTarget.hardWorkDone == 3;
    }

    @Test
    void cacheFile() {

        new File("cache.db").delete();
        ServiceImpl cacheTarget = new ServiceImpl();

        run(cacheTarget, 100, 100);
        assert cacheTarget.reallyHardWorkDone == 1;

        run(cacheTarget, 100, 100);
        assert cacheTarget.reallyHardWorkDone == 1;

        run(cacheTarget, 200, 100);
        assert cacheTarget.reallyHardWorkDone == 2;

        run(cacheTarget, 200, 100);
        assert cacheTarget.reallyHardWorkDone == 2;

    }

    List<Integer> run(ServiceImpl cacheTarget, int n, int m) {
        CacheProxy proxy = new CacheProxy();
        Service service = proxy.cache(cacheTarget);
        return service.doReallyHardWork(n, m);
    }
}