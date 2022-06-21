/*
 * 官网地址： https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/nonexternsuper/Book.java
 */

package jack.io.demo.ser.case5;

import java.io.*;

/**
 * 实现Externalizable的子类负责保存其不可Externalizable超类的状态
 */
class Book extends ReadingMaterial implements Externalizable {

    int numpages;
    String name;
    boolean ishardcover;

    // 其他相关数据和方法

    /*
     * 强制公共无参数构造函数如果超类没有无参数构造函数，将不得不为常规构造函数提供默认值。
     */
    public Book() { super(); }

    Book(int pages, String n, boolean hardcover, String author,
         String subject, int yearwritten) {

        super(author, subject, yearwritten);
        numpages = pages;
        name = n;
        ishardcover = hardcover;
    }

    /**
     * 强制 writeExernal 方法。
     *
     * @serialData 显式保存超类型的字段，然后保存此类字段。
     * 按以下顺序写入超类字段：作者作为对象，主题作为对象，年份写为整数。
     * 按以下顺序写入类字段：numpages 作为 int，name 作为 Object，ishardcover 作为 boolean。
     */
    public void writeExternal(ObjectOutput out) throws IOException  {

        /*
         * 由于超类没有实现 Serializable 接口，我们显式地进行了保存。由于这些字段不是私有的，我们可以直接访问它们。
         * 如果它们是私有的，则超类必须实现 get 方法，以允许子类进行必要的访问以正确保存。
         */
        out.writeObject(author);
        out.writeObject(subject);
        out.writeInt(yearwritten);

        // 现在我们处理这个子类的字段
        out.writeInt(numpages);
        out.writeObject(name);
        out.writeBoolean(ishardcover);
    }

    /**
     * 强制读取外部方法。显式恢复超类型的字段，然后恢复此类的字段
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        /*
         * 由于超类没有实现 Serializable 接口，我们显式地进行了恢复。因为这些字段不是私有的，我们可以直接访问它们。
         * 如果它们是私有的，则超类必须实现 set 方法，这些方法将允许子类进行必要的访问以进行正确的恢复。
         */
        author = (String) in.readObject();
        subject = (String) in.readObject();
        yearwritten = in.readInt();

        // 现在我们处理子类的字段
        numpages = in.readInt();
        name = (String) in.readObject();
        ishardcover = in.readBoolean();
    }

    /**
     * 打印出字段。用于测试！
     */
    public String toString() {
        return("Name: " + name + "\n" + "Author: " + author + "\n" + "Pages: "
                + numpages + "\n" + "Subject: " + subject + "\n" + "Year: " + yearwritten + "\n");
    }
}
