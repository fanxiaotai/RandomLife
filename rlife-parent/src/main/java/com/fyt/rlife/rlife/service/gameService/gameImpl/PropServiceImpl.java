package com.fyt.rlife.rlife.service.gameService.gameImpl;

import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.mapper.GamePropMapper;
import com.fyt.rlife.rlife.service.gameService.PropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: fanyitai
 * @Date: 2020/1/22 17:33
 * @Version 1.0
 */
@Service
public class PropServiceImpl implements PropService {

    @Autowired
    GamePropMapper gamePropMapper;

    @Override
    public List<GameProp> getPropsByPropIds(String[] propIds) {
        List<GameProp> props = gamePropMapper.selectPropsByPropIds(propIds);

        return props;
    }

    @Override
    public GameProp getPropByPropId(String id) {
        return gamePropMapper.selectByPrimaryKey(id);
    }
}
