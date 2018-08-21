package cn.itcast.mybatis.dao;

import java.util.List;

import cn.itcast.mybatis.domain.UserPo;


public interface IUserDao {
    public List<UserPo> list();
    public UserPo get(String id);
    public int insert(UserPo u);
    public int update(UserPo u);
    public int deleteById(String id);
}