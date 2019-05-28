package loader;

/**
 * Author: rsq0113
 * Date: 2019-05-28 14:38
 * Description:
 **/
public class ClassModifier {
    /*�����ص���ʼƫ��λ��*/
    private static final int CONSTANT_POOL_INDEX = 8;
    /*CONSTANT_Utf8_info������tag��־*/
    private static final int CONSTANT_UTF8_INFO = 1;
    /*��������11�ֳ�����ռ���ȣ���CONSTANT_Utf8_info��ǰ��λΪռλ��������3��ʼ����,1.7֮�������˺�����������*/
    private static final int[] CONSTANT_ITEM_LENGTH = {-1, -1, -1, 5, 5, 9, 9, 3, 3, 5, 5, 5, 5,4,3,5};
    private static final int U1 = 1;
    private static final int U2 = 2;
    private byte[] classByte;

    public ClassModifier(byte[] classByte) {
        this.classByte = classByte;
    }

    public byte[] modifyUTF8Constant(String srcStr, String destStr) {
        int cpc = getConstantPoolCount();
        /*ƫ����Ϊ0x0000000Aʮ����10��λ���ǵ�һ�������ʼ��λ�ã�U2Ϊ�������������������ֽ�*/
        int offset = CONSTANT_POOL_INDEX + U2;
        for (int i = 0; i < cpc; i++) {
            /*����Class�ļ��г������ƫ��λ�û�ȡ�����ı�־tag���䳤��Ϊ1�ֽ�
            * 14�ֳ�����־��1��utf8; 3��Integer; 4��Float�� 5��Long��6��Double��7��Class; 8��String; 9��Fieldref;
            * 10��Methodref; 11��InterfaceMethodref; 12��NameAndType; 15��MethodHandle; 16��MethodType; 18��InvokeDynamic
            * */
            int tag = ByteUtils.bytes2Int(classByte, offset, U1);
            if (tag == CONSTANT_UTF8_INFO) {
                /*��ȡClass�ļ���CONSTANT_Utf8_info������ĳ��ȣ���ƫ����Ϊtagƫ�������1�ֽڣ�����Ϊ2�ֽ�*/
                int len = ByteUtils.bytes2Int(classByte, offset + U1, U2);
                /*ƫ���������ƶ�u1+u2=3���ֽ�*/
                offset += (U1 + U2);
                /*��Class�ļ��еĵ�ǰƫ����λ������ȡlen���ȵ��ֽڣ���ΪUtf8������Class�еľ���ֵ����ת��Ϊ�ַ���*/
                String str = ByteUtils.bytes2String(classByte, offset, len);
                /*�Ƚϵ�ǰUtf8�������ַ�����Դ�ַ�������Ҫ�޸ĵģ��Ƿ���ͬ��
                * ��ͬ���ʾ��Utf8������Ϊ��Ҫ�޸ĵĳ�����
                * ����ͬ��ƫ������offset)���Ƶ�ǰUtf8�������ֽ�������len
                * */
                if (str.equalsIgnoreCase(srcStr)) {
                    /*��Ŀ���ַ��������ճ����޸ĵ�ֵ��ת��Ϊbyte[]*/
                    byte[] strBytes = ByteUtils.string2Bytes(destStr);
                    /*���ַ������ȵ�intֵת��Ϊ��byte[]�洢��������λ����������ʮ������ʾ��= U2ΪClass�ļ��д洢utf8���ͳ������ȵ������ֽڵĳ���*/
                    byte[] strLen = ByteUtils.int2Bytes(destStr.length(), U2);
                    /*��Class�ֽ����еı�ʾ��ǰutf8�����ĳ����滻ΪstrLen*/
                    classByte = ByteUtils.bytesReplace(classByte, offset - U2, U2, strLen);
                    /*��Class�ֽ����е�ǰutf8�����ľ���ֵ�滻ΪstrBytes*/
                    classByte = ByteUtils.bytesReplace(classByte, offset, len, strBytes);
                    return classByte;
                }else{
                    /*��ƫ���������ǰutf8������ֵ*/
                    offset += len;
                }
            } else {
                /*��utf8�������ƫ����offset���ӵ�ǰ��������ռ���ֽڳ���*/
                offset += CONSTANT_ITEM_LENGTH[tag];
            }
        }
        return classByte;
    }

    /*��ȡ�������г���������*/
    private int getConstantPoolCount() {
        return ByteUtils.bytes2Int(classByte, CONSTANT_POOL_INDEX, U2);
    }


}
