package com.webMagic.service.Impl;

import com.webMagic.dao.JobInfoDao;
import com.webMagic.pojo.JobInfo;
import com.webMagic.service.JobInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: wl
 * @Date: 2019/10/11 15:51
 */
@Service
public class JobInfoServiceImpl implements JobInfoService {
    @Autowired
    private JobInfoDao jobInfoDao;
    @Override
    @Transactional
    public void save(JobInfo jobInfo) {
        //判断数据库是否有已存在的数据  存在 更新  不存在 添加
        JobInfo param = new JobInfo();
        param.setUrl(jobInfo.getUrl());
        param.setTime(jobInfo.getTime());
        //查询
        List<JobInfo> list = this.findJobInfo(param);
        //判断查询结果是否为空
        if(list.size()==0){
            this.jobInfoDao.saveAndFlush(jobInfo);
        }
    }

    @Override
    public List<JobInfo> findJobInfo(JobInfo jobInfo) {
        //设置查询条件
        Example example = Example.of(jobInfo);
        //执行查询
        List list = this.jobInfoDao.findAll();
        return list;
    }














}
