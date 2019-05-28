package loader;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Author: rsq0113
 * Date: 2019-05-28 15:29
 * Description:
 **/
public class HackSystem{
    public static final InputStream in = System.in;
    private static ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    public static final PrintStream out = new PrintStream(buffer);
    public static final PrintStream err = out;
    public static String getBufferString(){
        return buffer.toString();
    }

    public static void clearBuffer(){
        buffer.reset();
    }
    public static void setSecurityManager(final SecurityManager s){
        System.setSecurityManager(s);
    }

    public static long currentTimeMillis(){
        return  System.currentTimeMillis();
    }

}
