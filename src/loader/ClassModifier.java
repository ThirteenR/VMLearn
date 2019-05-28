package loader;

/**
 * Author: rsq0113
 * Date: 2019-05-28 14:38
 * Description:
 **/
public class ClassModifier {
    /*常量池的起始偏移位置*/
    private static final int CONSTANT_POOL_INDEX = 8;
    /*CONSTANT_Utf8_info常量的tag标志*/
    private static final int CONSTANT_UTF8_INFO = 1;
    /*常量池中11种常量所占长度，除CONSTANT_Utf8_info，前三位为占位，从索引3开始算起,1.7之后新增了后面三个常量*/
    private static final int[] CONSTANT_ITEM_LENGTH = {-1, -1, -1, 5, 5, 9, 9, 3, 3, 5, 5, 5, 5,4,3,5};
    private static final int U1 = 1;
    private static final int U2 = 2;
    private byte[] classByte;

    public ClassModifier(byte[] classByte) {
        this.classByte = classByte;
    }

    public byte[] modifyUTF8Constant(String srcStr, String destStr) {
        int cpc = getConstantPoolCount();
        /*偏移量为0x0000000A十进制10的位置是第一个常量项开始的位置，U2为常量池总条数的两个字节*/
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
                    /*将字符串长度的int值转换为，byte[]存储的两个八位二进制数（十进制显示）= U2为Class文件中存储utf8类型常量长度的两个字节的长度*/
                    byte[] strLen = ByteUtils.int2Bytes(destStr.length(), U2);
                    /*将Class字节码中的表示当前utf8常量的长度替换为strLen*/
                    classByte = ByteUtils.bytesReplace(classByte, offset - U2, U2, strLen);
                    /*将Class字节码中当前utf8常量的具体值替换为strBytes*/
                    classByte = ByteUtils.bytesReplace(classByte, offset, len, strBytes);
                    return classByte;
                }else{
                    /*将偏移量跨过当前utf8常量的值*/
                    offset += len;
                }
            } else {
                /*非utf8常量项，将偏移量offset增加当前常量项所占的字节长度*/
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
