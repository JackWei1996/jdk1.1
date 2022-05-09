/*
 * @(#)Timestamp.java	1.15 01/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.sql;

/**
 * 此类是 java.util.Date 的一个瘦包装器，它允许 JDBC 将其识别为 SQL TIMESTAMP 值。
 * 它增加了保存 SQL TIMESTAMP nanos 值的能力，并提供格式化和解析操作以支持时间戳值的 JDBC 转义语法。
 *
 * 注意：此类型是 java.util.Date 和单独的 nanos 值的组合。
 * 只有整数秒存储在 java.util.Date 组件中。
 * 小数秒 - 纳秒 - 是分开的。 getTime 方法只会返回整数秒。
 * 如果需要包含小数秒的时间值，则必须将纳秒转换为毫秒 (nanos1000000) 并将其添加到 getTime 值。
 * 另请注意， hashcode() 方法使用底层 java.util.Data 实现，因此在其计算中不包括 nanos。
 */
public class Timestamp extends java.util.Date {

    /**
     * 构造时间戳对象
     *
     * @param year year-1900
     * @param month 0 to 11 
     * @param day 1 to 31
     * @param hour 0 to 23
     * @param minute 0 to 59
     * @param second 0 to 59
     * @param nano 0 to 999,999,999
     * @deprecated
     */
    public Timestamp(int year, int month, int date, 
		     int hour, int minute, int second, int nano) {
	super(year, month, date, hour, minute, second);
	if (nano > 999999999 || nano < 0) {
	    throw new IllegalArgumentException("nanos > 999999999 or < 0");
	}
	nanos = nano;
    }

    /**
     * 使用毫秒时间值构造时间戳。整数秒存储在基础日期值中；小数秒存储在 nanos 值中。
     *
     * @param time milliseconds since January 1, 1970, 00:00:00 GMT 
     */
    public Timestamp(long time) {
	super((time/1000)*1000);
	nanos = (int)((time%1000) * 1000000);
	if (nanos < 0) {
	    nanos = 1000000000 + nanos;	    
	    setTime(((time/1000)-1)*1000);
	}
    }

    private int nanos;

    /**
     * 将 JDBC 时间戳转义格式的字符串转换为时间戳值
     *
     * @param s timestamp in format "yyyy-mm-dd hh:mm:ss.fffffffff"
     * @return 对应的时间戳
     */
    public static Timestamp valueOf(String s) {
	String date_s;
	String time_s;
	String nanos_s;
	int year;
	int month;
	int day;
	int hour;
	int minute;
	int second;
	int a_nanos = 0;
	int firstDash;
	int secondDash;
	int dividingSpace;
	int firstColon = 0;
	int secondColon = 0;
	int period = 0;
	String formatError = "Timestamp format must be yyyy-mm-dd hh:mm:ss.fffffffff";
	String zeros = "000000000";

	if (s == null) throw new IllegalArgumentException("null string");

	// 将字符串拆分为日期和时间组件
	s = s.trim();
	dividingSpace = s.indexOf(' ');
	if (dividingSpace > 0) {
	    date_s = s.substring(0,dividingSpace);
	    time_s = s.substring(dividingSpace+1);
	} else {
	    throw new IllegalArgumentException(formatError);
	}


	// Parse the date
	firstDash = date_s.indexOf('-');
	secondDash = date_s.indexOf('-', firstDash+1);

	// Parse the time
	if (time_s == null) 
	    throw new IllegalArgumentException(formatError);
	firstColon = time_s.indexOf(':');
	secondColon = time_s.indexOf(':', firstColon+1);
	period = time_s.indexOf('.', secondColon+1);

	// Convert the date
	if ((firstDash > 0) & (secondDash > 0) & 
	    (secondDash < date_s.length()-1)) {
	    year = Integer.parseInt(date_s.substring(0, firstDash)) - 1900;
	    month = 
		Integer.parseInt(date_s.substring
				 (firstDash+1, secondDash)) - 1;
	    day = Integer.parseInt(date_s.substring(secondDash+1));
	} else {		
	    throw new IllegalArgumentException(formatError);
	}

	// 转换时间；默认缺少纳米
	if ((firstColon > 0) & (secondColon > 0) & 
	    (secondColon < time_s.length()-1)) {
	    hour = Integer.parseInt(time_s.substring(0, firstColon));
	    minute = 
		Integer.parseInt(time_s.substring(firstColon+1, secondColon));
	    if ((period > 0) & (period < time_s.length()-1)) {
		second = 
		    Integer.parseInt(time_s.substring(secondColon+1, period));
		nanos_s = time_s.substring(period+1);
		if (nanos_s.length() > 9) 
		    throw new IllegalArgumentException(formatError);
		if (!Character.isDigit(nanos_s.charAt(0)))
		    throw new IllegalArgumentException(formatError);
		nanos_s = nanos_s + zeros.substring(0,9-nanos_s.length());
		a_nanos = Integer.parseInt(nanos_s);
	    } else if (period > 0) {
		throw new IllegalArgumentException(formatError);
	    } else {
		second = Integer.parseInt(time_s.substring(secondColon+1));
	    }
	} else {
	    throw new IllegalArgumentException();
	}

	return new Timestamp(year, month, day, hour, minute, second, a_nanos);
    }

