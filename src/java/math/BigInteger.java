/*
 * @(#)BigInteger.java	1.12 2001/12/12
 *
 * 版权所有 2002年 Sun Microsystems，Inc.保留所有权利。
 * SUN 所有者/机密。使用受许可条款约束。
 */

package java.math;

import java.util.Random;

/**
 * 不可变的任意精度整数。所有操作的行为都好像 BigIntegers 以二进制补码表示法表示（如 Java 的原始整数类型）。
 * BigIntegers 提供了与 Java 的所有原始整数运算符以及来自 java.lang.Math 的所有相关静态方法的类似物。
 * 此外，BigIntegers 还提供模算术、GCD 计算、素数测试、素数生成、单比特操作以及其他一些零碎的操作。
 *
 * 算术运算的语义完全模仿 Java 的整数算术运算符，如 Java 语言规范中所定义。
 * 例如，除以零会引发 ArithmeticException异常，而负数除以正数会产生负数（或零）余数。
 * 规范中关于溢出的所有细节都被忽略了，因为 BigInteger 被制作得尽可能大以适应操作的结果。
 *
 *移位操作的语义扩展了 Java 移位运算符的语义，以允许负移位距离。
 * 具有负移位距离的右移会导致左移，反之亦然。
 * 无符号右移运算符 (>>>) 被省略，因为此操作与此类提供的“无限字长”抽象结合起来毫无意义。
 *
 * 位逻辑运算的语义完全模仿 Java 的位整数运算符。
 * 二元运算符（与、或、异或）在执行运算之前隐式地对两个操作数中较短的一个执行符号扩展。
 *
 * 比较操作执行有符号整数比较，类似于 java 的关系和相等运算符执行的比较。
 *
 * 提供模块化算术运算来计算余数、执行幂运算和计算乘法逆运算。
 * 这些方法总是返回一个非负的结果，介于 0 和 (modulus - 1) 之间，包括 0 和 (modulus - 1)。
 *
 * 位运算对其操作数的二进制补码表示的位进行操作。如有必要，操作数会进行符号扩展，使其包含指定位。
 * 任何位操作都不能产生与被操作的 BigInteger 符号不同的数字，因为它们只影响位，
 * 并且此类提供的“无限字长”抽象确保有无限多个“虚拟符号位”是在每个 BigInteger 之前。
 *
 *
 * @see BigDecimal
 * @version 	1.12, 2001/12/12
 * @author      Josh Bloch（乔什·布洛赫）
 */
public class BigInteger extends Number {

    /*
     * 该数字以“最小”符号幅度格式在内部存储（即，没有 BigInteger 在其幅度中具有前导零字节）。
     * 零用符号 0 表示（和零长度幅度）。因此，每个值都有一个表示。
     */
    private int signum;
    private byte[] magnitude;

    /*
     * 这些“冗余字段”使用可识别的无意义值进行初始化，并在第一次需要它们时缓存（或者永远不会，如果不需要它们）。
     */
    private int bitCount =  -1;
    private int bitLength = -1;
    private int firstNonzeroByteNum = -2;  /* 仅用于负数 */
    private int lowestSetBit = -2;

    //Constructors

    /**
     * 将包含（有符号）整数的二进制补码表示的字节数组转换为 BigInteger。
	 * 输入数组假定为大端（即最高有效字节位于 [0] 位置）。 （最高有效字节的最高有效位是符号位。）
	 * 数组必须至少包含一个字节，否则将抛出 NumberFormatException
     */
    public BigInteger(byte[] val) throws NumberFormatException{
	if (val.length == 0)
	    throw new NumberFormatException("Zero length BigInteger");

	if (val[0] < 0) {
	    magnitude = makePositive(val);
	    signum = -1;
	} else {
	    magnitude = stripLeadingZeroBytes(val);
	    signum = (magnitude.length == 0 ? 0 : 1);
	}
    }

    /**
     * 将整数的符号大小表示转换为 BigInteger。符号表示为整数符号值（-1 表示负数，0 表示零，1 表示正数）。
	 * 幅度表示为大端字节数组（即，最高有效字节位于 [0] 位置）。
	 * 无效的符号值或 0 符号值加上非零幅度将导致 NumberFormatException。
	 * 长度为零的幅度数组是允许的，并且将导致值 0（与给定的符号值无关）。
     */
    public BigInteger(int signum, byte[] magnitude)
    throws NumberFormatException{
	this.magnitude = stripLeadingZeroBytes(magnitude);

	if (signum < -1 || signum > 1)
	    throw(new NumberFormatException("Invalid signum value"));

	if (this.magnitude.length==0) {
	    this.signum = 0;
	} else {
	    if (signum == 0)
		throw(new NumberFormatException("signum-magnitude mismatch"));
	    this.signum = signum;
	}
    }
    
    /**
     * 将包含可选减号的字符串后跟指定基数中的一个或多个数字的序列转换为 BigInteger。
	 * 字符到数字的映射由 Character.digit 提供。
	 * 任何无关的字符（包括空格），或从 Character.MIN_RADIX(2) 到 Character.MAX_RADIX(36) 范围之外的基数，
	 * 包括在内，都将导致 NumberFormatException。
     */
    public BigInteger(String val, int radix) throws NumberFormatException {
	int cursor = 0, numDigits;

	if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
	    throw new NumberFormatException("Radix out of range");
	if (val.length() == 0)
	    throw new NumberFormatException("Zero length BigInteger");

	/* 检查前导减号 */
	signum = 1;
	if (val.charAt(0) == '-') {
	    if (val.length() == 1)
		throw new NumberFormatException("Zero length BigInteger");
	    signum = -1;
	    cursor = 1;
	}

	/* 跳过前导零并计算数量级的位数 */
	while (cursor<val.length() && val.charAt(cursor)==ZERO_CHAR)
	    cursor++;
	if (cursor==val.length()) {
	    signum = 0;
	    magnitude = new byte[0];
	    return;
	} else {
	    numDigits = val.length() - cursor;
	}

	/* 如有必要，处理第一个（可能很短）数字组 */
	int firstGroupLen = numDigits % digitsPerLong[radix];
	if (firstGroupLen == 0)
	    firstGroupLen = digitsPerLong[radix];
	String group = val.substring(cursor, cursor += firstGroupLen);
	BigInteger tmp = valueOf(Long.parseLong(group, radix));

	/* 处理剩余的数字组 */
	while (cursor < val.length()) {
	    group = val.substring(cursor, cursor += digitsPerLong[radix]);
	    long groupVal = Long.parseLong(group, radix);
	    if (groupVal <0)
		throw new NumberFormatException("Illegal digit");
	    tmp = tmp.multiply(longRadix[radix]).add(valueOf(groupVal));
	}

	magnitude = tmp.magnitude;
    }

