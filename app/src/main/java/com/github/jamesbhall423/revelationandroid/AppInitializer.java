package com.github.jamesbhall423.revelationandroid;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AppInitializer {
    private static String ASSET_PATH = "maps/";
    public static void run(Context context) {
        AssetManager manager = context.getAssets();
        File toDir = context.getFilesDir();
        if (toDir.exists()) {
            System.out.println("toDir exists");
            System.out.println(toDir.delete());
        }
        if (!toDir.exists()) {
            System.out.println("toDir does not exist");
            toDir.mkdir();
        }
        System.out.println("Dir Created");
        if (toDir.list().length==0) copyAssetFolder(manager,ASSET_PATH,toDir.getPath());
    }

    private static boolean copyAssetFolder(AssetManager assetManager,
                                           String fromAssetPath, String toPath) {
        System.out.println("Copying folder "+fromAssetPath+" "+toPath);
        try {
            String[] files = assetManager.list(fromAssetPath);
            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files)
                if (file.contains(".")) {
                    System.out.println("Copying file "+file);
                    res &= copyAsset(assetManager,
                            fromAssetPath + file,
                            toPath + "/" + file);
                }

                else {
                    System.out.println("Copying dir "+file);
                    res &= copyAssetFolder(assetManager,
                            fromAssetPath +file+"/",
                            toPath + "/" + file);
                }

            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean copyAsset(AssetManager assetManager,
                                     String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        System.out.println("path "+fromAssetPath);
        System.out.println("To path "+toPath);
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
