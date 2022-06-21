/*
 * 官方地址： https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/altimpl/OriginalClass.java
 */

package jack.io.demo.ser.case8.original;

import java.io.*;

/**
 *
 * 这个例子展示了如何将 Serializable Fields API 与 Serialization 一起使用，
 * 展示了该类可以将类中已有的字段定义为可序列化的字段。
 * 这不同于仅仅重写 writeObject 方法来自定义数据格式（参见自定义数据格式示例），因为在本示例中，版本控制支持仍然有效。
 *
 * 使用 Serializable Fields API，此示例专门将矩形的内部表示从 x1,y1,x2,y2 实现（参见原始类）
 * 更改为 Point(x1,y1)、Point(x2,y2)（参见进化类），同时外部表示仍然是 x1, y1, x2, y2。
 * 这确保了原始表示和进化表示之间的双向兼容性。
 *
 * 原始矩形类（在此文件中）由四个整数（x1、y1、x2、y2）组成。
 * 进化的矩形类（在 EvolvedClass.java 中）不是四个整数，而是有两个点作为字段。
 * 为了让这个进化类履行其与原始类的契约，进化类将其字段保存为四个整数（x1，y1，x2，y2）而不是两个点。
 * 通过这样做，进化的类确保与原始类的双向兼容性。
 *
 * How to Run:
 *
 * Compile Original Class with JDK1.2:
 *         javac OriginalClass.java
 * Run Original Class with serialization flag:
 *         java OriginalClass -s
 * Compile Evolved Class:
 *         javac EvolvedClass.java
 * Run Evolved Class with deserialization flag:
 *         java EvolvedClass -d
 *
 * 这测试了一个方向的兼容性。在其他方向执行相同操作以查看双向兼容性。
 *
 *  Compiled and Tested with JDK1.2
 */

public class OriginalClass {

    /**
     *  有两种选择：用户可以序列化对象或反序列化它。 （使用 -s 或 -d 标志）。
     *  这些选项允许演示原始类和进化类之间的双向可读性和可写性。
     *  换句话说，可以在这里序列化一个对象，然后用进化的类反序列化它，反之亦然。
     */
    public static void main(String args[]) {

        ARectangle orgClass = new ARectangle(0, 0, 2, 2);
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
 * 原始的 Rectangle 类。简单地由代表矩形的两个对角点的 4 个整数组成。
 * 没有写入 writeObject 或 readObject，因此外部和内部表示相同（4 个整数）
 */
class ARectangle implements java.io.Serializable {

    /**
     * 矩形的两个对角点的点 1 的 X 坐标。
     * @serial
     */
    int x1;

    /**
     * 矩形的两个对角点的点 1 的 Y 坐标。
     * @serial
     */
    int y1;

    /**
     * 矩形的两个对角点的点 2 的 X 坐标。
     * @serial
     */
    int x2;

    /**
     * 矩形的两个对角点的点 2 的 Y 坐标。
     * @serial
     */
    int y2;

    ARectangle(int xone, int yone, int xtwo, int ytwo) {
        x1 = xone;
        y1 = yone;
        x2 = xtwo;
        y2 = ytwo;
    }

    public String toString() {
        return("x1: " + x1 + "\ny1: " + y1 + "\nx2: " + x2 + "\ny2: " + y2);
    }
}
