/*
 * @(#)Visibility.java	1.7 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

/**
 * 在某些情况下，bean 可能会在没有 GUI 可用的服务器上运行。
 * 该接口可用于查询 bean 以确定它是否绝对需要 gui，并告知 bean 是否有可用的 GUI。
 *
 * 此接口适用于专家开发人员，普通的简单 bean 不需要此接口。
 * 为了避免混淆最终用户，我们避免对这些方法使用 getXXX setXXX 设计模式。
 */

public interface Visibility {

    /**
     * @return 如果 bean 绝对需要一个可用的 GUI 来完成其工作，则为真。
     */
    boolean needsGui();

    /**
     * 此方法指示 bean 它不应该使用 Gui。
     */
    void dontUseGui();

    /**
     * 该方法指示 bean 可以使用 Gui。
     */
    void okToUseGui();

    /**
     * @return 如果 bean 当前正在避免使用 Gui，则为 true。例如由于调用了 dontUseGui()。
     */
    boolean avoidingGui();

}
