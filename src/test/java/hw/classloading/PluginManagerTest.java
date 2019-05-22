package hw.classloading;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

class PluginManagerTest {

    @Test
    void loadPlugin() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        System.out.println(new SupportClass().getMessage());
        PluginManager pluginManager = new PluginManager(new File("C:\\Users\\IS\\IdeaProjects\\JavaSBT\\out\\artifacts").toURI().toURL().toString());
        Plugin plugin = pluginManager.loadPlugin("JavaPlugins_jar\\JavaPlugins.jar", "hw.classloading.SimplePlugin");
        plugin.doUseful();
    }
}