    /**
     * 将包含可选减号后跟一个或多个十进制数字序列的字符串转换为 BigInteger。
	 * 字符到数字的映射由 Character.digit 提供。任何无关字符（包括空格）都将导致 NumberFormatException。
     */
    public BigInteger(String val) throws NumberFormatException {
	this(val, 10);
    }

    /**
     * 返回一个均匀分布在 [0, 2**numBits - 1] 上的随机数（假设在 rndSrc 中提供了一个公平的随机位来源）。
	 * 请注意，此构造函数始终返回非负 BigInteger。如果 numBits < 0，则引发 IllegalArgumentException。
     */
    public BigInteger(int numBits, Random rndSrc)
    throws IllegalArgumentException {
	this(1, randomBits(numBits, rndSrc));
    }

    private static byte[] randomBits(int numBits, Random rndSrc)
    throws IllegalArgumentException {
	if (numBits < 0)
	    throw new IllegalArgumentException("numBits must be non-negative");
	int numBytes = (numBits+7)/8;
	byte[] randomBits = new byte[numBytes];

	/* 生成随机字节并屏蔽掉任何多余的位 */
	if (numBytes > 0) {
	    rndSrc.nextBytes(randomBits);
	    int excessBits = 8*numBytes - numBits;
	    randomBits[0] &= (1 << (8-excessBits)) - 1;
	}
	return randomBits;
    }


    /**
     * 返回一个随机选择的 BigInteger，其指定的 bitLength 可能是素数。
	 * 确定性参数是调用者愿意容忍的不确定性的度量：数字为素数的概率将超过 1 - 1/2**确定性。
	 * 执行时间与确定性参数的值成正比。给定的随机数生成器用于选择要测试素性的候选者。
	 * 如果 bitLength < 2，则引发 ArithmeticException。
     */
    public BigInteger(int bitLength, int certainty, Random rnd) {
	if (bitLength < 2)
	    throw new ArithmeticException("bitLength < 2");

	BigInteger p;
	do {
	    /*
	     * 选择一个长度正好合适的候选人。请注意，Plumb 的生成器不能正确处理 bitLength<=16。
	     */
	    do {
		p = new BigInteger(bitLength-1, rnd).setBit(bitLength-1);
		p = (bitLength <= 16
		     ? (bitLength > 2 ? p.setBit(0) : p)
		     : new BigInteger(plumbGeneratePrime(p.magnitude), 1));
	    } while (p.bitLength() != bitLength);
	} while (!p.isProbablePrime(certainty));

	signum = 1;
	magnitude = p.magnitude;
    }


    /**
     * 这个私有构造函数不同于它的公有构造函数，它的参数在两个方面被颠倒了：假设它的参数是正确的，并且它不复制幅度数组。
     */
    private BigInteger(byte[] magnitude, int signum) {
	this.signum = (magnitude.length==0 ? 0 : signum);
	this.magnitude = magnitude;
    }


    //Static Factory Methods（静态工厂方法）

    /**
     * 返回具有指定值的 BigInteger。该工厂优先于（长）构造函数提供，因为它允许重用经常使用的 BigInteger（如 0 和 1），从而无需导出常量。
     */
    public static BigInteger valueOf(long val) {
	/* 如果 -MAX_CONSTANT < val < MAX_CONSTANT，则返回隐藏的常量 */
	if (val == 0)
	    return ZERO;
	if (val > 0 && val <= MAX_CONSTANT)
	    return posConst[(int) val];
	else if (val < 0 && val >= -MAX_CONSTANT)
	    return negConst[(int) -val];

	/* 将二进制补码转储到 valArray */
	byte valArray[] = new byte[8];
	for (int i=0; i<8; i++, val >>= 8)
	    valArray[7-i] = (byte)val;
	return new BigInteger(valArray);
    }

    private final static BigInteger ZERO = new BigInteger(new byte[0], 0);

    /**
     * 加载类时初始化静态常量数组
     */
    private final static int MAX_CONSTANT = 16;
    private static BigInteger posConst[] = new BigInteger[MAX_CONSTANT+1];
    private static BigInteger negConst[] = new BigInteger[MAX_CONSTANT+1];
    static {
	for (int i = 1; i <= MAX_CONSTANT; i++) {
	    byte[] magnitude = new byte[1];
	    magnitude[0] = (byte) i;
	    posConst[i] = new BigInteger(magnitude,  1);
	    negConst[i] = new BigInteger(magnitude, -1);
	}
    }

    /**
     * 返回具有给定二进制补码表示的 BigInteger。假设输入数组不会被修改（如果可行，返回的 BigInteger 将引用输入数组）。
     */
    private static BigInteger valueOf(byte val[]) {
	return (val[0]>0 ? new BigInteger(val, 1) : new BigInteger(val));
    }


    // Arithmetic Operations（算术运算）

    /**
     * 返回值为 (this + val) 的 BigInteger
     */
    public BigInteger add(BigInteger val) throws ArithmeticException {
	if (val.signum == 0)
	    return this;
	else if (this.signum == 0)
	    return val;
	else if (val.signum == signum)
	    return new BigInteger(plumbAdd(magnitude, val.magnitude), signum);
	else if (this.signum < 0)
	    return plumbSubtract(val.magnitude, magnitude);
	else  /* val.signum < 0 */
	    return plumbSubtract(magnitude, val.magnitude);
    }

    /**
     * 返回值为 (this - val) 的 BigInteger。
     */
    public BigInteger subtract(BigInteger val) {
	return add(new BigInteger(val.magnitude, -val.signum));
    }

