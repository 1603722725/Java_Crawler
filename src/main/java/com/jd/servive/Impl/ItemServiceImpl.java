package com.jd.servive.Impl;

import com.jd.dao.ItemDao;
import com.jd.pojo.Item;

import com.jd.servive.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: wl
 * @Date: 2019/10/8 20:23
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDao itemDao;

    @Override
    @Transactional    //开启事务  提交事务
    public void save(Item item) {
        this.itemDao.save(item);
    }
    @Override
    public List<Item> findAll(Item item) {
        Example<Item> example = Example.of(item);
        List<Item> list = this.itemDao.findAll(example);
        return list;
    }



}
