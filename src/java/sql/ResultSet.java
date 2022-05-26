/*
 * @(#)ResultSet.java	1.9 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

import java.math.BigDecimal;

/**
 * ResultSet 提供对通过执行 Statement 生成的数据表的访问。表行按顺序检索。在一行内，可以按任何顺序访问其列值。
 * 
 * ResultSet 维护一个指向其当前数据行的游标。最初，光标位于第一行之前。 'next' 方法将光标移动到下一行。
 *
 * getXXX 方法检索当前行的列值。您可以使用列的索引号或使用列的名称来检索值。一般来说使用列索引会更有效率。列从 1 开始编号。
 *
 * 为了获得最大的可移植性，每行中的 ResultSet 列应按从左到右的顺序读取，并且每列应仅读取一次。
 *
 * 对于 getXXX 方法，JDBC 驱动程序尝试将底层数据转换为指定的 Java 类型并返回合适的 Java 值。
 * 有关使用 ResultSet.getXXX 方法从 SQL 类型到 Java 类型的允许映射，请参阅 JDBC 规范。
 *
 * 用作 getXXX 方法输入的列名不区分大小写。
 * 使用列名执行 getXXX 时，如果多个列具有相同的名称，则将返回第一个匹配列的值。
 * 列名选项设计为在 SQL 查询中使用列名时使用。对于查询中未明确命名的列，最好使用列号。
 * 如果使用了列名，程序员就无法保证它们实际上引用了预期的列。
 *
 * 当 Statement 关闭、重新执行或用于从多个结果序列中检索下一个结果时，生成它的 Statement 会自动关闭 ResultSet。
 * 
 * ResultSet 列的数量、类型和属性由 getMetaData 方法返回的 ResulSetMetaData 对象提供。
 *
 * @see Statement#executeQuery 
 * @see Statement#getResultSet 
 * @see ResultSetMetaData 
 */

public interface ResultSet {

    /**
     * ResultSet 最初位于其第一行之前；第一次调用 next 使第一行成为当前行；第二次调用使第二行成为当前行，依此类推。
     *
     * 如果前一行的输入流是打开的，则它是隐式关闭的。读取新行时会清除 ResultSet 的警告链。
     *
     * @return 如果新的当前行有效，则为 true；如果没有更多行，则为 false
     * @exception SQLException if a database-access error occurs.
     */
    boolean next() throws SQLException;


    /**
     * 在某些情况下，希望立即释放 ResultSet 的数据库和 JDBC 资源，而不是等待它自动关闭时发生； close 方法提供了这个即时发布。
     *
     * Note: 当 Statement 关闭、重新执行或用于从多个结果序列中检索下一个结果时，
     * 生成它的 Statement 会自动关闭 ResultSet。 ResultSet 在被垃圾回收时也会自动关闭。
     *
     * @exception SQLException if a database-access error occurs.
     */
    void close() throws SQLException;

    /**
     * 列可能具有 SQL NULL 的值； wasNull 报告最后读取的列是否具有此特殊值。
     * 请注意，您必须首先对列调用 getXXX 以尝试读取其值，然后调用 wasNull() 以查找该值是否为 SQL NULL。
     *
     * @return true if last column read was SQL NULL
     * @exception SQLException if a database-access error occurs.
     */
    boolean wasNull() throws SQLException;
    
    //======================================================================
    // 按列索引访问结果的方法
    //======================================================================

    /**
     * 获取当前行中列的值作为 Java 字符串。
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL NULL, the result is null
     * @exception SQLException if a database-access error occurs.
     */
    String getString(int columnIndex) throws SQLException;

    /**
     * 获取当前行中列的值作为 Java 布尔值。
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL NULL, the result is false
     * @exception SQLException if a database-access error occurs.
     */
    boolean getBoolean(int columnIndex) throws SQLException;

    /**
     * 获取当前行中列的值作为 Java 字节。
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL NULL, the result is 0
     * @exception SQLException if a database-access error occurs.
     */
    byte getByte(int columnIndex) throws SQLException;

    /**
     * Get the value of a column in the current row as a Java short.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL NULL, the result is 0
     * @exception SQLException if a database-access error occurs.
     */
    short getShort(int columnIndex) throws SQLException;

    /**
     * Get the value of a column in the current row as a Java int.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL NULL, the result is 0
     * @exception SQLException if a database-access error occurs.
     */
    int getInt(int columnIndex) throws SQLException;

