/*
 * 官网地址： https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/nonexternsuper/Nonexternsuper.java
 */

package jack.io.demo.ser.case5;

import java.io.*;

/**
 * 使用 Externalizable 接口时，可外部化对象必须实现 writeExternal 方法来保存对象的状态。
 * 它还必须显式地与其超类型协调以保存其状态。
 *
 * 这个简单的例子展示了如何对超类型不可外部化的对象执行此操作。
 *
 *   How to Run:
 *             Compile the file:  javac Nonexternsuper.java
 *             Then run: java Nonexternsuper.java
 *
 * 这应该在序列化之前和之后打印出书籍对象。
 *
 * 在 JDK 1.1 和 Java 2 SDK，v1.2 上测试和编译。
 */

public class Nonexternsuper {

    /**
     * 创建一个 Book（阅读材料的子类）对象，对其进行序列化、反序列化并查看它们是否相同。
     * 所以，基本上测试这个 Externalizable 示例的工作原理
     */
    public static void main(String args[]) {

        // 创建一个 Book 对象
        Book bookorg = new Book(100, "How to Serialize", true, "R.R", "Serialization", 97);
        Book booknew = null;

        // 序列化 book 对象
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
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }

        /*
         * 打印出原书和新书信息 如果我们都做对了应该是一样的！
         */
        System.out.println();
        System.out.println("Printing original book...");
        System.out.println(bookorg);
        System.out.println("Printing new book... ");
        System.out.println(booknew);
        System.out.println("Both original and new should be the same!");
        System.out.println();
    }
}
