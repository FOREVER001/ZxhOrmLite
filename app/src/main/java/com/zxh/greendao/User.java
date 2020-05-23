package com.zxh.greendao;

import com.zxh.dblib.annotation.DBFiled;
import com.zxh.dblib.annotation.DBTable;

@DBTable("tb_user")
public class User {
    private int id;
    @DBFiled("tb_name")
    private String name;

    public User() {
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