    /**
     * Get the value of a column in the current row as a Java long.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL NULL, the result is 0
     * @exception SQLException if a database-access error occurs.
     */
    long getLong(int columnIndex) throws SQLException;

    /**
     * Get the value of a column in the current row as a Java float.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL NULL, the result is 0
     * @exception SQLException if a database-access error occurs.
     */
    float getFloat(int columnIndex) throws SQLException;

    /**
     * Get the value of a column in the current row as a Java double.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL NULL, the result is 0
     * @exception SQLException if a database-access error occurs.
     */
    double getDouble(int columnIndex) throws SQLException;

    /**
     * Get the value of a column in the current row as a java.lang.BigDecimal object.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param scale the number of digits to the right of the decimal
     * @return the column value; if the value is SQL NULL, the result is null
     * @exception SQLException if a database-access error occurs.
     */
    BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException;

    /**
     * 获取当前行中列的值作为 Java 字节数组。字节代表驱动程序返回的原始值。
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL NULL, the result is null
     * @exception SQLException if a database-access error occurs.
     */
    byte[] getBytes(int columnIndex) throws SQLException;

    /**
     * Get the value of a column in the current row as a java.sql.Date object.z
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL NULL, the result is null
     * @exception SQLException if a database-access error occurs.
     */
    Date getDate(int columnIndex) throws SQLException;

    /**
     * Get the value of a column in the current row as a java.sql.Time object.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL NULL, the result is null
     * @exception SQLException if a database-access error occurs.
     */
    Time getTime(int columnIndex) throws SQLException;

    /**
     * Get the value of a column in the current row as a java.sql.Timestamp object.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL NULL, the result is null
     * @exception SQLException if a database-access error occurs.
     */
    Timestamp getTimestamp(int columnIndex) throws SQLException;

    /**
     * 列值可以作为 ASCII 字符流检索，然后从流中以块的形式读取。此方法特别适用于检索较大的 LONGVARCHAR 值。
     * JDBC 驱动程序将执行从数据库格式到 ASCII 的任何必要转换。
     *
     * Note: 在获取任何其他列的值之前，必须读取返回流中的所有数据。对 get 方法的下一次调用会隐式关闭流。
     * .此外，无论是否有可用数据，流都可能为 available() 返回 0。
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return 一个 Java 输入流，它将数据库列值作为一个字节的 ASCII 字符流传递。如果值为 SQL NULL，则结果为 null。
     * @exception SQLException if a database-access error occurs.
     */
    java.io.InputStream getAsciiStream(int columnIndex) throws SQLException;

    /**
     * 可以将列值作为 Unicode 字符流检索，然后从流中以块的形式读取。此方法特别适用于检索较大的 LONGVARCHAR 值。
     * JDBC 驱动程序将执行从数据库格式到 Unicode 的任何必要转换。
     *
     * Note: 在获取任何其他列的值之前，必须读取返回流中的所有数据。对 get 方法的下一次调用会隐式关闭流。
     * .此外，无论是否有可用数据，流都可能为 available() 返回 0。
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return 一个 Java 输入流，它将数据库列值作为两字节 Unicode 字符的流传递。如果值为 SQL NULL，则结果为 null。
     * @exception SQLException if a database-access error occurs.
     */
    java.io.InputStream getUnicodeStream(int columnIndex) throws SQLException;

    /**
     * 可以将列值作为未解释的字节流检索，然后从流中以块的形式读取。此方法特别适用于检索较大的 LONGVARBINARY 值。
     *
     * Note: 在获取任何其他列的值之前，必须读取返回流中的所有数据。
     * 对 get 方法的下一次调用会隐式关闭流。此外，无论是否有可用数据，流都可能为 available() 返回 0。
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return Java 输入流，将数据库列值作为未解释的字节流传递。如果值为 SQL NULL，则结果为 null。
     * @exception SQLException if a database-access error occurs.
     */
    java.io.InputStream getBinaryStream(int columnIndex)
        throws SQLException;


    //======================================================================
    // Methods for accessing results by column name（按列名访问结果的方法）
    //======================================================================

    /**
     * 字符串
     *
     * @param columnName is the SQL name of the column
     * @return the column value; if the value is SQL NULL, the result is null
     * @exception SQLException if a database-access error occurs.
     */
    String getString(String columnName) throws SQLException;

