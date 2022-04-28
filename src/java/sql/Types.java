/*
 * @(#)Types.java	1.6 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

/**
 * 此类定义用于标识 SQL 类型的常量。实际的类型常量值等同于 XOPEN 中的值。
 */
public class Types {

	public final static int BIT 		=  -7;
	public final static int TINYINT 	=  -6;
	public final static int SMALLINT	=   5;
	public final static int INTEGER 	=   4;
	public final static int BIGINT 		=  -5;

	public final static int FLOAT 		=   6;
	public final static int REAL 		=   7;
	public final static int DOUBLE 		=   8;

	public final static int NUMERIC 	=   2;
	public final static int DECIMAL		=   3;

	public final static int CHAR		=   1;
	public final static int VARCHAR 	=  12;
	public final static int LONGVARCHAR 	=  -1;

	public final static int DATE 		=  91;
	public final static int TIME 		=  92;
	public final static int TIMESTAMP 	=  93;

	public final static int BINARY		=  -2;
	public final static int VARBINARY 	=  -3;
	public final static int LONGVARBINARY 	=  -4;

	public final static int NULL		=   0;

    /**
     * OTHER 表示 SQL 类型是特定于数据库的，并被映射到可以通过 getObject 和 setObject 访问的 Java 对象。
     */
	public final static int OTHER		= 1111;

    // Prevent instantiation（防止实例化）
    private Types() {}
}
