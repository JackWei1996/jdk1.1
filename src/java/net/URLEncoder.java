/*
 * @(#)URLEncoder.java	1.13 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.net;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.BitSet;

/**
 * 该类包含一个实用方法，用于将 String 转换为称为“x-www-form-urlencoded”格式的 MIME 格式。
 * <p>
 * To convert a <code>String</code>, each character is examined in turn:
 * <ul>
 * <li>The ASCII characters '<code>a</code>' through '<code>z</code>', 
 *     '<code>A</code>' through '<code>Z</code>', and '<code>0</code>' 
 *     through '<code>9</code>' remain the same. 
 * <li>The space character '<code>&nbsp;</code>' is converted into a 
 *     plus sign '<code>+</code>'. 
 * <li>All other characters are converted into the 3-character string 
 *     "<code>%<i>xy</i></code>", where <i>xy</i> is the two-digit
 *     hexadecimal representation of the lower 8-bits of the character.
 * </ul>
 *
 * @author  Herb Jellinek
 * @version 1.13, 12/12/01
 * @since   JDK1.0
 */
public class URLEncoder {
    static BitSet dontNeedEncoding;
    static final int caseDiff = ('a' - 'A');

    /* 未编码的字符列表已通过参考 O'Reilly 的“HTML：权威指南”（第 164 页）确定。 */
       
    static {
	dontNeedEncoding = new BitSet(256);
	int i;
	for (i = 'a'; i <= 'z'; i++) {
	    dontNeedEncoding.set(i);
	}
	for (i = 'A'; i <= 'Z'; i++) {
	    dontNeedEncoding.set(i);
	}
	for (i = '0'; i <= '9'; i++) {
	    dontNeedEncoding.set(i);
	}
	dontNeedEncoding.set(' '); /* encoding a space to a + is done in the encode() method */
	dontNeedEncoding.set('-');
	dontNeedEncoding.set('_');
	dontNeedEncoding.set('.');
	dontNeedEncoding.set('*');
    }

    /**
     * You can't call the constructor.
     */
    private URLEncoder() { }

    /**
     * Translates a string into <code>x-www-form-urlencoded</code> format.
     *
     * @param   s   <code>String</code> to be translated.
     * @return  the translated <code>String</code>.
     * @since   JDK1.0
     */
    public static String encode(String s) {
	int maxBytesPerChar = 10;
	ByteArrayOutputStream out = new ByteArrayOutputStream(s.length());
	ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);
	OutputStreamWriter writer = new OutputStreamWriter(buf);

	for (int i = 0; i < s.length(); i++) {
	    int c = (int)s.charAt(i);
	    if (dontNeedEncoding.get(c)) {
		if (c == ' ') {
		    c = '+';
		}
		out.write(c);
	    } else {
		// convert to external encoding before hex conversion
		try {
		    writer.write(c);
		    writer.flush();
		} catch(IOException e) {
		    buf.reset();
		    continue;
		}
		byte[] ba = buf.toByteArray();
		for (int j = 0; j < ba.length; j++) {
		    out.write('%');
		    char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
		    // converting to use uppercase letter as part of
		    // the hex value if ch is a letter.
		    if (Character.isLetter(ch)) {
			ch -= caseDiff;
		    }
		    out.write(ch);
		    ch = Character.forDigit(ba[j] & 0xF, 16);
		    if (Character.isLetter(ch)) {
			ch -= caseDiff;
		    }
		    out.write(ch);
		}
		buf.reset();
	    }
	}

	return out.toString();
    }
}
