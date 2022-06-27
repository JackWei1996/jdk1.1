/*
 * @(#)PropertyChangeSupport.java	1.14 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。 SUN 专有机密。使用受许可条款的约束。
 */

package java.beans;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;


/**
 * 这是支持绑定属性的 bean 可以使用的实用程序类。您可以将此类的实例用作 bean 的成员字段并将各种工作委托给它。
 */

public class PropertyChangeSupport implements Serializable {

    /**
     * @sourceBean  The bean to be given as the source for any events.
     */

    public PropertyChangeSupport(Object sourceBean) {
	source = sourceBean;
    }

    /**
     * Add a PropertyChangeListener to the listener list.
     *
     * @param listener  The PropertyChangeListener to be added
     */

    public synchronized void addPropertyChangeListener(
				PropertyChangeListener listener) {
	if (listeners == null) {
	    listeners = new java.util.Vector();
	}
	listeners.addElement(listener);
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     *
     * @param listener  The PropertyChangeListener to be removed
     */

    public synchronized void removePropertyChangeListener(
				PropertyChangeListener listener) {
	if (listeners == null) {
	    return;
	}
	listeners.removeElement(listener);
    }

    /**
     * 向任何已注册的侦听器报告绑定的属性更新。如果 old 和 new 相等且非空，则不会触发任何事件。
     *
     * @param propertyName  The programmatic name of the property
     *		that was changed.
     * @param oldValue  The old value of the property.
     * @param newValue  The new value of the property.
     */
    public void firePropertyChange(String propertyName, 
					Object oldValue, Object newValue) {

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

	for (int i = 0; i < targets.size(); i++) {
	    PropertyChangeListener target = (PropertyChangeListener)targets.elementAt(i);
	    target.propertyChange(evt);
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
	    for(int i = 0; i < v.size(); i++) {
	        PropertyChangeListener l = (PropertyChangeListener)v.elementAt(i);
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
	  addPropertyChangeListener((PropertyChangeListener)listenerOrNull);
        }
    }

    transient private java.util.Vector listeners;
    private Object source;
    private int propertyChangeSupportSerializedDataVersion = 1;
}




