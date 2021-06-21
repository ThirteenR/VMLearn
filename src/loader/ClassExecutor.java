package loader;

import java.lang.reflect.Method;

/**
 * Author: rsq0113
 * Date: 2019-05-28 15:39
 * Description:
 **/
public class ClassExecutor {
    public static String execute(byte[] classByte){
        HackSystem.clearBuffer();
        ClassModifier classModifier = new ClassModifier(classByte);
        byte[] bytes = classModifier.modifyUTF8Constant("java/lang/System", "loader/HackSystem");
        HotSwapClassLoader hotSwapClassLoader = new HotSwapClassLoader();
        Class aClass = hotSwapClassLoader.loadByte(bytes);
        try {
            Method main = aClass.getMethod("main", new Class[]{String[].class});
            main.invoke(null,new Object[]{null});
        } catch (Exception e) {
            e.printStackTrace(HackSystem.out);
        }
        return HackSystem.getBufferString();
    }
}
