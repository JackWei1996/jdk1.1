/*
 * @(#)DatabaseMetaData.java	1.10 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */


package java.sql;

/**
 * 此类提供有关整个数据库的信息。
 *
 * 此处的许多方法返回 ResultSets 中的信息列表。
 * 您可以使用普通的 ResultSet 方法（例如 getString 和 getInt）从这些 ResultSet 中检索数据。
 * 如果给定形式的元数据不可用，这些方法应该抛出 SQLException。
 *
 * 其中一些方法采用字符串模式的参数。这些参数都具有诸如 fooPattern 之类的名称。
 * 在模式字符串中，“%”表示匹配任何 0 个或多个字符的子字符串，“_”表示匹配任何一个字符。
 * 仅返回与搜索模式匹配的元数据条目。如果搜索模式参数设置为空 ref，则意味着应从搜索中删除参数的条件。
 * 
 * 如果驱动程序不支持元数据方法，将引发 SQLException。
 * 对于返回 ResultSet 的方法，要么返回 ResultSet（可能为空），要么抛出 SQLException。
 */
public interface DatabaseMetaData {

    //----------------------------------------------------------------------
	// 一是关于目标数据库的各种次要信息。

    /**
     * getProcedures 返回的所有过程都可以被当前用户调用吗？
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean allProceduresAreCallable() throws SQLException;

    /**
     * getTable 返回的所有表都可以被当前用户选择吗？
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean allTablesAreSelectable() throws SQLException;

    /**
     * What's the url for this database?
     *
     * @return the url or null if it can't be generated
     * @exception SQLException if a database-access error occurs.
     */
	String getURL() throws SQLException;

    /**
     * What's our user name as known to the database?
     *
     * @return our database user name
     * @exception SQLException if a database-access error occurs.
     */
	String getUserName() throws SQLException;

    /**
     * Is the database in read-only mode?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean isReadOnly() throws SQLException;

    /**
     * Are NULL values sorted high?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean nullsAreSortedHigh() throws SQLException;

    /**
     * Are NULL values sorted low?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean nullsAreSortedLow() throws SQLException;

    /**
     * Are NULL values sorted at the start regardless of sort order?
     * （无论排序顺序如何，NULL 值是否都在开始时排序？）
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean nullsAreSortedAtStart() throws SQLException;

    /**
     * Are NULL values sorted at the end regardless of sort order?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean nullsAreSortedAtEnd() throws SQLException;

    /**
     * What's the name of this database product?(这个数据库产品叫什么名字？)
     *
     * @return database product name
     * @exception SQLException if a database-access error occurs.
     */
	String getDatabaseProductName() throws SQLException;

    /**
     * What's the version of this database product?
     *
     * @return database version
     * @exception SQLException if a database-access error occurs.
     */
	String getDatabaseProductVersion() throws SQLException;

    /**
     * What's the name of this JDBC driver?
     *
     * @return JDBC driver name
     * @exception SQLException if a database-access error occurs.
     */
	String getDriverName() throws SQLException;

    /**
     * What's the version of this JDBC driver?
     *
     * @return JDBC driver version
     * @exception SQLException if a database-access error occurs.
     */
	String getDriverVersion() throws SQLException;

    /**
     * What's this JDBC driver's major version number?
     * (这个 JDBC 驱动程序的主要版本号是多少？)
     *
     * @return JDBC driver major version
     */
	int getDriverMajorVersion();

    /**
     * What's this JDBC driver's minor version number?
     *
     * @return JDBC driver minor version number
     */
	int getDriverMinorVersion();

    /**
     * Does the database store tables in a local file?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean usesLocalFiles() throws SQLException;

    /**
     * Does the database use a file for each table?
     *
     * @return true if the database uses a local file for each table
     * @exception SQLException if a database-access error occurs.
     */
	boolean usesLocalFilePerTable() throws SQLException;

    /**
     * 数据库是否将混合大小写不带引号的 SQL 标识符视为区分大小写并因此以混合大小写存储它们？
     *
     * A JDBC-Compliant driver will always return false.
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsMixedCaseIdentifiers() throws SQLException;

    /**
     * 数据库是否将混合大小写不带引号的 SQL 标识符视为不区分大小写并将它们存储为大写？
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean storesUpperCaseIdentifiers() throws SQLException;

    /**
     * 数据库是否将混合大小写的不带引号的 SQL 标识符视为不区分大小写并将它们存储为小写？
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean storesLowerCaseIdentifiers() throws SQLException;

    /**
     * 数据库是否将混合大小写的不带引号的 SQL 标识符视为不区分大小写并将它们存储在混合大小写中？
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean storesMixedCaseIdentifiers() throws SQLException;

    /**
     * 数据库是否将混合大小写引用的 SQL 标识符视为区分大小写并因此以混合大小写存储它们？
     *
     * A JDBC-Compliant driver will always return true.
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsMixedCaseQuotedIdentifiers() throws SQLException;

    /**
     * 数据库是否将混合大小写引用的 SQL 标识符视为不区分大小写并将它们存储为大写？
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean storesUpperCaseQuotedIdentifiers() throws SQLException;

    /**
     * 数据库是否将混合大小写引用的 SQL 标识符视为不区分大小写并将它们存储为小写？
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean storesLowerCaseQuotedIdentifiers() throws SQLException;

    /**
     * 数据库是否将混合大小写引用的 SQL 标识符视为不区分大小写并将它们存储在混合大小写中？
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean storesMixedCaseQuotedIdentifiers() throws SQLException;

    /**
     * 用于引用 SQL 标识符的字符串是什么？如果不支持标识符引用，这将返回一个空格“ ”。
     *
     * A JDBC-Compliant driver always uses a double quote character.
     *
     * @return the quoting string
     * @exception SQLException if a database-access error occurs.
     */
	String getIdentifierQuoteString() throws SQLException;

    /**
     * 获取数据库的所有 SQL 关键字的逗号分隔列表，这些关键字也不是 SQL92 关键字。
     *
     * @return the list 
     * @exception SQLException if a database-access error occurs.
     */
	String getSQLKeywords() throws SQLException;

    /**
     * Get a comma separated list of math functions.（获取以逗号分隔的数学函数列表。）
     *
     * @return the list
     * @exception SQLException if a database-access error occurs.
     */
	String getNumericFunctions() throws SQLException;

    /**
     * Get a comma separated list of string functions.
     *
     * @return the list
     * @exception SQLException if a database-access error occurs.
     */
	String getStringFunctions() throws SQLException;

    /**
     * Get a comma separated list of system functions.
     *
     * @return the list
     * @exception SQLException if a database-access error occurs.
     */
	String getSystemFunctions() throws SQLException;

    /**
     * Get a comma separated list of time and date functions.
     *
     * @return the list
     * @exception SQLException if a database-access error occurs.
     */
	String getTimeDateFunctions() throws SQLException;

    /**
     * 这是可用于在字符串模式样式目录搜索参数中转义“_”或“%”的字符串。
     *
     * The '_' character represents any single character.
     * %' 字符代表零个或多个字符的任意序列。
     *
     * @return the string used to escape wildcard characters
     * @exception SQLException if a database-access error occurs.
     */
	String getSearchStringEscape() throws SQLException;

    /**
     * 获取所有可以在未加引号的标识符名称中使用的“额外”字符（那些超出 a-z、A-Z、0-9 和 _ 的字符）。
     *
     * @return the string containing the extra characters 
     * @exception SQLException if a database-access error occurs.
     */
	String getExtraNameCharacters() throws SQLException;

