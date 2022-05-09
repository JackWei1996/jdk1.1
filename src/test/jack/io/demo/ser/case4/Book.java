/*
 * 官方地址： https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/externsuper/Book.java
 */

package jack.io.demo.ser.case4;

import java.io.*;

/**
 * Externalizable 子类。将通过调用超类的 writeExternal 和 readExternal 方法,
 * 从其自己的 writeExternal 和 readExternal 方法中保存其超类的状态
 */
public class Book extends ReadingMaterial implements Externalizable {

    private int numpages;
    private String name;
    private boolean ishardcover;

    // 其他相关信息和方法

    /**
     * 强制公共无参数构造函数
     */
    public Book() {
        super(); }

    public Book(int pages, String n, boolean hardcover, String author,
                String subject, int yearwritten) {

        super(author, subject, yearwritten);
        numpages = pages;
        name = n;
        ishardcover = hardcover;
    }

    /**
     * 强制 writeExternal 方法。
     *
     * @serialData 通过调用其 writeExternal 方法保存其超类的状态，然后保存其自己的字段。
     * 将 numpages 字段写入 int，将 name 字段写入对象，将 ishardcover 字段写入布尔值。
     *
     * @see ReadingMaterial#writeExternal(ObjectOutput)
     */
    public void writeExternal(ObjectOutput out) throws IOException {

        // 首先我们调用超类的writeExternal来写入所有超类的数据字段
        super.writeExternal(out);

        // 现在我们处理这个类的字段
        out.writeInt(numpages);
        out.writeObject(name);
        out.writeBoolean(ishardcover);
    }

    /**
     * 强制读取外部方法。将读入我们在 writeExternal 方法中写入的数据。
     * 首先通过调用超类的 readExternal 方法恢复超类的状态。然后，恢复它自己的字段。
     * 这些字段必须与我们写出的顺序和类型相同。
     * 到调用 readExternal 时，已经使用无参构造函数创建了该类的对象，
     * 因此该方法用于将数据恢复到新创建对象的所有字段。
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        // 首先调用超类外部方法
        super.readExternal(in);

        // 现在处理这个子类的字段
        numpages = in.readInt();
        name = (String)in.readObject();
        ishardcover= in.readBoolean();
    }

    /**
     * 打印出字段。用于测试！
     */
    public String toString() {
        return("Name: " + name + "\n" + "Author: " + super.getAuthor() + "\n" + "Pages: "
                + numpages + "\n" + "Subject: " + super.getSubject() + "\n" + "Year: " + super.getYearwritten() + "\n" );
    }
}