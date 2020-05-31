package com.zxh.greendao.subdb;

import com.zxh.dblib.factory.BaseDaoFactory;
import com.zxh.greendao.User;
import com.zxh.greendao.UserDao;

import java.io.File;

/**
 * 用来产生私有库存放的位置
 */
public enum PrivateDatabaseEnums {
    database("");
    private String values;
    PrivateDatabaseEnums(String values){
    }
    public String getValues(String sqlitePath){
        UserDao userDao= BaseDaoFactory.getInstance(sqlitePath).getBaseDao(UserDao.class, User.class);
        if(userDao!=null){
            User currentUser = userDao.getCurrentUser();
            if(currentUser!=null){
                File file=new File("data/data/com.zxh.greendao/");
                if(!file.exists()){
                    file.mkdirs();
                }
                return file.getAbsolutePath()+"/u"+currentUser.getId()+"_private.db";
            }
        }
        return null;
    }
}
