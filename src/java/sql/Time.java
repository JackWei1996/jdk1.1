/*
 * @(#)Time.java	1.8 01/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

/**
 * 此类是 java.util.Date 的一个瘦包装器，它允许 JDBC 将其识别为 SQL TIME 值。
 * 它添加了格式化和解析操作以支持时间值的 JDBC 转义语法。
 */
public class Time extends java.util.Date {

    /**
     * 构造时间对象
     *
     * @param hour 0 to 23
     * @param minute 0 to 59
     * @param second 0 to 59
     */
    public Time(int hour, int minute, int second) {
	super(70, 0, 1, hour, minute, second);
    }
   
    /**
     * 使用毫秒时间值构造时间
     *
     * @param time milliseconds since January 1, 1970, 00:00:00 GMT
     */
    public Time(long time) {
	// 如果毫秒时间值包含日期信息，请将其屏蔽掉。
	super(time);
	int hours = getHours();
	int minutes = getMinutes();
	int seconds = getSeconds();
	super.setTime(0);
	setHours(hours);
	setMinutes(minutes);
	setSeconds(seconds);
    }

    /**
     * 使用毫秒时间值设置时间
     *
     * @param time milliseconds since January 1, 1970, 00:00:00 GMT
     */
    public void setTime(long time) {
	// 如果毫秒时间值包含日期信息，请将其屏蔽掉。
	super.setTime(time);
	int hours = getHours();
	int minutes = getMinutes();
	int seconds = getSeconds();
	super.setTime(0);
	setHours(hours);
	setMinutes(minutes);
	setSeconds(seconds);
    }

    /**
     * 将 JDBC 时间转义格式的字符串转换为时间值
     *
     * @param s time in format "hh:mm:ss"
     * @return corresponding Time
     */
    public static Time valueOf(String s) {
	int hour;
	int minute;
	int second;
	int firstColon;
	int secondColon;

	if (s == null) throw new IllegalArgumentException();

	firstColon = s.indexOf(':');
	secondColon = s.indexOf(':', firstColon+1);
	if ((firstColon > 0) & (secondColon > 0) & 
	    (secondColon < s.length()-1)) {
	    hour = Integer.parseInt(s.substring(0, firstColon));
	    minute = 
		Integer.parseInt(s.substring(firstColon+1, secondColon));
	    second = Integer.parseInt(s.substring(secondColon+1));	    
	} else {
	    throw new IllegalArgumentException();
	}

	return new Time(hour, minute, second);
    }
   
    /**
     * 以 JDBC 日期转义格式格式化时间
     *
     * @return a String in hh:mm:ss format
     */
    public String toString () {
	int hour = super.getHours();
	int minute = super.getMinutes();
	int second = super.getSeconds();
	String hourString;
	String minuteString;
	String secondString;

	if (hour < 10) {
	    hourString = "0" + hour;
	} else {		
	    hourString = Integer.toString(hour);
	}
	if (minute < 10) {
	    minuteString = "0" + minute;
	} else {		
	    minuteString = Integer.toString(minute);
	}
	if (second < 10) {
	    secondString = "0" + second;
	} else {		
	    secondString = Integer.toString(second);
	}
	return (hourString + ":" + minuteString + ":" + secondString);
    }

    // 覆盖所有继承自 java.util.Date 的日期操作；
    public int getYear() {
	throw new IllegalArgumentException();
    }

    public int getMonth() {
	throw new IllegalArgumentException();
    }
    
    public int getDay() {
	throw new IllegalArgumentException();
    }

    public int getDate() {
	throw new IllegalArgumentException();
    }

    public void setYear(int i) {
	throw new IllegalArgumentException();
    }

    public void setMonth(int i) {
	throw new IllegalArgumentException();
    }

    public void setDate(int i) {
	throw new IllegalArgumentException();
    }
}
