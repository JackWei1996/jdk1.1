/*
 * @(#)BigDecimal.java	1.11 2001年12月12
 *
 * 版权所有 2002年 Sun Microsystems，Inc.保留所有权利。
 * SUN 所有者/机密。使用受许可条款约束。
 */

package java.math;

/**
 * 不可变的、任意精度的有符号十进制数。 BigDecimal 由任意精度整数值和非负整数刻度组成，表示小数点右侧的小数位数。
 * （BigDecimal 表示的数字是 intVal/10**scale）BigDecimal 提供基本算术、比例操作、比较、格式转换和散列的操作。
 *
 * BigDecimal 类让用户可以完全控制舍入行为，强制用户为能够丢弃精度的操作（除法和SetScale）显式指定舍入行为。为此提供了八种舍入模式。
 *
 * 为操纵 BigDecimal 的比例提供了两种类型的操作：缩放/舍入操作和小数点运动操作。
 * 缩放/舍入操作 (SetScale) 返回一个 BigDecimal，其值大约（或完全）等于操作数的值，但其比例为指定值；
 *     也就是说，它们会增加或减少数字的精度，而对其值的影响最小。
 * 小数点移动操作（movePointLeft 和 movePointRight）返回一个 BigDecimal，
 *     该 BigDecimal 通过将小数点沿指定方向移动指定距离从而操作数；
 *     也就是说，它们在不影响其精度的情况下更改数字的值。
 *
 * @see BigInteger
 * @version 	1.11, 2001/12/12
 * @author      Josh Bloch（乔什·布洛赫）
 */
public class BigDecimal extends Number {
    private BigInteger intVal;
    private int	       scale = 0;

    /* Appease the serialization gods（安抚众神） */
    private static final long serialVersionUID = 6108874887143696463L;

    // 构造器

    /**
     * 从包含可选负号的字符串构造 BigDecimal，该字符串后跟零个或多个小数位的序列，
	 * 可选地后跟一个分数，该分数由一个小数点后跟零个或多个小数位组成。
	 * 字符串的整数或小数部分必须至少包含一位数字。
	 * 生成的 BigDecimal 的比例将是字符串中小数点右侧的位数，如果字符串不包含小数点，则为 0。
	 * 字符到数字的映射由 Character.digit 提供。任何无关字符（包括空格）都将导致 NumberFormatException。
     */
    public BigDecimal(String val) throws NumberFormatException {
	int pointPos = val.indexOf('.');
	if (pointPos == -1) {			 /* e.g.（例如） "123" */
	    intVal = new BigInteger(val);
	} else if (pointPos == val.length()-1) { /* e.g.（例如） "123." */
	    intVal = new BigInteger(val.substring(0, val.length()-1));
	} else {    /* 分数部分存在 */
	    String fracString = val.substring(pointPos+1);
	    scale = fracString.length();
	    BigInteger fraction =  new BigInteger(fracString);
	    if (fraction.signum() < 0)		 /* ".-123" 非法的数字! */
		throw new NumberFormatException();

	    if (pointPos==0) {			 /* e.g.  ".123" */
		intVal = fraction;
	    } else if (val.charAt(0)=='-' && pointPos==1) {
		intVal = fraction.negate();	 /* e.g. "-.123" */
	    } else  {				 /* e.g. "-123.456" */
		String intString = val.substring(0, pointPos);
		BigInteger intPart = new BigInteger(intString);
		if (val.charAt(0) == '-')
		    fraction = fraction.negate();
		intVal = timesTenToThe(intPart, scale).add(fraction);
	    }
	}
    }

