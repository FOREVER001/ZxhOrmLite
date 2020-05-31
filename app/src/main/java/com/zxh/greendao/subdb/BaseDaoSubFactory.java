package com.zxh.greendao.subdb;
import android.database.sqlite.SQLiteDatabase;
import com.zxh.dblib.db.BaseDao;
import com.zxh.dblib.factory.BaseDaoFactory;

public class BaseDaoSubFactory extends BaseDaoFactory {
    private static  BaseDaoSubFactory instance = null;
    protected SQLiteDatabase subSqliteDatabase;//定义一个用户实现分库的数据库对象
    private String sqlitePath;//公共库存放的位置
    protected BaseDaoSubFactory(String sqlitePath) {
        super(sqlitePath);
        this.sqlitePath=sqlitePath;
    }

    public static BaseDaoSubFactory getInstance(String sqlitePath) {
        if (instance == null) {
            synchronized (BaseDaoSubFactory.class) {
                if (instance == null) {
                    instance = new BaseDaoSubFactory(sqlitePath);
                }

            }
        }
        return instance;
    }

    public <T extends BaseDao<M>,M> T getBaseDao(Class<T> daoClass, Class<M> entityClass){
        BaseDao baseDao=null;
        if(map.get(PrivateDatabaseEnums.database.getValues(sqlitePath))!=null){
            return (T) map.get(PrivateDatabaseEnums.database.getValues(sqlitePath)); }
        try {
            baseDao=daoClass.newInstance();
            subSqliteDatabase= SQLiteDatabase.openOrCreateDatabase(PrivateDatabaseEnums.database.getValues(sqlitePath),null);
            baseDao.init(subSqliteDatabase,entityClass);
            map.put(PrivateDatabaseEnums.database.getValues(sqlitePath),baseDao);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }
}
