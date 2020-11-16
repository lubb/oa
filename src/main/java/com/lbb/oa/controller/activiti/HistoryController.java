package com.lbb.oa.controller.activiti;


import com.lbb.oa.pojo.sys.SecuritySysUser;
import com.lbb.oa.util.ResponseBean;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    /**
     * 获取用户的历史任务信息
     * @param securitySysUser
     * @return
     */
    @GetMapping(value = "/getInstancesByUserName")
    public ResponseBean instancesByUser(@AuthenticationPrincipal SecuritySysUser securitySysUser) {
        try {
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().asc()
                    .taskAssignee(securitySysUser.getUsername())
                    .list();
            return ResponseBean.success(historicTaskInstances, "获取用户的历史任务信息成功");
        } catch (Exception e) {
            return ResponseBean.error("获取用户的历史任务信息失败");
        }

    }

    /**
     * 根据流程实例id获取任务的详情
     * @param piID
     * @return
     */
    @GetMapping(value = "/getInstancesByPiID")
    public ResponseBean getInstancesByPiID(@RequestParam("piID") String piID) {
        try {
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().asc()
                    .processInstanceId(piID)
                    .list();
            return ResponseBean.success(historicTaskInstances, "根据流程实例id获取任务的详情成功");
        } catch (Exception e) {
            return ResponseBean.error("根据流程实例id获取任务的详情失败");
        }
    }
}