    /**
     * 获取当前行中列的值作为 Java 布尔值。
     *
     * @param columnName is the SQL name of the column
     * @return the column value; if the value is SQL NULL, the result is false
     * @exception SQLException if a database-access error occurs.
     */
    boolean getBoolean(String columnName) throws SQLException;

    /**
     * Get the value of a column in the current row as a Java byte.（获取当前行中列的值作为 Java 字节）
     *
     * @param columnName is the SQL name of the column
     * @return the column value; if the value is SQL NULL, the result is 0
     * @exception SQLException if a database-access error occurs.
     */
    byte getByte(String columnName) throws SQLException;

    /**
     * Get the value of a column in the current row as a Java short.（获取当前行中列的值作为 Java 短）
     *
     * @param columnName is the SQL name of the column
     * @return the column value; if the value is SQL NULL, the result is 0
     * @exception SQLException if a database-access error occurs.
     */
    short getShort(String columnName) throws SQLException;

    /**
     * Get the value of a column in the current row as a Java int.
     *
     * @param columnName is the SQL name of the column
     * @return the column value; if the value is SQL NULL, the result is 0
     * @exception SQLException if a database-access error occurs.
     */
    int getInt(String columnName) throws SQLException;

    /**
     * Get the value of a column in the current row as a Java long.
     *
     * @param columnName is the SQL name of the column
     * @return the column value; if the value is SQL NULL, the result is 0
     * @exception SQLException if a database-access error occurs.
     */
    long getLong(String columnName) throws SQLException;

    /**
     * Get the value of a column in the current row as a Java float.
     *
     * @param columnName is the SQL name of the column
     * @return the column value; if the value is SQL NULL, the result is 0
     * @exception SQLException if a database-access error occurs.
     */
    float getFloat(String columnName) throws SQLException;

    /**
     * Get the value of a column in the current row as a Java double.
     *
     * @param columnName is the SQL name of the column
     * @return the column value; if the value is SQL NULL, the result is 0
     * @exception SQLException if a database-access error occurs.
     */
    double getDouble(String columnName) throws SQLException;

    /**
     * Get the value of a column in the current row as a java.lang.BigDecimal object.
     *
     * @param columnName is the SQL name of the column
     * @param scale the number of digits to the right of the decimal
     * @return the column value; if the value is SQL NULL, the result is null
     * @exception SQLException if a database-access error occurs.
     */
    BigDecimal getBigDecimal(String columnName, int scale) throws SQLException;

    /**
     * 获取当前行中列的值作为 Java 字节数组。字节代表驱动程序返回的原始值。
     *
     * @param columnName is the SQL name of the column
     * @return the column value; if the value is SQL NULL, the result is null
     * @exception SQLException if a database-access error occurs.
     */
    byte[] getBytes(String columnName) throws SQLException;

    /**
     * Get the value of a column in the current row as a java.sql.Date object.
     *
     * @param columnName is the SQL name of the column
     * @return the column value; if the value is SQL NULL, the result is null
     * @exception SQLException if a database-access error occurs.
     */
    Date getDate(String columnName) throws SQLException;

    /**
     * Get the value of a column in the current row as a java.sql.Time object.
     *
     * @param columnName is the SQL name of the column
     * @return the column value; if the value is SQL NULL, the result is null
     * @exception SQLException if a database-access error occurs.
     */
    Time getTime(String columnName) throws SQLException;

    /**
     * Get the value of a column in the current row as a java.sql.Timestamp object.
     *
     * @param columnName is the SQL name of the column
     * @return the column value; if the value is SQL NULL, the result is null
     * @exception SQLException if a database-access error occurs.
     */
    Timestamp getTimestamp(String columnName) throws SQLException;

    /**
     * 列值可以作为 ASCII 字符流检索，然后从流中以块的形式读取。
     * 此方法特别适用于检索较大的 LONGVARCHAR 值。 JDBC 驱动程序将执行从数据库格式到 ASCII 的任何必要转换。
     *
     * Note: 在获取任何其他列的值之前，必须读取返回流中的所有数据。对 get 方法的下一次调用会隐式关闭流。
     *
     * @param columnName is the SQL name of the column
     * @return a Java input stream that delivers the database column value
     * as a stream of one byte ASCII characters.  If the value is SQL NULL
     * then the result is null.
     * @exception SQLException if a database-access error occurs.
     */
    java.io.InputStream getAsciiStream(String columnName) throws SQLException;

