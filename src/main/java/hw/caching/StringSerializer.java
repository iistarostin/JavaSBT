package hw.caching;

import java.io.*;
import java.util.Base64;

public class StringSerializer {

    public static String encode(Serializable obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    public static Object decode(String binary) throws IOException, ClassNotFoundException {
        byte[] binaryData = Base64.getDecoder().decode(binary);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(binaryData));
        Object obj  = objectInputStream.readObject();
        objectInputStream.close();
        return obj;
    }
}
