package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Author: rsq0113
 * Date: 2019-05-21 8:39
 * Description:
 **/
public abstract class GzipUtiils {
    private static final int BUFFER_LEN = 1024;
    private static final String END = ".gz";
    public static boolean compress(File file) throws  IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(new FileOutputStream(new File(file.getAbsolutePath() + END)));
        int len;
        byte[] buf = new byte[BUFFER_LEN];
        while ((len = fileInputStream.read(buf,0,buf.length)) != -1){
            gzipOutputStream.write(buf,0,len);
        }
        fileInputStream.close();
        fileInputStream.close();
        return false;
    }

    public static void decompress(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
        String outFileName = file.getAbsolutePath().replace(END, "");
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(new FileOutputStream(outFileName));
        int len;
        byte[] buf = new byte[BUFFER_LEN];
        while ((len = gzipInputStream.read(buf,0,buf.length)) != -1){
            gzipOutputStream.write(buf,0,len);
        }
        gzipInputStream.close();
        fileInputStream.close();
    }
}
