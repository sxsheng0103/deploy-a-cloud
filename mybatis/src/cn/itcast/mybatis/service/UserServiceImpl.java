package cn.itcast.mybatis.service;

import java.util.List;

import cn.itcast.mybatis.dao.IUserDao;
import cn.itcast.mybatis.domain.UserPo;

public class UserServiceImpl implements IUserService {

    private IUserDao userDao;
    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    public int deleteById(String id) {
        int i = userDao.deleteById(id);
                //≤‚ ‘ ¬ŒÒ
        System.out.println(id);
        return i;
    }

    public UserPo get(String id) {
        return userDao.get(id);
    }

    public int insert(UserPo u) {
        return userDao.insert(u);
    }

    public List<UserPo> list() {
        return userDao.list();
    }

    public int update(UserPo u) {
        return userDao.update(u);
    }

}