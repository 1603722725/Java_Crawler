package com.webMagic.service;

import com.webMagic.pojo.JobInfo;

import java.util.List;

/**
 * @Author: wl
 * @Date: 2019/10/11 15:49
 */
public interface JobInfoService {
    public void save(JobInfo jobInfo);  //保存工作信息
    public List<JobInfo> findJobInfo(JobInfo jobInfo);

}
