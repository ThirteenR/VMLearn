package test;

/**
 * Author: rsq0113
 * Date: 2019-05-24 10:50
 * Description:
 **/
public class TestConstantE extends TestConstant {

    public long b(){
        long i=super.a();
        int s = 3;
        s <<= 3*8;
        return s;

    }

    public static void main(String[] args) {
        TestConstantE testConstantE = new TestConstantE();
        long b = testConstantE.b();
        System.out.println(b);
    }
}
