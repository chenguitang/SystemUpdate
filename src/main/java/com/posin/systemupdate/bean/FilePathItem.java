package com.posin.systemupdate.bean;

import android.graphics.Bitmap;
import android.view.View;

import java.io.File;

/**
 * FileName: FilePathItem
 * Author: Greetty
 * Time: 2018/5/29 20:27
 * Desc: TODO
 */
public class FilePathItem {

    private File path;
    private String display;
    private View view = null;
    private Bitmap icon = null;

    public FilePathItem(File path, String display, View view, Bitmap icon) {
        this.path = path;
        this.display = display;
        this.view = view;
        this.icon = icon;
    }

    public FilePathItem() {
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "FilePathItem{" +
                "path=" + path +
                ", display='" + display + '\'' +
                ", view=" + view +
                ", icon=" + icon +
                '}';
    }
}
