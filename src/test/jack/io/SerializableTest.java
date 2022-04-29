package jack.io;

import jack.io.jack.User;

import java.io.*;

/**
 * @author Jack魏
 * @version 1.0 2022-04-20 00:01
 */
public class SerializableTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        User user = new User();
        user.setName("Jack魏");
        user.setAge(18);
        String fileName = "E:\\code\\tmp\\user.ser";

        // 序列化
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(user);
        fos.close();

        // 反序列化
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        ois.close();
        System.out.println(obj);
    }
}
