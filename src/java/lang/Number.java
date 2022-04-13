/*
 * @(#)Number.java	1.22 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。
 * SUN 专有机密。使用受许可条款的约束。
 */

package java.lang;

/**
 *
 * 抽象类 Number 是类 Byte、 Double、Float、Integer、Long 和 Short 的超类。
 * Number 的子类必须提供将表示的数值转换为 byte、double、float、int、long 和 short 的方法。
 *
 * @author	Lee Boynton（李博因顿）
 * @author	Arthur van Hoff（阿瑟·范霍夫）
 * @version 1.22, 2001/12/12
 * @see     Byte
 * @see     Double
 * @see     Float
 * @see     Integer
 * @see     Long
 * @see     Short
 * @since   JDK1.0
 */
public abstract class Number implements java.io.Serializable {
    /**
     * 以 int 形式返回指定数字的值。这可能涉及四舍五入。
     *
     * @return  转换为 int 的值
     * @since   JDK1.0
     */
    public abstract int intValue();

    /**
     * 以 long 形式返回指定数字的值。这可能涉及四舍五入。
     *
     * @return  转换为 long 的值
     * @since   JDK1.0
     */
    public abstract long longValue();

    /**
     * 以 float 形式返回指定数字的值。这可能涉及四舍五入。
     *
     * @return  转换为 float 的值
     * @since   JDK1.0
     */
    public abstract float floatValue();

    /**
     * 以 double 形式返回指定数字的值。这可能涉及四舍五入。
     *
     * @return  转换为 double 的值
     * @since   JDK1.0
     */
    public abstract double doubleValue();

    /**
     * 以 byte 形式返回指定数字的值。这可能涉及四舍五入。
     *
     * @return  转换为 byte 的值
     * @since   JDK1.1
     */
    public byte byteValue() {
	return (byte)intValue();
    }

    /**
     * 以 short 形式返回指定数字的值。这可能涉及四舍五入。
     *
     * @return  转换为 short 的值
     * @since   JDK1.1
     */
    public short shortValue() {
	return (short)intValue();
    }

    /** 使用 JDK 1.0.2 中的 serialVersionUID 以实现互操作性 */
    private static final long serialVersionUID = -8742448824652078965L;
}
