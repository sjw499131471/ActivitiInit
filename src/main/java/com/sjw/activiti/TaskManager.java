package com.sjw.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class TaskManager {
    @Test
    public void queryTask(){
        //1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取taskService
        TaskService taskService = processEngine.getTaskService();
//        List<Task> taskList = getTasks(taskService,"eulProcess","张三");
//        List<Task> taskList = getCandidateTasks(taskService);
        List<Task> taskList = getTasks(taskService,"gateway","销售主管");
        //4、输出
        for (Task task : taskList) {
            System.out.println("流程实例id="+task.getProcessInstanceId());
            System.out.println("任务Id="+task.getId());
            System.out.println("任务负责人="+task.getAssignee());
            System.out.println("任务名称="+task.getName());
        }
    }

    @Test
    public void querySingleTask(){
        //1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取taskService
        TaskService taskService = processEngine.getTaskService();
        getSingleTask(taskService,"");
    }

    private List<Task> getCandidateTasks(TaskService taskService) {
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("candidate")
                .taskCandidateUser("王伟") //根据候选人查询任务
                .list();
        if (taskList != null && taskList.size()>0){
            //领任务
            taskService.claim(taskList.get(0).getId(),"王伟");
            //归还并交接任务
            taskService.setAssignee(taskList.get(0).getId(),null);
            taskService.setAssignee(taskList.get(0).getId(),"黎明");
        }
        return taskList;
    }

    private Task getSingleTask(TaskService taskService,String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return task;
    }

    private List<Task> getTasks(TaskService taskService,String processKey,String assignee) {
        //3、根据流程key 和 任务的负责人 查询任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(processKey) //流程Key
                .taskAssignee(assignee)  //要查询的负责人
                .list();
        return taskList;
    }

    //    @Test
    public void finishTask(){
        //获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//获取操作任务的服务 TaskService
        TaskService taskService = processEngine.getTaskService();
//完成任务,参数：任务id,完成zhangsan的任务，10005是上述ACT_RU_TASK表的id
        taskService.complete("10005");

    }
}
