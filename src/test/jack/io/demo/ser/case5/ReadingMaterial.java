/*
 * 官方地址： https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/nonexternsuper/ReadingMaterial.java
 */

package jack.io.demo.ser.case5;

import java.io.*;

/**
 * 超类不实现可外部化
 */
class ReadingMaterial {

    /*
     * 为了使我们能够序列化这些数据，这些数据必须是可序列化的可外部化对象或原始数据类型。
     *
     *我们没有将它们设为私有，因为我们需要子类 Book 才能保存这个超类的状态。
     * 或者，我们可以将这些设为私有并创建允许访问 Book 子类的 set 和 get 函数。
     * 如果我们没有对私有字段做到这一点，那么字段将无法保存！
     */
    String author;
    String subject;
    int yearwritten;

    // 其他相关数据和方法

    /*
     * 一个公共的无参数构造函数，以便可外部化的子类可以使用它。
     * 如果这不存在，那么子类将不得不在其自己的公共无参数构造函数中使用默认参数调用常规参数构造函数。
     */
    public ReadingMaterial() {}

    ReadingMaterial(String auth, String sub, int year) {
        author = auth;
        subject = sub;
        yearwritten = year;
    }
}
