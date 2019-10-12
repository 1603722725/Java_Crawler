package com.webMagic.dao;

import com.webMagic.pojo.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: wl
 * @Date: 2019/10/11 15:48
 */
public interface JobInfoDao extends JpaRepository<JobInfo,Long> {
}
