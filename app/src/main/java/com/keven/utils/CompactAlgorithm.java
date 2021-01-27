package com.keven.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 压缩算法类 * 实现文件压缩，文件夹压缩，以及文件和文件夹的混合压缩
 */
public class CompactAlgorithm {
    private static final String TAG = CompactAlgorithm.class.getSimpleName();

    /**
     * 完成的结果文件--输出的压缩文件
     */
    File targetFile;
    /*
     * 向已有zip文件中添加
     */
    boolean append;

    public CompactAlgorithm(File target, boolean append) {
        targetFile = target;
        this.append = append;
    }

    /**
     * 压缩文件
     */
    public void zipFiles(File srcfile) {
        ZipOutputStream out = null;
        try {
            // first, copy contents from existing
            File tmpZip = null;
            if (targetFile.exists() && append) {
                tmpZip = new File(targetFile.getParent(), "tmp.zip");   //必须另存，因为 new ZipOutputStream 会删掉原来文件
                targetFile.renameTo(tmpZip);
            }
            out = new ZipOutputStream(new FileOutputStream(targetFile));
            if (tmpZip != null) {
                ZipFile war = new ZipFile(tmpZip);
                Enumeration<? extends ZipEntry> entries = war.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry e = entries.nextElement();
                    Log.d(TAG, "copy: " + e.getName() + " , isDirectory=" + e.isDirectory());
                    out.putNextEntry(e);
                    if (!e.isDirectory()) {
                        copy(war.getInputStream(e), out);
                    }
                    out.closeEntry();
                }
                tmpZip.delete();
            }
            // now append some extra content
            if (srcfile.isFile()) {
                zipFile(srcfile, out, "");
            } else {
                File[] list = srcfile.listFiles();
                for (int i = 0; i < list.length; i++) {
                    compress(list[i], out, srcfile.getName() + "/");
                }
            }
            Log.d(TAG, "压缩完毕");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩文件夹里的文件
     * 起初不知道是文件还是文件夹--- 统一调用该方法
     */
    private void compress(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            zipDirectory(file, out, basedir);
        } else {
            zipFile(file, out, basedir);
        }
    }

    /**
     * 压缩单个文件
     */
    private void zipFile(File srcfile, ZipOutputStream out, String basedir) {
        if (!srcfile.exists()) return;
        byte[] buf = new byte[1024];
        FileInputStream in = null;
        try {
            in = new FileInputStream(srcfile);
            out.putNextEntry(new ZipEntry(basedir + srcfile.getName()));
            copy(in, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.closeEntry();
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩文件夹
     */
    private void zipDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists()) return;
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /**
     * 将一个zip文件解压到一个目录
     *
     * @param srcFile    压缩包文件
     * @param destDir    解压位置
     * @param deleteFile 是否删除
     */
    public static void uncompress(File srcFile, File destDir, boolean deleteFile) throws IOException {
        if (!srcFile.exists()) {
            throw new IOException(srcFile.getAbsolutePath() + "FILE_NOT_EXISTS");
        }
        if (destDir.isFile()) {
            throw new IOException("ZIPUTIL_UNZIPPATHCANNOTFILE");
        }
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile);
            Enumeration<?> e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) e.nextElement();
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(destDir, name);
                    f.mkdirs();
                } else {
                    File f = new File(destDir, zipEntry.getName());
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                    InputStream is = null;
                    FileOutputStream fos = null;
                    try {
                        is = zipFile.getInputStream(zipEntry);
                        fos = new FileOutputStream(f);
                        copy(is, fos);
                    } finally {
                        if (is != null) is.close();
                        if (fos != null) fos.close();
                    }
                }
            }
        } finally {
            if (zipFile != null) zipFile.close();
        }
        if (deleteFile) {
            srcFile.deleteOnExit();
        }
    }

    /**
     * copy input to output stream - available in several StreamUtils or Streams classes
     */
    private static void copy(InputStream input, OutputStream output) throws IOException {
        int bytesRead;
        byte[] BUFFER = new byte[4096 * 1024];
        while ((bytesRead = input.read(BUFFER)) != -1) {
            output.write(BUFFER, 0, bytesRead);
        }
    }

    //测试
    public static void main(String[] args) {
        File f = new File("E:/Study/Java");
        new CompactAlgorithm(new File( "D:/test",f.getName()+".zip"), true).zipFiles(f);
    }
}

