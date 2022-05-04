/*
 * @(#)SQLException.java	1.7 01/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

/**
 * SQLException 类提供有关数据库访问错误的信息。
 *
 * 每个 SQLException 提供几种信息：
 * <UL>
 *   <LI> 描述错误的字符串。这用作 Java 异常消息，可通过 getMesage() 方法获得
 *   <LI> 遵循 XOPEN SQLstate 约定的“SQLstate”字符串。 XOPEN SQL 规范中描述的 SQLState 字符串的值。
 *   <LI> 供应商特定的整数错误代码。通常这将是底层数据库返回的实际错误代码。
 *   <LI> 到下一个异常的链。这可用于提供额外的错误信息。
 * </UL>
 */
public class SQLException extends Exception {

    /**
     * 构造一个完全指定的 SQLException
     *
     * @param reason 异常的描述
     * @param SQLState 标识异常的 XOPEN 代码
     * @param vendorCode 数据库供应商特定的异常代码
     */
    public SQLException(String reason, String SQLState, int vendorCode) {
	super(reason);
	this.SQLState = SQLState;
	this.vendorCode = vendorCode;
	if (!(this instanceof SQLWarning)) {
	    if (DriverManager.getLogStream() != null) {
		DriverManager.println("SQLException: SQLState(" + SQLState + 
						") vendor code(" + vendorCode + ")");
		printStackTrace(DriverManager.getLogStream());
	    }
	}
    }


    /**
     * 构造一个带有原因和 SQLState 的 SQLException； vendorCode 默认为 0。
     *
     * @param reason 异常的描述
     * @param SQLState 标识异常的 XOPEN 代码
     */
    public SQLException(String reason, String SQLState) {
	super(reason);
	this.SQLState = SQLState;
	this.vendorCode = 0;
	if (!(this instanceof SQLWarning)) {
	    if (DriverManager.getLogStream() != null) {
		printStackTrace(DriverManager.getLogStream());
		DriverManager.println("SQLException: SQLState(" + SQLState + ")");
	    }
	}
    }

    /**
     * 构造一个带有原因的 SQLException； SQLState 默认为 null，vendorCode 默认为 0。
     *
     * @param reason 异常的描述
     */
    public SQLException(String reason) {
	super(reason);
	this.SQLState = null;
	this.vendorCode = 0;
	if (!(this instanceof SQLWarning)) {
	    if (DriverManager.getLogStream() != null) {
		printStackTrace(DriverManager.getLogStream());
	    }
	}
    }

    /**
     * 构造一个 SQLException； reason 默认为 null，SQLState 默认为 null，vendorCode 默认为 0。
     * */
    public SQLException() {
	super();
	this.SQLState = null;
	this.vendorCode = 0;
	if (!(this instanceof SQLWarning)) {
	    if (DriverManager.getLogStream() != null) {
		printStackTrace(DriverManager.getLogStream());
	    }
	}
    }

    /**
     * Get the SQLState（获取 SQL 状态）
     *
     * @return the SQLState value
     */
    public String getSQLState() {
	return (SQLState);
    }	

    /**
     * 获取供应商特定的异常代码
     *
     * @return 供应商的错误代码
     */
    public int getErrorCode() {
	return (vendorCode);
    }

    /**
     * 获取链接到这个的异常。
     *
     * @return 链中的下一个 SQLException，如果没有则为 null
     */
    public SQLException getNextException() {
	return (next);
    }

    /**
     * 将 SQLException 添加到链的末尾。
     *
     * @param ex SQLException 链的新端
     */
    public synchronized void setNextException(SQLException ex) {
	SQLException theEnd = this;
	while (theEnd.next != null) {
	    theEnd = theEnd.next;
	}
	theEnd.next = ex;
    }

    private String SQLState;
    private int vendorCode;
    private SQLException next;
}
