package com.fyt.rlife.rlife.mapper;

import com.fyt.rlife.rlife.bean.game.common.GameProp;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author: fanyitai
 * @Date: 2020/1/22 17:44
 * @Version 1.0
 */
@Repository
public interface GamePropMapper extends Mapper<GameProp> {
    List<GameProp> selectPropsByPropIds(String[] propIds);
}