    /**
     * 可以将列值作为 Unicode 字符流检索，然后从流中以块的形式读取。
     * 此方法特别适用于检索较大的 LONGVARCHAR 值。 JDBC 驱动程序将执行从数据库格式到 Unicode 的任何必要转换。
     *
     * Note: 在获取任何其他列的值之前，必须读取返回流中的所有数据。对 get 方法的下一次调用会隐式关闭流。
     *
     * @param columnName is the SQL name of the column
     * @return 一个 Java 输入流，它将数据库列值作为两字节 Unicode 字符的流传递。如果值为 SQL NULL，则结果为 null。
     * @exception SQLException if a database-access error occurs.
     */
    java.io.InputStream getUnicodeStream(String columnName) throws SQLException;

    /**
     * 可以将列值作为未解释的字节流检索，然后从流中以块的形式读取。此方法特别适用于检索较大的 LONGVARBINARY 值。
     *
     * Note: 在获取任何其他列的值之前，必须读取返回流中的所有数据。对 get 方法的下一次调用会隐式关闭流。
     *
     * @param columnName is the SQL name of the column
     * @return Java 输入流，将数据库列值作为未解释的字节流传递。如果值为 SQL NULL，则结果为 null。
     * @exception SQLException if a database-access error occurs.
     */
    java.io.InputStream getBinaryStream(String columnName)
        throws SQLException;


    //=====================================================================
    // Advanced features(高级功能):
    //=====================================================================

    /**
     * 返回调用此 ResultSet 报告的第一个警告。随后的 ResultSet 警告将链接到此 SQLWarning。
     *
     * 每次读取新行时都会自动清除警告链。
     *
     * Note: 此警告链仅涵盖由 ResultSet 方法引起的警告。
     * 任何由语句方法（例如读取 OUT 参数）引起的警告都将链接在 Statement 对象上。
     *
     * @return the first SQLWarning or null 
     * @exception SQLException if a database-access error occurs.
     */
    SQLWarning getWarnings() throws SQLException;

    /**
     * 在此调用之后，getWarnings 返回 null，直到为此 ResultSet 报告新的警告。
     *
     * @exception SQLException if a database-access error occurs.
     */
    void clearWarnings() throws SQLException;

    /**
     * 获取此 ResultSet 使用的 SQL 游标的名称。
     *
     * 在 SQL 中，通过命名的游标检索结果表。可以使用引用游标名称的定位更新删除语句来更新或删除结果的当前行。
     * 
     * JDBC 通过提供 ResultSet 使用的 SQL 游标的名称来支持此 SQL 功能。 ResultSet 的当前行也是此 SQL 游标的当前行。
     *
     * Note: 如果不支持定位更新，则抛出 SQLException
     *
     * @return the ResultSet's SQL cursor name
     * @exception SQLException if a database-access error occurs.
     */
    String getCursorName() throws SQLException;

    /**
     * ResultSet 列的数量、类型和属性由 getMetaData 方法提供。
     *
     * @return the description of a ResultSet's columns
     * @exception SQLException if a database-access error occurs.
     */
    ResultSetMetaData getMetaData() throws SQLException;

    /**
     * <p>Get the value of a column in the current row as a Java object.
     *
     * 此方法将给定列的值作为 Java 对象返回。
     * Java 对象的类型将是与列的 SQL 类型对应的默认 Java 对象类型，遵循 JDBC 规范中指定的映射。
     *
     * 此方法还可用于读取数据库特定的抽象数据类型。
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return A java.lang.Object holding the column value.  
     * @exception SQLException if a database-access error occurs.
     */
    Object getObject(int columnIndex) throws SQLException;

    /**
     * <p>Get the value of a column in the current row as a Java object.
     *
     * 此方法将给定列的值作为 Java 对象返回。
     * Java 对象的类型将是与列的 SQL 类型对应的默认 Java 对象类型，遵循 JDBC 规范中指定的映射。
     *
     * <p>This method may also be used to read datatabase specific abstract
     * data types.
     *
     * @param columnName is the SQL name of the column
     * @return A java.lang.Object holding the column value.  
     * @exception SQLException if a database-access error occurs.
     */
    Object getObject(String columnName) throws SQLException;

    //----------------------------------------------------------------

    /**
     * Map a Resultset column name to a ResultSet column index.
     *
     * @param columnName the name of the column
     * @return the column index
     * @exception SQLException if a database-access error occurs.
     */
    int findColumn(String columnName) throws SQLException;
}
