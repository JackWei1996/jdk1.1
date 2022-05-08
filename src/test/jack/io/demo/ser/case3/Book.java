/*
 * 官方地址： https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/nonserialsuper/Book.java
 */

package jack.io.demo.ser.case3;

import java.io.*;

class Book extends ReadingMaterial implements Serializable {

    int numpages;
    String name;
    boolean ishardcover;

    // 其他相关信息和方法
    Book(int pages, String n, boolean hardcover, String author,
         String subject, int yearwritten)
    {
        super(author, subject, yearwritten);
        numpages = pages;
        name = n;
        ishardcover = hardcover;
    }

    /**
     * 通过调用 defaultWriteObject 保存自己的字段，然后显式保存其超类型的字段
     *
     * @serialData 通过调用 defaultWriteObject 存储自己的可序列化字段并将超类型字段保存为可选数据。
     * 可选数据按以下顺序写入； author 字段写为对象，subject 是对象，yearwritten 字段写为整数。
     */
    private void writeObject(ObjectOutputStream out)  throws IOException {

        // 首先通过调用 defaultWriteObject 来处理此类的字段
        out.defaultWriteObject();

        /*
         * 由于超类没有实现 Serializable 接口，我们显式地进行了保存……由于这些字段不是私有的，我们可以直接访问它们。
         * 如果它们是私有的，则超类必须实现 get 和 set 方法，这些方法将允许子类进行必要的访问以正确保存。
         */
        out.writeObject(author);
        out.writeObject(subject);
        out.writeInt(yearwritten);
    }

    /**
     * 通过调用 defaultReadObject 恢复其自己的字段，然后显式恢复其超类型的字段。
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {

        /*
         * 首先通过调用 defaultReadObject 来处理此类的字段
         */
        in.defaultReadObject();

        /*
         * 由于超类没有实现 Serializable 接口，我们显式地进行了恢复……因为这些字段不是私有的，我们可以直接访问它们。
         * 如果它们是私有的，则超类将必须实现 get 和 set 方法，这些方法将允许子类进行必要的访问以进行正确的保存或恢复。
         */
        author = (String) in.readObject();
        subject = (String) in.readObject();
        yearwritten = in.readInt();
    }

    /**
     * 打印出字段值。对测试很有用。
     */
    public String toString() {
        return("Name: " + name + "\n" + "Author: " + author + "\n" + "Pages: "
                + numpages + "\n" + "Subject: " + subject + "\n" + "Year: " + yearwritten
                + "\n");
    }
}