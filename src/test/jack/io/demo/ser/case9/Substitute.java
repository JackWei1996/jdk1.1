/*
 * 官网地址：https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/symbol/Substitute.java
 */

package jack.io.demo.ser.case9;

import java.io.*;
import java.util.*;


/**
 * 此示例说明如何使用 readResolve 方法。此方法在返回给调用者之前解析从流中读取的对象。
 * writeReplacea 方法允许对象在写入对象之前在流中指定自己的替换。
 *
 * 此示例创建一个符号类，其中每个符号绑定只存在一个实例。 Symbol 类定义了 readResolve 方法。
 * 使用 symbollookup 方法从外部创建符号，如果一个符号已经存在，则该方法查找并返回一个符号，如果不存在则创建一个。
 * 这确保了一个 VM 内的唯一性。然后，当读取符号时调用 readResolve 时，
 * 如果存在这样的符号，则从哈希表中替换预先存在的等效符号对象以保持唯一标识约束。
 * 否则，将新符号添加到哈希表并返回。当我们处理多个 VM 时，这确保了唯一性。
 *
 *
 * How to Run:
 *             Compile this file: javac Substitute.java
 *             Run this file:     java Substitute
 *
 * 这将打印出一个确认，即分别序列化但具有相同名称的两个符号确实是相同的符号。
 *
 *
 * 用JDK1.2编译测试
 */

public class Substitute {

    /**
     * 基本上，序列化和反序列化两个具有相同名称的符号并显示它们实际上是相同的符号。
     */
    public static void main(String args[]) {

        // 创建一些要序列化的符号
        Symbol s1 = Symbol.symbolLookup("blue");
        Symbol s2 = Symbol.symbolLookup("pink");
        Symbol s3 = Symbol.symbolLookup("blue");

        // 使用这些来反序列化符号
        Symbol obj1 = null, obj2 = null, obj3 = null;


        // serialize the symbols
        try {
            FileOutputStream fo = new FileOutputStream("symbol.tmp");
            ObjectOutputStream so = new ObjectOutputStream(fo);
            so.writeObject(s1);
            so.writeObject(s2);
            so.writeObject(s3);
            so.flush();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }

        // deserialize the symbols
        try {
            FileInputStream fi = new FileInputStream("symbol.tmp");
            ObjectInputStream si = new ObjectInputStream(fi);
            obj1 = (Symbol) si.readObject();
            obj2 = (Symbol) si.readObject();
            obj3 = (Symbol) si.readObject();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }

        // show the uniqueness（展现独特性）
        if (obj1 == obj3) {
            System.out.println("Symbol1 and Symbol3 are the same!");
        }
    }
}


/**
 * 实现 readResolve 方法的类。
 */
class Symbol implements Serializable {

    /**
     * @serial
     */
    String symbolname;

    /*
     * Hashtable 是静态的，因为我们需要对所有符号对象使用同一个。
     */
    static Hashtable ht = new Hashtable();

    /**
     * 此方法用作构造函数。它在哈希表中查找，如果该符号存在，将返回该符号......否则，
     * 将创建一个具有该名称的符号并将其添加到哈希表中。这将确保符号始终是唯一的。
     */
    static Symbol symbolLookup(String symname) {
        if (ht.containsKey(symname)) {
            return (Symbol)(ht.get(symname));
        }
        else {
            Symbol newSym = new Symbol(symname);
            ht.put(symname, newSym);
            return(newSym);
        }
    }

    /**
     * 私有构造函数，因为我们希望“局外人”使用 symbolLookup 来强制唯一性。
     */
    private Symbol (String name) {
        symbolname = name;
    }

    /**
     * 当我们处理多个 VM 时，通过将读取符号添加到哈希表来处理唯一性问题，如果它不存在的话。
     */
    public Object readResolve() throws ObjectStreamException {
        if (!ht.containsKey(symbolname))
            ht.put(symbolname, this);
        return (Symbol) (ht.get(symbolname));
    }
}