    /**
     * 将 double 转换为 BigDecimal。 BigDecimal 的小数位数是使 (10**scale * val) 为整数的最小值。
	 * 值为 -infinity（无穷）、+infinity 或 NaN 的 double 将导致 NumberFormatException。
     */
    public BigDecimal(double val) throws NumberFormatException{
	if (Double.isInfinite(val) || Double.isNaN(val))
	    throw new NumberFormatException("Infinite or NaN");

	/*
	 * 根据 JLS 第 20.10.22 节中的公式，将双精度数转换为符号、指数和尾数。
	 */
	long valBits = Double.doubleToLongBits(val);
	int sign = ((valBits >> 63)==0 ? 1 : -1);
	int exponent = (int) ((valBits >> 52) & 0x7ffL);
	long mantissa = (exponent==0 ? (valBits & ((1L<<52) - 1)) << 1
				     : (valBits & ((1L<<52) - 1)) | (1L<<52));
	exponent -= 1075;
	/* 此时, val == sign * mantissa * 2**exponent */

	/*
	 * 特殊情况为零以抑制非终止归一化和虚假比例计算。
	 */
	if (mantissa == 0) {
	    intVal = BigInteger.valueOf(0);
	    return;
	}

	/* Normalize（标准化） */
	while((mantissa & 1) == 0) {    /*  i.e.（那就是，换句话说）, 尾数是偶数 */
	    mantissa >>= 1;
	    exponent++;
	}

	/* 计算 intVal 和比例 */
	intVal = BigInteger.valueOf(sign*mantissa);
	if (exponent < 0) {
	    intVal = intVal.multiply(BigInteger.valueOf(5).pow(-exponent));
	    scale = -exponent;
	} else if (exponent > 0) {
	    intVal = intVal.multiply(BigInteger.valueOf(2).pow(exponent));
	}
    }

    /**
     * 将 BigInteger 转换为 BigDecimal。 BigDecimal 的小数位数为零
     */
    public BigDecimal(BigInteger val) {
	intVal = val;
    }

    /**
     * 将 BigInteger 根据比例转换为 BigDecimal。 BigDecimal 的值为 (BigInteger/10**scale)。
	 * 负比例将导致 NumberFormatException
     */
    public BigDecimal(BigInteger val, int scale) throws NumberFormatException {
	if (scale < 0)
	    throw new NumberFormatException("Negative scale");

	intVal = val;
	this.scale = scale;
    }


    // Static Factory Methods（静态工厂方法）

    /**
     * 返回值为 (val/10**scale) 的 BigDecimal。
	 * 该工厂优先于构造函数提供，因为它允许重用经常使用的 BigDecimal（如 0 和 1），从而无需导出常量。
	 * 负比例将导致 NumberFormatException
     */
    public static BigDecimal valueOf(long val, int scale)
	    throws NumberFormatException {
	return new BigDecimal(BigInteger.valueOf(val), scale);
    }

    /**
     * 返回具有给定值和零刻度的 BigDecimal。该工厂优先于构造函数提供，
	 * 因为它允许重用经常使用的 BigDecimal（如 0 和 1），从而无需导出常量
     */
    public static BigDecimal valueOf(long val) {
	return valueOf(val, 0);
    }


    // Arithmetic Operations（算术运算）

    /**
     * 返回一个 BigDecimal，其值为 (this + val)，其比例为 MAX(this.scale(), val.scale)。
     */
    public BigDecimal add(BigDecimal val){
	BigDecimal arg[] = new BigDecimal[2];
	arg[0] = this;	arg[1] = val;
	matchScale(arg);
	return new BigDecimal(arg[0].intVal.add(arg[1].intVal), arg[0].scale);
    }

    /**
     * 返回一个 BigDecimal，其值为 (this - val)，其比例为 MAX(this.scale(), val.scale)。
     */
    public BigDecimal subtract(BigDecimal val){
	BigDecimal arg[] = new BigDecimal[2];
	arg[0] = this;	arg[1] = val;
	matchScale(arg);
	return new BigDecimal(arg[0].intVal.subtract(arg[1].intVal),
			      arg[0].scale);
    }

    /**
     * 返回一个 BigDecimal，其值为 (this * val)，其比例为 this.scale() + val.scale。
     */
    public BigDecimal multiply(BigDecimal val){
	return new BigDecimal(intVal.multiply(val.intVal), scale+val.scale);
    }

