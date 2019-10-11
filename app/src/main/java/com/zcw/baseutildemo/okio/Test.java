package com.zcw.baseutildemo.okio;

import com.zcw.base.FileUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSource;
import okio.Okio;

/**
 * Created by 朱城委 on 2019/9/6.<br><br>
 */
public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        readString(new FileInputStream("README.md"));
    }

    public static void readString(InputStream inputStream) {
        BufferedSource source = null;
        try {
            source = Okio.buffer(Okio.source(inputStream));
            String s = source.readUtf8();
            System.out.println(s);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            FileUtils.closeStream(source);
        }
    }
}
