package com.example.recoketmq.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import com.example.recoketmq.model.User;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /* User selectUserInfoById(Integer id,String name);
    
    User selectUserInfoById(Map<String, Object> map);*/

    /* String selectUserInfoById(Integer id,String name);*/

    /*  Map<String, Object> selectUserInfoById(Integer id,String name);*/
    @MapKey("id")
    Map<Integer, User> selectUserInfoById(Integer id, String name);
}