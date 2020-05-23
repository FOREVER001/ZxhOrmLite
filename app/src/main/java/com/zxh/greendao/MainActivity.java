package com.zxh.greendao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.zxh.dblib.db.BaseDao;
import com.zxh.dblib.factory.BaseDaoFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void insert(View view) {
       String sqlitePath="data/data/com.zxh.greendao/ne.db";
        BaseDao<User> baseDao = BaseDaoFactory.getInstance(sqlitePath).getBaseDao(User.class);
        baseDao.insert(new User(1,"peter"));
        baseDao.insert(new User(2,"pony"));
        baseDao.insert(new User(3,"jack"));
    }
}