    /**
     * 返回值为 (this * val) 的 BigInteger。
     */
    public BigInteger multiply(BigInteger val) {

	if (val.signum == 0 || this.signum==0)
	    return ZERO;
	else
	    return new BigInteger(plumbMultiply(magnitude, val.magnitude),
				  signum * val.signum);
    }

    /**
     * 返回值为 (this / val) 的 BigInteger。如果 val == 0，则引发 ArithmeticException。
     */
    public BigInteger divide(BigInteger val) throws ArithmeticException {

	if (val.signum == 0)
	    throw new ArithmeticException("BigInteger divide by zero");
	else if (this.signum == 0)
	    return ZERO;
	else
	    return new BigInteger(plumbDivide(magnitude, val.magnitude),
				  signum * val.signum);
    }

    /**
     * 返回值为 (this % val) 的 BigInteger。如果 val == 0，则引发 ArithmeticException。
     */
    public BigInteger remainder(BigInteger val) throws ArithmeticException {

	if (val.signum == 0)
	    throw new ArithmeticException("BigInteger divide by zero");
	else if (this.signum == 0)
	    return ZERO;
	else if (this.magnitude.length < val.magnitude.length)
	    return this; /*** WORKAROUND FOR BUG IN R1.1 OF PLUMB'S PKG（PLUMB'S PKG R1.1 中的 BUG 解决方法） ***/
	else
	    return new BigInteger(plumbRemainder(magnitude,val.magnitude),
				  signum);
    }

    /**
     * 返回两个 BigInteger 的数组。返回值的第一个 ([0]) 元素是商 (this / val)，
	 * 第二个 ([1]) 元素是余数 (this % val)。如果 val == 0，则引发 ArithmeticException。
     */
    public BigInteger[] divideAndRemainder(BigInteger val)
    throws ArithmeticException {
	BigInteger result[] = new BigInteger[2];

	if (val.signum == 0) {
	    throw new ArithmeticException("BigInteger divide by zero");
	} else if (this.signum == 0) {
	    result[0] = result[1] = ZERO;
	} else if (this.magnitude.length < val.magnitude.length) {
	    /*** WORKAROUND FOR A BUG IN R1.1 OF PLUMB'S PACKAGE（PLUMB 软件包 R1.1 中的一个错误的解决方法） ***/
	    result[0] = ZERO;
	    result[1] = this;
	} else {
	    byte resultMagnitude[][] =
		plumbDivideAndRemainder(magnitude, val.magnitude);
	    result[0] = new BigInteger(resultMagnitude[0], signum*val.signum);
	    result[1] = new BigInteger(resultMagnitude[1], signum);
	}
	return result;
    }

    /**
     * 返回值为 (this ** exponent) 的 BigInteger。
	 * 如果 exponent < 0，则抛出 ArithmeticException（因为该操作将产生非整数值）。
	 * 请注意，指数是整数而不是 BigInteger。
     */
    public BigInteger pow(int exponent) throws ArithmeticException {
	if (exponent < 0)
	    throw new ArithmeticException("Negative exponent");
	if (signum==0)
	    return (exponent==0 ? ONE : this);

	/* 使用重复平方技巧执行求幂 */
	BigInteger result = valueOf(exponent<0 && (exponent&1)==1 ? -1 : 1);
	BigInteger baseToPow2 = this;
	while (exponent != 0) {
	    if ((exponent & 1)==1)
		result = result.multiply(baseToPow2);
	    if ((exponent >>= 1) != 0)
		baseToPow2 = new BigInteger(
				    plumbSquare(baseToPow2.magnitude), 1);
	}
	return result;
    }

    /**
     * 返回一个 BigInteger，其值为 abs(this) 和 abs(val) 的最大公分母。如果 this == 0 && val == 0，则返回 0。
     */
    public BigInteger gcd(BigInteger val) {
	if (val.signum == 0)
	    return this.abs();
	else if (this.signum == 0)
	    return val.abs();
	else
	    return new BigInteger(plumbGcd(magnitude, val.magnitude), 1);
    }

   /**
    * 返回一个 BigInteger，其值为该数字的绝对值。
    */
    public BigInteger abs() {
	return (signum >= 0 ? this : this.negate());
    }

    /**
     * 回值为 (-1 * this) 的 BigInteger。
     */
    public BigInteger negate() {
	return new BigInteger(this.magnitude, -this.signum);
    }

    /**
     * 返回此数字的符号函数（即，-1、0 或 1，因为此数字的值为负、零或正）。
     */
    public int signum() {
	return this.signum;
    }

    // Modular Arithmetic Operations(模块化算术运算)

    /**
     * 返回一个 BigInteger，其值为这个 mod m。如果 m <= 0，则引发 ArithmeticException。
     */
    public BigInteger mod(BigInteger m) {
	if (m.signum <= 0)
	    throw new ArithmeticException("BigInteger: modulus not positive");

	BigInteger result = this.remainder(m);
	return (result.signum >= 0 ? result : result.add(m));
    }

