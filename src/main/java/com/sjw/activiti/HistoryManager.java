package com.sjw.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.junit.Test;

import java.util.List;

/**
 * 历史信息
 */
public class HistoryManager {

    @Test
    public void query(){
        //获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
//获取 actinst表的查询对象
        HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
//查询 actinst表，条件：根据 InstanceId 查询
//instanceQuery.processInstanceId("2501");
//查询 actinst表，条件：根据 DefinitionId 查询
        instanceQuery.processDefinitionId("myprocess:2:7504");
//增加排序操作,orderByHistoricActivityInstanceStartTime 根据开始时间排序 asc 升序
        instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
//查询所有内容
        List<HistoricActivityInstance> activityInstanceList = instanceQuery.list();
//输出
        for (HistoricActivityInstance hi : activityInstanceList) {
            System.out.println(hi.getActivityId());
            System.out.println(hi.getActivityName());
            System.out.println(hi.getProcessDefinitionId());
            System.out.println(hi.getProcessInstanceId());
            System.out.println("<==========================>");
        }

    }
}
