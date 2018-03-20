package com.nettyrpc.io.netty.tcp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtils {

    public static byte[] gzip(byte[] val)throws IOException {

        ByteArrayOutputStream bos = newByteArrayOutputStream(val.length);

        GZIPOutputStream gos = null;

        try {

            gos = new GZIPOutputStream(bos);

            gos.write(val, 0, val.length);

            gos.finish();

            gos.flush();

            bos.flush();

            val = bos.toByteArray();

        } finally {

            if (gos != null)

                gos.close();

            if (bos != null)

                bos.close();

        }

        return val;

    }

 

    public static byte[] unGzip(byte[] buf)throws IOException {

        GZIPInputStream gzi = null;

        ByteArrayOutputStream bos = null;

        try {

            gzi = new GZIPInputStream(newByteArrayInputStream(buf));

            bos = newByteArrayOutputStream(buf.length);

            int count = 0;

            byte[] tmp = new byte[2048];

            while ((count = gzi.read(tmp)) !=-1) {

                bos.write(tmp, 0, count);

            }

            buf = bos.toByteArray();

        } finally {

            if (bos != null) {

                bos.flush();

                bos.close();

            }

            if (gzi != null)

                gzi.close();

        }

        return buf;

    }

}