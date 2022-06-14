package com.sjw.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

/**
 * 数据库表初始化
 */
public class ActivitiInit {

    /**
     * 方式一
     */
    @Test
    public void GenActivitiTables() {

        // 1.创建ProcessEngineConfiguration对象。第一个参数：配置文件名称；第二个参数：processEngineConfiguration的bean的id
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource( "activiti.cfg.xml", "processEngineConfiguration" );
        // 2.创建ProcessEngine对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        // 3.输出processEngine对象
        System.out.println( processEngine );

    }

    /**
     * 方式二
     */
    @Test
    public void GenActivitiTables2() {
        //条件：1.activiti配置文件名称：activiti.cfg.xml   2.bean的id="processEngineConfiguration"
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println( processEngine );
    }
}
