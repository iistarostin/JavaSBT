package hw.classloading;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

class EncryptedClassLoaderTest {

    @Test
    void findClass() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        EncryptedClassLoader classLoader = new EncryptedClassLoader((byte) 0,
                new File("C:\\Users\\IS\\IdeaProjects\\JavaSBT\\JavaPlugins\\target\\classes"),
                ClassLoader.getSystemClassLoader());
        try {
            Plugin p = (Plugin) classLoader.findClass("hw.classloading.SimplePlugin").getConstructor().newInstance();
            p.doUseful();

        } catch (Throwable e) {
            throw e;
        }
    }
}