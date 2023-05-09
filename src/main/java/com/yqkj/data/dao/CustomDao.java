package com.yqkj.data.dao;

import com.yqkj.data.bean.Custom;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CustomDao {
    @Select("select * from custom")
    List<Custom> getAllCustom();
    @Select("select * from ${tableName}")
    List<Custom> getTableAllCustom(@Param("tableName") String tableName);
    @Select("select count(*) from custom")
    Integer countAllCustom();
    @Select("select * from custom where id =#{id}")
    Custom getCustomById(@Param("id") Integer id);
    @Insert("insert into custom(id,username,tel,idcard) values(#{id},#{username},#{tel},#{idcard})")
    int addCustom(Custom custom);
    @Update("update custom set username=#{username},tel=#{tel},idcard=#{idcard} where id=#{id}")
    int updateCustom(Custom custom);
    @Update("update ${tableName} set username=#{username},tel=#{tel},idcard=#{idcard} where id=#{id}")
    int updateTableCustom(@Param("tableName") String tableName, @Param("id") Integer id, @Param("username") String username, @Param("tel") String tel, @Param("idcard") String idcard);
    @Delete("delete from custom where id=#{id}")
    int deleteCustom(@Param("id") Integer id);
}
