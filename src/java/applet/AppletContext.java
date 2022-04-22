/*
 * @(#)AppletContext.java	1.24 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.applet;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.ColorModel;
import java.net.URL;
import java.util.Enumeration;

/**
 * 该接口对应于applet 的环境：包含applet 的文档和同一文档中的其他applet。
 * 小程序可以使用此接口中的方法来获取有关其环境的信息。
 *
 * @author 	Arthur van Hoff（阿瑟·范霍夫）
 * @version     1.24, 2001/12/12
 * @since       JDK1.0
 */
public interface AppletContext {
    /**
     * 创建音频剪辑。
     *
     * @param   url   给出音频剪辑位置的绝对 URL。
     * @return  指定 URL 处的音频剪辑。
     * @since   JDK1.0
     */
    AudioClip getAudioClip(URL url);

    /**
     * 返回可以在屏幕上绘制的 Image 对象。作为参数传递的 url 参数必须指定一个绝对 URL。
     * 无论图像是否存在，此方法总是立即返回。
     * 当小程序尝试在屏幕上绘制图像时，将加载数据。绘制图像的图形基元将逐渐在屏幕上绘制。
     *
     * @param   url   给出图像位置的绝对 URL。
     * @return  指定 URL 处的图像。
     * @see     Image
     * @since   JDK1.0
     */
    Image getImage(URL url);

    /**
     * 在此小程序上下文表示的文档中查找并返回具有给定名称的小程序。
     * 可以通过设置 name 属性在 HTML 标记中设置名称。
     *
     * @param   name   小程序名称。
     * @return  具有给定名称的小程序，如果未找到，则为 null。
     * @since   JDK1.0
     */
    Applet getApplet(String name);

    /**
     * 在由该小程序上下文表示的文档中查找所有小程序。
     *
     * @return  此小程序上下文表示的文档中所有小程序的枚举。
     * @since   JDK1.0
     */
    Enumeration getApplets();

    /**
     * 用给定的 URL 替换当前正在查看的网页。不是浏览器的小程序上下文可能会忽略此方法。
     *
     * @param   url   给出文档位置的绝对 URL。
     * @since   JDK1.0
     */
    void showDocument(URL url);

    /**
     * 请求浏览器或小程序查看器显示由 url 参数指示的网页。 target 参数指示文档将在哪个 HTML 框架中显示。
     * 目标参数解释如下:
     * <center><table border="3"> 
     * <tr><td><code>"_self"</code>  <td>显示在包含小程序的窗口和框架中。</tr>
     * <tr><td><code>"_parent"</code><td>在小程序的父框架中显示。如果小程序的框架没有父框架，则与“_self”相同。</tr>
     * <tr><td><code>"_top"</code>   <td>显示在小程序窗口的顶层框架中。如果小程序的框架是顶级框架，则与“_self”作用相同。</tr>
     * <tr><td><code>"_blank"</code> <td>在一个新的、未命名的顶级窗口中显示。</tr>
     * <tr><td><i>name</i><td>在名为 name 的框架或窗口中显示。
     * 如果名为 name的目标尚不存在，则会创建一个具有指定名称的新顶层窗口，并在其中显示文档。</tr>
     * </table> </center>
     * <p>
     * 小程序查看器或浏览器可以随意忽略 showDocument。
     *
     * @param   url   给出文档位置的绝对 URL。
     * @param   target   String 指示页面的显示位置。
     * @since   JDK1.0
     */
    public void showDocument(URL url, String target);

    /**
     * 请求在“状态窗口”中显示参数字符串。许多浏览器和小程序查看器都提供了这样一个窗口，应用程序可以在其中通知用户其当前状态。
     *
     * @param   status   要在状态窗口中显示的字符串。
     * @since   JDK1.0
     */
    void showStatus(String status);
}
