/*
 * @(#)SQLWarning.java	1.7 01/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

/**
 * SQLWarning 类提供有关数据库访问警告的信息。警告以静默方式链接到其方法导致报告的对象。
 *
 * @see Connection#getWarnings
 * @see Statement#getWarnings
 * @see ResultSet#getWarnings 
 */
public class SQLWarning extends SQLException {

    /**
     * 构造一个完全指定的 SQLWarning。
     *
     * @param reason 警告的描述
     * @param SQLState 标识警告的 XOPEN 代码
     * @param vendorCode 数据库供应商特定的警告代码
     */
     public SQLWarning(String reason, String SQLstate, int vendorCode) {
	super(reason, SQLstate, vendorCode);
	DriverManager.println("SQLWarning: reason(" + reason + 
			      ") SQLstate(" + SQLstate + 
			      ") vendor code(" + vendorCode + ")");
    }


    /**
     * 构造一个带有原因和 SQLState 的 SQLWarning； vendorCode 默认为 0。
     *
     * @param reason 警告的描述
     * @param SQLState 标识警告的 XOPEN 代码
     */
    public SQLWarning(String reason, String SQLstate) {
	super(reason, SQLstate);
	DriverManager.println("SQLWarning: reason(" + reason + 
				  ") SQLState(" + SQLstate + ")");
    }

    /**
     * 构造一个带有原因的 SQLWarning； SQLState 默认为 null，vendorCode 默认为 0。
     *
     * @param reason 警告的描述
     */
    public SQLWarning(String reason) {
	super(reason);
	DriverManager.println("SQLWarning: reason(" + reason + ")");
    }

    /**
     * 构造一个 SQLWarning ； reason 默认为 null，SQLState 默认为 null，vendorCode 默认为 0。
     */
    public SQLWarning() {
	super();
	DriverManager.println("SQLWarning: ");
    }


    /**
     * 将警告链接到这个
     *
     * @return 链中的下一个 SQLException，如果没有则为 null
     */
    public SQLWarning getNextWarning() {
	try {
	    return ((SQLWarning)getNextException());
	} catch (ClassCastException ex) {
	    // 链接的值不是 SQLWarning。
	    // 这是添加它的人的编程错误
	    // SQLWarning 链。我们抛出一个Java“错误”。
	    throw new Error("SQLWarning chain holds value that is not a SQLWarning");
	}
    }

    /**
     * 将 SQLWarning 添加到链的末尾。
     *
     * @param w SQLException 链的新端
     */
    public void setNextWarning(SQLWarning w) {
	setNextException(w);
    }

}
