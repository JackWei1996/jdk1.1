/*
 * @(#)CallableStatement.java	1.9 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

import java.math.BigDecimal;

/**
 * CallableStatement 用于执行 SQL 存储过程。
 *
 * JDBC 提供了一种存储过程 SQL 转义，允许以标准方式为所有 RDBMS 调用存储过程。
 * 这种转义语法有一种形式包含结果参数，另一种形式不包含。
 * 如果使用，结果参数必须注册为 OUT 参数。其他参数可用于输入、输出或两者。
 * 参数按编号顺序引用。第一个参数是 1。
 *
 * <CODE>
 * {?= call <procedure-name>[<arg1>,<arg2>, ...]}<BR>
 * {call <procedure-name>[<arg1>,<arg2>, ...]}
 * </CODE>
 *    
 * IN 参数值是使用从 PreparedStatement 继承的 set 方法设置的。
 * 所有 OUT 参数的类型必须在执行存储过程之前注册；
 * 它们的值在执行后通过此处提供的 get 方法检索。
 *
 * 一个 Callable 语句可能返回一个 ResultSet 或多个 ResultSet。
 * 使用从 Statement 继承的操作来处理多个 ResultSet。
 *
 * 为了获得最大的可移植性，调用的 ResultSet 和更新计数应在获取输出参数的值之前进行处理。
 *
 * @see Connection#prepareCall
 * @see ResultSet 
 */
public interface CallableStatement extends PreparedStatement {

    /**
     * 在执行存储过程调用之前，必须显式调用 registerOutParameter 来注册每个 out 参数的 java.sql.Type。
     *
     * Note: 读取out参数的值时，必须使用getXXX方法，其Java类型XXX对应参数注册的SQL类型。
     *
     * @param parameterIndex 第一个参数是1，第二个是2，...
     * @param sqlType java.sql.Types定义的SQL类型码；
     * 对于 Numeric 或 Decimal 类型的参数，使用接受比例值的 registerOutParameter 版本
     * @exception SQLException 如果发生数据库访问错误。
     * @see Type 
     */
    void registerOutParameter(int parameterIndex, int sqlType)
	    throws SQLException;

    /**
     * 使用此版本的 registerOutParameter 注册数字或小数输出参数。
     *
     * Note: 读取out参数的值时，必须使用getXXX方法，其Java类型XXX对应参数注册的SQL类型。
     *
     * @param parameterIndex 第一个参数是1，第二个是2，...
     * @param sqlType 使用 java.sql.Type.NUMERIC 或 java.sql.Type.DECIMAL
     * @param scale 大于或等于零的值，表示小数点右侧所需的位数
     * @exception SQLException 如果发生数据库访问错误。
     * @see Type 
     */
    void registerOutParameter(int parameterIndex, int sqlType, int scale)
	    throws SQLException;

    /**
     * OUT 参数的值可能为 SQL NULL； wasNull 报告最后读取的值是否具有此特殊值。
     *
     * Note: 您必须首先对参数调用 getXXX 以读取其值，然后调用 wasNull() 以查看该值是否为 SQL NULL。
     *
     * @return 如果最后读取的参数是 SQL NULL，则为 true
     * @exception SQLException 如果发生数据库访问错误。
     */
    boolean wasNull() throws SQLException;

    /**
     * 将 CHAR、VARCHAR 或 LONGVARCHAR 参数的值作为 Java 字符串获取。
     *
     * @param parameterIndex 第一个参数是1，第二个是2，...
     * @return 参数值；如果值为 SQL NULL，则结果为 null
     * @exception SQLException 如果发生数据库访问错误。
     */
    String getString(int parameterIndex) throws SQLException;

    /**
     * 获取 BIT 参数的值作为 Java 布尔值。
     *
     * @param parameterIndex 第一个参数是1，第二个是2，...
     * @return 参数值；如果值为 SQL NULL，则结果为 false
     * @exception SQLException 如果发生数据库访问错误。
     */
    boolean getBoolean(int parameterIndex) throws SQLException;

    /**
     * 将 TINYINT 参数的值作为 Java 字节获取。
     *
     * @param parameterIndex 第一个参数是1，第二个是2，...
     * @return 参数值；如果值为 SQL NULL，则结果为 0
     * @exception SQLException 如果发生数据库访问错误。
     */
    byte getByte(int parameterIndex) throws SQLException;

