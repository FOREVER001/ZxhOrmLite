package com.zxh.dblib.db;

/**
 * 数据库操作接口
 * @param <T>
 */
public interface IBaseDao<T> {
    /**
     * 插入
     * @param entity
     * @return
     */
    long insert( T entity);

    /**
     * 更新
     * @return
     */
    long update(T entity,T where);

    /**
     * 删除
     * @param entity
     * @return
     */
    int delete(T entity);
}