    //--------------------------------------------------------------------
    // 描述支持哪些功能的函数。

    /**
     * Is "ALTER TABLE" with add column supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsAlterTableWithAddColumn() throws SQLException;

    /**
     * Is "ALTER TABLE" with drop column supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsAlterTableWithDropColumn() throws SQLException;

    /**
     * Is column aliasing supported? 
     *
     * 如果是这样，SQL AS 子句可用于为计算列提供名称或根据需要为列提供别名。
     *
     * A JDBC-Compliant driver always returns true.
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsColumnAliasing() throws SQLException;

    /**
     * NULL 和非 NULL 值之间的连接是否为 NULL？
     *
     * A JDBC-Compliant driver always returns true.
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean nullPlusNonNullIsNull() throws SQLException;

    /**
     * 是否支持 SQL 类型之间的 CONVERT 函数？
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsConvert() throws SQLException;

    /**
     * 是否支持给定 SQL 类型之间的 CONVERT？
     *
     * @param fromType the type to convert from
     * @param toType the type to convert to     
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     * @see Types
     */
	boolean supportsConvert(int fromType, int toType) throws SQLException;

    /**
     * Are table correlation names supported?
     *
     * A JDBC-Compliant driver always returns true.
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsTableCorrelationNames() throws SQLException;

    /**
     * 如果支持表相关名称，是否限制它们与表的名称不同？
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsDifferentTableCorrelationNames() throws SQLException;

    /**
     * Are expressions in "ORDER BY" lists supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsExpressionsInOrderBy() throws SQLException;

    /**
     * Can an "ORDER BY" clause use columns not in the SELECT?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsOrderByUnrelated() throws SQLException;

    /**
     * Is some form of "GROUP BY" clause supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsGroupBy() throws SQLException;

    /**
     * Can a "GROUP BY" clause use columns not in the SELECT?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsGroupByUnrelated() throws SQLException;

    /**
     * 如果“GROUP BY”子句指定了 SELECT 中的所有列，是否可以添加不在 SELECT 中的列？
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsGroupByBeyondSelect() throws SQLException;

    /**
     * Is the escape character in "LIKE" clauses supported?
     *
     * A JDBC-Compliant driver always returns true.
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsLikeEscapeClause() throws SQLException;

    /**
     * Are multiple ResultSets from a single execute supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsMultipleResultSets() throws SQLException;

    /**
     * 我们可以一次打开多个事务（在不同的连接上）吗？
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsMultipleTransactions() throws SQLException;

    /**
     * Can columns be defined as non-nullable?
     *
     * A JDBC-Compliant driver always returns true.
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsNonNullableColumns() throws SQLException;

    /**
     * Is the ODBC Minimum SQL grammar supported?
     *
     * All JDBC-Compliant drivers must return true.
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsMinimumSQLGrammar() throws SQLException;

    /**
     * Is the ODBC Core SQL grammar supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsCoreSQLGrammar() throws SQLException;

    /**
     * Is the ODBC Extended SQL grammar supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsExtendedSQLGrammar() throws SQLException;

    /**
     * Is the ANSI92 entry level SQL grammar supported?
     *
     * All JDBC-Compliant drivers must return true.
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsANSI92EntryLevelSQL() throws SQLException;

    /**
     * Is the ANSI92 intermediate SQL grammar supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsANSI92IntermediateSQL() throws SQLException;

    /**
     * Is the ANSI92 full SQL grammar supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsANSI92FullSQL() throws SQLException;

    /**
     * Is the SQL Integrity Enhancement Facility supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsIntegrityEnhancementFacility() throws SQLException;

    /**
     * Is some form of outer join supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsOuterJoins() throws SQLException;

    /**
     * Are full nested outer joins supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsFullOuterJoins() throws SQLException;

    /**
     * 对外部联接的支持是否有限？ （如果 supportFullOuterJoins 为真，则为真。）
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsLimitedOuterJoins() throws SQLException;

    /**
     * What's the database vendor's preferred term for "schema"?
     *
     * @return the vendor term
     * @exception SQLException if a database-access error occurs.
     */
	String getSchemaTerm() throws SQLException;

    /**
     * What's the database vendor's preferred term for "procedure"?
     *
     * @return the vendor term
     * @exception SQLException if a database-access error occurs.
     */
	String getProcedureTerm() throws SQLException;

    /**
     * What's the database vendor's preferred term for "catalog"?
     *
     * @return the vendor term
     * @exception SQLException if a database-access error occurs.
     */
	String getCatalogTerm() throws SQLException;

    /**
     * 目录是否出现在限定表名的开头？ （否则出现在最后）
     *
     * @return true if it appears at the start 
     * @exception SQLException if a database-access error occurs.
     */
	boolean isCatalogAtStart() throws SQLException;

    /**
     * What's the separator between catalog and table name?
     *
     * @return the separator string
     * @exception SQLException if a database-access error occurs.
     */
	String getCatalogSeparator() throws SQLException;

