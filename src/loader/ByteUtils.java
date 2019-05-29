package loader;

/**
 * Author: rsq0113
 * Date: 2019-05-28 15:01
 * Description:
 **/
public class ByteUtils {

    public static int bytes2Int(byte[] b,int start,int len){
        int sum = 0;
        int end = start + len;
        for(int i = start;i < end;i++){
            int n = ((int)b[i]) & 0xff;
            /*将高位左移--len*8位，即n*(2的((--len)*8)次方),因为len=2,所以高位右移8位，n*256*/
            n <<= (--len) * 8;
            /*将高位与低位相加得到十进制int数*/
            sum = n + sum;
        }
        return sum;
    }

    public static byte[] int2Bytes(int value,int len){
        byte[] b = new byte[len];
        for(int i = 0;i < len;i++){
            /*value>>8*i是value/2的(8*i)次方的优化写法，& 0xff 是按位与，即满256进1，因为一个字节位8位二进制最多表示255*/
            b[len-i-1] = (byte)((value >> 8*i) & 0xff);
        }
        return b;
    }
    public static String bytes2String(byte[] b,int start,int len){
        return new String(b,start,len);
    }

    public static byte[] string2Bytes(String str){
        return str.getBytes();
    }

    public static byte[] bytesReplace(byte[] srcBytes,int offset,int len,byte[] destBytes){
        byte[] newBytes = new byte[srcBytes.length + (destBytes.length - len)];
        /*将源Class字节码从0到需要替换的字节码的偏移位置offset这段字节码复制到新的newBytes[]中位置不变*/
        System.arraycopy(srcBytes,0,newBytes,0,offset);
        /*将目标字节码（需要替换为的字节码）复制到newBytes[]的需要替换的字节码的位置offset，匹配长度*/
        System.arraycopy(destBytes,0,newBytes,offset,destBytes.length);
        /*将剩余不需要替换的字节码复制到newBytes的对应位置*/
        System.arraycopy(srcBytes,offset+len,newBytes,offset+destBytes.length,srcBytes.length-offset-len);
        return newBytes;
    }
}
