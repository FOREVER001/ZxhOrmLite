package com.zxh.greendao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zxh.dblib.db.BaseDao;
import com.zxh.dblib.factory.BaseDaoFactory;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    String sqlitePath="data/data/com.zxh.greendao/ne.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void insert(View view) {

        BaseDao<User> baseDao = BaseDaoFactory.getInstance(sqlitePath).getBaseDao(User.class);
        baseDao.insert(new User(1,"peter"));
        baseDao.insert(new User(2,"pony"));
        baseDao.insert(new User(3,"jack"));
        baseDao.insert(new User(4,"jack"));
        baseDao.insert(new User(5,"jack"));
        baseDao.insert(new User(6,"jack"));
    }

    public void query(View view) {
        BaseDao<User> baseDao = BaseDaoFactory.getInstance(sqlitePath).getBaseDao(User.class);
        User user=new User();
        List<User> query = baseDao.query(user);
        Log.e("====query==size=",query.size()+"");
        for (int i = 0; i < query.size(); i++) {
            Log.e("====query===",query.get(i).toString());
        }
    }

    public void update(View view) {
        BaseDao<User> baseDao = BaseDaoFactory.getInstance(sqlitePath).getBaseDao(User.class);
        User user=new User();
        user.setName("张三");
        User where=new User();
        where.setId(2);
        baseDao.update(user,where);
    }

    public void delete(View view) {
        BaseDao<User> baseDao = BaseDaoFactory.getInstance(sqlitePath).getBaseDao(User.class);
        User where=new User();
        where.setId(2);
        baseDao.delete(where);
    }
}