    /**
     * Can a schema name be used in a data manipulation statement?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsSchemasInDataManipulation() throws SQLException;

    /**
     * Can a schema name be used in a procedure call statement?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsSchemasInProcedureCalls() throws SQLException;

    /**
     * Can a schema name be used in a table definition statement?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsSchemasInTableDefinitions() throws SQLException;

    /**
     * Can a schema name be used in an index definition statement?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsSchemasInIndexDefinitions() throws SQLException;

    /**
     * Can a schema name be used in a privilege definition statement?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsSchemasInPrivilegeDefinitions() throws SQLException;

    /**
     * Can a catalog name be used in a data manipulation statement?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsCatalogsInDataManipulation() throws SQLException;

    /**
     * Can a catalog name be used in a procedure call statement?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsCatalogsInProcedureCalls() throws SQLException;

    /**
     * Can a catalog name be used in a table definition statement?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsCatalogsInTableDefinitions() throws SQLException;

    /**
     * Can a catalog name be used in an index definition statement?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsCatalogsInIndexDefinitions() throws SQLException;

    /**
     * Can a catalog name be used in a privilege definition statement?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException;


    /**
     * Is positioned DELETE supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsPositionedDelete() throws SQLException;

    /**
     * Is positioned UPDATE supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsPositionedUpdate() throws SQLException;

    /**
     * Is SELECT for UPDATE supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsSelectForUpdate() throws SQLException;

    /**
     * 是否支持使用存储过程转义语法的存储过程调用？
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsStoredProcedures() throws SQLException;

    /**
     * Are subqueries in comparison expressions supported?
     *
     * A JDBC-Compliant driver always returns true.
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsSubqueriesInComparisons() throws SQLException;

    /**
     * Are subqueries in 'exists' expressions supported?
     *
     * A JDBC-Compliant driver always returns true.
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsSubqueriesInExists() throws SQLException;

    /**
     * Are subqueries in 'in' statements supported?
     *
     * A JDBC-Compliant driver always returns true.
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsSubqueriesInIns() throws SQLException;

    /**
     * Are subqueries in quantified expressions supported?
     *
     * A JDBC-Compliant driver always returns true.
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsSubqueriesInQuantifieds() throws SQLException;

    /**
     * Are correlated subqueries supported?
     *
     * A JDBC-Compliant driver always returns true.
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsCorrelatedSubqueries() throws SQLException;

    /**
     * Is SQL UNION supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsUnion() throws SQLException;

    /**
     * Is SQL UNION ALL supported?
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsUnionAll() throws SQLException;

    /**
     * Can cursors remain open across commits? 
     * 
     * @return true if cursors always remain open; false if they might not remain open
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsOpenCursorsAcrossCommit() throws SQLException;

    /**
     * Can cursors remain open across rollbacks?
     * 
     * @return true if cursors always remain open; false if they might not remain open
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsOpenCursorsAcrossRollback() throws SQLException;

    /**
     * Can statements remain open across commits?
     * 
     * @return true if statements always remain open; false if they might not remain open
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsOpenStatementsAcrossCommit() throws SQLException;

    /**
     * Can statements remain open across rollbacks?
     * 
     * @return true if statements always remain open; false if they might not remain open
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsOpenStatementsAcrossRollback() throws SQLException;

	

    //----------------------------------------------------------------------
    // 以下一组方法基于当前驱动程序的目标数据库公开了各种限制。
    // 除非另有说明，否则结果为零表示没有限制，或者限制未知。
	
    /**
     * How many hex characters can you have in an inline binary literal?
     *
     * @return max literal length
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxBinaryLiteralLength() throws SQLException;

    /**
     * What's the max length for a character literal?
     *
     * @return max literal length
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxCharLiteralLength() throws SQLException;

    /**
     * What's the limit on column name length?
     *
     * @return max literal length
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxColumnNameLength() throws SQLException;

    /**
     * What's the maximum number of columns in a "GROUP BY" clause?
     *
     * @return max number of columns
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxColumnsInGroupBy() throws SQLException;

    /**
     * What's the maximum number of columns allowed in an index?
     *
     * @return max columns
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxColumnsInIndex() throws SQLException;

    /**
     * What's the maximum number of columns in an "ORDER BY" clause?
     *
     * @return max columns
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxColumnsInOrderBy() throws SQLException;

    /**
     * What's the maximum number of columns in a "SELECT" list?
     *
     * @return max columns
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxColumnsInSelect() throws SQLException;

    /**
     * What's the maximum number of columns in a table?
     *
     * @return max columns
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxColumnsInTable() throws SQLException;

    /**
     * How many active connections can we have at a time to this database?
     *
     * @return max connections
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxConnections() throws SQLException;

    /**
     * What's the maximum cursor name length?
     *
     * @return max cursor name length in bytes
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxCursorNameLength() throws SQLException;

    /**
     * What's the maximum length of an index (in bytes)?	
     *
     * @return max index length in bytes
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxIndexLength() throws SQLException;

    /**
     * What's the maximum length allowed for a schema name?
     *
     * @return max name length in bytes
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxSchemaNameLength() throws SQLException;

    /**
     * What's the maximum length of a procedure name?
     *
     * @return max name length in bytes 
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxProcedureNameLength() throws SQLException;

    /**
     * What's the maximum length of a catalog name?
     *
     * @return max name length in bytes
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxCatalogNameLength() throws SQLException;

    /**
     * What's the maximum length of a single row?
     *
     * @return max row size in bytes
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxRowSize() throws SQLException;

    /**
     * getMaxRowSize() 是否包含 LONGVARCHAR 和 LONGVARBINARY blob？
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean doesMaxRowSizeIncludeBlobs() throws SQLException;

    /**
     * What's the maximum length of a SQL statement?
     *
     * @return max length in bytes
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxStatementLength() throws SQLException;

    /**
     * 我们可以一次打开多少个活动语句到这个数据库？
     *
     * @return the maximum 
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxStatements() throws SQLException;

    /**
     * What's the maximum length of a table name?
     *
     * @return max name length in bytes
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxTableNameLength() throws SQLException;

    /**
     * What's the maximum number of tables in a SELECT?
     *
     * @return the maximum
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxTablesInSelect() throws SQLException;

    /**
     * What's the maximum length of a user name?
     *
     * @return max name length  in bytes
     * @exception SQLException if a database-access error occurs.
     */
	int getMaxUserNameLength() throws SQLException;

    //----------------------------------------------------------------------

    /**
     * 数据库的默认事务隔离级别是多少？这些值在 java.sql.Connection 中定义。
     *
     * @return the default isolation level 
     * @exception SQLException if a database-access error occurs.
     * @see Connection
     */
	int getDefaultTransactionIsolation() throws SQLException;

    /**
     * 是否支持交易？如果不是，commit 是 noop，隔离级别是 TRANSACTION_NONE。
     *
     * @return true if transactions are supported 
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsTransactions() throws SQLException;

    /**
     * Does the database support the given transaction isolation level?
     *
     * @param level the values are defined in java.sql.Connection
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     * @see Connection
     */
	boolean supportsTransactionIsolationLevel(int level)
							throws SQLException;

    /**
     * 是否支持事务中的数据定义和数据操作语句？
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsDataDefinitionAndDataManipulationTransactions()
							 throws SQLException;
    /**
     * 是否仅支持事务中的数据操作语句？
     *
     * @return true if so
     * @exception SQLException if a database-access error occurs.
     */
	boolean supportsDataManipulationTransactionsOnly()
							throws SQLException;
    /**
     * 事务中的数据定义语句是否强制事务提交？
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean dataDefinitionCausesTransactionCommit()
							throws SQLException;
    /**
     * Is a data definition statement within a transaction ignored?
     *
     * @return true if so 
     * @exception SQLException if a database-access error occurs.
     */
	boolean dataDefinitionIgnoredInTransactions()
							throws SQLException;


    /**
     * 获取目录中可用存储过程的描述。
     *
     * 仅返回与模式和过程名称标准匹配的过程描述。它们按 PROCEDURE_SCHEM 和 PROCEDURE_NAME 排序。
     *
     * 每个过程描述都有以下列：
     *  <OL>
     *	<LI><B>PROCEDURE_CAT</B> String => procedure catalog (may be null)
     *	<LI><B>PROCEDURE_SCHEM</B> String => procedure schema (may be null)
     *	<LI><B>PROCEDURE_NAME</B> String => procedure name
     *  <LI> reserved for future use
     *  <LI> reserved for future use
     *  <LI> reserved for future use
     *	<LI><B>REMARKS</B> String => explanatory comment on the procedure
     *	<LI><B>PROCEDURE_TYPE</B> short => kind of procedure:
     *      <UL>
     *      <LI> procedureResultUnknown - May return a result
     *      <LI> procedureNoResult - Does not return a result
     *      <LI> procedureReturnsResult - Returns a result
     *      </UL>
     *  </OL>
     *
     * @param catalog 目录名称； "" 检索那些没有目录的； null 表示从选择条件中删除目录名称
     * @param schemaPattern 模式名称模式； "" 检索那些没有架构的
     * @param procedureNamePattern a procedure name pattern 
     * @return ResultSet - each row is a procedure description 
     * @exception SQLException if a database-access error occurs.
     * @see #getSearchStringEscape 
     */
	ResultSet getProcedures(String catalog, String schemaPattern,
			String procedureNamePattern) throws SQLException;

    /**
     * PROCEDURE_TYPE - May return a result.
     */
	int procedureResultUnknown	= 0;
    /**
     * PROCEDURE_TYPE - Does not return a result.
     */
	int procedureNoResult		= 1;
    /**
     * PROCEDURE_TYPE - Returns a result.
     */
	int procedureReturnsResult	= 2;

