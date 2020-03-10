package com.fyt.rlife.rlife.service.gameService.gameImpl;

import com.fyt.rlife.rlife.mapper.BlackMarketMapper;
import com.fyt.rlife.rlife.service.gameService.BlackMarketService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: fanyitai
 * @Date: 2020/1/25 19:46
 * @Version 1.0
 */
@Service
public class BlackMarketServiceImpl implements BlackMarketService {

    @Resource
    BlackMarketMapper blackMarketMapper;
}
