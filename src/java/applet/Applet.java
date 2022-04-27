/*
 * @(#)Applet.java	1.44 01/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */
package java.applet;

import java.awt.*;
import java.awt.image.ColorModel;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Locale;

/**
 * 小程序是一个小程序，它不打算单独运行，而是嵌入到另一个应用程序中。
 * Applet 类必须是要嵌入网页中或由 Java Applet 查看器查看的任何小程序的超类。
 * Applet 类在 applet 和它们的环境之间提供了一个标准接口。
 *
 * @author      Arthur van Hoff（阿瑟·范霍夫）
 * @author      Chris Warth（克里斯·沃思）
 * @version     1.44, 2001/12/12
 * @since       JDK1.0
 */
public class Applet extends Panel {
    /**
     * 小程序可以序列化，但必须遵循以下约定：
     *
     * 序列化之前:
     * 小程序必须处于已停止状态。
     *
     * 反序列化后：
     * 小程序将恢复为 STOPPED 状态（大多数客户端可能会将其移至 RUNNING 状态）。
     * 存根字段将由阅读器恢复。
     */
    transient private AppletStub stub;

    /**
     * 设置此小程序的存根。这是由系统自动完成的。
     * 
     * @param   stub   新的存根。
     * @since   JDK1.0
     */
    public final void setStub(AppletStub stub) {
	this.stub = (AppletStub)stub;
    }

    /**
     * 确定此小程序是否处于活动状态。小程序在其 start 方法被调用之前被标记为活动的。
     * 它在调用 stop 方法后立即变为非活动状态。
     *
     * @return  true 如果小程序处于活动状态； false 否则。
     * @see     Applet#start()
     * @see     Applet#stop()
     * @since   JDK1.0
     */
    public boolean isActive() {
	if (stub != null) {
	    return stub.isActive();
	} else {	// 如果未填写存根字段，则小程序永远不会激活
	    return false;
	}
    }
    
    /**
     * 获取文档 URL。这是嵌入小程序的文档的 URL。
     *
     * @return  包含此小程序的文档的 <a href="java.net.URL.html_top_"> URL <a>。
     * @see     Applet#getCodeBase()
     * @since   JDK1.0
     */
    public URL getDocumentBase() {
	return stub.getDocumentBase();
    }

    /**
     * 获取基本 URL。这是小程序本身的 URL。
     *
     * @return  此小程序的 <a href="java.net.URL.html_top_">URL<a>。
     * @see     Applet#getDocumentBase()
     * @since   JDK1.0
     */
    public URL getCodeBase() {
	return stub.getCodeBase();
    }

    /**
     * 返回 HTML 标记中命名参数的值。例如，如果这个小程序被指定为
     * <ul>
     *	&lt;applet code="Clock" width=50 height=50&gt;<br>
     *  &lt;param name=Color value="blue"&gt;<br>
     *  &lt;/applet&gt;
     * </ul>
     * 然后调用getParameter("Color") 返回"blue"。
     * <p>
     * name 参数不区分大小写。
     *
     * @param   name   参数名称。
     * @return  命名参数的值。
     * @since   JDK1.0
     */
     public String getParameter(String name) {
	 return stub.getParameter(name);
     }

    /**
     * 确定这个小程序的上下文，它允许小程序查询和影响它运行的环境。
     * 小程序的这个环境表示包含小程序的文档。
     *
     * @return  小程序的上下文。
     * @since   JDK1.0
     */
    public AppletContext getAppletContext() {
	return stub.getAppletContext();
    }
   
    /**
     * 请求调整此小程序的大小。
     *
     * @param   width    小程序的新请求宽度。
     * @param   height   小程序的新请求高度。
     * @since   JDK1.0
     */
    public void resize(int width, int height) {
	Dimension d = size();
	if ((d.width != width) || (d.height != height)) {
	    super.resize(width, height);
	    if (stub != null) {
		stub.appletResize(width, height);
	    }
	}
    }

