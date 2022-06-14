package com.sjw.activiti;


import com.sjw.activiti.model.Employee;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程定义查询
 *
 **/
public class ProcessManage {

    @Test
    public void startProcess(){
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到RunService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3.创建流程实例  流程定义的key需要知道
//        normalStart(runtimeService,"myprocess");
//        startWithBusinessId(runtimeService);
//        startWithParams(runtimeService);
//        startCandidate(runtimeService);
//        normalStart(runtimeService,"listener");
        startWithParams4Gateway(runtimeService);
    }

    private void startCandidate(RuntimeService runtimeService) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey( "candidate" );
        //4.输出实例的相关信息
        System.out.println( "流程部署ID" + processInstance.getDeploymentId() );
        System.out.println( "流程定义ID" + processInstance.getProcessDefinitionId() );
        System.out.println( "流程实例ID" + processInstance.getId() );
        System.out.println( "活动ID" + processInstance.getActivityId() );
    }

    private void startWithParams(RuntimeService runtimeService) {
        //设定assignee的值，用来替换uel表达式
        Map<String,Object> assigneeMap = new HashMap<>();
        assigneeMap.put("assignee0","张三");
        assigneeMap.put("assignee1","李经理");
        assigneeMap.put("assignee2","王总经理");
        assigneeMap.put("assignee3","赵财务");
        //启动流程实例
        runtimeService.startProcessInstanceByKey("eulProcess",assigneeMap);
    }
    private void startWithParams4Gateway(RuntimeService runtimeService) {
        Map<String,Object> assigneeMap = new HashMap<>();
        Employee employee = new Employee();
        employee.setDept(1);
        assigneeMap.put("emp",employee);
        //启动流程实例
        runtimeService.startProcessInstanceByKey("gateway",assigneeMap);
    }

    private void startWithBusinessId(RuntimeService runtimeService) {
        //第二个参数：businessKey，业务表的id，就是1001
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myprocess", "1001");
        //4、输出
        System.out.println("businessKey=="+processInstance.getBusinessKey());
    }

    private void normalStart(RuntimeService runtimeService,String key) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey( key );
        //4.输出实例的相关信息
        System.out.println( "流程部署ID" + processInstance.getDeploymentId() );
        System.out.println( "流程定义ID" + processInstance.getProcessDefinitionId() );
        System.out.println( "流程实例ID" + processInstance.getId() );
        System.out.println( "活动ID" + processInstance.getActivityId() );
    }

    //    @Test
    public void queryProcessDefinition() {
        // 流程定义key
        String processDefinitionKey = "myprocess";
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 查询流程定义
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //遍历查询结果
        List<ProcessDefinition> list = processDefinitionQuery
                .processDefinitionKey( processDefinitionKey )
                .orderByProcessDefinitionVersion().desc().list();

        for (ProcessDefinition processDefinition : list) {
            System.out.println( "------------------------" );
            System.out.println( " 流 程 部 署 id ： " + processDefinition.getDeploymentId() );
            System.out.println( "流程定义id： " + processDefinition.getId() );
            System.out.println( "流程定义名称： " + processDefinition.getName() );
            System.out.println( "流程定义key： " + processDefinition.getKey() );
            System.out.println( "流程定义版本： " + processDefinition.getVersion() );
        }
    }

    /**
     * 删除指定流程id的流程
     */
//    @Test
    public void deleteDeployment() {
        // 流程部署id
        String deploymentId = "2501";
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 通过流程引擎获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //删除流程定义， 如果该流程定义已有流程实例启动则删除时出错
        repositoryService.deleteDeployment( deploymentId );
        //设置true 级联删除流程定义，即使该流程有流程实例启动也可以删除，设
        //置为false非级别删除方式，如果流程
        repositoryService.deleteDeployment( deploymentId, true );
    }

    /**
     * 获取资源
     */
//    @Test
    public void getProcessResources() throws IOException {
        // 流程定义id
        String processDefinitionId = "myprocess:1:2504";
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 流程定义对象
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId( processDefinitionId ).singleResult();
        //获取bpmn
        String resource_bpmn = processDefinition.getResourceName();
        //获取png
        String resource_png = processDefinition.getDiagramResourceName();
        // 资源信息
        System.out.println( "bpmn： " + resource_bpmn );
        System.out.println( "png： " + resource_png );
        File file_png = new File( "d:/purchasingflow01.png" );
        File file_bpmn = new File( "d:/purchasingflow01.bpmn" );
        // 输出bpmn
        InputStream resourceAsStream = null;
        resourceAsStream = repositoryService.getResourceAsStream( processDefinition.getDeploymentId(), resource_bpmn );
        FileOutputStream fileOutputStream = new FileOutputStream( file_bpmn );
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read( b, 0, 1024 )) != -1) {
            fileOutputStream.write( b, 0, len );
        }
        // 输出图片
        resourceAsStream = repositoryService.getResourceAsStream( processDefinition.getDeploymentId(), resource_png );
        fileOutputStream = new FileOutputStream( file_png );
        // byte[] b = new byte[1024];
        // int len = -1;
        while ((len = resourceAsStream.read( b, 0, 1024 )) != -1) {
            fileOutputStream.write( b, 0, len );
        }
    }


}

