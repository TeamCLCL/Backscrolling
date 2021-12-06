package com.dao.impl;

import com.dao.Dao;
import com.dao.UserDao;
import com.model.User;

import java.util.List;

/**
 *  用户接口实现类
 */
public class UserDaoImpl extends Dao<User> implements UserDao {
    /**
    public static void main(String[] args) {
        List<User> list1 = new UserDaoImpl().getAllUser();
        for(User user:list1){
            System.out.println(JsonUtil.toJson(user));
        }

        System.out.println(new UserDaoImpl().nameCheck("zhangsan"));
    }
    */

    /**
     * 用户名唯一性验证
     * @param name
     * @return
     */
    @Override
    public long nameCheck(String name) {
        String sql = "select count(*) from t_user where name = ?";
        return getForValue(sql, name);
    }

    /**
     * 邮箱唯一性验证
     * @param email
     * @return
     */
    @Override
    public long emailCheck(String email) {
        String sql = "select count(*) from t_user where email = ?";
        return getForValue(sql, email);
    }

    /**
     * 用户注册
     * @param user
     */
    @Override
    public int logon(User user) {
        String sql = "insert into t_user(name,password,email) values(?,?,?)";
        return update(sql, user.getName(), user.getPassword(), user.getEmail());
    }

    /**
     * 用户登录验证
     * @param user
     */
    @Override
    public User loginCheck(User user, String accountType) {
        if("username".equals(accountType)){
            String sql = "select * from t_user where name = ? and password = ?";
            return getForObj(sql, user.getName(), user.getPassword());
        }else{
            String sql = "select * from t_user where email = ? and password = ?";
            return getForObj(sql, user.getEmail(), user.getPassword());
        }
    }

    /**
     * 用户重置密码
     * @param user
     * @return
     */
    @Override
    public int resetPassword(User user) {
        String sql = "update t_user set password = ? where email = ?";
        return update(sql, user.getPassword(), user.getEmail());
    }

    /**
     * 获取id对应的用户对象
     * @param user_id
     * @return
     */
    @Override
    public User getUser(Integer user_id) {
        String sql = "select * from t_user where id = ?";
        return getForObj(sql, user_id);
    }

    /**
     * 管理员查询所有用户
     * @return
     */
    @Override
    public List<User> getAllUser() {
        String sql = "select * from t_user";
        return getForBeanList(sql);
    }

    /**
     * 收藏资源
     * @param user_id
     * @param resource_id
     */
    @Override
    public boolean collect(Integer user_id, Integer resource_id) {
        String sql1 = "insert into t_user_collect(user_id,resource_id) values(?,?)";
        String sql2 = "update t_resource set collect = collect + 1 where id = ?";
        return update(new String[]{sql1, sql2}, user_id, resource_id);
    }

    /**
     * 移除所收藏的资源
     * @param user_id
     * @param resource_id
     */
    @Override
    public boolean removeCollect(Integer user_id, Integer resource_id) {
        String sql1 = "delete from t_user_collect where user_id = ? and resource_id = ?";
        String sql2 = "update t_resource set collect = collect - 1 where id = ?";
        return update(new String[]{sql1, sql2}, user_id, resource_id);
    }

    /**
     * 用户修改个人信息
     * @param user
     * @return
     */
    @Override
    public int updateMessage(User user, String ...args) {
        if(args.length == 1){
            // 上传图片
            String sql = "update t_user set image = ? where id = ?";
            return update(sql, args[0], user.getId());
        }else{
            // 更新个人信息
            String sql = "update t_user set name = ? , sex = ? , address = ? where id = ?";
            return update(sql, args[0], args[1], args[2], user.getId());
        }
    }
}