    /**
     * 获取目录的存储过程参数和结果列的描述。
     *
     * 仅返回与模式、过程和参数名称标准匹配的描述。它们按 PROCEDURE_SCHEM 和 PROCEDURE_NAME 排序。
     * 在此范围内，返回值（如果有）是第一位的。接下来是调用顺序中的参数说明。列说明按照列号顺序排列。
     *
     * ResultSet 中的每一行都是参数描述或列描述，包含以下字段：
     *  <OL>
     *	<LI><B>PROCEDURE_CAT</B> String => procedure catalog (may be null)
     *	<LI><B>PROCEDURE_SCHEM</B> String => procedure schema (may be null)
     *	<LI><B>PROCEDURE_NAME</B> String => procedure name
     *	<LI><B>COLUMN_NAME</B> String => column/parameter name 
     *	<LI><B>COLUMN_TYPE</B> Short => kind of column/parameter:
     *      <UL>
     *      <LI> procedureColumnUnknown - nobody knows
     *      <LI> procedureColumnIn - IN parameter
     *      <LI> procedureColumnInOut - INOUT parameter
     *      <LI> procedureColumnOut - OUT parameter
     *      <LI> procedureColumnReturn - procedure return value
     *      <LI> procedureColumnResult - result column in ResultSet
     *      </UL>
     *  <LI><B>DATA_TYPE</B> short => SQL type from java.sql.Types
     *	<LI><B>TYPE_NAME</B> String => SQL type name
     *	<LI><B>PRECISION</B> int => precision
     *	<LI><B>LENGTH</B> int => length in bytes of data
     *	<LI><B>SCALE</B> short => scale
     *	<LI><B>RADIX</B> short => radix
     *	<LI><B>NULLABLE</B> short => can it contain NULL?
     *      <UL>
     *      <LI> procedureNoNulls - does not allow NULL values
     *      <LI> procedureNullable - allows NULL values
     *      <LI> procedureNullableUnknown - nullability unknown
     *      </UL>
     *	<LI><B>REMARKS</B> String => comment describing parameter/column
     *  </OL>
     *
     * Note: 某些数据库可能不会返回过程的列描述。数据库可以定义 REMARKS 之外的其他列。
     *
     * @param catalog 目录名称； "" 检索那些没有目录的； null 表示从选择条件中删除目录名称
     * @param schemaPattern 模式名称模式； "" 检索那些没有架构的
     * @param procedureNamePattern a procedure name pattern 
     * @param columnNamePattern a column name pattern 
     * @return ResultSet - 每行是一个存储过程参数或列描述
     * @exception SQLException if a database-access error occurs.
     * @see #getSearchStringEscape 
     */
	ResultSet getProcedureColumns(String catalog,
			String schemaPattern,
			String procedureNamePattern, 
			String columnNamePattern) throws SQLException;

    /**
     * COLUMN_TYPE - nobody knows.
     */
	int procedureColumnUnknown = 0;

    /**
     * COLUMN_TYPE - IN parameter.
     */
	int procedureColumnIn = 1;

    /**
     * COLUMN_TYPE - INOUT parameter.
     */
	int procedureColumnInOut = 2;

    /**
     * COLUMN_TYPE - OUT parameter.
     */
	int procedureColumnOut = 4;
    /**
     * COLUMN_TYPE - procedure return value.
     */
	int procedureColumnReturn = 5;

    /**
     * COLUMN_TYPE - result column in ResultSet.
     */
	int procedureColumnResult = 3;

    /**
     * TYPE NULLABLE - does not allow NULL values.
     */
    int procedureNoNulls = 0;

    /**
     * TYPE NULLABLE - allows NULL values.
     */
    int procedureNullable = 1;

    /**
     * TYPE NULLABLE - nullability unknown.
     */
    int procedureNullableUnknown = 2;


    /**
     * 获取目录中可用表的描述。
     *
     * 仅返回与目录、模式、表名和类型条件匹配的表描述。它们按 TABLE_TYPE、TABLE_SCHEM 和 TABLE_NAME 排序。
     *
     * <P>Each table description has the following columns:
     *  <OL>
     *	<LI><B>TABLE_CAT</B> String => table catalog (may be null)
     *	<LI><B>TABLE_SCHEM</B> String => table schema (may be null)
     *	<LI><B>TABLE_NAME</B> String => table name
     *	<LI><B>TABLE_TYPE</B> String => table type.  Typical types are "TABLE",
     *			"VIEW",	"SYSTEM TABLE", "GLOBAL TEMPORARY", 
     *			"LOCAL TEMPORARY", "ALIAS", "SYNONYM".
     *	<LI><B>REMARKS</B> String => explanatory comment on the table
     *  </OL>
     *
     * Note: 某些数据库可能不会返回所有表的信息。
     *
     * @param catalog 目录名称； "" 检索那些没有目录的； null 表示从选择条件中删除目录名称
     * @param schemaPattern 模式名称模式； "" 检索那些没有架构的
     * @param tableNamePattern a table name pattern 
     * @param types a list of table types to include; null returns all types 
     * @return ResultSet - each row is a table description
     * @exception SQLException if a database-access error occurs.
     * @see #getSearchStringEscape 
     */
	ResultSet getTables(String catalog, String schemaPattern,
		String tableNamePattern, String types[]) throws SQLException;

    /**
     * 获取此数据库中可用的模式名称。结果按模式名称排序。
     *
     * <P>The schema column is:
     *  <OL>
     *	<LI><B>TABLE_SCHEM</B> String => schema name
     *  </OL>
     *
     * @return ResultSet - 每行都有一个 String 列，它是一个模式名称
     * @exception SQLException if a database-access error occurs.
     */
	ResultSet getSchemas() throws SQLException;

    /**
     * 获取此数据库中可用的目录名称。结果按目录名称排序。
     *
     * <P>The catalog column is:
     *  <OL>
     *	<LI><B>TABLE_CAT</B> String => catalog name
     *  </OL>
     *
     * @return ResultSet - 每行都有一个 String 列，它是一个目录名称
     * @exception SQLException if a database-access error occurs.
     */
	ResultSet getCatalogs() throws SQLException;

    /**
     * 获取此数据库中可用的表类型。结果按表类型排序。
     *
     * <P>The table type is:
     *  <OL>
     *	<LI><B>TABLE_TYPE</B> String => table type.  Typical types are "TABLE",
     *			"VIEW",	"SYSTEM TABLE", "GLOBAL TEMPORARY", 
     *			"LOCAL TEMPORARY", "ALIAS", "SYNONYM".
     *  </OL>
     *
     * @return ResultSet - 每行都有一个 String 列，它是一个表类型
     * @exception SQLException if a database-access error occurs.
     */
	ResultSet getTableTypes() throws SQLException;

