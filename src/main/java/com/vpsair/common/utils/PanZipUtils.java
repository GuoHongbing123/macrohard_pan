package com.vpsair.common.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PanZipUtils {
    private static final int numOfEncAndDec = 0x99;
    private static int dataOfFile = 0;

    private PanZipUtils(){
    }

    public static void doCompress(String srcFile,String zipFile)throws IOException {
        doCompress(new File(srcFile),new File(zipFile));
    }

    public static void doCompress(File srcFile,File zipFile)throws IOException {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));
            doCompress(srcFile,out);
        }catch (Exception e){
            throw e;
        }finally {
            out.close();
        }
    }

    public static void doCompress(String fileName,ZipOutputStream out)throws IOException{
        File file = new File(fileName);
        File file1 = new File(file.getName());
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file1));
        while ((dataOfFile = input.read())> -1){
            output.write(dataOfFile^numOfEncAndDec);
        }
        input.close();
        output.flush();
        output.close();
        doCompress(file1,out);
        file1.delete();
    }

    public static void doCompressEver(String fileName,ZipOutputStream out)throws IOException{
        File file = new File(fileName);
        doCompress(file,out);
        file.delete();
    }

    public static void doCompress(File file,ZipOutputStream out)throws IOException{
        doCompress(file,out,"");
    }

    public static void doCompress(File inFile,ZipOutputStream out,String dir)throws IOException{
        if (inFile.isDirectory()){
            File[] files = inFile.listFiles();
            if (files != null && files.length > 0){
                for (File file:files){
                    String name = inFile.getName();
                    if (!"".equals(dir)){
                        name = dir + "/" + name;
                    }
                    PanZipUtils .doCompress(file,out,name);
                }
            }
        }else {
            PanZipUtils.doZip(inFile,out,dir, "123");
        }
    }

    public static void doZip(File inFile,ZipOutputStream out,String dir,String fileName)throws IOException{
        String entryName = null;
        if (!"".equals(dir)){
            entryName = dir + "/" + fileName;
        }else {
            entryName = fileName;
        }
        ZipEntry entry = new ZipEntry(entryName);
        out.putNextEntry(entry);

        int len = 0;
        byte [] buffer = new byte[1024];
        FileInputStream fis = new FileInputStream(inFile);
        while ((len = fis.read(buffer)) > 0){
            out.write(buffer,0,len);
            out.flush();
        }
        out.closeEntry();
        fis.close();
    }


}
