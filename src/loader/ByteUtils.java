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
            /*����λ����--len*8λ����n*(2��((--len)*8)�η�),��Ϊlen=2,���Ը�λ����8λ��n*256*/
            n <<= (--len) * 8;
            /*����λ���λ��ӵõ�ʮ����int��*/
            sum = n + sum;
        }
        return sum;
    }

    public static byte[] int2Bytes(int value,int len){
        byte[] b = new byte[len];
        for(int i = 0;i < len;i++){
            /*value>>8*i��value/2��(8*i)�η����Ż�д����& 0xff �ǰ�λ�룬����256��1����Ϊһ���ֽ�λ8λ����������ʾ255*/
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
        /*��ԴClass�ֽ����0����Ҫ�滻���ֽ����ƫ��λ��offset����ֽ��븴�Ƶ��µ�newBytes[]��λ�ò���*/
        System.arraycopy(srcBytes,0,newBytes,0,offset);
        /*��Ŀ���ֽ��루��Ҫ�滻Ϊ���ֽ��룩���Ƶ�newBytes[]����Ҫ�滻���ֽ����λ��offset��ƥ�䳤��*/
        System.arraycopy(destBytes,0,newBytes,offset,destBytes.length);
        /*��ʣ�಻��Ҫ�滻���ֽ��븴�Ƶ�newBytes�Ķ�Ӧλ��*/
        System.arraycopy(srcBytes,offset+len,newBytes,offset+destBytes.length,srcBytes.length-offset-len);
        return newBytes;
    }
}
