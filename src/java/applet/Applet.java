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
     * Gets the base URL. This is the URL of the applet itself. 
     *
     * @return  the <a href="java.net.URL.html#_top_"><code>URL</code></a> of
     *          this applet.
     * @see     Applet#getDocumentBase()
     * @since   JDK1.0
     */
    public URL getCodeBase() {
	return stub.getCodeBase();
    }

    /**
     * Returns the value of the named parameter in the HTML tag. For 
     * example, if this applet is specified as
     * <ul><code>
     *	&lt;applet code="Clock" width=50 height=50&gt;<br>
     *  &lt;param name=Color value="blue"&gt;<br>
     *  &lt;/applet&gt;
     * </code></ul>
     * <p>
     * then a call to <code>getParameter("Color")</code> returns the 
     * value <code>"blue"</code>. 
     * <p>
     * The <code>name</code> argument is case insensitive. 
     *
     * @param   name   a parameter name.
     * @return  the value of the named parameter.
     * @since   JDK1.0
     */
     public String getParameter(String name) {
	 return stub.getParameter(name);
     }

    /**
     * Determines this applet's context, which allows the applet to 
     * query and affect the environment in which it runs. 
     * <p>
     * This environment of an applet represents the document that 
     * contains the applet. 
     *
     * @return  the applet's context.
     * @since   JDK1.0
     */
    public AppletContext getAppletContext() {
	return stub.getAppletContext();
    }
   
    /**
     * Requests that this applet be resized. 
     *
     * @param   width    the new requested width for the applet.
     * @param   height   the new requested height for the applet.
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
     * Requests that this applet be resized. 
     *
     * @param   d   an object giving the new width and height.
     * @since   JDK1.0
     */    
    public void resize(Dimension d) {
	resize(d.width, d.height);
    }

    /**
     * Requests that the argument string be displayed in the 
     * "status window". Many browsers and applet viewers 
     * provide such a window, where the application can inform users of 
     * its current state. 
     *
     * @param   msg   a string to display in the status window.
     * @since   JDK1.0
     */
    public void showStatus(String msg) {
	getAppletContext().showStatus(msg);
    }

    /**
     * Returns an <code>Image</code> object that can then be painted on 
     * the screen. The <code>url</code> that is passed as an argument 
     * must specify an absolute URL. 
     * <p>
     * This method always returns immediately, whether or not the image 
     * exists. When this applet attempts to draw the image on the screen, 
     * the data will be loaded. The graphics primitives that draw the 
     * image will incrementally paint on the screen. 
     *
     * @param   url   an absolute URL giving the location of the image.
     * @return  the image at the specified URL.
     * @see     Image
     * @since   JDK1.0
     */
    public Image getImage(URL url) {
	return getAppletContext().getImage(url);
    }

    /**
     * Returns an <code>Image</code> object that can then be painted on 
     * the screen. The <code>url</code> argument must specify an absolute 
     * URL. The <code>name</code> argument is a specifier that is 
     * relative to the <code>url</code> argument. 
     * <p>
     * This method always returns immediately, whether or not the image 
     * exists. When this applet attempts to draw the image on the screen, 
     * the data will be loaded. The graphics primitives that draw the 
     * image will incrementally paint on the screen. 
     *
     * @param   url    an absolute URL giving the base location of the image.
     * @param   name   the location of the image, relative to the
     *                 <code>url</code> argument.
     * @return  the image at the specified URL.
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
     * Returns the <code>AudioClip</code> object specified by the 
     * <code>URL</code> argument. 
     * <p>
     * This method always returns immediately, whether or not the audio 
     * clip exists. When this applet attempts to play the audio clip, the 
     * data will be loaded. 
     *
     * @param   url  an absolute URL giving the location of the audio clip.
     * @return  the audio clip at the specified URL.
     * @see     AudioClip
     * @since   JDK1.0
     */
    public AudioClip getAudioClip(URL url) {
	return getAppletContext().getAudioClip(url);
    }

    /**
     * Returns the <code>AudioClip</code> object specified by the 
     * <code>URL</code> and <code>name</code> arguments. 
     * <p>
     * This method always returns immediately, whether or not the audio 
     * clip exists. When this applet attempts to play the audio clip, the 
     * data will be loaded. 
     * 
     * @param   url    an absolute URL giving the base location of the
     *                 audio clip.
     * @param   name   the location of the audio clip, relative to the
     *                 <code>url</code> argument.
     * @return  the audio clip at the specified URL.
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
     * Returns information about this applet. An applet should override 
     * this method to return a <code>String</code> containing information 
     * about the author, version, and copyright of the applet. 
     * <p>
     * The implementation of this method provided by the 
     * <code>Applet</code> class returns <code>null</code>. 
     *
     * @return  a string containing information about the author, version, and
     *          copyright of the applet.
     * @since   JDK1.0
     */
    public String getAppletInfo() {
	return null;
    }

    /** 
     * Gets the Locale for the applet, if it has been set.
     * If no Locale has been set, then the default Locale 
     * is returned.
     *
     * @return  [Needs to be documented!]
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
     * Returns information about the parameters than are understood by 
     * this applet. An applet should override this method to return an 
     * array of <code>Strings</code> describing these parameters. 
     * <p>
     * Each element of the array should be a set of three 
     * <code>Strings</code> containing the name, the type, and a 
     * description. For example:
     * <p><blockquote><pre>
     * String pinfo[][] = {
     *	 {"fps",    "1-10",    "frames per second"},
     *	 {"repeat", "boolean", "repeat image loop"},
     *	 {"imgs",   "url",     "images directory"}
     * };
     * </pre></blockquote>
     * <p>
     * The implementation of this method provided by the 
     * <code>Applet</code> class returns <code>null</code>. 
     *
     * @return  an array describing the parameters this applet looks for.
     * @since   JDK1.0
     */
    public String[][] getParameterInfo() {
	return null;
    }

    /**
     * Plays the audio clip at the specified absolute URL. Nothing 
     * happens if the audio clip cannot be found. 
     *
     * @param   url   an absolute URL giving the location of the audio clip.
     * @since   JDK1.0
     */
    public void play(URL url) {
	AudioClip clip = getAudioClip(url);
	if (clip != null) {
	    clip.play();
	}
    }

    /**
     * Plays the audio clip given the URL and a specifier that is 
     * relative to it. Nothing happens if the audio clip cannot be found. 
     *
     * @param   url    an absolute URL giving the base location of the
     *                 audio clip.
     * @param   name   the location of the audio clip, relative to the
     *                 <code>url</code> argument.
     * @since   JDK1.0
     */
    public void play(URL url, String name) {
	AudioClip clip = getAudioClip(url, name);
	if (clip != null) {
	    clip.play();
	}
    }

    /**
     * Called by the browser or applet viewer to inform 
     * this applet that it has been loaded into the system. It is always 
     * called before the first time that the <code>start</code> method is 
     * called. 
     * <p>
     * A subclass of <code>Applet</code> should override this method if 
     * it has initialization to perform. For example, an applet with 
     * threads would use the <code>init</code> method to create the 
     * threads and the <code>destroy</code> method to kill them. 
     * <p>
     * The implementation of this method provided by the 
     * <code>Applet</code> class does nothing. 
     *
     * @see     Applet#destroy()
     * @see     Applet#start()
     * @see     Applet#stop()
     * @since   JDK1.0
     */
    public void init() {
    }

    /**
     * Called by the browser or applet viewer to inform 
     * this applet that it should start its execution. It is called after 
     * the <code>init</code> method and each time the applet is revisited 
     * in a Web page. 
     * <p>
     * A subclass of <code>Applet</code> should override this method if 
     * it has any operation that it wants to perform each time the Web 
     * page containing it is visited. For example, an applet with 
     * animation might want to use the <code>start</code> method to 
     * resume animation, and the <code>stop</code> method to suspend the 
     * animation. 
     * <p>
     * The implementation of this method provided by the 
     * <code>Applet</code> class does nothing. 
     *
     * @see     Applet#destroy()
     * @see     Applet#init()
     * @see     Applet#stop()
     * @since   JDK1.0
     */
    public void start() {
    }

    /**
     * Called by the browser or applet viewer to inform 
     * this applet that it should stop its execution. It is called when 
     * the Web page that contains this applet has been replaced by 
     * another page, and also just before the applet is to be destroyed. 
     * <p>
     * A subclass of <code>Applet</code> should override this method if 
     * it has any operation that it wants to perform each time the Web 
     * page containing it is no longer visible. For example, an applet 
     * with animation might want to use the <code>start</code> method to 
     * resume animation, and the <code>stop</code> method to suspend the 
     * animation. 
     * <p>
     * The implementation of this method provided by the 
     * <code>Applet</code> class does nothing. 
     *
     * @see     Applet#destroy()
     * @see     Applet#init()
     * @since   JDK1.0
     */
    public void stop() {
    }

    /**
     * Called by the browser or applet viewer to inform 
     * this applet that it is being reclaimed and that it should destroy 
     * any resources that it has allocated. The <code>stop</code> method 
     * will always be called before <code>destroy</code>. 
     * <p>
     * A subclass of <code>Applet</code> should override this method if 
     * it has any operation that it wants to perform before it is 
     * destroyed. For example, an applet with threads would use the 
     * <code>init</code> method to create the threads and the 
     * <code>destroy</code> method to kill them. 
     * <p>
     * The implementation of this method provided by the 
     * <code>Applet</code> class does nothing. 
     *
     * @see     Applet#init()
     * @see     Applet#start()
     * @see     Applet#stop()
     * @since   JDK1.0
     */
    public void destroy() {
    }
}