    /**
     * Format a timestamp in JDBC timestamp escape format
     *
     * @return a String in yyyy-mm-dd hh:mm:ss.fffffffff format
     */
    public String toString () {
	int year = super.getYear() + 1900;
	int month = super.getMonth() + 1;
	int day = super.getDate();
	int hour = super.getHours();
	int minute = super.getMinutes();
	int second = super.getSeconds();
	String yearString;
	String monthString;
	String dayString;
	String hourString;
	String minuteString;
	String secondString;
	String nanosString;
	String zeros = "000000000";

		
	yearString = "" + year;
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
	if (nanos == 0) {
	    nanosString = "0";
	} else {
	    nanosString = Integer.toString(nanos);

	    // Add leading zeros
	    nanosString = zeros.substring(0,(9-nanosString.length())) + 
		nanosString;
	    
	    // Truncate trailing zeros
	    char[] nanosChar = new char[nanosString.length()];
	    nanosString.getChars(0, nanosString.length(), nanosChar, 0);
	    int truncIndex = 8;	    
	    while (nanosChar[truncIndex] == '0') {
		truncIndex--;
	    }
	    nanosString = new String(nanosChar,0,truncIndex+1);
	}
	
	return (yearString + "-" + monthString + "-" + dayString + " " + 
		hourString + ":" + minuteString + ":" + secondString + "."
                + nanosString);
    }

    /**
     * Get the Timestamp's nanos value
     *
     * @return the Timestamp's fractional seconds component
     */
    public int getNanos() {
	return nanos;
    }

    /**
     * Set the Timestamp's nanos value
     *
     * @param n the new fractional seconds component
     */
    public void setNanos(int n) {
	if (n > 999999999 || n < 0) {
	    throw new IllegalArgumentException("nanos > 999999999 or < 0");
	}
	nanos = n;
    }

    /**
     * Test Timestamp values for equality
     *
     * @param ts the Timestamp value to compare with
     */
    public boolean equals(Timestamp ts) {
	if (super.equals(ts)) {
	    if (nanos == ts.nanos) {
		return true;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    /**
     * Is this timestamp earlier than the timestamp argument?
     *
     * @param ts the Timestamp value to compare with
     */
    public boolean before(Timestamp ts) {
	if (super.before(ts)) {
	    return true;
	} else {
	    if (super.equals(ts)) {
		if (nanos < ts.nanos) {
		    return true;
		} else {
		    return false;
		}
	    } else {
		return false;
	    }
	}
    }

    /**
     * Is this timestamp later than the timestamp argument?
     *
     * @param ts the Timestamp value to compare with
     */
    public boolean after(Timestamp ts) {
	if (super.after(ts)) {
	    return true;
	} else {
	    if (super.equals(ts)) {
		if (nanos > ts.nanos) {
		    return true;
		} else {
		    return false;
		}
	    } else {
		return false;
	    }
	}
    }
}

