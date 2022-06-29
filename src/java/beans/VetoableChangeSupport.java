/*
 * @(#)VetoableChangeSupport.java	1.15 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;


/**
 * 这是一个可供支持受限属性的 bean 使用的实用程序类。您可以将此类的实例用作 bean 的成员字段并将各种工作委托给它。
 */

public class VetoableChangeSupport implements Serializable {

    /**
     * @sourceBean  The bean to be given as the source for any events.
     */

    public VetoableChangeSupport(Object sourceBean) {
	source = sourceBean;
    }

    /**
     * Add a VetoableListener to the listener list.
     *
     * @param listener  The VetoableChangeListener to be added
     */

    public synchronized void addVetoableChangeListener(
					VetoableChangeListener listener) {
	if (listeners == null) {
	    listeners = new java.util.Vector();
	}
	listeners.addElement(listener);
    }

    /**
     * Remove a VetoableChangeListener from the listener list.
     *
     * @param listener  The VetoableChangeListener to be removed
     */
    public synchronized void removeVetoableChangeListener(
					VetoableChangeListener listener) {
	if (listeners == null) {
	    return;
	}
	listeners.removeElement(listener);
    }

    /**
     * 向任何已注册的侦听器报告可否决的属性更新。
	 * 如果有人否决更改，则触发一个新事件，将每个人恢复为旧值，然后重新抛出 PropertyVetoException。
     *
     * No event is fired if old and new are equal and non-null.
     *
     * @param propertyName  The programmatic name of the property
     *		that was changed.
     * @param oldValue  The old value of the property.
     * @param newValue  The new value of the property.
     * @exception PropertyVetoException if the recipient wishes the property
     *              change to be rolled back.
     */
    public void fireVetoableChange(String propertyName, 
					Object oldValue, Object newValue)
					throws PropertyVetoException {

	if (oldValue != null && oldValue.equals(newValue)) {
	    return;
	}

	java.util.Vector targets;
	synchronized (this) {
	    if (listeners == null) {
	    	return;
	    }
	    targets = (java.util.Vector) listeners.clone();
	}
        PropertyChangeEvent evt = new PropertyChangeEvent(source,
					    propertyName, oldValue, newValue);

	try {
	    for (int i = 0; i < targets.size(); i++) {
	        VetoableChangeListener target = 
				(VetoableChangeListener)targets.elementAt(i);
	        target.vetoableChange(evt);
	    }
	} catch (PropertyVetoException veto) {
	    // Create an event to revert everyone to the old value.
       	    evt = new PropertyChangeEvent(source, propertyName, newValue, oldValue);
	    for (int i = 0; i < targets.size(); i++) {
		try {
	            VetoableChangeListener target =
				(VetoableChangeListener)targets.elementAt(i);
	            target.vetoableChange(evt);
		} catch (PropertyVetoException ex) {
		     // We just ignore exceptions that occur during reversions.
		}
	    }
	    // And now rethrow the PropertyVetoException.
	    throw veto;
	}
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();

	java.util.Vector v = null;
	synchronized (this) {
	    if (listeners != null) {
	        v = (java.util.Vector) listeners.clone();
            }
	}

	if (v != null) {
	    for(int i = 0; i < listeners.size(); i++) {
	        VetoableChangeListener l = (VetoableChangeListener)v.elementAt(i);
	        if (l instanceof Serializable) {
	            s.writeObject(l);
	        }
            }
        }
        s.writeObject(null);
    }


    private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
        s.defaultReadObject();
      
        Object listenerOrNull;
        while(null != (listenerOrNull = s.readObject())) {
	    addVetoableChangeListener((VetoableChangeListener)listenerOrNull);
        }
    }

    transient private java.util.Vector listeners;
    private Object source;
    private int vetoableChangeSupportSerializedDataVersion = 1;
}