    /**
     * 返回一个 BigInteger，其值为 (this ** exponent) mod m。 （如果 exponent == 1，则返回值为 (this mod m)。
	 * 如果 exponent < 0，则返回值为 (this ** -exponent) 的模乘逆。）如果 m <= 0，则抛出 ArithmeticException。
     */
    public BigInteger modPow(BigInteger exponent, BigInteger m) {
	if (m.signum <= 0)
	    throw new ArithmeticException("BigInteger: modulus not positive");

	/* Plumb 中的一个错误的解决方法：x^0 (y) 为 x != 0 转储核心 */
	if (exponent.signum == 0)
	    return ONE;

	boolean invertResult;
	if ((invertResult = (exponent.signum < 0)))
	    exponent = exponent.negate();

	BigInteger base = (this.signum < 0 || this.compareTo(m) >= 0 
			   ? this.mod(m) : this);
	BigInteger result;
	if (m.testBit(0)) { /* 奇数模数：只需将其传递给 Plumb */
	    result = new BigInteger
	     (plumbModPow(base.magnitude, exponent.magnitude, m.magnitude), 1);
	} else {
	    /*
	     * 偶数模数。 Plumb 只支持奇数，所以拆成“奇数部分”（m1）和二的幂（m2），
	     * 用 Plumb 对 mod m1 取幂，手动对 mod m2 取幂，用中国剩余定理组合结果。
	     */

	    /* 将 m 分成奇数部分 (m1) 和 2 的幂 (m2) */
	    int p = m.getLowestSetBit();      /* 划分的最大 pow 为 2 m */
	    BigInteger m1 = m.shiftRight(p);  /* m/2**p */
	    BigInteger m2 = ONE.shiftLeft(p); /* 2**p */

	    /* 计算（基指数）mod m1 */
	    BigInteger a1 = new BigInteger
	     (plumbModPow(base.magnitude, exponent.magnitude, m1.magnitude),1);

	    /* 计算（指数）mod m2 */
	    BigInteger a2 = base.modPow2(exponent, p);

	    /* 使用中国剩余定理(Chinese Remainder Theorem)组合结果 */
	    BigInteger y1 = m2.modInverse(m1);
	    BigInteger y2 = m1.modInverse(m2);
	    result = a1.multiply(m2).multiply(y1).add
		    (a2.multiply(m1).multiply(y2)).mod(m);
	}

	return (invertResult ? result.modInverse(m) : result);
    }
    
    /**
     * Returns (this ** exponent) mod(2**p)
     */
    private BigInteger modPow2(BigInteger exponent, int p) {
	/*
	 * 使用重复平方技巧执行求幂运算，如模数所示切掉高阶位。
	 */
	BigInteger result = valueOf(1);
	BigInteger baseToPow2 = this.mod2(p);
	while (exponent.signum != 0) {
	    if (exponent.testBit(0))
		result = result.multiply(baseToPow2).mod2(p);
	    exponent = exponent.shiftRight(1);
	    if (exponent.signum != 0)
		baseToPow2 = new BigInteger(
			       plumbSquare(baseToPow2.magnitude), 1).mod2(p);
	}
	return result;
    }

    /**
     * 返回此 mod(2**p)。假设这个 BigInteger >= 0 并且 p > 0。
     */
    private BigInteger mod2(int p) {
	if (bitLength() <= p)
	    return this;

	/* 复制剩余字节的大小 */
	int numBytes = (p+7)/8;
	byte[] mag = new byte[numBytes];
	for (int i=0; i<numBytes; i++)
	    mag[i] = magnitude[i + (magnitude.length - numBytes)];

	/* 屏蔽掉任何多余的位 */
	int excessBits = 8*numBytes - p;
	mag[0] &= (1 << (8-excessBits)) - 1;

	return (mag[0]==0 ? new BigInteger(1, mag) : new BigInteger(mag, 1));
    }

    /**
     * 返回这个的模乘逆，mod m。如果 m <= 0 或 this 没有乘法逆模 m（即 gcd(this, m) != 1），则抛出 ArithmeticException
     */
    public BigInteger modInverse(BigInteger m) throws ArithmeticException {
	if (m.signum != 1)
	    throw new ArithmeticException("BigInteger: modulus not positive");

	/* 计算（这个 mod m） */
	BigInteger modVal = this.remainder(m);
	if (modVal.signum < 0)
	    modVal = modVal.add(m);
	if (!modVal.gcd(m).equals(ONE))
	    throw new ArithmeticException("BigInteger not invertible");

	return new BigInteger(plumbModInverse(modVal.magnitude,m.magnitude),1);
    }


    // Shift Operations（班次操作）

    /**
     * 返回值为 (this << n) 的 BigInteger。 （计算地板（this * 2**n）。）
     */
    public BigInteger shiftLeft(int n) {
	if (n==0)
	    return this;
	if (n<0)
	    return shiftRight(-n);

	int nBytes = n/8;
	int nBits = n%8;

	byte result[] = new byte[(bitLength()+n)/8+1];
	if (nBits == 0) {
	    for (int i=nBytes; i<result.length; i++)
		result[result.length-1-i] = getByte(i-nBytes);
	} else {
	    for (int i=nBytes; i<result.length; i++)
		result[result.length-1-i] = (byte)
		    ((getByte(i-nBytes) << nBits)
			| (i==nBytes ? 0
			   : ((getByte(i-nBytes-1)&0xff) >> (8-nBits))));
	}

	return valueOf(result);
    }

    /**
     * 返回值为 (this >> n) 的 BigInteger。执行符号扩展。 （计算地板（this / 2**n）。）
     */
    public BigInteger shiftRight(int n) {
	if (n==0)
	    return this;
	if (n<0)
	    return shiftLeft(-n);
	if (n >= bitLength())
	    return (signum<0 ? valueOf(-1) : ZERO);

	int nBytes = n/8;
	int nBits = n%8;

	byte result[] = new byte[(bitLength-n)/8+1];
	if (nBits == 0) {
	    for (int i=0; i<result.length; i++)
		result[result.length-1-i] = getByte(nBytes+i);
	} else {
	    for (int i=0; i<result.length; i++)
		result[result.length-1-i] = (byte)
		((getByte(nBytes+i+1)<<8 | (getByte(nBytes+i)&0xff)) >> nBits);
	}

	return valueOf(result);
    }


    // Bitwise Operations

    /**
     * 返回值为 (this & val) 的 BigInteger。 （如果 this 和 val 都是负数，此方法返回负数。）
     */
    public BigInteger and(BigInteger val) {
	byte[] result = new byte[Math.max(byteLength(), val.byteLength())];
	for (int i=0; i<result.length; i++)
	    result[i] = (byte) (getByte(result.length-i-1)
				& val.getByte(result.length-i-1));

	return valueOf(result);
    }

    /**
     * 返回值为 (this | val) 的 BigInteger。 （如果 this 或 val 为负，则此方法返回负数。）
     */
    public BigInteger or(BigInteger val) {
	byte[] result = new byte[Math.max(byteLength(), val.byteLength())];
	for (int i=0; i<result.length; i++)
	    result[i] = (byte) (getByte(result.length-i-1)
				| val.getByte(result.length-i-1));

	return valueOf(result);
    }

