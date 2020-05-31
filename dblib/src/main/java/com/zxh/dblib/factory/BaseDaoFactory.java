package com.zxh.dblib.factory;

import android.database.sqlite.SQLiteDatabase;

import com.zxh.dblib.db.BaseDao;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BaseDaoFactory {
    private static BaseDaoFactory instance;
    private SQLiteDatabase sqLiteDataBase;
    private String sqlitePath;

    //创建数据库连接池，new 容器，只要new一次，下次就不会再创建了， 考虑多线程的问题
    protected Map<String,BaseDao> map= Collections.synchronizedMap(new HashMap<String, BaseDao>());
    protected BaseDaoFactory(String sqlitePath){
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
    public <T extends BaseDao<M>,M> T getBaseDao(Class<T> daoClass, Class<M> entityClass){
        BaseDao baseDao=null;
        if(map.get(daoClass.getSimpleName())!=null){
            return (T) map.get(daoClass.getSimpleName());
        }
        try {
            baseDao=daoClass.newInstance();
            baseDao.init(sqLiteDataBase,entityClass);
            map.put(daoClass.getSimpleName(),baseDao);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }
}
