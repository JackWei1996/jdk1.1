/*
 * 官方地址： https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/custom/CustomDataExample.java
 */

package jack.io.demo.ser.case2;

import java.io.*;

/**
 * 此示例说明如何使用 writeObject 和 readObject 对自定义数据格式进行编码。
 * 当持久化数据笨重时，适合以更方便、更简洁的格式存储。
 *
 * 具体来说，这个例子考虑了三角阵列的情况。三角阵列只是一个对称的二维阵列。
 * 所以在序列化它时，最好只保存二维数组中的 1/2 个而不是全部。
 *
 * 这与序列化和可序列化字段 API 示例的不同之处在于该示例不支持版本控制。
 * 此示例与使用 Externalizable 接口的不同之处在于此示例不必担心超类。
 *
 * Complied and tested on JDK 1.1 & the Java 2 SDK, v1.2.
 *
 * How to run this example:
 *                         Compile this file: javac CustomDataExample.java
 *                         Then run:          java CustomDataExample
 *
 * 这将打印出两个数组：一个来自序列化之前，另一个来自反序列化之后。
 */
public class CustomDataExample implements Serializable {

    transient int dimension;
    transient int thearray[][];

    /**
     * 创建维度为dim的三角数组并初始化
     */
    CustomDataExample (int dim) {
        dimension = dim;
        thearray = new int[dim][dim];
        arrayInit();
    }

    /**
     * 创建一个 CustomDataExample 对象，对其进行序列化、反序列化并查看它们是否相同。
     * 所以，基本上测试这个自定义数据示例的序列化是否有效。
     */
    public static void main(String args[]) {
        CustomDataExample corg = new CustomDataExample(4);
        CustomDataExample cnew = null;

        // 序列化原始类对象
        try {
            FileOutputStream fo = new FileOutputStream("cde.tmp");
            ObjectOutputStream so = new ObjectOutputStream(fo);
            so.writeObject(corg);
            so.flush();
            so.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // 反序列化为新的类对象
        try {
            FileInputStream fi = new FileInputStream("cde.tmp");
            ObjectInputStream si = new ObjectInputStream(fi);
            cnew = (CustomDataExample) si.readObject();
            si.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // 打印出来检查正确性
        System.out.println();
        System.out.println("Printing the original array...");
        System.out.println(corg);
        System.out.println();
        System.out.println("Printing the new array...");
        System.out.println(cnew);
        System.out.println();
        System.out.println("The original and new arrays should be the same!");
        System.out.println();
    }

    /**
     * 将二维数组的维度的1/2 写入 ObjectOutputStream 。 readObject 取决于此数据格式。
     *
     * @serialData 写入可序列化字段（如果存在）。写出对称二维数组的整数 Dimension。写出组成二维数组的 1/2 个整数。
     */
    private void writeObject(ObjectOutputStream s)
            throws IOException {
        // 即使没有默认的可序列化字段也调用。
        s.defaultWriteObject();

        // 保存维度
        s.writeInt(dimension);

        // 只写入二维数组的1/2
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j <= i; j++) {
                s.writeInt(thearray[i][j]);
            }
        }
    }

    /**
     * 从 ObjectInputStream 中读取二维数组的维度的1/2。
     * 由 writeObject 写入。此外，将1/2数组复制到另一半以完全填充对称数组。
     *
     * @serialData 读取可序列化字段（如果存在）。读取由表示二维数组的两个维度的整数组成的可选数据。读入1/2个二维数组。
     */
    private void readObject(ObjectInputStream s)
            throws IOException, ClassNotFoundException  {
        /*
         *即使没有默认的可序列化字段也调用。允许在未来版本中添加默认可序列化字段，并被没有默认可序列化字段的此版本跳过。
         */
        s.defaultReadObject();

        // 恢复维度
        dimension = s.readInt();

        // 为数组分配空间
        thearray = new int[dimension][dimension];

        // 先恢复二维数组的1/2
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j <= i; j++) {
                thearray[i][j] = s.readInt();
            }
        }

        // 复制到另一边
        for (int i = 0; i < dimension; i++) {
            for (int j = dimension - 1; j > i; j--) {
                thearray[i][j] = thearray[j][i];
            }
        }
    }

    /**
     * 将数组初始化为从 0 开始的一些数字 - 使其对称
     */
    void arrayInit() {
        int x = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j <= i; j++) {
                thearray[i][j] = x;
                thearray[j][i] = x;
                x++;
            }
        }
    }

    /**
     * 打印二维数组。对测试很有用。
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                sb.append(Integer.toString(thearray[i][j])+ " ");
            }
            sb.append("\n");
        }
        return(sb.toString());
    }
}
