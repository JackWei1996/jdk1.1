/*
 * @(#)AudioClip.java	1.11 2001/12/12
 *
 * 版权所有 2002 Sun Microsystems, Inc. 保留所有权利。
 * SUN 专有机密。使用受许可条款的约束。
 */

package java.applet;

/**
 * AudioClip 接口是播放声音剪辑的简单抽象。
 * 可以同时播放多个 AudioClip 项目，并将生成的声音混合在一起以产生复合声音。
 *
 * @author 	Arthur van Hoff(阿瑟·范霍夫)
 * @version     1.11, 2001/12/12
 * @since       JDK1.0
 */
public interface AudioClip {
    /**
     * 开始播放此音频剪辑。每次调用此方法时，剪辑都会从头开始。
     *
     * @since   JDK1.0
     */
    void play();

    /**
     * 开始循环播放此音频剪辑。
     *
     * @since   JDK1.0
     */
    void loop();

    /**
     * 停止播放此音频剪辑。
     *
     * @since   JDK1.0
     */
    void stop();
}
