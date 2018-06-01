package com.posin.systemupdate.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.posin.systemupdate.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * FileName: UDiskUtils
 * Author: Greetty
 * Time: 2018/6/1 19:29
 * Desc: TODO
 */
public class UDiskUtils {

    public static HashMap<String, File> initRootPath(Context context) throws IOException {
        HashMap<String, File> ROOT_PATHS = new HashMap<String, File>();

        MiniposInfo.load();

        String pl = MiniposInfo.getPlatform();
        if (pl != null && pl.contains("Freescale")) {

            ROOT_PATHS.put(context.getResources().getString(R.string.str_internal_storage),
                    new File("/mnt/sdcard"));
            ROOT_PATHS.put(context.getResources().getString(R.string.str_external_storage),
                    new File("/mnt/extsd"));
            ROOT_PATHS.put(context.getResources().getString(R.string.str_usb_storage),
                    new File("/mnt/udisk"));

        } else if (pl != null && (pl.contains("RK30") || pl.contains("Rockchip RK3128"))) {

            ROOT_PATHS.put(context.getResources().getString(R.string.str_internal_storage),
                    new File("/mnt/sdcard"));
            ROOT_PATHS.put(context.getResources().getString(R.string.str_external_storage),
                    new File("/mnt/external_sd"));
            ROOT_PATHS.put(context.getResources().getString(R.string.str_usb_storage),
                    new File("/mnt/usb_storage"));

        } else if (Build.VERSION.SDK_INT >= 21) {

            ROOT_PATHS.put(context.getResources().getString(R.string.str_internal_storage),
                    new File("/mnt/sdcard"));

            genSystemUid();

            File f = new File("/mnt/media_rw");
            File[] flst = f.listFiles();
            if (flst != null) {
                for (File ff : flst) {
                    Log.d(TAG, "storage : " + ff.getAbsolutePath());
                    ROOT_PATHS.put(context.getResources().getString(R.string.str_other_external_storage) + ff.getName(), ff);
                }
            } else {
                Log.d(TAG, "null storage.");
                return null;
            }

        } else {
            throw new IOException("unsupported platform : " + pl);
        }
        return ROOT_PATHS;
    }


    private static void genSystemUid() {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream("/dev/setuid");
            fos.write("1000 1000".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