    /**
     * 获取 SMALLINT 参数的值作为 Java 缩写。
     *
     * @param parameterIndex 第一个参数是1，第二个是2，...
     * @return 参数值；如果值为 SQL NULL，则结果为 0
     * @exception SQLException 如果发生数据库访问错误。
     */
    short getShort(int parameterIndex) throws SQLException;

    /**
     * 将 INTEGER 参数的值作为 Java int 获取。
     *
     * @param parameterIndex 第一个参数是1，第二个是2，...
     * @return 参数值；如果值为 SQL NULL，则结果为 0
     * @exception SQLException 如果发生数据库访问错误。
     */
    int getInt(int parameterIndex) throws SQLException;

    /**
     * 将 BIGINT 参数的值作为 Java long 获取。
     *
     * @param parameterIndex 第一个参数是1，第二个是2，...
     * @return 参数值；如果值为 SQL NULL，则结果为 0
     * @exception SQLException 如果发生数据库访问错误。
     */
    long getLong(int parameterIndex) throws SQLException;

    /**
     * 将 FLOAT 参数的值作为 Java 浮点数获取。
     *
     * @param parameterIndex 第一个参数是1，第二个是2，...
     * @return 参数值；如果值为 SQL NULL，则结果为 0
     * @exception SQLException 如果发生数据库访问错误。
     */
    float getFloat(int parameterIndex) throws SQLException;

    /**
     * 将 DOUBLE 参数的值作为 Java double 获取。
     *
     * @param parameterIndex 第一个参数是1，第二个是2，...
     * @return 参数值；如果值为 SQL NULL，则结果为 0
     * @exception SQLException 如果发生数据库访问错误。
     */
    double getDouble(int parameterIndex) throws SQLException;

    /** 
     * 以 java.math.BigDecimal 对象的形式获取 NUMERIC 参数的值。
     *
     * @param parameterIndex 第一个参数是1，第二个是2，...
     *
     * @param scale 大于或等于零的值，表示小数点右侧所需的位数
     *
     * @return 参数值；如果值为 SQL NULL，则结果为 null
     * @exception SQLException 如果发生数据库访问错误。
     */
    BigDecimal getBigDecimal(int parameterIndex, int scale) 
    throws SQLException;

    /**
     * 以 Java byte[] 形式获取 SQL BINARY 或 VARBINARY 参数的值
     *
     * @param parameterIndex 第一个参数是1，第二个是2，...
     * @return 参数值；如果值为 SQL NULL，则结果为 null
     * @exception SQLException 如果发生数据库访问错误。
     */
    byte[] getBytes(int parameterIndex) throws SQLException;

    /**
     * 将 SQL DATE 参数的值作为 java.sql.Date 对象获取
     *
     * @param parameterIndex 第一个参数是1，第二个是2，...
     * @return 参数值；如果值为 SQL NULL，则结果为 null
     * @exception SQLException 如果发生数据库访问错误。
     */
    Date getDate(int parameterIndex) throws SQLException;

    /**
     * Get the value of a SQL TIME parameter as a java.sql.Time object.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return the parameter value; if the value is SQL NULL, the result is null
     * @exception SQLException if a database-access error occurs.
     */
    Time getTime(int parameterIndex) throws SQLException;

    /**
     * Get the value of a SQL TIMESTAMP parameter as a java.sql.Timestamp object.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return the parameter value; if the value is SQL NULL, the result is null
     * @exception SQLException if a database-access error occurs.
     */
    Timestamp getTimestamp(int parameterIndex)
	    throws SQLException;

    //----------------------------------------------------------------------
    // Advanced features:


    /**
     * Get the value of a parameter as a Java object.
     *
     * <p>This method returns a Java object whose type coresponds to the SQL
     * type that was registered for this parameter using registerOutParameter.
     *
     * <p>Note that this method may be used to read
     * datatabase-specific, abstract data types. This is done by
     * specifying a targetSqlType of java.sql.types.OTHER, which
     * allows the driver to return a database-specific Java type.
     *
     * @param parameterIndex The first parameter is 1, the second is 2, ...
     * @return A java.lang.Object holding the OUT parameter value.
     * @exception SQLException if a database-access error occurs.
     * @see Types 
     */
    Object getObject(int parameterIndex) throws SQLException;

}