    /**
     * 获取目录中可用表列的描述。
     *
     * 仅返回与目录、架构、表和列名条件匹配的列描述。它们按 TABLE_SCHEM、TABLE_NAME 和 ORDINAL_POSITION 排序。
     *
     * <P>Each column description has the following columns:
     *  <OL>
     *	<LI><B>TABLE_CAT</B> String => table catalog (may be null)
     *	<LI><B>TABLE_SCHEM</B> String => table schema (may be null)
     *	<LI><B>TABLE_NAME</B> String => table name
     *	<LI><B>COLUMN_NAME</B> String => column name
     *	<LI><B>DATA_TYPE</B> short => SQL type from java.sql.Types
     *	<LI><B>TYPE_NAME</B> String => Data source dependent type name
     *	<LI><B>COLUMN_SIZE</B> int => column size.  For char or date
     *	    types this is the maximum number of characters, for numeric or
     *	    decimal types this is precision.
     *	<LI><B>BUFFER_LENGTH</B> is not used.
     *	<LI><B>DECIMAL_DIGITS</B> int => the number of fractional digits
     *	<LI><B>NUM_PREC_RADIX</B> int => Radix (typically either 10 or 2)
     *	<LI><B>NULLABLE</B> int => is NULL allowed?
     *      <UL>
     *      <LI> columnNoNulls - might not allow NULL values
     *      <LI> columnNullable - definitely allows NULL values
     *      <LI> columnNullableUnknown - nullability unknown
     *      </UL>
     *	<LI><B>REMARKS</B> String => comment describing column (may be null)
     * 	<LI><B>COLUMN_DEF</B> String => default value (may be null)
     *	<LI><B>SQL_DATA_TYPE</B> int => unused
     *	<LI><B>SQL_DATETIME_SUB</B> int => unused
     *	<LI><B>CHAR_OCTET_LENGTH</B> int => for char types the 
     *       maximum number of bytes in the column
     *	<LI><B>ORDINAL_POSITION</B> int	=> index of column in table 
     *      (starting at 1)
     *	<LI><B>IS_NULLABLE</B> String => "NO" means column definitely 
     *      does not allow NULL values; "YES" means the column might 
     *      allow NULL values.  An empty string means nobody knows.
     *  </OL>
     *
     * @param catalog 目录名称； "" 检索那些没有目录的； null 表示从选择条件中删除目录名称
     * @param schemaPattern 模式名称模式； "" 检索那些没有架构的
     * @param tableNamePattern a table name pattern 
     * @param columnNamePattern a column name pattern 
     * @return ResultSet - each row is a column description
     * @exception SQLException if a database-access error occurs.
     * @see #getSearchStringEscape 
     */
	ResultSet getColumns(String catalog, String schemaPattern,
		String tableNamePattern, String columnNamePattern)
					throws SQLException;
    /**
     * COLUMN NULLABLE - might not allow NULL values.
     */
    int columnNoNulls = 0;

    /**
     * COLUMN NULLABLE - definitely allows NULL values.
     */
    int columnNullable = 1;

    /**
     * COLUMN NULLABLE - nullability unknown.
     */
    int columnNullableUnknown = 2;

    /**
     * 获取对表列的访问权限的描述。
     *
     * 仅返回与列名条件匹配的权限。它们按 COLUMN_NAME 和 PRIVILEGE 排序。
     *
     * <P>Each privilige description has the following columns:
     *  <OL>
     *	<LI><B>TABLE_CAT</B> String => table catalog (may be null)
     *	<LI><B>TABLE_SCHEM</B> String => table schema (may be null)
     *	<LI><B>TABLE_NAME</B> String => table name
     *	<LI><B>COLUMN_NAME</B> String => column name
     *	<LI><B>GRANTOR</B> => grantor of access (may be null)
     *	<LI><B>GRANTEE</B> String => grantee of access
     *	<LI><B>PRIVILEGE</B> String => name of access (SELECT, 
     *      INSERT, UPDATE, REFRENCES, ...)
     *	<LI><B>IS_GRANTABLE</B> String => "YES" if grantee is permitted 
     *      to grant to others; "NO" if not; null if unknown 
     *  </OL>
     *
     * @param catalog 目录名称； "" 检索那些没有目录的； null 表示从选择条件中删除目录名称
     * @param schema a schema name; "" retrieves those without a schema
     * @param table a table name
     * @param columnNamePattern a column name pattern 
     * @return ResultSet - each row is a column privilege description
     * @exception SQLException if a database-access error occurs.
     * @see #getSearchStringEscape 
     */
	ResultSet getColumnPrivileges(String catalog, String schema,
		String table, String columnNamePattern) throws SQLException;

    /**
     * 获取目录中每个可用表的访问权限的描述。请注意，表权限适用于表中的一个或多个列。
     * 假设此特权适用于所有列是错误的（这对于某些系统可能是正确的，但并非对所有系统都是正确的。）
     *
     * 仅返回与架构和表名条件匹配的权限。它们按 TABLE_SCHEM、TABLE_NAME 和 PRIVILEGE 排序。
     *
     * <P>Each privilige description has the following columns:
     *  <OL>
     *	<LI><B>TABLE_CAT</B> String => table catalog (may be null)
     *	<LI><B>TABLE_SCHEM</B> String => table schema (may be null)
     *	<LI><B>TABLE_NAME</B> String => table name
     *	<LI><B>GRANTOR</B> => grantor of access (may be null)
     *	<LI><B>GRANTEE</B> String => grantee of access
     *	<LI><B>PRIVILEGE</B> String => name of access (SELECT, 
     *      INSERT, UPDATE, REFRENCES, ...)
     *	<LI><B>IS_GRANTABLE</B> String => "YES" if grantee is permitted 
     *      to grant to others; "NO" if not; null if unknown 
     *  </OL>
     *
     * @param catalog 目录名称； "" 检索那些没有目录的； null 表示从选择条件中删除目录名称
     * @param schemaPattern 模式名称模式； "" 检索那些没有架构的
     * @param tableNamePattern a table name pattern 
     * @return ResultSet - each row is a table privilege description
     * @exception SQLException if a database-access error occurs.
     * @see #getSearchStringEscape 
     */
	ResultSet getTablePrivileges(String catalog, String schemaPattern,
				String tableNamePattern) throws SQLException;

    /**
     * 获取对唯一标识行的表的最佳列集的描述。它们由 SCOPE 订购。
     *
     * <P>Each column description has the following columns:
     *  <OL>
     *	<LI><B>SCOPE</B> short => actual scope of result
     *      <UL>
     *      <LI> bestRowTemporary - very temporary, while using row
     *      <LI> bestRowTransaction - valid for remainder of current transaction
     *      <LI> bestRowSession - valid for remainder of current session
     *      </UL>
     *	<LI><B>COLUMN_NAME</B> String => column name
     *	<LI><B>DATA_TYPE</B> short => SQL data type from java.sql.Types
     *	<LI><B>TYPE_NAME</B> String => Data source dependent type name
     *	<LI><B>COLUMN_SIZE</B> int => precision
     *	<LI><B>BUFFER_LENGTH</B> int => not used
     *	<LI><B>DECIMAL_DIGITS</B> short	 => scale
     *	<LI><B>PSEUDO_COLUMN</B> short => is this a pseudo column 
     *      like an Oracle ROWID
     *      <UL>
     *      <LI> bestRowUnknown - may or may not be pseudo column
     *      <LI> bestRowNotPseudo - is NOT a pseudo column
     *      <LI> bestRowPseudo - is a pseudo column
     *      </UL>
     *  </OL>
     *
     * @param catalog 目录名称； "" 检索那些没有目录的； null 表示从选择条件中删除目录名称
     * @param schema a schema name; "" retrieves those without a schema
     * @param table a table name
     * @param scope the scope of interest; use same values as SCOPE
     * @param nullable include columns that are nullable?
     * @return ResultSet - each row is a column description 
     * @exception SQLException if a database-access error occurs.
     */
	ResultSet getBestRowIdentifier(String catalog, String schema,
		String table, int scope, boolean nullable) throws SQLException;
	
    /**
     * BEST ROW SCOPE - very temporary, while using row.
     */
	int bestRowTemporary   = 0;

    /**
     * BEST ROW SCOPE - valid for remainder of current transaction.
     */
	int bestRowTransaction = 1;

    /**
     * BEST ROW SCOPE - valid for remainder of current session.
     */
	int bestRowSession     = 2;

