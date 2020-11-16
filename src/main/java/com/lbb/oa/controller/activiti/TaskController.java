package com.lbb.oa.controller.activiti;

import com.alibaba.fastjson.JSONObject;
import com.lbb.oa.util.ResponseBean;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private ProcessRuntime processRuntime;

    /**
     * 获取我的代办任务
     * @return
     */
    @PostMapping(value = "/getTasks")
    public ResponseBean getTasks(@RequestBody JSONObject jsonObject) {
        try {
            Integer pageSize = jsonObject.getInteger("pageSize");
            Integer pageNum = jsonObject.getInteger("pageNum");
            Page<Task> tasks = taskRuntime.tasks(Pageable.of(pageNum-1, pageSize));
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            for (Task tk : tasks.getContent()) {
                ProcessInstance processInstance = processRuntime.processInstance(tk.getProcessInstanceId());
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", tk.getId());
                hashMap.put("name", tk.getName());
                hashMap.put("status", tk.getStatus());
                hashMap.put("createdDate", tk.getCreatedDate());
                if (tk.getAssignee() == null) {//执行人，null时前台显示未拾取
                    hashMap.put("assignee", "待拾取任务");
                } else {
                    hashMap.put("assignee", tk.getAssignee());//
                }
                hashMap.put("instanceName", processInstance.getName());
                listMap.add(hashMap);
            }
            return ResponseBean.success(listMap, "获取我的代办任务成功");
        } catch (Exception e) {
            return ResponseBean.error("获取我的代办任务失败");
        }
    }

    /**
     * 完成待办任务
     * @param taskID
     * @return
     */
    @GetMapping(value = "/completeTask")
    public ResponseBean completeTask(@RequestParam("taskID") String taskID) {
        try {
            Task task = taskRuntime.task(taskID);
            if (task.getAssignee() == null) {
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            }
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId())
                    //.withVariable("num", "2")//执行环节设置变量
                    .build());
            return ResponseBean.success(task.getId(), "完成待办任务成功");
        } catch (Exception e) {
            return ResponseBean.error("完成待办任务失败");
        }
    }
}
