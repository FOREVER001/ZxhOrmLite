package com.zxh.dblib.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.zxh.dblib.annotation.DBFiled;
import com.zxh.dblib.annotation.DBTable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BaseDao<T> implements IBaseDao<T> {
    //持有数据库操作的引用
    private SQLiteDatabase mSQLiteDatabase;
    //表名字
    private String tabName;
    //操作数据库对应的java类型
    private Class<T> entityClass;
    //用来标识，是否已经做过初始化
    private boolean isInit = false;
    //定义一个缓存空间(key是字段名，value是成员变量对应的Filed)
    private HashMap<String,Field> cacheMap;

    public boolean init(SQLiteDatabase sqLiteDatabase, Class<T> entityClass) {
        this.entityClass = entityClass;
        this.mSQLiteDatabase = sqLiteDatabase;
        if (!isInit) {
            //根据传入的class 进行数据表的创建 本例中对应的是User对象
            DBTable dt = entityClass.getAnnotation(DBTable.class);
            if (dt != null && !"".equals(dt.value())) {
                tabName = dt.value();
            } else {
                tabName = entityClass.getName();
            }
            if(!mSQLiteDatabase.isOpen()){
                return false;
            }
            String createTableSql = getCreateTableSql();
            mSQLiteDatabase.execSQL(createTableSql);
            cacheMap=new HashMap<>();
            initCachMap();
            isInit = true;
        }
        return isInit;
    }

    private void initCachMap() {
        //取得所以的列名
        String sql="select * from "+tabName+" limit 1,0";//将表的结构取出来
        Cursor cursor=mSQLiteDatabase.rawQuery(sql,null);
        String[] columnNames = cursor.getColumnNames();
        //获取所有的成员变量
        Field[] columnFields = entityClass.getDeclaredFields();
        for (Field columnField : columnFields) {
            columnField.setAccessible(true);
        }
        for (String columnName : columnNames) {
            Field columField=null;
            for (Field field : columnFields) {
                String fieldName=null;
                if(field.getAnnotation(DBFiled.class)!=null){
                    fieldName=field.getAnnotation(DBFiled.class).value();
                }else {
                    fieldName=field.getName();
                }
                if(columnName.equals(fieldName)){
                    columField=field;
                    break;
                }
            }
            if(columField!=null){
                cacheMap.put(columnName,columField);
            }
        }
    }

    private String getCreateTableSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists ");
        sb.append(tabName + "(");
        //反射得到所以的成员变量
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class type = field.getType();
            DBFiled dbField = field.getAnnotation(DBFiled.class);
            if (dbField != null && !"".equals(dbField.value())) {
                if (type == String.class) {
                    sb.append(dbField.value() + " TEXT,");
                } else if (type == Integer.class) {
                    sb.append(dbField.value() + " INTEGER,");
                } else if (type == Long.class) {
                    sb.append(dbField.value() + " BIGINT,");
                } else if (type == Double.class) {
                    sb.append(dbField.value() + " DOUBLE,");
                } else if (type == byte[].class) {
                    sb.append(dbField.value() + " BLOB,");
                } else {
                    continue;
                }
            } else {
                if (type == String.class) {
                    sb.append(field.getName() + " TEXT,");
                } else if (type == Integer.class) {
                    sb.append(field.getName() + " INTEGER,");
                } else if (type == Long.class) {
                    sb.append(field.getName() + " BIGINT,");
                } else if (type == Double.class) {
                    sb.append(field.getName() + " DOUBLE,");
                } else if (type == byte[].class) {
                    sb.append(field.getName() + " BLOB,");
                } else {
                    continue;
                }
            }
        }
        if (sb.charAt(sb.length() - 1)==',') {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(")");
        return sb.toString();
    }



    @Override
    public long insert(T entity) {
        //将数据库model实体类对象转换为ContentValues
        Map<String,String> map=getValues(entity);
        ContentValues values=getContentValues(map);
        return mSQLiteDatabase.insert(tabName,null,values);

    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues=new ContentValues();
        Set<String> keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            String value = map.get(key);
            if(value!=null){
                contentValues.put(key,value);
            }
        }
        return contentValues;
    }

    private Map<String, String> getValues(T entity) {
        HashMap<String,String> map=new HashMap<>();
        //得到所有的成员变量，user的成员变量
        Iterator<Field> fieldIterator= cacheMap.values().iterator();
        while (fieldIterator.hasNext()){
            Field field = fieldIterator.next();
            field.setAccessible(true);
            //获取成员变量的值
            try {
                Object obj=field.get(entity);
                if(obj==null){
                    continue;
                }
                String value=obj.toString();
                //获取列名
                String key=null;
                DBFiled dbFiled = field.getAnnotation(DBFiled.class);
                if(dbFiled!=null &&!"".equals(dbFiled.value())){
                    key=dbFiled.value();
                }else {
                    key=field.getName();
                }
                if(!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)){
                    map.put(key,value);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public long update(T entity, T where) {
        return 0;
    }

    @Override
    public int delete(T entity) {
        return 0;
    }
}