    /**
     * BEST ROW PSEUDO_COLUMN - may or may not be pseudo column.
     */
	int bestRowUnknown	= 0;

    /**
     * BEST ROW PSEUDO_COLUMN - is NOT a pseudo column.
     */
	int bestRowNotPseudo	= 1;

    /**
     * BEST ROW PSEUDO_COLUMN - is a pseudo column.
     */
	int bestRowPseudo	= 2;

    /**
     * 获取在更新行中的任何值时自动更新的表列的描述。它们是无序的。
     *
     * <P>Each column description has the following columns:
     *  <OL>
     *	<LI><B>SCOPE</B> short => is not used
     *	<LI><B>COLUMN_NAME</B> String => column name
     *	<LI><B>DATA_TYPE</B> short => SQL data type from java.sql.Types
     *	<LI><B>TYPE_NAME</B> String => Data source dependent type name
     *	<LI><B>COLUMN_SIZE</B> int => precision
     *	<LI><B>BUFFER_LENGTH</B> int => length of column value in bytes
     *	<LI><B>DECIMAL_DIGITS</B> short	 => scale
     *	<LI><B>PSEUDO_COLUMN</B> short => is this a pseudo column 
     *      like an Oracle ROWID
     *      <UL>
     *      <LI> versionColumnUnknown - may or may not be pseudo column
     *      <LI> versionColumnNotPseudo - is NOT a pseudo column
     *      <LI> versionColumnPseudo - is a pseudo column
     *      </UL>
     *  </OL>
     *
     * @param catalog 目录名称； "" 检索那些没有目录的； null 表示从选择条件中删除目录名称
     * @param schema a schema name; "" retrieves those without a schema
     * @param table a table name
     * @return ResultSet - each row is a column description 
     * @exception SQLException if a database-access error occurs.
     */
	ResultSet getVersionColumns(String catalog, String schema,
				String table) throws SQLException;
	
    /**
     * VERSION COLUMNS PSEUDO_COLUMN - may or may not be pseudo column.
     */
	int versionColumnUnknown	= 0;

    /**
     *  VERSION COLUMNS PSEUDO_COLUMN - is NOT a pseudo column.
     */
	int versionColumnNotPseudo	= 1;

    /**
     *  VERSION COLUMNS PSEUDO_COLUMN - is a pseudo column.
     */
	int versionColumnPseudo	= 2;

    /**
     * 获取表的主键列的描述。它们按 COLUMN_NAME 排序。
     *
     * <P>Each primary key column description has the following columns:
     *  <OL>
     *	<LI><B>TABLE_CAT</B> String => table catalog (may be null)
     *	<LI><B>TABLE_SCHEM</B> String => table schema (may be null)
     *	<LI><B>TABLE_NAME</B> String => table name
     *	<LI><B>COLUMN_NAME</B> String => column name
     *	<LI><B>KEY_SEQ</B> short => sequence number within primary key
     *	<LI><B>PK_NAME</B> String => primary key name (may be null)
     *  </OL>
     *
     * @param catalog 目录名称； "" 检索那些没有目录的； null 表示从选择条件中删除目录名称
     * @param schema a schema name pattern; "" retrieves those
     * without a schema
     * @param table a table name
     * @return ResultSet - each row is a primary key column description 
     * @exception SQLException if a database-access error occurs.
     */
	ResultSet getPrimaryKeys(String catalog, String schema,
				String table) throws SQLException;

    /**
     * 获取表的外键列（表导入的主键）引用的主键列的描述。
     * 它们按 PKTABLE_CAT、PKTABLE_SCHEM、PKTABLE_NAME 和 KEY_SEQ 排序。
     *
     * <P>Each primary key column description has the following columns:
     *  <OL>
     *	<LI><B>PKTABLE_CAT</B> String => primary key table catalog 
     *      being imported (may be null)
     *	<LI><B>PKTABLE_SCHEM</B> String => primary key table schema
     *      being imported (may be null)
     *	<LI><B>PKTABLE_NAME</B> String => primary key table name
     *      being imported
     *	<LI><B>PKCOLUMN_NAME</B> String => primary key column name
     *      being imported
     *	<LI><B>FKTABLE_CAT</B> String => foreign key table catalog (may be null)
     *	<LI><B>FKTABLE_SCHEM</B> String => foreign key table schema (may be null)
     *	<LI><B>FKTABLE_NAME</B> String => foreign key table name
     *	<LI><B>FKCOLUMN_NAME</B> String => foreign key column name
     *	<LI><B>KEY_SEQ</B> short => sequence number within foreign key
     *	<LI><B>UPDATE_RULE</B> short => What happens to 
     *       foreign key when primary is updated:
     *      <UL>
     *      <LI> importedNoAction - do not allow update of primary 
     *               key if it has been imported
     *      <LI> importedKeyCascade - change imported key to agree 
     *               with primary key update
     *      <LI> importedKeySetNull - change imported key to NULL if 
     *               its primary key has been updated
     *      <LI> importedKeySetDefault - change imported key to default values 
     *               if its primary key has been updated
     *      <LI> importedKeyRestrict - same as importedKeyNoAction 
     *                                 (for ODBC 2.x compatibility)
     *      </UL>
     *	<LI><B>DELETE_RULE</B> short => What happens to 
     *      the foreign key when primary is deleted.
     *      <UL>
     *      <LI> importedKeyNoAction - do not allow delete of primary 
     *               key if it has been imported
     *      <LI> importedKeyCascade - delete rows that import a deleted key
     *      <LI> importedKeySetNull - change imported key to NULL if 
     *               its primary key has been deleted
     *      <LI> importedKeyRestrict - same as importedKeyNoAction 
     *                                 (for ODBC 2.x compatibility)
     *      <LI> importedKeySetDefault - change imported key to default if 
     *               its primary key has been deleted
     *      </UL>
     *	<LI><B>FK_NAME</B> String => foreign key name (may be null)
     *	<LI><B>PK_NAME</B> String => primary key name (may be null)
     *	<LI><B>DEFERRABILITY</B> short => can the evaluation of foreign key 
     *      constraints be deferred until commit
     *      <UL>
     *      <LI> importedKeyInitiallyDeferred - see SQL92 for definition
     *      <LI> importedKeyInitiallyImmediate - see SQL92 for definition 
     *      <LI> importedKeyNotDeferrable - see SQL92 for definition 
     *      </UL>
     *  </OL>
     *
     * @param catalog 目录名称； "" 检索那些没有目录的； null 表示从选择条件中删除目录名称
     * @param schema 模式名称模式； "" 检索那些没有架构的
     * @param table a table name
     * @return ResultSet - each row is a primary key column description 
     * @exception SQLException if a database-access error occurs.
     * @see #getExportedKeys 
     */
	ResultSet getImportedKeys(String catalog, String schema,
				String table) throws SQLException;

    /**
     * IMPORT KEY UPDATE_RULE 和 DELETE_RULE - 用于更新，更改导入的键以同意主键更新；对于删除，删除导入已删除键的行。
     */
	int importedKeyCascade	= 0;

    /**
     * IMPORT KEY UPDATE_RULE 和 DELETE_RULE - 如果已导入，则不允许更新或删除主键。
     */
	int importedKeyRestrict = 1;

    /**
     * IMPORT KEY UPDATE_RULE 和 DELETE_RULE - 如果其主键已更新或删除，则将导入的键更改为 NULL。
     */
	int importedKeySetNull  = 2;

    /**
     * IMPORT KEY UPDATE_RULE 和 DELETE_RULE - 如果已导入，则不允许更新或删除主键。
     */
	int importedKeyNoAction = 3;