    /**
     * 请求调整此小程序的大小。
     *
     * @param   d   一个给出新宽度和高度的对象。
     * @since   JDK1.0
     */    
    public void resize(Dimension d) {
	resize(d.width, d.height);
    }

    /**
     * 请求在“状态窗口”中显示参数字符串。
     * 许多浏览器和小程序查看器都提供了这样一个窗口，应用程序可以在其中通知用户其当前状态。
     *
     * @param   msg   要在状态窗口中显示的字符串。
     * @since   JDK1.0
     */
    public void showStatus(String msg) {
	getAppletContext().showStatus(msg);
    }

    /**
     * 返回可以在屏幕上绘制的 Image 对象。
     * 作为参数传递的 url 必须指定一个绝对 URL。
     * 无论图像是否存在，此方法总是立即返回。当此小程序尝试在屏幕上绘制图像时，将加载数据。
     * 绘制图像的图形基元将逐渐在屏幕上绘制。
     *
     * @param   url   给出图像位置的绝对 URL。
     * @return  指定 URL 处的图像。
     * @see     Image
     * @since   JDK1.0
     */
    public Image getImage(URL url) {
	return getAppletContext().getImage(url);
    }

    /**
     * 返回可以在屏幕上绘制的 Image 对象。 url 参数必须指定一个绝对 URL。
     * name 参数是相对于 url 参数的说明符。
     * 无论图像是否存在，此方法总是立即返回。当此小程序尝试在屏幕上绘制图像时，将加载数据。
     * 绘制图像的图形基元将逐渐在屏幕上绘制。
     *
     * @param   url    给出图像基本位置的绝对 URL。
     * @param   name   图片的位置，相对于 url 参数。
     * @return  指定 URL 处的图像。
     * @see     Image
     * @since   JDK1.0
     */
    public Image getImage(URL url, String name) {
	try {
	    return getImage(new URL(url, name));
	} catch (MalformedURLException e) {
	    return null;
	}
    }

    /**
     * 返回由 URL 参数指定的 AudioClip 对象。
     * 无论音频剪辑是否存在，此方法总是立即返回。当此小程序尝试播放音频剪辑时，将加载数据。
     *
     * @param   url  给出音频剪辑位置的绝对 URL。
     * @return  指定 URL 处的音频剪辑。
     * @see     AudioClip
     * @since   JDK1.0
     */
    public AudioClip getAudioClip(URL url) {
	return getAppletContext().getAudioClip(url);
    }

    /**
     * 返回由 URL 和 name 参数指定的 AudioClip 对象。
     * 无论音频剪辑是否存在，此方法总是立即返回。当此小程序尝试播放音频剪辑时，将加载数据。
     * 
     * @param   url    一个绝对 URL，给出音频剪辑的基本位置。
     * @param   name   音频剪辑的位置，相对于 url 参数。
     * @return  指定 URL 处的音频剪辑。
     * @see     AudioClip
     * @since   JDK1.0
     */
    public AudioClip getAudioClip(URL url, String name) {
	try {
	    return getAudioClip(new URL(url, name));
	} catch (MalformedURLException e) {
	    return null;
	}
    }

    /**
     * 返回有关此小程序的信息。小程序应覆盖此方法以返回包含有关小程序的作者、版本和版权信息的 String
     * Applet 类提供的此方法的实现返回 null
     *
     * @return  一个字符串，包含有关小程序的作者、版本和版权的信息。
     * @since   JDK1.0
     */
    public String getAppletInfo() {
	return null;
    }

    /** 
     * 获取小程序的区域设置（如果已设置）。如果没有设置区域设置，则返回默认区域设置。
     *
     * @return  [Needs to be documented!(需要备案！)]
     * @since   JDK1.1
     */

    public Locale getLocale() {
      Locale locale = super.getLocale();
      if (locale == null) {
	return Locale.getDefault();
      }
      return locale;
    }

