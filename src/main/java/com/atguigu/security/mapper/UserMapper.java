package com.atguigu.security.mapper;

import com.atguigu.security.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {

    Integer updatePasswordByUser(User user);

}