    /**
     * 返回一个 BigDecimal，其值为 (this / val)，其舍人模式为指定的。
	 * 如果必须执行舍入以生成具有给定比例的结果，则应用指定舍入模式。
	 * 如果 val == 0、scale < 0 或舍入模式为 ROUND_UNNECESSARY
	 *     并且指定的比例不足以准确表示除法的结果，则引发 ArithmeticException。
	 * 如果 roundingMode 不表示有效的舍入模式，则引发 IllegalArgumentException
     */
    public BigDecimal divide(BigDecimal val, int scale, int roundingMode)
	    throws ArithmeticException, IllegalArgumentException {
	if (scale < 0)
	    throw new ArithmeticException("Negative scale");
	if (roundingMode < ROUND_UP || roundingMode > ROUND_UNNECESSARY)
	    throw new IllegalArgumentException("Invalid rounding mode");

	/*
	 * 重新调整被除数或除数（无论哪个可以“放大”以产生正确缩放的商）。
	 */
	BigDecimal dividend, divisor;
	if (scale + val.scale >= this.scale) {
	    dividend = this.setScale(scale + val.scale);
	    divisor = val;
	} else {
	    dividend = this;
	    divisor = val.setScale(this.scale - scale);
	}

	/* 如果准确，则进行除法并返回结果 */
	BigInteger i[] = dividend.intVal.divideAndRemainder(divisor.intVal);
	BigInteger q = i[0], r = i[1];
	if (r.signum() == 0)
	    return new BigDecimal(q, scale);
	else if (roundingMode == ROUND_UNNECESSARY) /* Rounding prohibited（禁止四舍五入） */
	    throw new ArithmeticException("Rounding necessary");

	/* 适当取整 */
	int signum = dividend.signum() * divisor.signum(); /* Sign of result（结果的标志） */
	boolean increment;
	if (roundingMode == ROUND_UP) {		    /* Away from zero（远离零） */
	    increment = true;
	} else if (roundingMode == ROUND_DOWN) {    /* Towards zero （趋于零）*/
	    increment = false;
	} else if (roundingMode == ROUND_CEILING) { /* Towards +infinity （趋于正无穷大）*/
	    increment = (signum > 0);
	} else if (roundingMode == ROUND_FLOOR) {   /* Towards -infinity （趋于负无穷大）*/
	    increment = (signum < 0);
	} else { /* 基于最近邻确定的剩余模式 */
	    int cmpFracHalf = r.abs().multiply(BigInteger.valueOf(2)).
					 compareTo(divisor.intVal.abs());
	    if (cmpFracHalf < 0) {	   /* 我们更接近更高的数字 */
		increment = false;
	    } else if (cmpFracHalf > 0) {  /* 我们更接近低位数 */
		increment = true;
	    } else { 			   /* We're dead-center (我们死定了)*/
		if (roundingMode == ROUND_HALF_UP)
		    increment = true;
		else if (roundingMode == ROUND_HALF_DOWN)
		    increment = false;
		else  /* roundingMode == ROUND_HALF_EVEN */
		    increment = q.testBit(0);	/* true iff q is odd（当且仅当q为奇数） */
	    }
	}
	return (increment
		? new BigDecimal(q.add(BigInteger.valueOf(signum)), scale)
		: new BigDecimal(q, scale));
    }

    /**
     * 返回一个 BigDecimal，其值为 (this / val)，其刻度为 this.scale()。
	 * 如果必须执行舍入以生成具有给定比例的结果，则应用指定的舍入模式。
	 * 如果 val == 0，则引发 ArithmeticException。
	 * 如果 roundingMode 不代表有效的舍入模式，则引发 IllegalArgumentException
     */
    public BigDecimal divide(BigDecimal val, int roundingMode)
	throws ArithmeticException, IllegalArgumentException{
	    return this.divide(val, scale, roundingMode);
    }

   /**
    * 返回一个 BigDecimal，其值为该数字的绝对值，其刻度为 this.scale()
    */
    public BigDecimal abs(){
	return (signum() < 0 ? negate() : this);
    }

    /**
     * 返回一个 BigDecimal，其值为 -1 * this，其比例为 this.scale()
     */
    public BigDecimal negate(){
	return new BigDecimal(intVal.negate(), scale);
    }

    /**
     * 返回此数字的符号函数（即，-1、0 或 1，因为此数字的值为负、零或正）。
     */
    public int signum(){
	return intVal.signum();
    }