    /**
     * 返回有关此小程序无法理解的参数的信息。小程序应覆盖此方法以返回描述这些参数的 Strings 数组。
     * <p>
     * 数组的每个元素应该是一组包含名称、类型和描述的三个 Strings。例如：
     * <p><blockquote><pre>
     * String pinfo[][] = {
     *	 {"fps",    "1-10",    "frames per second"},
     *	 {"repeat", "boolean", "repeat image loop"},
     *	 {"imgs",   "url",     "images directory"}
     * };
     * </pre></blockquote>
     * <p>
     * Applet 类提供的此方法的实现返回 null。
     *
     * @return  描述此小程序查找的参数的数组。
     * @since   JDK1.0
     */
    public String[][] getParameterInfo() {
	return null;
    }

    /**
     * 在指定的绝对 URL 播放音频剪辑。如果找不到音频剪辑，则不会发生任何事情。
     *
     * @param   url   给出音频剪辑位置的绝对 URL。
     * @since   JDK1.0
     */
    public void play(URL url) {
	AudioClip clip = getAudioClip(url);
	if (clip != null) {
	    clip.play();
	}
    }

    /**
     * 播放给定 URL 和与其相关的说明符的音频剪辑。如果找不到音频剪辑，则不会发生任何事情。
     *
     * @param   url    一个绝对 URL，给出音频剪辑的基本位置。
     * @param   name   音频剪辑的位置，相对于 url 参数。
     * @since   JDK1.0
     */
    public void play(URL url, String name) {
	AudioClip clip = getAudioClip(url, name);
	if (clip != null) {
	    clip.play();
	}
    }

    /**
     * 由浏览器或小程序查看器调用以通知此小程序已加载到系统中。它总是在第一次调用 start 方法之前被调用。
     * <p>
     * Applet 的子类如果要执行初始化，则应覆盖此方法。
     * 例如，带有线程的小程序将使用 init 方法来创建线程，并使用 destroy 方法来杀死它们。
     * <p>
     * Applet 类提供的这个方法的实现什么也不做。
     *
     * @see     Applet#destroy()
     * @see     Applet#start()
     * @see     Applet#stop()
     * @since   JDK1.0
     */
    public void init() {
    }

    /**
     * 由浏览器或小程序查看器调用以通知此小程序它应该开始执行。
     * 它在 init 方法之后以及每次在 Web 页面中重新访问 applet 时调用。
     * <p>
     * 如果 Applet 的子类在每次访问包含它的网页时都有想要执行的任何操作，则应该重写此方法。
     * 例如，带有动画的小程序可能希望使用 start 方法来恢复动画，并使用 stop 方法来暂停动画。
     * <p>
     * Applet 类提供的这个方法的实现什么也不做。
     *
     * @see     Applet#destroy()
     * @see     Applet#init()
     * @see     Applet#stop()
     * @since   JDK1.0
     */
    public void start() {
    }

    /**
     * 由浏览器或小程序查看器调用以通知此小程序应停止执行。
     * 当包含此小程序的网页已被另一个页面替换时，并且在小程序要被销毁之前调用它。
     * <p>
     * 如果 Applet 的子类在每次包含它的网页不再可见时都希望执行任何操作，则应覆盖此方法。
     * 例如，带有动画的小程序可能希望使用 start 方法来恢复动画，并使用 stop 方法来暂停动画。
     * <p>
     * Applet 类提供的这个方法的实现什么也不做。
     *
     * @see     Applet#destroy()
     * @see     Applet#init()
     * @since   JDK1.0
     */
    public void stop() {
    }

    /**
     * 由浏览器或小程序查看器调用以通知此小程序它正在被回收并且它应该销毁它已分配的任何资源。
     * stop 方法总是在 destroy 之前调用。
     * <p>
     * 如果 Applet 的子类在销毁之前有任何想要执行的操作，则应覆盖此方法。
     * 例如，带有线程的小程序将使用 init 方法来创建线程，并使用 destroy 方法来杀死它们。
     * <p>
     * Applet 类提供的这个方法的实现什么也不做。
     *
     * @see     Applet#init()
     * @see     Applet#start()
     * @see     Applet#stop()
     * @since   JDK1.0
     */
    public void destroy() {
    }
}
