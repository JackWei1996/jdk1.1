/*
 * 官方地址：https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/evolveserial/EvolutionExampleOriginalClass.java
 */

package jack.io.demo.ser.case6;

import java.io.*;
import java.util.Hashtable;

/**
 * 当 Java 对象使用序列化来存储对象时，可能会出现读取数据的类的版本与写入该数据的类的版本不同的可能性。
 *
 * 此示例演示了序列化在没有特定类方法的情况下处理的一些兼容更改
 *
 * How to Run:
 *
 * 编译 Original Class:
 *         javac EvolutionExampleOriginalClass.java
 * 运行 Original Class 并且使用序列化标识:
 *         java EvolutionExampleOriginalClass -s
 * Compile Evolved Class:
 *         javac EvolutionExampleEvolvedClass.java
 * Run Evolved Class with deserialization flag:
 *         java EvolutionExampleEvolvedClass -d
 *
 * 这仅测试一个方向的兼容性。在另一个方向执行相同的步骤以查看双向兼容性。
 *
 * 用JDK1.1.4 & JDK1.2 编译和测试这个文件包含原始类。进化类在名为 EvolutionExampleEvolvedClass.java 的文件中
 */
public class EvolutionExampleOriginalClass {
    /**
     *  有两种选择：用户可以序列化对象或反序列化它。 （使用 -s 或 -d 标志）。
     *  这些选项允许演示原始类和进化类之间的双向可读性和可写性。
     *  换句话说，可以在这里序列化一个对象，然后用进化的类反序列化它，反之亦然。
     */
    public static void main(String args[]) {

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

        AClass serializeclass = new AClass(10, "serializedByOriginalClass");
        AClass deserializeclass = null;

        /*
         * 如果选择了该选项，则序列化原始类
         */
        if (serialize) {
            try {
                FileOutputStream fo = new FileOutputStream("evolve.tmp");
                ObjectOutputStream so = new ObjectOutputStream(fo);
                so.writeObject(serializeclass);
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
                deserializeclass = (AClass) si.readObject();
            } catch (Exception e) {
                System.out.println(e);
                System.exit(1);
            }
            /*
             * 打印出来看看确实和序列化时是同一个对象（要看是序列化的原始类还是进化的类）
             */
            System.out.println("Now printing deserialized object's name: ");
            System.out.println();
            System.out.println("name: " + deserializeclass.name);
            System.out.println();
        }
    }

    /**
     * 打印出使用情况
     */
    static void usage() {
        System.out.println("Usage:");
        System.out.println("      -s (in order to serialize)");
        System.out.println("      -d (in order to deserialize)");
    }
}

/*
 * Original Class
 * 稍后会发展（见另一个文件）
 */
class AClass implements Serializable {

    // 一些数据字段

    /**
     * @serial
     */
    private int num;

    /**
     * @serial
     */
    String name;

    static Hashtable ht = new Hashtable();

    // ...
    // ...
    // ...

    AClass(int n, String s) {
        num = n;
        name = s;
    }

    // some methods...
    // ...
    // ...

    /**
     * 这些 writeObject 和 readObject 只是 defaultwriteObject 和 defaultreadObject
     * - 所以它们不做任何事情。但是它们被放置在这里，以便我们可以表明我们可以在进化类中删除它们而没有任何影响。
     *
     * @serialData 编写可序列化的字段。没有写入可选数据。
     */
    private void writeObject(ObjectOutputStream s)
            throws IOException {
        s.defaultWriteObject();
    }

    /**
     * readObject - just calls defaultreadObject()
     *
     * @serialData 读取可序列化的字段。没有可选的数据读取。
     */
    private void readObject(ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        s.defaultReadObject();
    }
}
