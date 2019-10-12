package com.webMagic.Task;


import com.webMagic.pojo.JobInfo;
import com.webMagic.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @Author: wl
 * @Date: 2019/10/12 6:42
 */
@Component
public class SpringDataPipeline implements Pipeline {
    @Autowired
    private JobInfoService jobInfoService;
    @Override
    public void process(ResultItems resultItems, Task task) {
        JobInfo jobInfo = resultItems.get("jobInfo");
        if(jobInfo!=null){
            this.jobInfoService.save(jobInfo);
        }
    }
}
