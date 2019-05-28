package loader;

/**
 * Author: rsq0113
 * Date: 2019-05-28 14:38
 * Description:
 **/
public class ClassModifier {
    /*常量池的起始偏移*/
    private static final int CONSTANT_POOL_INDEX = 8;
    /*CONSTANT_Utf8_info常量的tag标志*/
    private static final int CONSTANT_UTF8_INFO = 1;
    /*常量池中13中常量所长长度，除CONSTANT_Utf8_info*/
    private static final int[] CONSTANT_ITEM_LENGTH = {-1, -1, -1, 5, 5, 9, 9, 3, 3, 5, 5, 5, 5};
    private static final int U1 = 1;
    private static final int U2 = 2;
    private byte[] classByte;

    public ClassModifier(byte[] classByte) {
        this.classByte = classByte;
    }

    public byte[] modifyUTF8Constant(String srcStr, String destStr) {
        int cpc = getConstantPoolCount();
        int offset = CONSTANT_POOL_INDEX + U2;
        for (int i = 0; i < cpc; i++) {
            int tag = ByteUtils.bytes2Int(classByte, offset, U1);
            if (tag == CONSTANT_UTF8_INFO) {
                int len = ByteUtils.bytes2Int(classByte, offset + U1, U2);
                offset += (U1 + U2);
                String str = ByteUtils.bytes2String(classByte, offset, len);
                if (str.equalsIgnoreCase(srcStr)) {
                    byte[] strBytes = ByteUtils.string2Bytes(destStr);
                    byte[] strLen = ByteUtils.int2Bytes(destStr.length(), U2);
                    classByte = ByteUtils.bytesReplace(classByte, offset - U2, U2, strLen);
                    classByte = ByteUtils.bytesReplace(classByte, offset, len, strBytes);
                    return classByte;
                }else{
                    offset += len;
                }
            } else {
                offset += CONSTANT_ITEM_LENGTH[tag];
            }
        }
        return classByte;
    }

    /*获取常量池中常量的数量*/
    private int getConstantPoolCount() {
        return ByteUtils.bytes2Int(classByte, CONSTANT_POOL_INDEX, U2);
    }


}