    /**
     * 返回scale
     */
    public int scale(){
	return scale;
    }


    // Rounding Modes(舍入模式)

    /**
     * 始终在非零丢弃分数之前增加数字。请注意，这种舍入模式永远不会降低幅度。 （从零四舍五入。）
     */
    public final static int ROUND_UP = 		 0;

    /**
     * 永远不要在丢弃的分数之前增加数字（即截断）。请注意，这种舍入模式永远不会增加幅度。 （向零舍入。）
     */
    public final static int ROUND_DOWN = 	 1;

    /**
     * 如果 BigDecimal 为正数，则与 ROUND_UP 一样；如果为负，则表现与 ROUND_DOWN 相同。
	 * 请注意，此舍入模式永远不会减少该值。 （向正无穷大四舍五入。）
     */
    public final static int ROUND_CEILING = 	 2;

    /**
     * 如果 BigDecimal 为正，则与 ROUND_DOWN 一样；如果为负，则表现为 ROUND_UP。
	 * 请注意，此舍入模式永远不会增加该值。 （向负无穷舍入。）
     */
    public final static int ROUND_FLOOR = 	 3;

    /**
     * 如果丢弃的分数 >= .5，则与 ROUND_UP 一样；否则，行为与 ROUND_DOWN 相同。
	 * （向“最近的邻居”四舍五入，除非两个邻居是等距的，在这种情况下四舍五入。）
     */
    public final static int ROUND_HALF_UP = 	 4;

    /**
     * 如果丢弃的分数 > .5，则与 ROUND_UP 一样；否则，行为与 ROUND_DOWN 相同。
	 * （向“最近的邻居”四舍五入，除非两个邻居是等距的，在这种情况下向下舍入。）
     */
    public final static int ROUND_HALF_DOWN = 	 5;

    /**
     * 如果丢弃的分数左边的数字是奇数，则与 ROUND_HALF_UP 一样；如果它是偶数，则表现为 ROUND_HALF_DOWN。
	 * （向“最近的邻居”四舍五入，除非两个邻居距离相等，在这种情况下，向偶数邻居四舍五入。）
     */
    public final static int ROUND_HALF_EVEN = 	 6;

    /**
     * 这种“伪舍入模式”实际上是断言所请求的操作具有精确的结果，因此不需要舍入。
	 * 如果在产生不精确结果的操作上指定此舍入模式，则会引发算术异常。
     */
    public final static int ROUND_UNNECESSARY =  7;


    // Scaling/Rounding Operations（缩放四舍五入操作）

    /**
     * 返回一个 BigDecimal，其比例为指定值，其整数值是通过将此 BigDecimal 的整数值乘以或除以适当的 10 次方来确定的，以保持整体值。
	 * 如果通过操作缩小比例，则整数值必须除（而不是相乘），可能会丢失精度；在这种情况下，指定的舍入模式应用于除法。
	 * 如果 scale 为负数，或者舍入模式为 ROUND_UNNECESSARY 并且不可能在不损失精度的情况下执行指定的缩放操作，
	 * 则引发 ArithmeticException。
	 * 如果 roundingMode 不表示有效的舍入模式，则引发 IllegalArgumentException。
     */
    public BigDecimal setScale(int scale, int roundingMode)
	throws ArithmeticException, IllegalArgumentException {
	if (scale < 0)
	    throw new ArithmeticException("Negative scale");
	if (roundingMode < ROUND_UP || roundingMode > ROUND_UNNECESSARY)
	    throw new IllegalArgumentException("Invalid rounding mode");

	/* Handle the easy cases（处理简单的案件） */
	if (scale == this.scale)
	    return this;
	else if (scale > this.scale)
	    return new BigDecimal(timesTenToThe(intVal, scale-this.scale),
				  scale);
	else /* scale < this.scale */
	    return divide(valueOf(1), scale, roundingMode);
    }

