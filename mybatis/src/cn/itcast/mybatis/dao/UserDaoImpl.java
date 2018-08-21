package cn.itcast.mybatis.dao;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import cn.itcast.mybatis.domain.UserPo;

public class UserDaoImpl extends SqlSessionDaoSupport implements IUserDao {
    
    //ɾ��
    public int deleteById(String id) {
        return this.getSqlSession().delete("cn.itcast.mybatis.domain.UserPo.deleteOne",id);
    }

    //����
    public int insert(UserPo u) {
        return this.getSqlSession().insert("cn.itcast.mybatis.domain.UserPo.create", u);
    }

    //�б�
    public List<UserPo> list() {
        return this.getSqlSession().selectList("cn.itcast.mybatis.domain.UserPo.listAll");
    }

    //�޸�
    public int update(UserPo u) {
        return this.getSqlSession().update("cn.itcast.mybatis.domain.UserPo.update",u);
    }

    //��ȡ����
    public UserPo get(String id) {
        return (UserPo) this.getSqlSession().selectOne("cn.itcast.mybatis.domain.UserPo.get", id);
    }

}