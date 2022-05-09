/*
 * 官方地址： https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/externsuper/ReadingMaterial.java
 */

package jack.io.demo.ser.case4;

import java.io.*;

/**
 * Externalizable超类：当Externalizable子类Book实现其writeExternal和readExternal方法时，
 * 需要使用超类的writeExternal和readExternal方法保存其超类的状态
 */
public class ReadingMaterial implements Externalizable {

    /*
     * 为了使我们能够序列化这些数据，这些数据必须是可序列化的可外部化对象或原始数据类型。
     */
    private  String author;
    private  String subject;
    private  int yearwritten;

    // 其他相关数据和方法

    /*
     * 实现 Externalizable 时必须有一个公共的无参数构造函数
     */
    public ReadingMaterial() {}

    /**
     * 初始化字段
     */
    public ReadingMaterial(String auth, String sub, int year) {
        author = auth;
        subject = sub;
        yearwritten = year;
    }

    /**
     * 公共字段访问方法，因为数据字段是私有的，需要由子类访问以打印它们或以其他方式使用它们。
     */
    public String getAuthor() {
        return author; }
    /**
     * 字段访问方法，因为数据字段是私有的，需要由子类访问以打印它们或以其他方式使用它们。
     */
    public String getSubject() {
        return subject; }
    /**
     * 字段访问方法，因为数据字段是私有的，需要由子类访问以打印它们或以其他方式使用它们。
     */
    public int getYearwritten() {
        return yearwritten; }

    /**
     * 强制 writeExternal 方法。
     * @serialData 将作者和主题字段写为对象，然后将年份字段写为整数。
     */
    public void writeExternal(ObjectOutput out) throws IOException {

        out.writeObject(author);
        out.writeObject(subject);
        out.writeInt(yearwritten);
    }

    /**
     * 强制读取外部方法。将读入我们在 writeExternal 方法中写出的数据。
     * 必须按照我们写出来的顺序和类型。到调用 readExternal 时，
     * 已经使用 public no-arg 构造函数创建了该类的对象，因此该方法用于将数据恢复到新创建对象的所有字段。
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        author = (String)in.readObject();
        subject = (String)in.readObject();
        yearwritten = in.readInt();
    }
}