    /**
     * 返回一个 BigDecimal，它的比例是指定的值，并且它的值正好等于这个数字的值。
	 * 如果这是不可能的，则抛出 ArithmeticException。
	 * 此调用通常用于增加比例，在这种情况下，可以保证存在指定比例的 BigDecimal 和正确的值。
	 * 如果调用者知道该数字在其小数部分的末尾有足够多的零（即，其整数值中的 10 倍）以允许在不损失精度的情况下重新缩放，
	 * 则该调用也可用于减小比例。
	 * 请注意，此调用返回与 setScale 的两个参数版本相同的结果，但在不相关的情况下为调用者省去了指定舍入模式的麻烦。
     */
    public BigDecimal setScale(int scale)
	throws ArithmeticException, IllegalArgumentException
    {
	return setScale(scale, ROUND_UNNECESSARY);
    }


    // Decimal Point Motion Operations（小数点运动运算）

    /**
     * 返回一个 BigDecimal，它等价于小数点向左移动 n 位。如果 n 为非负数，则调用仅将 n 添加到标度中。
	 * 如果 n 为负数，则调用等效于 movePointRight(-n)。
	 * （此调用返回的 BigDecimal 具有值 (this * 10**-n) 和 scale MAX(this.scale()+n, 0)。）
     */
    public BigDecimal movePointLeft(int n){
	return (n>=0 ? new BigDecimal(intVal, scale+n) : movePointRight(-n));
    }

    /**
     * 将小数点向右移动指定的位数。如果这个数字的比例 >= n，调用只是从比例中减去 n；
	 * 否则，它将比例设置为零，并将整数值乘以 10 ** (n - this.scale)。
	 * 如果 n 为负数，则调用等效于 movePointLeft(-n)。
	 * （此调用返回的 BigDecimal 具有值 (this * 10**n) 和 scale MAX(this.scale()-n, 0)。）
     */
    public BigDecimal movePointRight(int n){
	return (scale >= n ? new BigDecimal(intVal, scale-n)
		           : new BigDecimal(timesTenToThe(intVal, n-scale),0));
    }

    // Comparison Operations（比较操作）

    /**
     * 返回 -1、0 或 1，因为此数字小于、等于或大于 val。
	 * 此方法认为两个值相等但比例不同（例如，2.0、2.00）的 BigDecimal 是相等的。
	 * 此方法优先于六个布尔比较运算符（<、==、>、>=、!=、<=）中的每一个的单独方法提供。
	 * 执行这些比较的建议习语是：(x.compareTo(y)  0), 其中   是六个比较运算符之一。
     */
    public int compareTo(BigDecimal val){
	/* Optimization: would run fine without the next three lines（优化：没有接下来的三行就可以正常运行） */
	int sigDiff = signum() - val.signum();
	if (sigDiff != 0)
	    return (sigDiff > 0 ? 1 : -1);

	/* 如果符号匹配，缩放并比较 intVals */
	BigDecimal arg[] = new BigDecimal[2];
	arg[0] = this;	arg[1] = val;
	matchScale(arg);
	return arg[0].intVal.compareTo(arg[1].intVal);
    }

    /**
     * 如果 x 是一个 BigDecimal，其值等于该数字，则返回 true。
	 * 提供此方法以便 BigDecimals 可以用作散列键。
	 * 与 compareTo 不同，此方法仅当两个 BigDecimal 的值和比例相等时才认为它们相等。
     */
    public boolean equals(Object x){
	if (!(x instanceof BigDecimal))
	    return false;
	BigDecimal xDec = (BigDecimal) x;

	return scale == xDec.scale && intVal.equals(xDec.intVal);
    }

    /**
     * 返回值为 this 和 val 中较小值的 BigDecimal。如果值相等（由 compareTo 运算符定义），则可以返回任何一个。
     */
    public BigDecimal min(BigDecimal val){
	return (compareTo(val)<0 ? this : val);
    }

    /**
     * 返回值为 this 和 val 中较大者的 BigDecimal。如果值相等（由 compareTo 运算符定义），则可以返回任何一个。
     */
    public BigDecimal max(BigDecimal val){
	return (compareTo(val)>0 ? this : val);
    }


    // Hash Function（哈希函数）

    /**
     * 计算此对象的哈希码。请注意，数值相等但比例不同的两个 BigDecimal（例如，2.0、2.00）通常不会具有相同的哈希码。
     */
    public int hashCode(){
	return 37*intVal.hashCode() + scale;
    }

