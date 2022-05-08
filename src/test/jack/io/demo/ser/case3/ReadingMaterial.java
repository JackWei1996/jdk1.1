/*
 * 官方地址： https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/nonserialsuper/ReadingMaterial.java
 */

package jack.io.demo.ser.case3;

import java.io.*;

// 超类不实现可序列化
class ReadingMaterial  {

    /**
     * 我们没有将它们设为私有，因为我们需要子类 Book 才能保存这个超类的状态。
     * 或者，我们可以将这些设为私有并创建允许访问 Book 子类的 set 和 get 函数。
     * 如果我们没有对私有字段做到这一点，那么字段将无法保存！
     */
    protected String author;
    protected String subject;
    protected int yearwritten;

    // 其他相关数据和方法
    /*
     * 一个强制性的公共无参数构造函数......将用于重建这个不可序列化的类。
     */
    public ReadingMaterial() {}

    ReadingMaterial(String auth, String sub, int year) {
        author = auth;
        subject = sub;
        yearwritten = year;
    }
}