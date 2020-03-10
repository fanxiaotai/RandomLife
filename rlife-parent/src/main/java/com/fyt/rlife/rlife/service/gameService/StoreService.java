package com.fyt.rlife.rlife.service.gameService;

import com.fyt.rlife.rlife.bean.Store;

/**
 * @Author: fanyitai
 * @Date: 2020/1/22 18:59
 * @Version 1.0
 */
public interface StoreService {
    Store getStoreByUserId(String memberId);

    void updateStore(Store store, String userId);

    void insertStore(String userId);
}