    // Format Converters（格式转换器）

    /**
     * 返回此数字的字符串表示形式。使用 Character.forDigit 提供的数字到字符映射。
	 * 减号和小数点用于表示符号和比例。 （此表示与 (String, int) 构造函数兼容。）
     */
    public String toString(){
	if (scale == 0)	/* No decimal point（没有小数点） */
	    return intVal.toString();

	/* Insert decimal point （插入小数点）*/
	StringBuffer buf;
	String intString = intVal.abs().toString();
	int signum = signum();
	int insertionPoint = intString.length() - scale;
	if (insertionPoint == 0) {  /* Point goes right before intVal（点在 intVal 之前） */
	    return (signum<0 ? "-0." : "0.") + intString;
	} else if (insertionPoint > 0) { /* Point goes inside intVal（点进入 intVal） */
	    buf = new StringBuffer(intString);
	    buf.insert(insertionPoint, '.');
	    if (signum < 0)
		buf.insert(0, '-');
	} else { /* We must insert zeros between point and intVal(我们必须在 point 和 intVal 之间插入零) */
	    buf = new StringBuffer(3-insertionPoint + intString.length());
	    buf.append(signum<0 ? "-0." : "0.");
	    for (int i=0; i<-insertionPoint; i++)
		buf.append('0');
	    buf.append(intString);
	}
	return buf.toString();
    }

    /**
     * 将此数字转换为 BigInteger。根据 Java 语言规范的标准缩小原语转换。特别要注意，此数字的任何小数部分都将被截断。
     */
    public BigInteger toBigInteger(){
	return (scale==0 ? intVal
			 : intVal.divide(BigInteger.valueOf(10).pow(scale)));
    }

    /**
     * 将此数字转换为 int。根据 Java 语言规范的标准缩小原语转换。特别要注意，此数字的任何小数部分都将被截断。
     */
    public int intValue(){
	return toBigInteger().intValue();
    }

    /**
     * 将此数字转换为长整数。根据 Java 语言规范的标准缩小原语转换。特别要注意，此数字的任何小数部分都将被截断。
     */
    public long longValue(){
	return toBigInteger().longValue();
    }

    /**
     * 将此数字转换为浮点数。
	 * 类似于 Java 语言规范中定义的双浮点数缩小原语转换：如果数字的幅度太大而无法表示为浮点数，它将酌情转换为无穷大或负无穷大。
     */
    public float floatValue(){
	/* Somewhat inefficient, but guaranteed to work. （有点低效，但保证可以工作。）*/
	return Float.valueOf(this.toString()).floatValue();
    }

    /**
     * 将数字转换为双精度数。
	 * 类似于 Java 语言规范中定义的 double 到 float 的缩小原语转换：
	 * 如果数字的幅度太大而无法表示为 double，则将酌情将其转换为无穷大或负无穷大。
     */
    public double doubleValue(){
	/* Somewhat inefficient, but guaranteed to work. （有点低效，但保证可以工作。） */
	return Double.valueOf(this.toString()).doubleValue();
    }


    // Private "Helper" Methods（私有“助手”方法）

    /* Returns (a * 10^b) */
    private static BigInteger timesTenToThe(BigInteger a, int b) {
	return a.multiply(BigInteger.valueOf(10).pow(b));
    }

    /*
     * 如果 val[0] 和 val[1] 的比例不同，则重新缩放（非破坏性地）较低比例的 BigDecimal 以使其匹配。
     */
    private static void matchScale(BigDecimal[] val) {
	if (val[0].scale < val[1].scale)
	    val[0] = val[0].setScale(val[1].scale);
	else if (val[1].scale < val[0].scale)
	    val[1] = val[1].setScale(val[0].scale);
    }

    /**
     * 从流中重构 BigDecimal 实例（即反序列化它）。
     */
    private synchronized void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        // Read in all fields（读取所有字段）
	s.defaultReadObject();

        // Validate scale factor(验证scale因子)
        if (scale < 0)
	    throw new java.io.StreamCorruptedException(
                                      "BigDecimal: Negative scale");
    }
}
