/*
 * @(#)Serializable.java	1.8 2001/12/12
 *
 * 版权所有 2002年 Sun Microsystems，Inc.保留所有权利。
 * SUN 所有者/机密。使用受许可条款约束。
 */

package java.io;

/**
 * 类的序列化由java.io.Serializable接口来标识。
 * 没有实现此接口的类是无法序列化或反序列化的。
 * 可序列化的子类也是可以序列化的。
 * 序列化接口没有方法或字段，只是用于标识可序列化。
 * 
 * 为了使不可序列化类的子类允许序列化并且负责保存和恢复超类的公共、受保护和（若可访问）包字段的状态。
 * 只有当它扩展的类具有可访问的无参构造函数来初始化类的状态时，子类才可以实现上面的功能。
 * 在这种情况下，声明一个类Serializable是错误的。这会在运行时检测到错误。
 * 
 * 在反序列化过程中，不可序列化类的字段将使用类的公共或受保护的无参数构造函数进行初始化。
 * 可序列化的子类必须可以访问无参数构造函数。可序列化子类的字段将从流中恢复。
 *
 * 在遍历时，可能会遇到不支持Serializable接口的对象。此时，会抛出NotSerializableException异常，并识别不可序列化对象的类。
 *
 * 在序列化和反序列化过程中需要特殊处理的类必须实现具有这些签名的特殊方法：
 *
 * private void writeObject(java.io.ObjectOutputStream out)
 *     throws IOException
 * private void readObject(java.io.ObjectInputStream in)
 *     throws IOException, ClassNotFoundException;

 * writeObject方法负责为其特定类写入对象的状态，以便相应的readObject方法可以恢复它。
 * 可以通过调用out.defaultWriteObject来保存Object字段的默认机制。该方法不需要关注属于其超类或子类的状态。
 * 可通过使用writeObject方法或使用DataOutput支持的原始数据类型的方法将各个字段写入ObjectOutputStream来保存对象的状态。

 * readObject方法负责从流中读取并恢复类字段。它可以调用in.defaultReadObject来恢复对象的非静态和非瞬态字段的默认机制。
 * defaultReadObject方法使用流中的信息将保存在流中的对象的字段分配给当前对象中相应命名的字段。
 * 这可以处理类已经演变为添加新字段的情况。该方法不需要关注属于其超类或子类的状态。
 * 通过使用writeObject方法或使用DataOutput支持的原始数据类型的方法将各个字段写入ObjectOutputStream来保存对象的状态。
 *
 * @author  名可名，非常名
 * @version 1.8, 2001/12/12
 * @see ObjectOutputStream
 * @see ObjectInputStream
 * @see ObjectOutput
 * @see ObjectInput
 * @see Externalizable
 * @since   JDK1.1
 */
public interface Serializable {
}
