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
            /*根据Class文件中常量表的偏移位置获取常量的标志tag，其长度为1字节
            * 14种常量标志：1、utf8; 3、Integer; 4、Float； 5、Long；6、Double；7、Class; 8、String; 9、Fieldref;
            * 10、Methodref; 11、InterfaceMethodref; 12、NameAndType; 15、MethodHandle; 16、MethodType; 18、InvokeDynamic
            * */
            int tag = ByteUtils.bytes2Int(classByte, offset, U1);
            if (tag == CONSTANT_UTF8_INFO) {
                /*获取Class文件中CONSTANT_Utf8_info常量表的长度，其偏移量为tag偏移量向后1字节，长度为2字节*/
                int len = ByteUtils.bytes2Int(classByte, offset + U1, U2);
                /*偏移量向右移动u1+u2=3个字节*/
                offset += (U1 + U2);
                /*从Class文件中的当前偏移量位置向后读取len长度的字节，即为Utf8常量在Class中的具体值，并转换为字符串*/
                String str = ByteUtils.bytes2String(classByte, offset, len);
                /*比较当前Utf8常量的字符串与源字符串（想要修改的）是否相同；
                * 相同则表示此Utf8常量即为需要修改的常量；
                * 不相同则将偏移量（offset)右移当前Utf8常量的字节数，即len
                * */
                if (str.equalsIgnoreCase(srcStr)) {
                    /*将目标字符串（最终常量修改的值）转化为byte[]*/
                    byte[] strBytes = ByteUtils.string2Bytes(destStr);
                    /**/
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
