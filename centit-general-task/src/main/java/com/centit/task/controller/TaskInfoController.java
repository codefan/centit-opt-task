package com.centit.task.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.support.common.ObjectException;
import com.centit.support.common.WorkTimeSpan;
import com.centit.support.database.utils.PageDesc;
import com.centit.task.po.TaskInfo;
import com.centit.task.service.TaskInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author liu_cc
 * @create 2021-07-01 17:31
 */
@Controller
@Api(value = "任务信息", tags = "任务信息接口类")
@RequestMapping("/general/task")
@Slf4j
public class TaskInfoController extends BaseController {

    @Autowired
    private TaskInfoService taskInfoService;

    @ApiOperation(value = "任务信息列表", notes = "任务信息列表")
    @WrapUpResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public PageQueryResult listAllTaskInfo(PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> filterMap = BaseController.collectRequestParameters(request);
        List<TaskInfo> listObjects = taskInfoService.listTaskInfos(filterMap, pageDesc);

        JSONArray jsonArray = DictionaryMapUtils.objectsToJSONArray(listObjects);
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            WorkTimeSpan workTimeSpan = new WorkTimeSpan();
            translateWorkLoadDate(jsonObject,workTimeSpan);
        }
        return PageQueryResult.createResult(jsonArray, pageDesc);
    }

    @ApiOperation(value = "查询单个任务信息", notes = "查询单个任务信息")
    @WrapUpResponseBody
    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public JSONObject getTaskInfoByCode(@PathVariable String taskId) {
        TaskInfo taskInfo = taskInfoService.getTaskInfoByCode(taskId);
        if (null == taskId){
            return new JSONObject();
        }
        JSONObject jsonObject = (JSONObject)DictionaryMapUtils.objectToJSON(taskInfo);
        WorkTimeSpan workTimeSpan = new WorkTimeSpan();
        translateWorkLoadDate(jsonObject,workTimeSpan);
        return jsonObject;
    }



    @ApiOperation(value = "保存任务信息", notes = "保存任务信息")
    @WrapUpResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public TaskInfo saveTaskInfo(@RequestBody TaskInfo taskInfo,HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(userCode)){
            throw new ObjectException("您还未登录");
        }
        taskInfo.setUserCode(userCode);
        taskInfo.setUnitCode(WebOptUtils.getCurrentTopUnit(request));
        taskInfoService.saveTaskInfo(taskInfo);
        return taskInfo;
    }

    @ApiOperation(value = "修改任务信息", notes = "修改任务信息,同时会添加备注信息")
    @WrapUpResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseData updateTaskInfo(@RequestBody TaskInfo taskInfo,HttpServletRequest request) {
        if (StringUtils.isBlank(taskInfo.getTaskId())){
            return ResponseData.makeErrorMessage("taskId不能为空");
        }
        String userCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(userCode)){
            throw new ObjectException("您还未登录");
        }
        taskInfo.setUserCode(userCode);
        taskInfoService.updateTaskInfo(taskInfo);
        return ResponseData.makeSuccessResponse();
    }

    @ApiOperation(value = "删除任务信息", notes = "删除任务信息")
    @WrapUpResponseBody
    @RequestMapping(value = "/{taskId}", method = RequestMethod.DELETE)
    public void deleteFlowRoleByCode(@PathVariable String taskId,HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(userCode)){
            throw new ObjectException("您未登录");
        }
        taskInfoService.deleteTaskInfoByCode(taskId,userCode);
    }

    /**
     * 对TaskInfo转换的jsonObject对象中
     * workload和estimateWorkload进行翻译
     * @param jsonObject 由TaskInfo转换而来的jsonObject对象
     * @param workTimeSpan
     */
    private void translateWorkLoadDate(JSONObject jsonObject,WorkTimeSpan workTimeSpan) {
        workTimeSpan.fromNumberAsMinute(jsonObject.getLongValue("workload"));
        jsonObject.put("workloadMinute", workTimeSpan.toStringAsMinute().toLowerCase());
        workTimeSpan.fromNumberAsMinute(jsonObject.getLongValue("estimateWorkload"));
        jsonObject.put("estimateWorkloadMinute", workTimeSpan.toStringAsMinute().toLowerCase());
    }
}
