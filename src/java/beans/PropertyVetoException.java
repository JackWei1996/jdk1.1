/*
 * @(#)PropertyVetoException.java	1.8 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

 
/**
 * 当对属性的提议更改表示不可接受的值时，将引发 PropertyVetoException。
 */

public
class PropertyVetoException extends Exception {

 
    /**
     * @param mess Descriptive message(描述性消息)
     * @param evt A PropertyChangeEvent describing the vetoed change.(描述被否决的更改的 PropertyChangeEvent)
     */
    public PropertyVetoException(String mess, PropertyChangeEvent evt) {
        super(mess);
	this.evt = evt;	
    }

    public PropertyChangeEvent getPropertyChangeEvent() {
	return evt;
    }

    private PropertyChangeEvent evt;
}
