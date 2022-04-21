/*
 * @(#)AppletStub.java	1.14 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。
 * SUN 专有机密。使用受许可条款的约束。
 */
package java.applet;

import java.net.URL;

/**
 * 首次创建小程序时，使用小程序的 setStub 方法将一个小程序存根附加到它。
 * 该存根用作小程序与运行应用程序的浏览器环境或小程序查看器环境之间的接口。
 *
 * @author 	Arthur van Hoff（阿瑟·范霍夫）
 * @version     1.14, 2001/12/12
 * @see         Applet#setStub(AppletStub)
 * @since       JDK1.0
 */
public interface AppletStub {
    /**
     * 确定小程序是否处于活动状态。小程序在其 start 方法被调用之前处于活动状态。
     * 它在调用 stop 方法后立即变为非活动状态。
     *
     * @return  true 小程序处于活动状态;
     *          false 否则.
     * @since   JDK1.0
     */
    boolean isActive();
    
    /**
     * 获取文档 URL。
     *
     * @return  包含小程序的文档的 URL。
     * @since   JDK1.0
     */
    URL getDocumentBase();

    /**
     * 获取基本 URL。
     *
     * @return  小程序的 URL。
     * @since   JDK1.0
     */
    URL getCodeBase();

    /**
     * 返回 HTML 标记中命名参数的值。例如，如果一个小程序被指定为
     * <ul>
     *	&lt;applet code="Clock" width=50 height=50&gt;<br>
     *  &lt;param name=Color value="blue"&gt;<br>
     *  &lt;/applet&gt;
     * </ul>
     * 然后调用 getParameter("Color") 返回"blue"
     *
     * @param   name   参数名称。
     * @return  命名参数的值。
     * @since   JDK1.0
     */
    String getParameter(String name);

    /**
     * 获取小程序上下文的处理程序。
     *
     * @return  小程序的上下文。
     * @since   JDK1.0
     */
    AppletContext getAppletContext();

    /**
     * 当小程序想要调整大小时调用。
     *
     * @param   width    小程序的新请求宽度。
     * @param   height   小程序的新请求高度。
     * @since   JDK1.0
     */
    void appletResize(int width, int height);
}
