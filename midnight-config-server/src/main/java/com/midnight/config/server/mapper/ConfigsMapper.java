package com.midnight.config.server.mapper;

import com.midnight.config.server.model.Configs;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface ConfigsMapper {

    @Insert("insert into configs(app,ns,env,pkey,pval)" +
            " values (#{app},#{ns},#{env},#{pkey},#{pval})")
    int insert(Configs configs);

    @Select("select * from configs where app=#{app} and ns=#{ns} and env=#{env}")
    List<Configs> selectAll(String app, String ns, String env);

    @Select("select * from configs where app=#{app} and ns=#{ns} and env=#{env}" +
            " and pkey=#{pkey}")
    Configs select(String app, String ns, String env, String pkey);

    @Select("update configs set pval=#{pval} where app=#{app} and ns=#{ns} and env=#{env}" +
            " and pkey=#{pkey}")
    Configs update(Configs configs);

}