    /**
     * IMPORT KEY UPDATE_RULE 和 DELETE_RULE - 如果其主键已更新或删除，则将导入的键更改为默认值。
     */
	int importedKeySetDefault  = 4;

    /**
     * IMPORT KEY DEFERRABILITY - see SQL92 for definition
     */
	int importedKeyInitiallyDeferred  = 5;

    /**
     * IMPORT KEY DEFERRABILITY - see SQL92 for definition
     */
	int importedKeyInitiallyImmediate  = 6;

    /**
     * IMPORT KEY DEFERRABILITY - see SQL92 for definition
     */
	int importedKeyNotDeferrable  = 7;

    /**
     * 获取引用表的主键列（表导出的外键）的外键列的描述。
     * 它们按 FKTABLE_CAT、FKTABLE_SCHEM、FKTABLE_NAME 和 KEY_SEQ 排序。
     *
     * <P>Each foreign key column description has the following columns:
     *  <OL>
     *	<LI><B>PKTABLE_CAT</B> String => primary key table catalog (may be null)
     *	<LI><B>PKTABLE_SCHEM</B> String => primary key table schema (may be null)
     *	<LI><B>PKTABLE_NAME</B> String => primary key table name
     *	<LI><B>PKCOLUMN_NAME</B> String => primary key column name
     *	<LI><B>FKTABLE_CAT</B> String => foreign key table catalog (may be null)
     *      being exported (may be null)
     *	<LI><B>FKTABLE_SCHEM</B> String => foreign key table schema (may be null)
     *      being exported (may be null)
     *	<LI><B>FKTABLE_NAME</B> String => foreign key table name
     *      being exported
     *	<LI><B>FKCOLUMN_NAME</B> String => foreign key column name
     *      being exported
     *	<LI><B>KEY_SEQ</B> short => sequence number within foreign key
     *	<LI><B>UPDATE_RULE</B> short => What happens to 
     *       foreign key when primary is updated:
     *      <UL>
     *      <LI> importedNoAction - do not allow update of primary 
     *               key if it has been imported
     *      <LI> importedKeyCascade - change imported key to agree 
     *               with primary key update
     *      <LI> importedKeySetNull - change imported key to NULL if 
     *               its primary key has been updated
     *      <LI> importedKeySetDefault - change imported key to default values 
     *               if its primary key has been updated
     *      <LI> importedKeyRestrict - same as importedKeyNoAction 
     *                                 (for ODBC 2.x compatibility)
     *      </UL>
     *	<LI><B>DELETE_RULE</B> short => What happens to 
     *      the foreign key when primary is deleted.
     *      <UL>
     *      <LI> importedKeyNoAction - do not allow delete of primary 
     *               key if it has been imported
     *      <LI> importedKeyCascade - delete rows that import a deleted key
     *      <LI> importedKeySetNull - change imported key to NULL if 
     *               its primary key has been deleted
     *      <LI> importedKeyRestrict - same as importedKeyNoAction 
     *                                 (for ODBC 2.x compatibility)
     *      <LI> importedKeySetDefault - change imported key to default if 
     *               its primary key has been deleted
     *      </UL>
     *	<LI><B>FK_NAME</B> String => foreign key name (may be null)
     *	<LI><B>PK_NAME</B> String => primary key name (may be null)
     *	<LI><B>DEFERRABILITY</B> short => can the evaluation of foreign key 
     *      constraints be deferred until commit
     *      <UL>
     *      <LI> importedKeyInitiallyDeferred - see SQL92 for definition
     *      <LI> importedKeyInitiallyImmediate - see SQL92 for definition 
     *      <LI> importedKeyNotDeferrable - see SQL92 for definition 
     *      </UL>
     *  </OL>
     *
     * @param catalog 目录名称； "" 检索那些没有目录的； null 表示从选择条件中删除目录名称
     * @param schema a schema name pattern; "" retrieves those
     * without a schema
     * @param table a table name
     * @return ResultSet - each row is a foreign key column description 
     * @exception SQLException if a database-access error occurs.
     * @see #getImportedKeys 
     */
	ResultSet getExportedKeys(String catalog, String schema,
				String table) throws SQLException;

    /**
     * 获取外键表中引用主键表的主键列的外键列的描述（描述一个表如何导入另一个表的键。）
     * 这通常应该返回单个外键主键对（大多数表只导入一个表中的外键一次。）
     * 它们按 FKTABLE_CAT、FKTABLE_SCHEM、FKTABLE_NAME 和 KEY_SEQ 排序。
     *
     * <P>Each foreign key column description has the following columns:
     *  <OL>
     *	<LI><B>PKTABLE_CAT</B> String => primary key table catalog (may be null)
     *	<LI><B>PKTABLE_SCHEM</B> String => primary key table schema (may be null)
     *	<LI><B>PKTABLE_NAME</B> String => primary key table name
     *	<LI><B>PKCOLUMN_NAME</B> String => primary key column name
     *	<LI><B>FKTABLE_CAT</B> String => foreign key table catalog (may be null)
     *      being exported (may be null)
     *	<LI><B>FKTABLE_SCHEM</B> String => foreign key table schema (may be null)
     *      being exported (may be null)
     *	<LI><B>FKTABLE_NAME</B> String => foreign key table name
     *      being exported
     *	<LI><B>FKCOLUMN_NAME</B> String => foreign key column name
     *      being exported
     *	<LI><B>KEY_SEQ</B> short => sequence number within foreign key
     *	<LI><B>UPDATE_RULE</B> short => What happens to 
     *       foreign key when primary is updated:
     *      <UL>
     *      <LI> importedNoAction - do not allow update of primary 
     *               key if it has been imported
     *      <LI> importedKeyCascade - change imported key to agree 
     *               with primary key update
     *      <LI> importedKeySetNull - change imported key to NULL if 
     *               its primary key has been updated
     *      <LI> importedKeySetDefault - change imported key to default values 
     *               if its primary key has been updated
     *      <LI> importedKeyRestrict - same as importedKeyNoAction 
     *                                 (for ODBC 2.x compatibility)
     *      </UL>
     *	<LI><B>DELETE_RULE</B> short => What happens to 
     *      the foreign key when primary is deleted.
     *      <UL>
     *      <LI> importedKeyNoAction - do not allow delete of primary 
     *               key if it has been imported
     *      <LI> importedKeyCascade - delete rows that import a deleted key
     *      <LI> importedKeySetNull - change imported key to NULL if 
     *               its primary key has been deleted
     *      <LI> importedKeyRestrict - same as importedKeyNoAction 
     *                                 (for ODBC 2.x compatibility)
     *      <LI> importedKeySetDefault - change imported key to default if 
     *               its primary key has been deleted
     *      </UL>
     *	<LI><B>FK_NAME</B> String => foreign key name (may be null)
     *	<LI><B>PK_NAME</B> String => primary key name (may be null)
     *	<LI><B>DEFERRABILITY</B> short => can the evaluation of foreign key 
     *      constraints be deferred until commit
     *      <UL>
     *      <LI> importedKeyInitiallyDeferred - see SQL92 for definition
     *      <LI> importedKeyInitiallyImmediate - see SQL92 for definition 
     *      <LI> importedKeyNotDeferrable - see SQL92 for definition 
     *      </UL>
     *  </OL>
     *
     * @param primaryCatalog a catalog name; "" retrieves those without a
     * catalog; null means drop catalog name from the selection criteria
     * @param primarySchema a schema name pattern; "" retrieves those
     * without a schema
     * @param primaryTable the table name that exports the key
     * @param foreignCatalog a catalog name; "" retrieves those without a
     * catalog; null means drop catalog name from the selection criteria
     * @param foreignSchema a schema name pattern; "" retrieves those
     * without a schema
     * @param foreignTable the table name that imports the key
     * @return ResultSet - each row is a foreign key column description 
     * @exception SQLException if a database-access error occurs.
     * @see #getImportedKeys 
     */
	ResultSet getCrossReference(
		String primaryCatalog, String primarySchema, String primaryTable,
		String foreignCatalog, String foreignSchema, String foreignTable
		) throws SQLException;

