package test;


import loader.ClassExecutor;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: rsq0113
 * Date: 2019-05-15 8:39
 * Description:
 **/
public class ThreadT{

    public  int a()  {
              int i=1;
              return i;
    }
    public void b(){
        int a = a();
        System.out.println(a);

    }

    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("/Users/thirteen/IdeaProjects/VMLearn/out/production/VMLearn/test/TestConstantE.class");
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        fileInputStream.close();
        String execute = ClassExecutor.execute(bytes);
        System.out.println(execute);
    }
}
