package com.jd.dao;

import com.jd.pojo.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: wl
 * @Date: 2019/10/8 20:19
 */
public interface ItemDao extends JpaRepository<Item,Long> {
}