    /**
     * 返回值为 (this ^ val) 的 BigInteger。 （如果 this 和 val 恰好其中之一为负，则此方法返回负数。）
     */
    public BigInteger xor(BigInteger val) {
	byte[] result = new byte[Math.max(byteLength(), val.byteLength())];
	for (int i=0; i<result.length; i++)
	    result[i] = (byte) (getByte(result.length-i-1)
				^ val.getByte(result.length-i-1));

	return valueOf(result);
    }

    /**
     * 返回值为 (~this) 的 BigInteger。 （如果此数字为非负数，则此方法返回负值。）
     */
    public BigInteger not() {
	byte[] result = new byte[byteLength()];
	for (int i=0; i<result.length; i++)
	    result[i] = (byte) ~getByte(result.length-i-1);

	return valueOf(result);
    }

    /**
     * 返回值为 (this & ~val) 的 BigInteger。此方法等效于 and(val.not())，是为了方便屏蔽操作而提供的。
	 * （如果 this 为负且 val 为正，则此方法返回一个负数。）
     */
    public BigInteger andNot(BigInteger val) {
	byte[] result = new byte[Math.max(byteLength(), val.byteLength())];
	for (int i=0; i<result.length; i++)
	    result[i] = (byte) (getByte(result.length-i-1)
				& ~val.getByte(result.length-i-1));

	return valueOf(result);
    }


    // Single Bit Operations（单位操作）

    /**
     * 如果设置了指定位，则返回 true。 （计算 ((this & (1<<n)) != 0)。）如果 n < 0，则引发 ArithmeticException。
     */
    public boolean testBit(int n) throws ArithmeticException {
	if (n<0)
	    throw new ArithmeticException("Negative bit address");

	return (getByte(n/8) & (1 << (n%8))) != 0;
    }

    /**
     * 返回一个 BigInteger，其值等于设置了指定位的此数字。 （计算 (this | (1<<n))。）如果 n < 0，则引发 ArithmeticException。
     */
    public BigInteger setBit(int n) throws ArithmeticException {
	if (n<0)
	    throw new ArithmeticException("Negative bit address");

	int byteNum = n/8;
	byte[] result = new byte[Math.max(byteLength(), byteNum+2)];

	for (int i=0; i<result.length; i++)
	    result[result.length-i-1] = getByte(i);

	result[result.length-byteNum-1] |= (1 << (n%8));

	return valueOf(result);
    }

    /**
     * 返回一个 BigInteger，其值等于此数字，指定位已清除。 （计算 (this & ~(1<<n))。）如果 n < 0，则引发 ArithmeticException。
     */
    public BigInteger clearBit(int n) throws ArithmeticException {
	if (n<0)
	    throw new ArithmeticException("Negative bit address");

	int byteNum = n/8;
	byte[] result = new byte[Math.max(byteLength(), (n+1)/8+1)];

	for (int i=0; i<result.length; i++)
	    result[result.length-i-1] = getByte(i);

	result[result.length-byteNum-1] &= ~(1 << (n%8));

	return valueOf(result);
    }

    /**
     * 返回一个 BigInteger，其值与指定位翻转后的此数字等效。 （计算 (this ^ (1<<n))。）如果 n < 0，则引发 ArithmeticException。
     */
    public BigInteger flipBit(int n) throws ArithmeticException {
	if (n<0)
	    throw new ArithmeticException("Negative bit address");

	int byteNum = n/8;
	byte[] result = new byte[Math.max(byteLength(), byteNum+2)];

	for (int i=0; i<result.length; i++)
	    result[result.length-i-1] = getByte(i);

	result[result.length-byteNum-1] ^= (1 << (n%8));

	return valueOf(result);
    }

    /**
     * 返回此数字中最右侧（最低位）一位的索引（即，最右侧一位右侧的零位的数量）。如果此数字不包含一位，则返回 -1。
	 * （计算 (this==0?-1 : log2(this & -this))。）
     */
    public int getLowestSetBit() {
	/*
	 * 第一次执行此方法时初始化最低设置位字段。该方法依赖于 int 修改的原子性；如果没有这个保证，它就必须同步。
	 */
	if (lowestSetBit == -2) {
	    if (signum == 0) {
		lowestSetBit = -1;
	    } else {
		/* 搜索最低阶非零字节 */
		int i;
		byte b;
		for (i=0; (b = getByte(i))==0; i++)
		    ;
		lowestSetBit = 8*i + trailingZeroCnt[b & 0xFF];
	    }
	}
	return lowestSetBit;
    }


    // Miscellaneous Bit Operations（其他位操作）

    /**
     * 返回此数字的最小二进制补码表示中的位数，*不包括*符号位，即 (ceil(log2(this < 0 ? -this : this + 1)))。
	 * （对于正数，这相当于普通二进制表示中的位数。）
     */
    public int bitLength() {
	/*
	 * 第一次执行此方法时初始化 bitLength 字段。该方法依赖于 int 修改的原子性；如果没有这个保证，它就必须同步。
	 */
	if (bitLength == -1) {
	    if (signum == 0) {
		bitLength = 0;
	    } else {
		/* 计算幅度的位长 */
		int magBitLength = 8*(magnitude.length-1)
		    		   + bitLen[magnitude[0] & 0xff];

		if (signum < 0) {
		    /* 检查幅度是否是 2 的幂 */
		    boolean pow2 = (bitCnt[magnitude[0]&0xff] == 1);
		    for(int i=1; i<magnitude.length && pow2; i++)
			pow2 = (magnitude[i]==0);

		    bitLength = (pow2 ? magBitLength-1 : magBitLength);
		} else {
		    bitLength = magBitLength;
		}
	    }
	}
	return bitLength;
    }

    /*
     * bitLen[i] 是 i 的二进制表示中的位数。
     */
    private final static byte bitLen[] = {
	0, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4,
	5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
	6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
	6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
	7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
	7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
	7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
	7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
	8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
	8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
	8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
	8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
	8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
	8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
	8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
	8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8};

