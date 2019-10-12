package com.jd.servive;

import com.jd.pojo.Item;

import java.util.List;

/**
 * @Author: wl
 * @Date: 2019/10/8 20:21
 */
public interface ItemService {
    public void save(Item item);  //保存商品
    public List<Item> findAll(Item item);  //根据条件查询商品




}
