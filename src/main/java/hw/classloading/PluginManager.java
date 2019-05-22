package hw.classloading;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginManager {
    //C:\Users\IS\IdeaProjects\JavaSBT\out\artifacts
    private final String pluginRootDirectory;

    public PluginManager(String pluginRootDirectory) {
        this.pluginRootDirectory = pluginRootDirectory;
    }

    public Plugin loadPlugin(String pluginName, String className) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        URL targetURL = new URL(pluginRootDirectory + "\\" + pluginName);
        PluginClassLoader pluginClassLoader = new PluginClassLoader(new URL[]{targetURL}, Plugin.class.getClassLoader());
        Class<?> pluginClass = pluginClassLoader.loadClass(className);
        return (Plugin) pluginClass.getConstructor().newInstance();
    }
}

class PluginClassLoader extends URLClassLoader {
    ClassLoader realParent;
    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, null);
        this.realParent = parent;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            if (Plugin.class.getName().equals(name)){
                c = realParent.loadClass(name);
            }
        }
        if (c == null) {
            try {
                c = super.loadClass(name);

            } catch (ClassNotFoundException e) { }
        }
        if (c == null) {
            c = realParent.loadClass(name);
        }
        return c;
    }
}