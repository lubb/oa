package com.lbb.oa.controller.activiti;

import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.runtime.TaskRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRuntime taskRuntime;

    @GetMapping("/list")
    public void getTasks() {
        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0,100));
        List<Task> list=tasks.getContent();
        for(Task tk : list){
            System.out.println("-------------------");
            System.out.println("getId："+ tk.getId());
            System.out.println("getName："+ tk.getName());
            System.out.println("getStatus："+ tk.getStatus());
            System.out.println("getCreatedDate："+ tk.getCreatedDate());
            if(tk.getAssignee() == null){
                //候选人为当前登录用户，null的时候需要前端拾取
                System.out.println("Assignee：待拾取任务");
            }else{
                System.out.println("Assignee："+ tk.getAssignee());
            }

        }



    }
}
