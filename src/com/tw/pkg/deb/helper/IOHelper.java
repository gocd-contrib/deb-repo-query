package com.tw.pkg.deb.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.GZIPInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

public class IOHelper {
    public static void fetchFile(String url, String filePath) throws Exception {
        URL urlObj = new URL(url);
        if (urlObj.getProtocol().equals("file")) {
            FileUtils.copyFile(new File(urlObj.getFile()), new File(filePath));
            return;
        }

        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        if (urlObj.getUserInfo() != null && !urlObj.getUserInfo().isEmpty()){
        	String encoding = Base64.encodeBase64String(urlObj.getUserInfo().getBytes());
        	connection.setRequestProperty("Authorization", "Basic " + encoding);
        }
        ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        rbc.close();
        fos.close();
    }

    public static void gunzip(String inputGzipFile, String outputFile) throws Exception {
        byte[] buffer = new byte[1024];
        GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(inputGzipFile));
        FileOutputStream out = new FileOutputStream(outputFile);

        int len;
        while ((len = gzis.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }

        gzis.close();
        out.close();
    }

    public static FileLock getLockOnFile(String filePath) throws Exception {
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        FileLock lock = file.getChannel().lock();
        return lock;
    }

    public static void releaseFileLock(FileLock lock) throws Exception {
        if (lock != null) {
            lock.release();
        }
    }
}
