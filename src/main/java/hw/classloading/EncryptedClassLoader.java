package hw.classloading;

import java.io.*;
import java.nio.file.Files;

public class EncryptedClassLoader extends ClassLoader {

    private XOREncryptor encryptor;
    private final File dir;

    public EncryptedClassLoader(byte key, File dir, ClassLoader parent) {
        super(parent);
        this.encryptor = new XOREncryptor(key);
        this.dir = dir;
    }

    @Override
    public Class<?> findClass(String name) {
        try {
            byte[] classCode = loadBinary(name);
            classCode = encryptor.decode(classCode);
            return super.defineClass(name, classCode, 0, classCode.length);
        } catch (IOException e) {
            return null;
        }
    }

    private byte[] loadBinary(String className) throws IOException {
        String path = dir.getAbsolutePath() + "/" + className.replace(".", "/") + ".class";
        return Files.readAllBytes(new File(path).toPath());
    }

    class XOREncryptor {

        private final byte key;

        XOREncryptor(byte key) {
            this.key = key;
        }

        public byte[] encode(byte[] message) {
            byte[] result = new byte[message.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = (byte)(message[i] ^ key);
            }
            return result;
        }

        public byte[] decode(byte[] message) {
            return encode(message);
        }
    }
}