    /**
     * 返回此数字的二进制补码表示中与其符号位不同的位数。在 BigInteger 上实现位向量样式集时，此方法很有用。
     */
    public int bitCount() {
	/*
	 * 第一次执行此方法时初始化 bitCount 字段。该方法依赖于 int 修改的原子性；如果没有这个保证，它就必须同步。
	 */
	if (bitCount == -1) {
	    /* 计算幅度中的位数 */
	    int magBitCount = 0;
	    for (int i=0; i<magnitude.length; i++)
		magBitCount += bitCnt[magnitude[i] & 0xff];

	    if (signum < 0) {
		/* 计算幅度中的尾随零 */
		int magTrailingZeroCount = 0, j;
		for (j=magnitude.length-1; magnitude[j]==0; j--)
		    magTrailingZeroCount += 8;
		magTrailingZeroCount += trailingZeroCnt[magnitude[j] & 0xff];

		bitCount = magBitCount + magTrailingZeroCount - 1;
	    } else {
		bitCount = magBitCount;
	    }
	}
	return bitCount;
    }

    /*
     * bitCnt[i] 是 i 的二进制表示中 1 的位数。
     */
    private final static byte bitCnt[] = {
	0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4,
	1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
	1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
	2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
	1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
	2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
	2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
	3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
	1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
	2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
	2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
	3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
	2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
	3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
	3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
	4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8};

    /*
     * trailing Zero not[i] 是 i 的二进制表示中的尾随零位数。
     */
    private final static byte trailingZeroCnt[] = {
	8, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	6, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	7, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	6, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0};



    // Primality Testing(素性测试)

    /**
     * 如果这个 BigInteger 可能是素数，则返回 true，如果它肯定是复合的，则返回 false。
	 * 确定性参数是调用者愿意容忍的不确定性的度量：如果这个数是素数的概率超过 1 - 1/2**确定性，则该方法返回 true。
	 * 执行时间与确定性参数的值成正比。
     */
    public boolean isProbablePrime(int certainty) {
	/*
	 * 此测试取自 DSA 规范。
	 */
	int n = certainty/2;
	if (n <= 0)
	    return true;
	BigInteger w = this.abs();
	if (w.equals(TWO))
	    return true;
	if (!w.testBit(0) || w.equals(ONE))
	    return false;

	/* 找到 a 和 m 使得 m 是奇数并且 w == 1 + 2**a * m */
	BigInteger m = w.subtract(ONE);
	int a = m.getLowestSetBit();
	m = m.shiftRight(a);

	/* Do the tests */
	Random rnd = new Random();
	for(int i=0; i<n; i++) {
	    /* 在 (1, w) 上生成均匀随机数 */
	    BigInteger b;
	    do {
		b = new BigInteger(w.bitLength(), rnd);
	    } while (b.compareTo(ONE) <= 0 || b.compareTo(w) >= 0);

	    int j = 0;
	    BigInteger z = b.modPow(m, w);
	    while(!((j==0 && z.equals(ONE)) || z.equals(w.subtract(ONE)))) {
		if (j>0 && z.equals(ONE) || ++j==a)
		    return false;
		z = z.modPow(TWO, w);
	    }
	}
	return true;
    }


    // Comparison Operations（比较操作）

    /**
	 * 返回 -1、0 或 1，因为此数字小于、等于或大于 val。
	 * 此方法优先于六个布尔比较运算符（<、==、>、>=、!=、<=）中的每一个的单独方法提供。
	 * 执行这些比较的建议习语是：(x.compareTo(y) <op> 0), 其中 <op> 是六个比较运算符之一。
     */
    public int compareTo(BigInteger val) {
	return (signum==val.signum
		? signum*byteArrayCmp(magnitude, val.magnitude)
		: (signum>val.signum ? 1 : -1));
    }

    /*
     * 返回 -1、0 或 +1，因为大端无符号字节数组 arg1 是 <、== 或 > arg2。
     */
    private static int byteArrayCmp(byte[] arg1, byte[] arg2) {
	if (arg1.length < arg2.length)
	    return -1;
	if (arg1.length > arg2.length)
	    return 1;

	/* 参数长度相等；比较值 */
	for (int i=0; i<arg1.length; i++) {
	    int b1 = arg1[i] & 0xff;
	    int b2 = arg2[i] & 0xff;
	    if (b1 < b2)
		return -1;
	    if (b1 > b2)
		return 1;
	}
	return 0;
    }

    /**
     * 如果 x 是一个 BigInteger，其值等于该数字，则返回 true。提供此方法以便 BigIntegers 可以用作哈希键。
     */
    public boolean equals(Object x) {
	if (!(x instanceof BigInteger))
	    return false;
	BigInteger xInt = (BigInteger) x;

	if (xInt.signum != signum || xInt.magnitude.length != magnitude.length)
	    return false;

	/* 这个测试只是一个优化，可能有帮助也可能没有帮助 */
	if (xInt == this)
	    return true;

	for (int i=0; i<magnitude.length; i++)
	    if (xInt.magnitude[i] != magnitude[i])
		return false;

	return true;
    }

    /**
     * 返回值为 this 和 val 中较小值的 BigInteger。如果值相等，则可以返回任何一个。
     */
    public BigInteger min(BigInteger val) {
	return (compareTo(val)<0 ? this : val);
    }

    /**
     * 返回值为 this 和 val 中较大者的 BigInteger。如果值相等，则可以返回任何一个。
     */
    public BigInteger max(BigInteger val) {
	return (compareTo(val)>0 ? this : val);
    }


    // Hash Function

    /**
     * Computes a hash code for this object.(计算此对象的哈希码。)
     */
    public int hashCode() {
	int hashCode = 0;

	for (int i=0; i<magnitude.length; i++)
	    hashCode = 37*hashCode + (magnitude[i] & 0xff);

	return hashCode * signum;
    }

    // Format Converters(格式转换器)

