package com.fyt.rlife.rlife.mapper;

import com.fyt.rlife.rlife.bean.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author: fanyitai
 * @Date: 2019/12/9 17:17
 * @Version 1.0
 */
@Repository
public interface UserMapper extends Mapper<User> {
}