    /**
     * 获取此数据库支持的所有标准 SQL 类型的描述。
     * 它们按 DATA_TYPE 排序，然后按数据类型映射到相应 JDBC SQL 类型的紧密程度排序。
     *
     * <P>Each type description has the following columns:
     *  <OL>
     *	<LI><B>TYPE_NAME</B> String => Type name
     *	<LI><B>DATA_TYPE</B> short => SQL data type from java.sql.Types
     *	<LI><B>PRECISION</B> int => maximum precision
     *	<LI><B>LITERAL_PREFIX</B> String => prefix used to quote a literal 
     *      (may be null)
     *	<LI><B>LITERAL_SUFFIX</B> String => suffix used to quote a literal 
            (may be null)
     *	<LI><B>CREATE_PARAMS</B> String => parameters used in creating 
     *      the type (may be null)
     *	<LI><B>NULLABLE</B> short => can you use NULL for this type?
     *      <UL>
     *      <LI> typeNoNulls - does not allow NULL values
     *      <LI> typeNullable - allows NULL values
     *      <LI> typeNullableUnknown - nullability unknown
     *      </UL>
     *	<LI><B>CASE_SENSITIVE</B> boolean=> is it case sensitive?
     *	<LI><B>SEARCHABLE</B> short => can you use "WHERE" based on this type:
     *      <UL>
     *      <LI> typePredNone - No support
     *      <LI> typePredChar - Only supported with WHERE .. LIKE
     *      <LI> typePredBasic - Supported except for WHERE .. LIKE
     *      <LI> typeSearchable - Supported for all WHERE ..
     *      </UL>
     *	<LI><B>UNSIGNED_ATTRIBUTE</B> boolean => is it unsigned?
     *	<LI><B>FIXED_PREC_SCALE</B> boolean => can it be a money value?
     *	<LI><B>AUTO_INCREMENT</B> boolean => can it be used for an 
     *      auto-increment value?
     *	<LI><B>LOCAL_TYPE_NAME</B> String => localized version of type name 
     *      (may be null)
     *	<LI><B>MINIMUM_SCALE</B> short => minimum scale supported
     *	<LI><B>MAXIMUM_SCALE</B> short => maximum scale supported
     *	<LI><B>SQL_DATA_TYPE</B> int => unused
     *	<LI><B>SQL_DATETIME_SUB</B> int => unused
     *	<LI><B>NUM_PREC_RADIX</B> int => usually 2 or 10
     *  </OL>
     *
     * @return ResultSet - each row is a SQL type description 
     * @exception SQLException if a database-access error occurs.
     */
	ResultSet getTypeInfo() throws SQLException;
	
    /**
     * TYPE NULLABLE - does not allow NULL values.
     */
    int typeNoNulls = 0;

    /**
     * TYPE NULLABLE - allows NULL values.
     */
    int typeNullable = 1;

    /**
     * TYPE NULLABLE - nullability unknown.
     */
    int typeNullableUnknown = 2;

    /**
     * TYPE INFO SEARCHABLE - No support.
     */
	int typePredNone = 0;

    /**
     * TYPE INFO SEARCHABLE - Only supported with WHERE .. LIKE.
     */
	int typePredChar = 1;

    /**
     * TYPE INFO SEARCHABLE -  Supported except for WHERE .. LIKE.
     */
	int typePredBasic = 2;

    /**
     * TYPE INFO SEARCHABLE - Supported for all WHERE ...
     */
	int typeSearchable  = 3;

    /**
     * 获取表的索引和统计信息的描述。它们按 NON_UNIQUE、TYPE、INDEX_NAME 和 ORDINAL_POSITION 排序。
     *
     * <P>Each index column description has the following columns:
     *  <OL>
     *	<LI><B>TABLE_CAT</B> String => table catalog (may be null)
     *	<LI><B>TABLE_SCHEM</B> String => table schema (may be null)
     *	<LI><B>TABLE_NAME</B> String => table name
     *	<LI><B>NON_UNIQUE</B> boolean => Can index values be non-unique? 
     *      false when TYPE is tableIndexStatistic
     *	<LI><B>INDEX_QUALIFIER</B> String => index catalog (may be null); 
     *      null when TYPE is tableIndexStatistic
     *	<LI><B>INDEX_NAME</B> String => index name; null when TYPE is 
     *      tableIndexStatistic
     *	<LI><B>TYPE</B> short => index type:
     *      <UL>
     *      <LI> tableIndexStatistic - this identifies table statistics that are
     *           returned in conjuction with a table's index descriptions
     *      <LI> tableIndexClustered - this is a clustered index
     *      <LI> tableIndexHashed - this is a hashed index
     *      <LI> tableIndexOther - this is some other style of index
     *      </UL>
     *	<LI><B>ORDINAL_POSITION</B> short => column sequence number 
     *      within index; zero when TYPE is tableIndexStatistic
     *	<LI><B>COLUMN_NAME</B> String => column name; null when TYPE is 
     *      tableIndexStatistic
     *	<LI><B>ASC_OR_DESC</B> String => column sort sequence, "A" => ascending, 
     *      "D" => descending, may be null if sort sequence is not supported; 
     *      null when TYPE is tableIndexStatistic	
     *	<LI><B>CARDINALITY</B> int => When TYPE is tableIndexStatistic, then 
     *      this is the number of rows in the table; otherwise, it is the 
     *      number of unique values in the index.
     *	<LI><B>PAGES</B> int => When TYPE is  tableIndexStatisic then 
     *      this is the number of pages used for the table, otherwise it 
     *      is the number of pages used for the current index.
     *	<LI><B>FILTER_CONDITION</B> String => Filter condition, if any.  
     *      (may be null)
     *  </OL>
     *
     * @param catalog a catalog name; "" retrieves those without a
     * catalog; null means drop catalog name from the selection criteria
     * @param schema a schema name pattern; "" retrieves those without a schema
     * @param table a table name  
     * @param unique when true, return only indices for unique values; 
     *     when false, return indices regardless of whether unique or not 
     * @param approximate when true, result is allowed to reflect approximate 
     *     or out of data values; when false, results are requested to be 
     *     accurate
     * @return ResultSet - each row is an index column description 
     * @exception SQLException if a database-access error occurs.
     */
	ResultSet getIndexInfo(String catalog, String schema, String table,
			boolean unique, boolean approximate)
					throws SQLException;

    /**
     * INDEX INFO TYPE - 标识与表的索引描述一起返回的表统计信息
     */
	short tableIndexStatistic = 0;

    /**
     * INDEX INFO TYPE - this identifies a clustered index
     */
	short tableIndexClustered = 1;

    /**
     * INDEX INFO TYPE - this identifies a hashed index
     */
	short tableIndexHashed    = 2;

    /**
     * INDEX INFO TYPE - this identifies some other form of index
     */
	short tableIndexOther     = 3;
 }

