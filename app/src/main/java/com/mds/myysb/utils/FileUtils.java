package com.mds.myysb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    public static void copyToFile(InputStream input, File to){
        File p = to.getParentFile();
        if (!p.exists()){
            p.mkdirs();
        }
        try {
            copyToFile(input,new FileOutputStream(to));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static void copyToFile(InputStream input,String path){
        copyToFile(input,new File(path));
    }
    public static void copyToFile(File from,File to){
        try {
            copyToFile(new FileInputStream(from),to);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void copyToFile(String from ,File to){
        try {
            copyToFile(new FileInputStream(from),to);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void copyToFile(InputStream from , OutputStream to){
        try {
            byte[] data = new byte[1024*512];
            int temp = -1;
            while ((temp=from.read(data))!=-1){
                to.write(data,0,temp);
            }
            from.close();
            to.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
