package com.lbb.oa.controller.activiti;

import com.lbb.oa.util.ResponseBean;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 流程定义控制器
 */
@RestController
@RequestMapping("/processDefinition")
public class ProcessDefinitionController {

    @Autowired
    private RepositoryService repositoryService;

    @PostMapping(value = "/uploadStreamAndDeployment")
    public ResponseBean uploadStreamAndDeployment(@RequestParam("processFile") MultipartFile multipartFile, @RequestParam("processName") String processName) {
        // 获取上传的文件名
        String fileName = multipartFile.getOriginalFilename();
        try {
            // 得到输入流（字节流）对象
            InputStream fileInputStream = multipartFile.getInputStream();
            // 文件的扩展名
            String extension = FilenameUtils.getExtension(fileName);
            Deployment deployment = null;
            if (extension.equals("zip")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment()//初始化流程
                        .addZipInputStream(zip)
                        .name(processName)
                        .deploy();
            } else {
                deployment = repositoryService.createDeployment()//初始化流程
                        .addInputStream(fileName, fileInputStream)
                        .name(processName)
                        .deploy();
            }
            return ResponseBean.success(deployment.getId(), "流程定义部署成功");
        } catch (Exception e) {
            return ResponseBean.error("流程定义部署流程失败");
        }
    }

    /**
     * 获取流程定义列表
     * @return
     */
    @GetMapping(value = "/getDefinitions")
    public ResponseBean getDefinitions() {
        try {
            List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
            list.sort((y,x)->x.getVersion()-y.getVersion());
            for (ProcessDefinition pd : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("processDefinitionID", pd.getId());
                hashMap.put("name", pd.getName());
                hashMap.put("key", pd.getKey());
                hashMap.put("resourceName", pd.getResourceName());
                hashMap.put("deploymentID", pd.getDeploymentId());
                hashMap.put("version", pd.getVersion());
                listMap.add(hashMap);
            }
            return ResponseBean.success(listMap, "获取流程定义列表成功");
        }catch (Exception e) {
            return ResponseBean.error("获取流程定义列表失败");
        }
    }
}
