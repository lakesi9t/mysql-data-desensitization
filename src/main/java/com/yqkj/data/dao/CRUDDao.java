package com.yqkj.data.dao;

import com.yqkj.data.bean.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CRUDDao{
    @Select("select * from user")
    List<User> getAllUser();
    @Select("select * from ${tableName}")
    List<User> getTableAllUser(@Param("tableName") String tableName);
    @Select("select count(*) from user")
    Integer countAllUser();
    @Select("select * from user where id =#{id}")
    User getUserById(@Param("id") Integer id);
    @Insert("insert into user(id,username,password) values(#{id},#{username},#{password})")
    int addUser(User user);
    @Update("update user set username=#{username},password=#{password} where id=#{id}")
    int updateUser(User user);
    @Update("update ${tableName} set username=#{username},password=#{password} where id=#{id}")
    int updateTableCustom(@Param("tableName") String tableName, @Param("id") Integer id, @Param("username") String username, @Param("password") String password);
    @Delete("delete from user where id=#{id}")
    int deleteUser(@Param("id") Integer id);
}
