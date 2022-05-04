/*
 * 官方地址： https://docs.oracle.com/javase/1.5.0/docs/guide/serialization/examples/sockets/Client.java
 */

package jack.io.demo.ser.case1;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * 这个例子展示了如何使用套接字和序列化来发送将被接收的对象（参见类 Server 查看接收部分）
 * <p>
 * 使用JDK1.1 & JDK1.2编译测试
 */
public class Client {

    public static void main(String args[]) {

        try {
            // 创建一个 Socket
            Socket soc = new Socket(InetAddress.getLocalHost(), 8020);

            // 将今天的日期序列化为与Socket关联的输出流
            OutputStream o = soc.getOutputStream();
            ObjectOutput s = new ObjectOutputStream(o);

            s.writeObject("Today's date");
            s.writeObject(new Date());
            s.flush();
            s.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error during serialization");
            System.exit(1);
        }
    }
}
