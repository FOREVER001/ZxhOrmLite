package com.zxh.greendao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zxh.dblib.db.BaseDao;
import com.zxh.dblib.db.OrderDao;
import com.zxh.dblib.factory.BaseDaoFactory;
import com.zxh.greendao.subdb.BaseDaoSubFactory;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String sqlitePath="data/data/com.zxh.greendao/ne.db";
    private int i=0;
    private UserDao mUserDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserDao=BaseDaoFactory.getInstance(sqlitePath).getBaseDao(UserDao.class,User.class);
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
        user.setName("lisi");
        User where=new User();
        where.setId(1);
        baseDao.update(user,where);
    }

    public void delete(View view) {
//        BaseDao<User> baseDao = BaseDaoFactory.getInstance(sqlitePath).getBaseDao(User.class);
//        User where=new User();
//        where.setId(2);
//        baseDao.delete(where);

        OrderDao orderDao = BaseDaoFactory.getInstance(sqlitePath).getBaseDao(OrderDao.class, User.class);
        User where=new User();
//        where.setId(0);
        orderDao.delete(where);
    }

    /**
     * 分库
     * @param view
     */
    public void subDb(View view) {
             Photo photo=new Photo();
             photo.setPath("/data/data/xxx.jpg");
             photo.setTime(new Date().toString());
        PhotoDao photoDao = BaseDaoSubFactory.getInstance(sqlitePath).getBaseDao(PhotoDao.class, Photo.class);
        photoDao.insert(photo);
    }

    /**
     * 登录
     * @param view
     */
    public void login(View view) {
        //模拟服务器返回的信息
      User user=new User();
      user.setName("张三"+(i++));
      user.setId(i);
      //数据插入
        mUserDao.insert(user);
        Toast.makeText(this, "执行成功", Toast.LENGTH_SHORT).show();
    }
}
