/*
 * 官方地址： https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/altimpl/EvolvedClass.java
 */

package jack.io.demo.ser.case8;

import java.io.*;
import java.awt.*;

/**
 *
 * 这个例子展示了如何将 Serializable Fields API 与 Serialization 一起使用，
 * 展示了该类可以将类中已有的字段定义为可序列化的字段。
 * 这不同于仅仅重写 writeObject 方法来自定义数据格式（参见自定义数据格式示例），因为在本示例中，版本控制支持仍然有效。
 *
 * 使用 Serializable Fields API，此示例专门将矩形的内部表示从 x1,y1,x2,y2
 * 实现（参见原始类）更改为 Point(x1,y1)、Point(x2,y2)（参见进化类），
 * 同时外部表示仍然是 x1, y1, x2, y2。这确保了原始表示和进化表示之间的双向兼容性。
 *
 * 原始矩形类（在 OriginalClass.java 中）由四个整数（x1、y1、x2、y2）组成。
 * 进化的矩形类（在这个文件中）不是四个整数，而是有两个点作为字段。
 * 为了让这个进化类履行其与原始类的契约，进化类将其字段保存为四个整数（x1，y1，x2，y2）而不是两个点。
 * 通过这样做，进化的类确保与原始类的双向兼容性。
 *
 * 要查看如何运行它：请参阅 OriginalClass.java
 *
 *  Compiled and Tested with JDK1.2
 */

public class EvolvedClass {

    /**
     *  有两种选择：用户可以序列化对象或反序列化它。 （使用 -s 或 -d 标志）。
     *  这些选项允许演示原始类和进化类之间的双向可读性和可写性。
     *  换句话说，可以在这里序列化一个对象，然后用进化的类反序列化它，反之亦然。
     */
    public static void main(String args[]) {

        ARectangle orgClass = new ARectangle(100, 100, 102, 102);
        ARectangle newClass = null;

        boolean serialize = false;
        boolean deserialize = false;

        /*
         * 看看我们是在序列化还是反序列化。反序列化或序列化的能力让我们看到了双向可读性和可写性
         */
        if (args.length == 1) {
            if (args[0].equals("-d")) {
                deserialize = true;
            } else if (args[0].equals("-s")) {
                serialize = true;
            } else {
                usage();
                System.exit(0);
            }
        } else {
            usage();
            System.exit(0);
        }

        /*
         * 如果选择了该选项，则序列化原始类
         */
        if (serialize) {
            try {
                FileOutputStream fo = new FileOutputStream("evolve.tmp");
                ObjectOutputStream so = new ObjectOutputStream(fo);
                so.writeObject(orgClass);
                so.flush();
            } catch (Exception e) {
                System.out.println(e);
                System.exit(1);
            }
        }

        /*
         * 反序列化，如果这是选择的选项并打印对象的名称，这将允许我们查看是谁序列化了对象，原始类或进化的类文件
         */
        if (deserialize) {
            try {
                FileInputStream fi = new FileInputStream("evolve.tmp");
                ObjectInputStream si = new ObjectInputStream(fi);
                newClass = (ARectangle) si.readObject();
            } catch (Exception e) {
                System.out.println(e);
                System.exit(1);
            }
            System.out.println("Now printing deserialized object: ");
            System.out.println();
            System.out.println(newClass);
        }
    }

    /**
     * Prints out the usage
     */
    static void usage() {
        System.out.println("Usage:");
        System.out.println("      -s (in order to serialize)");
        System.out.println("      -d (in order to deserialize)");
    }
}

/**
 * 进化的矩形类。 Interally 由两个 Point 类型的字段组成，但 externally 仍然是 4 个整数（因此它与原始矩形类兼容）
 *
 * 为了使这成为可能，我们需要使用 Serializable Field API，以便我们可以定义不属于实现类的可序列化字段。
 */
class ARectangle implements java.io.Serializable {

    // 新的矩形表示

    /**
     * 形成矩形对角线的两个点中的第一个。
     *
     * 请注意，由于在此类中使用了 serialPersistentFields 成员，因此该字段不是默认的可序列化字段。
     */
    Point point1;


    /**
     * 形成矩形对角线的两点中的第二个。
     *
     * 请注意，由于在此类中使用了 serialPersistentFields 成员，因此该字段不是默认的可序列化字段。
     */
    Point point2;

    /*
     * 进化的可序列化类的强制 SUID 字段。
     * 通过对原始类执行 serialver 命令获得 serialVersionUID：
     *                  serialver ARectangle (the original rectangle)
     */
    static final long serialVersionUID = 9030593813711490592L;


    /**
     * 特殊成员 serialPeristentFields 显式声明此类的 Serializable 字段。
     * 这允许除了类中的字段之外的字段是持久的。
     * 由于我们要保存两个点point1和point2的状态，我们将这4个int声明为串行持久字段
     *
     * @serialField x1	Integer 
     *              矩形对角点第 1 点的 X 坐标。
     * @serialField y1	Integer 
     *              矩形对角点第 1 点的 Y 坐标。
     * @serialField x2	Integer 
     *              矩形对角点第 2 点的 X 坐标。
     * @serialField y2	Integer 
     *              矩形对角点第 2 点的 Y 坐标。
     */
    private static final ObjectStreamField[] serialPersistentFields = {
            new ObjectStreamField("x1", Integer.TYPE),
            new ObjectStreamField("y1", Integer.TYPE),
            new ObjectStreamField("x2", Integer.TYPE),
            new ObjectStreamField("y2", Integer.TYPE)
    };

    ARectangle(int x1, int y1, int x2, int y2) {
        point1 = new Point(x1, y1);
        point2 = new Point(x2, y2);
    }

    /**
     * writeObject - 使用 Serializable Field API 写出可序列化的字段（4 个整数，x1、y1、x2、y2）。
     * （ObjectOutputStream类的putFields和writeFields方法和ObjectOutputStream.PutField内部类的put方法）
     *
     * @serialData 仅写入类的可序列化字段。没有写入可选数据。
     */
    private void writeObject(ObjectOutputStream s)
            throws IOException {

        // set the values of the Serializable fields
        ObjectOutputStream.PutField fields = s.putFields();
        fields.put("x1", point1.x);
        fields.put("y1", point1.y);
        fields.put("x2", point2.x);
        fields.put("y2", point2.y);

        // save them
        s.writeFields();
    }

    /**
     * readsObject - 使用 Serializable Field API 读取可序列化字段（4 个整数，x1、y1、x2、y2）。
     * （ObjectInputStream类的getFields和readFields方法和ObjectOutputStream.GetField内部类的get方法）
     *
     * @serialData No optional data is read.
     */
    private void readObject(ObjectInputStream s)
            throws IOException {

        // 准备读取备用持久字段
        ObjectInputStream.GetField fields = null;
        try {
            fields = s.readFields();
        } catch (Exception ClassNotFoundException) {
            throw new IOException();
        }

        // 读取备用持久字段
        int x1 = (int)fields.get("x1", 0);
        int y1 = (int)fields.get("y1", 0);
        int x2 = (int)fields.get("x2", 0);
        int y2 = (int)fields.get("y2", 0);

        // save them back as Points.
        point1 = new Point(x1, y1);
        point2 = new Point(x2, y2);
    }

    public String toString() {
        return("point1.x: " + point1.x + "\npoint1.y: " + point1.y + "\npoint2.x: " + point2.x + "\npoint2.y: " + point2.y);
    }
}