    /**
     * 返回给定基数中此数字的字符串表示形式。
	 * 如果基数超出从 Character.MIN_RADIX(2) 到 Character.MAX_RADIX(36) 的范围，
	 * 它将默认为 10（如 Integer.toString 的情况）。
	 * 使用 Character.forDigit 提供的数字到字符的映射，并在适当的情况下添加减号。
	 * （此表示与 (String, int) 构造函数兼容。）
     */
    public String toString(int radix) {
	if (signum == 0)
	    return "0";
	if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
	    radix = 10;

	/* 计算数字组数的上限并分配空间 */
	int maxNumDigitGroups = (magnitude.length + 6)/7;
	String digitGroup[] = new String[maxNumDigitGroups];

	/* 将数字转换为字符串，一次转换一个数字组 */
	BigInteger tmp = this.abs();
	int numGroups = 0;
	while (tmp.signum != 0) {
	    BigInteger b[] = tmp.divideAndRemainder(longRadix[radix]);
	    digitGroup[numGroups++] = Long.toString(b[1].longValue(), radix);
	    tmp = b[0];
	}

	/* 将符号（如果有）和第一个数字组放入结果缓冲区 */
	StringBuffer buf = new StringBuffer(numGroups*digitsPerLong[radix]+1);
	if (signum<0)
	    buf.append('-');
	buf.append(digitGroup[numGroups-1]);

	/* 附加用前导零填充的剩余数字组 */
	for (int i=numGroups-2; i>=0; i--) {
	    /* 为此数字组添加（任何）前导零 */
	    int numLeadingZeros = digitsPerLong[radix]-digitGroup[i].length();
	    if (numLeadingZeros != 0)
		buf.append(zeros[numLeadingZeros]);
	    buf.append(digitGroup[i]);
	}

	return buf.toString();
    }

    /* zero[i] 是由 i 个连续的零组成的字符串。 */
    private static String zeros[] = new String[64];
    static {
	zeros[63] =
	    "000000000000000000000000000000000000000000000000000000000000000";
	for (int i=0; i<63; i++)
	    zeros[i] = zeros[63].substring(0, i);
    }

    /**
     * 返回此数字的字符串表示形式，基数为 10。使用 Character.forDigit 提供的数字到字符映射，并在适当的情况下添加减号。
	 * （此表示与 (String) 构造函数兼容，并允许使用 Java 的 + 运算符进行字符串连接。）
     */
    public String toString() {
	return toString(10);
    }

    /**
     * 返回此数字的二进制补码表示。该数组是大端的（即最高有效字节位于 [0] 位置）。
	 * 该数组包含表示数字所需的最小字节数 (ceil((this.bitLength() + 1)/8))。 （此表示与 (byte[]) 构造函数兼容。）
     */
    public byte[] toByteArray() {
	byte[] result = new byte[byteLength()];
	for(int i=0; i<result.length; i++)
	    result[i] = getByte(result.length-i-1);

	return result;
    }

    /**
     * 将此数字转换为 int。根据 Java 语言规范的标准缩小原语转换。
     */
    public int intValue() {
	int result = 0;

	for (int i=3; i>=0; i--)
	    result = (result << 8) + (getByte(i) & 0xff);
	return result;
    }

    /**
     * 将此数字转换为长整数。根据 Java 语言规范的标准缩小原语转换。
     */
    public long longValue() {
	long result = 0;

	for (int i=7; i>=0; i--)
	    result = (result << 8) + (getByte(i) & 0xff);
	return result;
    }

    /**
     * 将此数字转换为浮点数。
	 * 类似于 Java 语言规范中定义的双浮点数缩小原语转换：如果数字的幅度太大而无法表示为浮点数，它将酌情转换为无穷大或负无穷大。
     */
    public float floatValue() {
	/* Somewhat inefficient, but guaranteed to work.（有点低效，但保证可以工作。） */
	return Float.valueOf(this.toString()).floatValue();
    }

    /**
     * 将数字转换为双精度数。
	 * 类似于 Java 语言规范中定义的 double 到 float 的缩小原语转换：
	 * 如果数字的幅度太大而无法表示为 double，则将酌情将其转换为无穷大或负无穷大。
     */
    public double doubleValue() {
	/* Somewhat inefficient, but guaranteed to work. （有点低效，但保证可以工作。）*/
	return Double.valueOf(this.toString()).doubleValue();
    }

    static {
	System.loadLibrary("math");
	plumbInit();
    }


    private final static BigInteger ONE = valueOf(1);
    private final static BigInteger TWO = valueOf(2);

    private final static char ZERO_CHAR = Character.forDigit(0, 2);

    /**
     * 返回去除任何前导零字节的输入数组的副本。
     */
    static private byte[] stripLeadingZeroBytes(byte a[]) {
	int keep;
	
	/* 查找第一个非零字节 */
	for (keep=0; keep<a.length && a[keep]==0; keep++)
	    ;

	/* 分配新数组并复制输入数组的相关部分 */
	byte result[] = new byte[a.length - keep];
	for (int i = keep; i<a.length; i++)
	    result[i - keep] = a[i];

	return result;
    }

    /**
     * 接受一个表示负 2 的补数的数组 a 并返回其值为 -a 的最小（无前导零字节）无符号数。
     */
    static private byte[] makePositive(byte a[]) {
	int keep, j;

	/* 查找输入的第一个非符号 (0xff) 字节 */
	for (keep=0; keep<a.length && a[keep]==-1; keep++)
	    ;

	/* 分配输出数组。如果所有非符号字节都是 0x00，我们必须为一个额外的输出字节分配空间。 */
	for (j=keep; j<a.length && a[j]==0; j++)
	    ;
	int extraByte = (j==a.length ? 1 : 0);
	byte result[] = new byte[a.length - keep + extraByte];

	/* 将输入的补码复制到输出中，留下额外的字节（如果存在）== 0x00 */
	for (int i = keep; i<a.length; i++)
	    result[i - keep + extraByte] = (byte) ~a[i];

	/* 将一个补码加一以生成二进制补码 */
	for (int i=result.length-1; ++result[i]==0; i--)
	    ;

	return result;
    }

    /*
     * 以下两个数组用于快速字符串转换。两者都由基数索引。
     * 第一个是给定基数的位数，它可以适合 Java long 而不会“变为负数”，即最大整数 n 使得 基数 ** n < 2 ** 63。
     * 第二个是撕裂每个数字的“长基数”成“长数字”，每个数字由digitsPerLong中对应元素的位数组成（longRadix[i] = i ** digitPerLong[i]）。
     * 两个数组在它们的 0 和 1 元素中都有无意义的值，因为没有使用基数 0 和 1。
     */

