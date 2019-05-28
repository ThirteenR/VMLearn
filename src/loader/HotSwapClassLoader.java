package loader;

/**
 * Author: rsq0113
 * Date: 2019-05-28 11:49
 * Description:
 **/
public class HotSwapClassLoader  extends ClassLoader {
    public HotSwapClassLoader(){
        super(HotSwapClassLoader.class.getClassLoader());
    }
    public Class loadByte(byte[] classByte){
        return defineClass(null,classByte,0,classByte.length);
    }
}
