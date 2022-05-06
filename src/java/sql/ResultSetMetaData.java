/*
 * @(#)ResultSetMetaData.java	1.7 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

/**
 * ResultSetMetaData 对象可用于了解 ResultSet 中列的类型和属性。
 */

public interface ResultSetMetaData {

    /**
     * ResultSet 中的列数是多少？
     *
     * @return the number
     * @exception SQLException 如果发生数据库访问错误。
     */
	int getColumnCount() throws SQLException;

    /**
     * 该列是否自动编号，因此是只读的？
     *
     * @param column 第一列是 1，第二列是 2，...
     * @return true if so
     * @exception SQLException 如果发生数据库访问错误。
     */
	boolean isAutoIncrement(int column) throws SQLException;

    /**
     * 列的大小写重要吗？
     *
     * @param column 第一列是 1，第二列是 2，...
     * @return true if so
     * @exception SQLException 如果发生数据库访问错误。
     */
	boolean isCaseSensitive(int column) throws SQLException;	

    /**
     * 该列可以在 where 子句中使用吗？
     *
     * @param column 第一列是 1，第二列是 2，...
     * @return true if so
     * @exception SQLException 如果发生数据库访问错误。
     */
	boolean isSearchable(int column) throws SQLException;

    /**
     * 该列是现金价值吗？
     *
     * @param column 第一列是 1，第二列是 2，...
     * @return true if so
     * @exception SQLException 如果发生数据库访问错误。
     */
	boolean isCurrency(int column) throws SQLException;

    /**
     * Can you put a NULL in this column?		
     *
     * @param column the first column is 1, the second is 2, ...
     * @return columnNoNulls, columnNullable or columnNullableUnknown
     * @exception SQLException if a database-access error occurs.
     */
	int isNullable(int column) throws SQLException;

    /**
     * Does not allow NULL values.
     */
    int columnNoNulls = 0;

    /**
     * Allows NULL values.
     */
    int columnNullable = 1;

    /**
     * Nullability unknown.
     */
    int columnNullableUnknown = 2;

    /**
     * Is the column a signed number?
     *
     * @param column the first column is 1, the second is 2, ...
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean isSigned(int column) throws SQLException;

    /**
     * What's the column's normal max width in chars?
     *
     * @param column the first column is 1, the second is 2, ...
     * @return max width
     * @exception SQLException if a database-access error occurs.
     */
	int getColumnDisplaySize(int column) throws SQLException;

    /**
     * What's the suggested column title for use in printouts and
     * displays?
     *
     * @param column the first column is 1, the second is 2, ...
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	String getColumnLabel(int column) throws SQLException;	

    /**
     * What's a column's name?
     *
     * @param column the first column is 1, the second is 2, ...
     * @return column name
     * @exception SQLException if a database-access error occurs.
     */
	String getColumnName(int column) throws SQLException;

    /**
     * What's a column's table's schema?
     *
     * @param column the first column is 1, the second is 2, ...
     * @return schema name or "" if not applicable
     * @exception SQLException if a database-access error occurs.
     */
	String getSchemaName(int column) throws SQLException;

    /**
     * What's a column's number of decimal digits?
     *
     * @param column the first column is 1, the second is 2, ...
     * @return precision
     * @exception SQLException if a database-access error occurs.
     */
	int getPrecision(int column) throws SQLException;

    /**
     * What's a column's number of digits to right of the decimal point?
     *
     * @param column the first column is 1, the second is 2, ...
     * @return scale
     * @exception SQLException if a database-access error occurs.
     */
	int getScale(int column) throws SQLException;	

    /**
     * What's a column's table name? 
     *
     * @return table name or "" if not applicable
     * @exception SQLException if a database-access error occurs.
     */
	String getTableName(int column) throws SQLException;

    /**
     * What's a column's table's catalog name?
     *
     * @param column the first column is 1, the second is 2, ...
     * @return column name or "" if not applicable.
     * @exception SQLException if a database-access error occurs.
     */
	String getCatalogName(int column) throws SQLException;

    /**
     * What's a column's SQL type?
     *
     * @param column the first column is 1, the second is 2, ...
     * @return SQL type
     * @exception SQLException if a database-access error occurs.
     * @see Types
     */
	int getColumnType(int column) throws SQLException;

    /**
     * What's a column's data source specific type name?
     *
     * @param column the first column is 1, the second is 2, ...
     * @return type name
     * @exception SQLException if a database-access error occurs.
     */
	String getColumnTypeName(int column) throws SQLException;

    /**
     * Is a column definitely not writable?
     *
     * @param column the first column is 1, the second is 2, ...
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean isReadOnly(int column) throws SQLException;

    /**
     * Is it possible for a write on the column to succeed?
     *
     * @param column the first column is 1, the second is 2, ...
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean isWritable(int column) throws SQLException;

    /**
     * Will a write on the column definitely succeed?	
     *
     * @param column the first column is 1, the second is 2, ...
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean isDefinitelyWritable(int column) throws SQLException;
}
