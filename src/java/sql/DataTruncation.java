/*
 * @(#)DataTruncation.java	1.7 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

/**
 * 当 JDBC 意外截断数据值时，它会报告 DataTruncation 警告（读取时）或抛出 DataTruncation 异常（写入时）。
 *
 * DataTruncation 的 SQL 状态是“01004”。
 */

public class DataTruncation extends SQLWarning {

    /**
     * 创建一个数据截断对象。 SQLState 初始化为 01004，原因设置为“数据截断”并且 vendorCode 设置为 SQLException 默认值。
     *
     * @param index 参数或列值的索引
     * @param parameter 如果参数值被截断，则为 true
     * @param read 如果读取被截断，则为 true
     * @param dataSize 数据的原始大小
     * @param transferSize 截断后的大小
     */
    public DataTruncation(int index, boolean parameter,
			  boolean read, int dataSize,
			  int transferSize) {
	super("Data truncation", "01004");
	this.index = index;
	this.parameter = parameter;
	this.read = read;
	this.dataSize = dataSize;
	this.transferSize = transferSize;
	DriverManager.println("    DataTruncation: index(" + index +
			      ") parameter(" + parameter +
			      ") read(" + read +
			      ") data size(" + dataSize +
			      ") transfer size(" + transferSize + ")");
    }

    /**
     * 获取被截断的列或参数的索引。
     *
     * 如果列或参数索引未知，这可能为 -1，在这种情况下，“参数”和“读取”字段应被忽略。
     *
     * @return 截断参数或列值的索引。
     */
    public int getIndex() {
	return index;
    }

    /**
     * 这是截断的参数值吗？
     *
     * @return 如果值是参数则为真；如果它是列值，则为 false。
     */
    public boolean getParameter() {
	return parameter;
    }

    /**
     * 这是读取截断吗？
     *
     * @return 如果从数据库中读取值被截断，则为真；如果数据在写入时被截断，则为 false。
     */
    public boolean getRead() {
	return read;
    }

    /**
     * 获取应该传输的数据字节数。如果正在执行数据转换，此数字可能是近似值。如果大小未知，则该值可能为“-1”。
     *
     * @return 应该传输的数据字节数
     */
    public int getDataSize() {
	return dataSize;
    }

    /**
     * 获取实际传输的数据字节数。如果大小未知，则该值可能为“-1”。
     *
     * @return 实际传输的数据字节数
     */
    public int getTransferSize() {
	return transferSize;
    }

    private int index;
    private boolean parameter;
    private boolean read;	
    private int dataSize;
    private int transferSize;

}
