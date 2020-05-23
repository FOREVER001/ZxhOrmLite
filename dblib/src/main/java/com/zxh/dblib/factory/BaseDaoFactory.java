package com.zxh.dblib.factory;

import android.database.sqlite.SQLiteDatabase;

import com.zxh.dblib.db.BaseDao;

public class BaseDaoFactory {
    private static BaseDaoFactory instance;
    private SQLiteDatabase sqLiteDataBase;
    private String sqlitePath;
    private BaseDaoFactory(String sqlitePath){
        this.sqlitePath=sqlitePath;

        sqLiteDataBase=SQLiteDatabase.openOrCreateDatabase(sqlitePath,null);
    }
    public static BaseDaoFactory getInstance(String sqlitePath){
        if(instance==null){
            synchronized (BaseDaoFactory.class){
                if(instance==null){
                    instance=new BaseDaoFactory(sqlitePath);
                }
            }
        }
        return instance;
    }

    //生成BaseDao
    public <T>BaseDao<T> getBaseDao(Class<T> entityClass){
        BaseDao baseDao=null;
        try {
            baseDao=BaseDao.class.newInstance();
            baseDao.init(sqLiteDataBase,entityClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return baseDao;
    }

}
