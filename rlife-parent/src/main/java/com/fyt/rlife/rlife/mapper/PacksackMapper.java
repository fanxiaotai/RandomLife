package com.fyt.rlife.rlife.mapper;

import com.fyt.rlife.rlife.bean.game.common.Packsack;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author: fanyitai
 * @Date: 2020/1/8 21:36
 * @Version 1.0
 */
@Repository
public interface PacksackMapper extends Mapper<Packsack> {
    Packsack selectPacksackByRoleId(String roleId);

    void deleteByRoleIdOrPropId(@Param("packId") String packId, @Param("propId")String propId);

    void updateByRoleIdOrPropIdLess(@Param("packId") String packId, @Param("propId")String propId);

    void updateByRoleIdOrPropIdUP(@Param("packId") String packId, @Param("propId")String propId);

    void instProp(@Param("packId") String packId, @Param("propId")String propId);

    Packsack selectUserPacksackByUserId(String userId);
}
