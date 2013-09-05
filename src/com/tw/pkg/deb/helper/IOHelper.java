package com.tw.pkg.deb.helper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.GZIPInputStream;

public class IOHelper {
    public static void fetchFile(String url, String filePath) throws Exception {
        URL urlObj = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(urlObj.openStream());
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
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
}
