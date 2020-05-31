package com.zxh.greendao;

import com.zxh.dblib.annotation.DBTable;

@DBTable("tb_photo")
public class Photo {
    private String time;
    private String path;

    public String getTime() {
        return time == null ? "" : time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path == null ? "" : path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
