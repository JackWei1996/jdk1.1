/*
 * 官方地址： https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/nonserialsuper/NonSerialSuperExample.java
 */

package jack.io.demo.ser.case3;

import java.io.*;

/**
 * 此示例显示如何序列化其超类不可序列化的子类。
 *
 * 当特定的超类不可序列化时，子类负责保存其超类的状态（在其 writeObject 中）
 *
 * 在JDK1.1 & JDK1.2上编译测试
 *
 * How to run this example:
 *                         Compile this file: javac NonSerialSuperExample.java
 *                         Then run:          java NonSerialSuperExample
 *
 * 这将在序列化前后打印出书籍对象。
 */
public class NonSerialSuperExample {

    /**
     * 创建一个 book 对象，对其进行序列化、反序列化，然后打印出来以测试序列化是否有效。
     */
    public static void main(String args[]) {

        // 创建一个 Book 对象
        Book bookorg = new Book(100, "How to Serialize", true, "R.R", "Serialization", 1997);
        Book booknew = null;

        // 序列化 Book
        try {
            FileOutputStream fo = new FileOutputStream("tmp");
            ObjectOutputStream so = new ObjectOutputStream(fo);
            so.writeObject(bookorg);
            so.flush();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }

        // 反序列化 Book
        try {
            FileInputStream fi = new FileInputStream("tmp");
            ObjectInputStream si = new ObjectInputStream(fi);
            booknew = (Book) si.readObject();
        }catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }

        // 如果我们做的一切都正确，这些书应该是一样的！
        System.out.println();
        System.out.println("Printing original book...");
        System.out.println(bookorg);
        System.out.println("Printing new book... ");
        System.out.println(booknew);
        System.out.println("The original and new should be the same!");
        System.out.println();
    }
}
