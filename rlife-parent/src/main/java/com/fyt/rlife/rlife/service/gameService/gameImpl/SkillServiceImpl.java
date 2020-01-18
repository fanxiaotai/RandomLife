package com.fyt.rlife.rlife.service.gameService.gameImpl;

import com.fyt.rlife.rlife.mapper.SkillMapper;
import com.fyt.rlife.rlife.service.gameService.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: fanyitai
 * @Date: 2020/1/16 19:48
 * @Version 1.0
 */
@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    SkillMapper skillMapper;
}
