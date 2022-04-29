/*
 * @(#)DriverPropertyInfo.java	1.7 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

/**
 * DriverPropertyInfo 类只对需要通过 getDriverProperties 与驱动程序交互以发现和提供连接属性的高级程序员感兴趣。
 */

public class DriverPropertyInfo {

    /**
     * 构造一个带有名称和值的 DriverPropertyInfo；其他成员默认为其初始值。
     *
     * @param name 物业名称
     * @param value 当前值，可能为 null
     */
    public DriverPropertyInfo(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 属性的名称。
     */
    public String name;

    /**
     * 物业的简要说明。这可能为空。
     */
    public String description = null;

    /**
     * 如果必须在 Driver.connect 期间为此属性提供值，则 "required" 为真。否则，该属性是可选的。
     */
    public boolean required = false;

    /**
     * “value”根据提供给 getPropertyInfo 的信息、Java 环境和驱动程序提供的默认值的组合指定属性的当前值。
     * 如果不知道任何值，这可能为 null。
     */
    public String value = null;

    /**
     * 如果该值可以从一组特定的值中选择，那么这是一个可能值的数组。否则它应该为空。
     */
    public String[] choices = null;
}
