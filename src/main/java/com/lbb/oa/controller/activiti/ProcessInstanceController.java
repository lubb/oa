package com.lbb.oa.controller.activiti;

import com.lbb.oa.util.ResponseBean;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/processInstance")
public class ProcessInstanceController {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 获取流程实例
     *
     * @return
     */
    @GetMapping(value = "/getInstances")
    public ResponseBean getInstances() {
        Page<ProcessInstance> processInstances = null;
        try {
            processInstances = processRuntime.processInstances(Pageable.of(0, 50));
            List<ProcessInstance> list = processInstances.getContent();
            list.sort((y, x) -> x.getStartDate().toString().compareTo(y.getStartDate().toString()));
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            for (ProcessInstance pi : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", pi.getId());
                hashMap.put("name", pi.getName());
                hashMap.put("status", pi.getStatus());
                hashMap.put("processDefinitionId", pi.getProcessDefinitionId());
                hashMap.put("processDefinitionKey", pi.getProcessDefinitionKey());
                hashMap.put("startDate", pi.getStartDate());
                hashMap.put("processDefinitionVersion", pi.getProcessDefinitionVersion());
                //因为processRuntime.processDefinition("流程部署ID")查询的结果没有部署流程与部署ID，所以用repositoryService查询
                ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(pi.getProcessDefinitionId())
                        .singleResult();
                hashMap.put("resourceName", pd.getResourceName());
                hashMap.put("deploymentId", pd.getDeploymentId());
                listMap.add(hashMap);
            }
            return ResponseBean.success(listMap, "获取流程实例列表成功");
        } catch (Exception e) {
            return ResponseBean.error("获取流程实例流程失败");
        }


    }

    /**
     * 启动流程实例
     *
     * @param processDefinitionKey
     * @param instanceName
     * @param instanceVariable
     * @return
     */
    @GetMapping(value = "/startProcess")
    public ResponseBean startProcess(@RequestParam("processDefinitionKey") String processDefinitionKey,
                                     @RequestParam("instanceName") String instanceName,
                                     @RequestParam("instanceVariable") String instanceVariable) {
        try {
            ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                    .start()
                    .withProcessDefinitionKey(processDefinitionKey)
                    .withName(instanceName)
                    //.withVariable("content", instanceVariable)
                    //.withVariable("参数2", "参数2的值")
                    .withBusinessKey("自定义BusinessKey")
                    .build());
            return ResponseBean.success(processInstance, "启动流程实例成功");
        } catch (Exception e) {
            return ResponseBean.error("启动流程实例失败");
        }
    }

    /**
     * 删除流程实例
     *
     * @param instanceID
     * @return
     */
    @GetMapping(value = "/deleteInstance")
    public ResponseBean deleteInstance(@RequestParam("instanceID") String instanceID) {
        try {
            ProcessInstance processInstance = processRuntime.delete(ProcessPayloadBuilder
                    .delete()
                    .withProcessInstanceId(instanceID)
                    .build()
            );
            return ResponseBean.success(processInstance, "删除流程实例成功");
        } catch (Exception e) {
            return ResponseBean.error("删除流程实例失败");
        }

    }

    /**
     * 挂起流程实例
     * @param instanceID
     * @return
     */
    @GetMapping(value = "/suspendInstance")
    public ResponseBean suspendInstance(@RequestParam("instanceID") String instanceID) {
        try {
            ProcessInstance processInstance = processRuntime.suspend(ProcessPayloadBuilder
                    .suspend()
                    .withProcessInstanceId(instanceID)
                    .build()
            );
            return ResponseBean.success(processInstance, "挂起流程实例成功");
        } catch (Exception e) {
            return ResponseBean.error("挂起流程实例失败");
        }
    }

    /**
     * 激活流程实例
     * @param instanceID
     * @return
     */
    @GetMapping(value = "/resumeInstance")
    public ResponseBean resumeInstance(@RequestParam("instanceID") String instanceID) {
        try {
            ProcessInstance processInstance = processRuntime.resume(ProcessPayloadBuilder
                    .resume()
                    .withProcessInstanceId(instanceID)
                    .build()
            );
            return ResponseBean.success(processInstance, "激活流程实例成功");
        } catch (Exception e) {
            return ResponseBean.error("激活流程实例失败");
        }
    }
}
