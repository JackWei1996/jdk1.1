/*
 * 版权所有 2004、2010年 Oracle 及其附属公司。版权所有。
 *
 * 如果满足以下条件，则允许以源代码和二进制形式重新分发和使用，无论是否经过修改：
 *
 * - 重新分发源代码必须保留上述版权声明、此条件列表和以下免责声明。
 *
 * - 以二进制形式重新分发必须在随分发提供的文档和或其他材料中复制上述版权声明、此条件列表和以下免责声明。
 *
 * 甲骨文及其附属公司的名称都不是。或贡献者的姓名可用于认可或推广从该软件派生的产品，而无需事先明确书面许可。
 *
 * 本软件按“原样”提供，不提供任何形式的保证。特此排除所有明示或暗示的条件、陈述和保证，
 * 包括对适销性、特定用途的适用性或不侵权的任何暗示保证。
 * SUN 及其许可方不对被许可方因使用、修改或分发本软件或其衍生产品而遭受的或与之相关的任何损害或责任承担任何责任。
 * 在任何情况下，SUN 或其许可方均不对因使用或无法使用软件，即使 SUN 已被告知存在此类损害的可能性。
 *
 * 您承认软件并非设计、许可或打算用于任何核设施的设计、建造、操作或维护。
 *
 * 官方连接： https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/sockets/Server.java
 */

package jack.io.demo.ser.case1;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * 这个例子展示了如何使用套接字来发送和接收对象。
 * 该文件包含类Server，它从WriteSocket.java 文件中的WriteSocket 类接收对象。
 * Server 必须首先运行并等待WriteSocket 发送信息。
 *
 * 使用JDK1.1 & JDK1.2编译测试
 */
public class Server {

    /**
     * 创建 serversocket 并使用它的流来接收序列化的对象
     */
    public static void main(String args[]) {

        ServerSocket ser = null;
        Socket soc = null;
        String str = null;
        Date d = null;

        try {
            ser = new ServerSocket(8020);
            /*
             * 这将等待与此套接字建立连接。
             */
            soc = ser.accept();
            InputStream o = soc.getInputStream();
            ObjectInput s = new ObjectInputStream(o);
            str = (String) s.readObject();
            d = (Date) s.readObject();
            s.close();

            // 打印我们刚收到的
            System.out.println(str);
            System.out.println(d);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error during serialization");
            System.exit(1);
        }
    }
}
