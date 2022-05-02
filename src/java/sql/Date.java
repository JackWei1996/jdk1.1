/*
 * @(#)Date.java	1.7 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

/**
 * 此类是 java.util.Date 的一个瘦包装器，它允许 JDBC 将其识别为 SQL DATE 值。
 * 它添加了格式化和解析操作以支持日期值的 JDBC 转义语法。
 */
public class Date extends java.util.Date {

    /**
     * Construct a Date  
     *
     * @param year year-1900
     * @param month 0 to 11 
     * @param day 1 to 31
     */
    public Date(int year, int month, int day) {
	super(year, month, day);
    }

    /**
     * 使用毫秒时间值构造日期
     *
     * @param date milliseconds since January 1, 1970, 00:00:00 GMT
     */
    public Date(long date) {
	// 如果毫秒日期值包含时间信息，请将其屏蔽掉。
	super(date);
	int year = getYear();
	int month = getMonth();
	int day = getDate();
	super.setTime(0);
	setYear(year);
	setMonth(month);
	setDate(day);
    }

    /**
     * 使用毫秒时间值构造日期
     *
     * @param date milliseconds since January 1, 1970, 00:00:00 GMT
     */
    public void setTime(long date) {
	// 如果毫秒日期值包含时间信息，请将其屏蔽掉。
	super.setTime(date);
	int year = getYear();
	int month = getMonth();
	int day = getDate();
	super.setTime(0);
	setYear(year);
	setMonth(month);
	setDate(day);
    }

    /**
     * 将 JDBC 日期转义格式的字符串转换为日期值
     *
     * @param s date in format "yyyy-mm-dd"
     * @return 对应日期
     */
    public static Date valueOf(String s) {
	int year;
	int month;
	int day;
	int firstDash;
	int secondDash;

	if (s == null) throw new IllegalArgumentException();

	firstDash = s.indexOf('-');
	secondDash = s.indexOf('-', firstDash+1);
	if ((firstDash > 0) & (secondDash > 0) & (secondDash < s.length()-1)) {
	    year = Integer.parseInt(s.substring(0, firstDash)) - 1900;
	    month = Integer.parseInt(s.substring(firstDash+1, secondDash)) - 1;
	    day = Integer.parseInt(s.substring(secondDash+1));	 
	} else {
	    throw new IllegalArgumentException();
	}
			
	return new Date(year, month, day);
    }

    /**
     * 以 JDBC 日期转义格式格式化日期
     *
     * @return a String in yyyy-mm-dd format
     */
    public String toString () {
	int year = super.getYear() + 1900;
	int month = super.getMonth() + 1;
	int day = super.getDate();
	String yearString;
	String monthString;
	String dayString;
		
	yearString = Integer.toString(year);

	if (month < 10) {
	    monthString = "0" + month;
	} else {		
	    monthString = Integer.toString(month);
	}

	if (day < 10) {
	    dayString = "0" + day;
	} else {		
	    dayString = Integer.toString(day);
	}

	return ( yearString + "-" + monthString + "-" + dayString);
    }

    // 覆盖从 java.util.Date 继承的所有时间操作；
    public int getHours() {
	throw new IllegalArgumentException();
    }

    public int getMinutes() {
	throw new IllegalArgumentException();
    }
    
    public int getSeconds() {
	throw new IllegalArgumentException();
    }

    public void setHours(int i) {
	throw new IllegalArgumentException();
    }

    public void setMinutes(int i) {
	throw new IllegalArgumentException();
    }

    public void setSeconds(int i) {
	throw new IllegalArgumentException();
    }

}
