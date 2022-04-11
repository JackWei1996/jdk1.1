/*
 * @(#)Serializable.java	1.8 2001/12/12
 *
 * 版权所有 2002年 Sun Microsystems，Inc.保留所有权利。
 * SUN 所有者/机密。使用受许可条款约束。
 */

package java.io;

/**
 * 类的序列化由 java.io.Serializable 接口来标识。
 * 没有实现此接口的类是无法序列化或反序列化的。
 * 可序列化的子类也是可以序列化的。
 * 序列化接口没有方法或字段，只是用于标识可序列化。
 * 
 * To allow subtypes of non-serializable classes to be serialized, the
 * subtype may assume responsibility for saving and restoring the
 * state of the supertype's public, protected, and (if accessible)
 * package fields.  The subtype may assume this responsibility only if
 * the class it extends has an accessible no-arg constructor to
 * initialize the class's state.  It is an error to declare a class
 * Serializable in this case.  The error will be detected at runtime. <p>
 * 
 * During deserialization, the fields of non-serializable classes will
 * be initialized using the public or protected no-arg constructor of
 * the class.  A no-arg constructor must be accessible to the subclass
 * that is serializable.  The fields of serializable subclasses will
 * be restored from the stream. <p>
 *
 * When traversing a graph, an object may be encountered that does not
 * support the Serializable interface. In this case the
 * NotSerializableException will be thrown and will identify the class
 * of the non-serializable object. <p>
 *
 * Classes that require special handling during the serialization and deserialization
 * process must implement special methods with these exact signatures: <p>
 *
 * <PRE>
 * private void writeObject(java.io.ObjectOutputStream out)
 *     throws IOException
 * private void readObject(java.io.ObjectInputStream in)
 *     throws IOException, ClassNotFoundException; 
 * </PRE><p>

 * The writeObject method is responsible for writing the state of the
 * object for its particular class so that the corresponding
 * readObject method can restore it.  The default mechanism for saving
 * the Object's fields can be invoked by calling
 * out.defaultWriteObject. The method does not need to concern
 * itself with the state belonging to its superclasses or subclasses.
 * State is saved by writing the individual fields to the
 * ObjectOutputStream using the writeObject method or by using the
 * methods for primitive data types supported by DataOutput. <p>

 * The readObject method is responsible for reading from the stream and restoring
 * the classes fields. It may call in.defaultReadObject to invoke
 * the default mechanism for restoring the object's non-static and non-transient
 * fields.  The defaultReadObject method uses information in the stream to
 * assign the fields of the object saved in the stream with the correspondingly
 * named fields in the current object.  This handles the case when the class
 * has evolved to add new fields. The method does not need to concern
 * itself with the state belonging to its superclasses or subclasses.
 * State is saved by writing the individual fields to the
 * ObjectOutputStream using the writeObject method or by using the
 * methods for primitive data types supported by DataOutput. <p>
 *
 * @author  unascribed
 * @version 1.8, 12/12/01
 * @see ObjectOutputStream
 * @see ObjectInputStream
 * @see ObjectOutput
 * @see ObjectInput
 * @see Externalizable
 * @since   JDK1.1
 */
public interface Serializable {
}
