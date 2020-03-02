package com.fyt.rlife.rlife.service.gameService.gameImpl;

import com.fyt.rlife.rlife.mapper.BlackMarketMapper;
import com.fyt.rlife.rlife.service.gameService.BlackMarketService;

import javax.annotation.Resource;

/**
 * @Author: fanyitai
 * @Date: 2020/1/25 19:46
 * @Version 1.0
 */
public class BlackMarketServiceImpl implements BlackMarketService {

    @Resource
    BlackMarketMapper blackMarketMapper;
}
