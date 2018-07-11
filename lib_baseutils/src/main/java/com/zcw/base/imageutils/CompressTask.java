package com.zcw.base.imageutils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by 朱城委 on 2017/10/16.<br><br>
 * 异步压缩图片<br>
 * {@link #execute(Object[])}需要传入一个参数，标示图片要压缩到多少kb以内。
 */

public class CompressTask extends AsyncTask<Integer, Void, String> {
    private Context context;

    private List<String> imagePaths;
    private ProgressDialog dialog;

    public CompressTask(Context context, List<String> paths) {
        this.context = context;
        imagePaths = paths;

        initDialog();
    }

    private void initDialog() {
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("正在压缩图片...");
    }

    @Override
    protected String doInBackground(Integer... params) {
        if(params[0] < 0) {
            throw new IllegalArgumentException("参数不能小于0");
        }

        for(String path : imagePaths) {
            ImageCompress.imageCompress(path, params[0]);
        }
        return "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
    }
}
