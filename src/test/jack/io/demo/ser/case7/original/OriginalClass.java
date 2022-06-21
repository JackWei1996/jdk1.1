/*
 * 官方地址：https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/evolvenewsuper/OriginalClass.java
 */

package jack.io.demo.ser.case7.original;

import java.io.*;

/**
 * 这个例子展示了如何在序列化中使用版本控制来进化类以拥有一个新的超类。
 *
 * 例如，如果最初的类结构是：
 *
 * class A {...};
 * class C extends A {...};
 *
 * and the evolved clss structure looks like:（进化后的 class 结构看起来像。这里的class拼错了）
 *
 * class A {...};
 * class B extends A {...};
 * class C extends B {...};
 *
 * 那么应该可以用新版本的 C 读取旧版本的 C，反之亦然。
 *
 * 这个例子证明了这一点。
 *
 * 注意：在本例中，超类（上例中的 A 和 B）实现了 Serializable 接口。
 * 如果他们不这样做，那么子类 C 将负责保存和恢复 A 和 B 的字段。
 * 请参阅名为“Serialization with a NonSerializable Superclass”的相关示例
 *
 * 查看代码注释就足够了，但是如果您想运行：
 *
 * How to Run:
 *
 * Compile Original Class:
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
 * 用JDK1.2编译和测试这个文件包含原始类。原始类在名为 EvolvedClass.java 的文件中
 */

public class OriginalClass {

    /**
     * 有两种选择：用户可以序列化对象或反序列化它。 （使用 -s 或 -d 标志）。
     * 这些选项允许演示原始类和进化类之间的双向可读性和可写性。
     * 换句话说，可以在这里序列化一个对象，然后用进化的类反序列化它，反之亦然。
     */
    public static void main(String args[]) {
        ASubClass corg = new ASubClass(1, "SerializedByOriginalClass");
        ASubClass cnew = null;
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

        if (serialize) {
            // Serialize the subclass
            try {
                FileOutputStream fo = new FileOutputStream("tmp");
                ObjectOutputStream so = new ObjectOutputStream(fo);
                so.writeObject(corg);
                so.flush();
            } catch (Exception e) {
                System.out.println(e);
                System.exit(1);
            }
        }


        if (deserialize) {
            // Deserialize the subclass
            try {
                FileInputStream fi = new FileInputStream("tmp");
                ObjectInputStream si = new ObjectInputStream(fi);
                cnew = (ASubClass) si.readObject();

            } catch (Exception e) {
                System.out.println(e);
                System.exit(1);
            }
            System.out.println();
            System.out.println("Printing deserialized class: ");
            System.out.println();
            System.out.println(cnew);
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
 * 一个实现 Serializable 的简单超类
 */
class ASuperClass implements Serializable {

    /**
     * @serial
     */
    String name;

    ASuperClass(String name) {
        this.name = name;
    }

    public String toString() {
        return("Name: " + name);
    }
}

/**
 * 一个简单的子类，它实现了 Serializable 并扩展了一个可序列化的超类。
 *
 * 再次注意，如果超类不可序列化，那么这个子类将负责保存和恢复超类的字段。
 * 有关更多详细信息，请参见名为“使用 NonSerializable 超类的序列化”的示例。
 */
class ASubClass extends ASuperClass implements Serializable {

    /**
     * @serial
     */
    int num;

    ASubClass(int num, String name) {
        super(name);
        this.num = num;
    }

    public String toString() {
        return (super.toString() + "\nNum:  " + num);
    }
}
