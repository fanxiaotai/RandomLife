package com.fyt.rlife.rlife.service.gameService.gameImpl;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.mapper.MonsterMapper;
import com.fyt.rlife.rlife.service.gameService.MonsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: fanyitai
 * @Date: 2020/1/5 15:54
 * @Version 1.0
 */
@Service
public class MonsterServiceImpl implements MonsterService {

    @Autowired
    MonsterMapper monsterMapper;

    @Override
    public Monster getMonsterById(int id) {
        return monsterMapper.selectByPrimaryKey(id);
    }
}