    private static int digitsPerLong[] = {0, 0,
	62, 39, 31, 27, 24, 22, 20, 19, 18, 18, 17, 17, 16, 16, 15, 15, 15, 14,
	14, 14, 14, 13, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 12};

    private static BigInteger longRadix[] = {null, null,
        valueOf(0x4000000000000000L), valueOf(0x383d9170b85ff80bL),
	valueOf(0x4000000000000000L), valueOf(0x6765c793fa10079dL),
	valueOf(0x41c21cb8e1000000L), valueOf(0x3642798750226111L),
        valueOf(0x1000000000000000L), valueOf(0x12bf307ae81ffd59L),
	valueOf( 0xde0b6b3a7640000L), valueOf(0x4d28cb56c33fa539L),
	valueOf(0x1eca170c00000000L), valueOf(0x780c7372621bd74dL),
	valueOf(0x1e39a5057d810000L), valueOf(0x5b27ac993df97701L),
	valueOf(0x1000000000000000L), valueOf(0x27b95e997e21d9f1L),
	valueOf(0x5da0e1e53c5c8000L), valueOf( 0xb16a458ef403f19L),
	valueOf(0x16bcc41e90000000L), valueOf(0x2d04b7fdd9c0ef49L),
	valueOf(0x5658597bcaa24000L), valueOf( 0x6feb266931a75b7L),
	valueOf( 0xc29e98000000000L), valueOf(0x14adf4b7320334b9L),
	valueOf(0x226ed36478bfa000L), valueOf(0x383d9170b85ff80bL),
	valueOf(0x5a3c23e39c000000L), valueOf( 0x4e900abb53e6b71L),
	valueOf( 0x7600ec618141000L), valueOf( 0xaee5720ee830681L),
	valueOf(0x1000000000000000L), valueOf(0x172588ad4f5f0981L),
	valueOf(0x211e44f7d02c1000L), valueOf(0x2ee56725f06e5c71L),
	valueOf(0x41c21cb8e1000000L)};


    /**
     * 这些例程提供对 BigInteger 的二进制补码表示的访问。
     */

    /**
     * 返回二进制补码表示的字节长度，包括至少一个符号位的空间，
     */
    private int byteLength() {
	return bitLength()/8 + 1;
    }

    /* Returns sign bit */
    private int signBit() {
	return (signum < 0 ? 1 : 0);
    }

    /* Returns a byte of sign bits */
    private byte signByte() {
	return (byte) (signum < 0 ? -1 : 0);
    }

    /**
     * 返回小端二进制补码表示的指定字节（字节 0 是 LSB）。字节数可以任意高（值在逻辑上前面有无限多个符号字节）。
     */
    private byte getByte(int n) {
	if (n >= magnitude.length)
	    return signByte();

	byte magByte = magnitude[magnitude.length-n-1];

	return (byte) (signum >= 0 ? magByte :
		       (n <= firstNonzeroByteNum() ? -magByte : ~magByte));
    }

    /**
     * 返回幅度的小端二进制表示中第一个非零字节的索引（字节 0 是 LSB）。如果幅度为零，则返回值未定义。
     */

     private int firstNonzeroByteNum() {
	/*
	 * 第一次执行此方法时初始化 bitCount 字段。该方法依赖于 int 修改的原子性；如果没有这个保证，它就必须同步。
	 */
	if (firstNonzeroByteNum == -2) {
	    /* 搜索第一个非零字节 */
	    int i;
	    for (i=magnitude.length-1; i>=0 && magnitude[i]==0; i--)
		;
	    firstNonzeroByteNum = magnitude.length-i-1;
	}
	return firstNonzeroByteNum;
    }


    /**
     * Colin Plumb 的大正整数包的本机方法包装器。
	 * 这些包装器中的每一个（除了plumbInit）的行为如下：
	 *  1) 将输入参数从java 字节数组转换为plumbNums。
	 *  2) 执行请求的计算。
	 *  3) 将结果转换为 Java 字节数组。 （减法运算将其结果转换为 BigInteger，因为它的结果实际上是有符号的。）
	 *  4) 释放所有的 plumbNums。
	 *  5) 返回生成的 Java 数组（或者，在减法的情况下，返回 BigInteger）。
     */

    private static native void plumbInit();
    private static native byte[] plumbAdd(byte[] a, byte[] b);
    private static native BigInteger plumbSubtract(byte[] a, byte[] b);
    private static native byte[] plumbMultiply(byte[] a, byte[] b);
    private static native byte[] plumbDivide(byte[] a, byte[] b);
    private static native byte[] plumbRemainder(byte[] a, byte[] b);
    private static native byte[][] plumbDivideAndRemainder(byte[] a, byte[] b);
    private static native byte[] plumbGcd(byte[] a, byte[] b);
    private static native byte[] plumbModPow(byte[] a, byte[] b, byte[] m);
    private static native byte[] plumbModInverse(byte[] a, byte[] m);
    private static native byte[] plumbSquare(byte[] a);
    private static native byte[] plumbGeneratePrime(byte[] a);

    /** 用 JDK 1.1 中的 serialVersionUID。互操作性 */
    private static final long serialVersionUID = -8287574255936472291L;

    /**
     * 从流中重构BigInteger实例（即反序列化它）。
     */
    private synchronized void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        // Read in all fields
	s.defaultReadObject();

        // 防御性复制大小以确保不变性
        magnitude = (byte[]) magnitude.clone();

        // Validate signum（验证代码）
	if (signum < -1 || signum > 1)
	    throw new java.io.StreamCorruptedException(
                        "BigInteger: Invalid signum value");
	if ((magnitude.length==0) != (signum==0))
	    throw new java.io.StreamCorruptedException(
                        "BigInteger: signum-magnitude mismatch");

        // 将“缓存计算”字段设置为其初始值
        bitCount = bitLength = -1;
        lowestSetBit = firstNonzeroByteNum = -2;
    }
}
