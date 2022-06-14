package com.sjw.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class MyTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        if("创建申请".equals(delegateTask.getName()) &&
                "create".equals(delegateTask.getEventName())){
            delegateTask.setAssignee("张三");
        }
    }
}
