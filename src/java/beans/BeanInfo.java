/*
 * @(#)BeanInfo.java	1.20 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

/**
 * 希望提供有关其 bean 的显式信息的 bean 实现者可以提供一个 BeanInfo 类，
 * 该类实现此 BeanInfo 接口并提供有关其 bean 的方法、属性、事件等的显式信息。
 *
 * bean 实现者不需要提供一整套显式信息。您可以选择要提供的信息，
 * 其余信息将通过使用 bean 类方法的低级反射和应用标准设计模式的自动分析来获得。
 *
 * 您有机会在各种 XyZDescriptor 类中提供大量不同的信息。
 * 但不要惊慌，您只需要提供各种构造函数所需的最少核心信息。
 *
 * 另请参阅 SimpleBeanInfo 类，它为 BeanInfo 类提供了一个方便的“noop”基类，您可以在您想要返回显式信息的特定位置覆盖它。
 *
 * To learn about all the behaviour of a bean see the Introspector class.
 */

public interface BeanInfo {

    /**
     * @return  一个 BeanDescriptor 提供有关 bean 的整体信息，
     * 例如它的 displayName、它的自定义程序等。如果应该通过自动分析获取信息，则可能返回 null。
     */
    BeanDescriptor getBeanDescriptor();
    
    /**
     * @return  一个 EventSetDescriptor 数组，描述了此 bean 触发的事件类型。
     * 如果应该通过自动分析获取信息，则可能返回 null。
     */
    EventSetDescriptor[] getEventSetDescriptors();

    /**
     * 一个 bean 可能有一个“默认”事件，这是人类在使用 bean 时最常使用的事件。
     * @return getEventSetDescriptors 返回的 EventSetDescriptor 数组中默认事件的索引。
     * <P>	Returns -1 if there is no default event.
     */
    int getDefaultEventIndex();

    /**
     * @return 描述此 bean 支持的可编辑属性的 PropertyDescriptor 数组。如果应该通过自动分析获取信息，则可能返回 null。
     *
     * 如果一个属性被索引，那么它在结果数组中的条目将属于 PropertyDescriptor 的 IndexedPropertyDescriptor 子类。
     * getPropertyDescriptor 的客户端可以使用“instanceof”来检查给定的 PropertyDescriptor 是否是 IndexedPropertyDescriptor。
     */
    PropertyDescriptor[] getPropertyDescriptors();

    /**
     * bean 可能具有“默认”属性，该属性通常最初通常由正在定制 bean 的人选择用于更新。
     * @return  getPropertyDescriptors 返回的 PropertyDescriptor 数组中默认属性的索引。
     * <P>	Returns -1 if there is no default property.
     */
    int getDefaultPropertyIndex();

    /**
     * @return 描述此 bean 支持的外部可见方法的 MethodDescriptor 数组。如果应该通过自动分析获取信息，则可能返回 null。
     */
    MethodDescriptor[] getMethodDescriptors();

    /**
     * 此方法允许 BeanInfo 对象返回提供有关当前 bean 的附加信息的其他 BeanInfo 对象的任意集合。
     *
     * 如果不同 BeanInfo 对象提供的信息之间存在冲突或重叠，
     * 则当前 BeanInfo 优先于 getAdditionalBeanInfo 对象，数组中后面的元素优先于前面的元素。
     *
     * @return an array of BeanInfo objects.  May return null.
     */
    BeanInfo[] getAdditionalBeanInfo();

    /**
     * 此方法返回一个图像对象，该对象可用于在工具箱、工具栏等中表示 bean。图标图像通常是 GIF，但将来可能包括其他格式。
     *
     * Bean 不需要提供图标，并且可以从此方法返回 null。
     *
     * 有四种可能的图标风格（16x16 色、32x32 色、16x16 单色、32x32 单色）。
     * 如果 bean 选择仅支持单个图标，我们建议支持 16x16 颜色。
     *
     * 我们建议图标具有“透明”背景，以便可以将它们呈现到现有背景上。
     *
     * @param  iconKind  请求的图标类型。这应该是常量值 ICON_COLOR_16x16、
     *                   ICON_COLOR_32x32、ICON_MONO_16x16 或 ICON_MONO_32x32 之一。
     * @return 表示请求图标的图像对象。如果没有合适的图标可用，则可能返回 null。
     */
    java.awt.Image getIcon(int iconKind);
     
    /**
     * Constant to indicate a 16 x 16 color icon.
     */
    final static int ICON_COLOR_16x16 = 1;

    /**
     * Constant to indicate a 32 x 32 color icon.
     */
    final static int ICON_COLOR_32x32 = 2;

    /**
     * Constant to indicate a 16 x 16 monochrome icon.
     */
    final static int ICON_MONO_16x16 = 3;

    /**
     * Constant to indicate a 32 x 32 monochrome icon.
     */
    final static int ICON_MONO_32x32 = 4;
}
