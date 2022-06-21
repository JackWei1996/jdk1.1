/**
 * 官方地址： https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/evolveserial/EvolutionExampleEvolvedClass.java
 */
package jack.io.demo.ser.case6;

import java.io.*;
import java.util.*;

/**
 * 当 Java 对象使用序列化来存储对象时，可能会出现读取数据的类的版本与写入该数据的类的版本不同的可能性
 *
 * 此示例演示了序列化在没有特定类方法的情况下处理的一些兼容更改
 *
 * 有关如何运行的说明：请参阅原始类文件。
 *
 * 用JDK1.2编译测试这个文件包含了进化的类。原始类在名为 EvolutionExampleOriginalClass.java 的文件中
 */
public class EvolutionExampleEvolvedClass {

    /**
     *  有两种选择：用户可以序列化对象或反序列化它。 （使用 -s 或 -d 标志）。
     *  这些选项允许演示原始类和进化类之间的双向可读性和可写性。
     *  换句话说，可以在这里序列化一个对象，然后用进化的类反序列化它，反之亦然。
     */
    public static void main(String args[]) {

        AClass serializeclass = new AClass(20, "serializedByEvolvedClass");
        AClass deserializeclass = null;

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
     * Prints out the usage
     */
    static void usage() {
        System.out.println("Usage:");
        System.out.println("      -s (in order to serialize)");
        System.out.println("      -d (in order to deserialize)");
    }
}


/**
 * Evolved Class
 * 具有以下兼容更改
 * 1) add a field
 * 2) change a access to a field (for example -> public to private)
 * 3) Remove the writeObject/readObject methods
 * 4) change a static field to non-static - this is equivalent to adding
 *    a field.

 * 此示例未演示的兼容更改
 * 1) Adding classes/Removing classes
 * 2) Adding writeObject/readObject methods
 */
class AClass implements Serializable {

    // 强制 suid 字段（在原始 Aclass 上使用 serialver）
    static final long serialVersionUID = -6756364686697947626L;

    // Change: removed the private
    /**
     * @serial
     */
    int num;

    // Change: added this field
    /**
     * @serial
     */
    boolean b;

    /**
     * @serial
     */
    String name;

    // Change: removed the static.. 所以这个字段现在将被序列化
    // 相当于添加一个字段
    /**
     * @serial
     */
    Hashtable ht = new Hashtable();
    // ...
    // ...
    // ...

    AClass(int n, String s) {
        num = n;
        name = s;
        boolean b = true;
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
