package com.atguigu.security.flowable;

import liquibase.pro.packaged.S;
import lombok.Data;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FlowableTest01 {
    ProcessEngineConfiguration cfg = null;
    ProcessEngine processEngine =null;//流程引擎对象
    {
        this.cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://localhost:3306/spring_security?serverTimezone=UTC&nullCatalogMeansCurrent=true")
                .setJdbcUsername("root")
                .setJdbcPassword("root")
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                //如果数据库中表结构不存在则新建
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
         this.processEngine = cfg.buildProcessEngine();
    }
    public static void main(String[] args) {
        FlowableTest01 t1 = new FlowableTest01();
//        t1.deploy();
        t1.start();
//        t1.completeTask();
//        t1.queryHistory();
    }
    public void queryHistory(){
        HistoryService historyService = this.processEngine.getHistoryService();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processDefinitionId("holidayRequest:1:2503")
                .finished().orderByHistoricActivityInstanceEndTime().asc().list();
        list.forEach(e ->{
            System.out.println(e.getActivityName()+":"+e.getAssignee()+":"+e.getActivityId()+":"+e.getDurationInMillis());
        });
    }

    public void start(){
        //通过RuntimeService来启动流程实例
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        Map<String,Object> map=new HashMap<>();
        map.put("employee","张三");
        map.put("request-Hoilday",3);
        map.put("desc","出去玩");
        ProcessInstance request = runtimeService.startProcessInstanceById("MyFirst:1:27504", map);
        System.out.println(request.getProcessDefinitionId());
        System.out.println(request.getActivityId());
        System.out.println(request.getDeploymentId());
    }
    public void delete(){
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        repositoryService.deleteDeployment("1");//删除部署流程，如果部署流程启动了就不允许删除
        repositoryService.deleteDeployment("1", true);//第二个参数是级联删除，是否允许删除已经启动的流程

    }
    public void completeTask(){
        TaskService taskService = this.processEngine.getTaskService();
        Task result = taskService.createTaskQuery().processDefinitionKey("holidayRequest").singleResult();//holidayRequest:1:2503
        Map<String,Object> map=new HashMap<>();//创建流程变量
        map.put("approved",false);//approved对应hoilday-request.bpmn20.xml中${approved}表达式
        taskService.complete(result.getId(),map);//完成任务
    }
    public void queryTask(){
        TaskQuery taskQuery = this.processEngine.getTaskService().createTaskQuery();
        Task result = taskQuery.processDefinitionKey("holidayRequest").singleResult();//holidayRequest:1:2503
        System.out.println(result.getAssignee());//查询任务处理人
        System.out.println("result.getId() = " + result.getId());
    }
    public void queryDeploy(){
        ProcessDefinitionQuery definitionQuery = this.processEngine.getRepositoryService().createProcessDefinitionQuery();
        ProcessDefinition resut = definitionQuery.deploymentId("1").singleResult();
        System.out.println(resut.getDeploymentId());//1
        System.out.println(resut.getName());//请假流程
    }
    public void deploy(){
        // 部署流程 获取RepositoryService对象
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()// 创建Deployment对象
                .addClasspathResource("MyFirst.bpmn20.xml") // 添加流程部署文件
//                .addZipInputStream()ZIP或者Bar需要使用zipInputStream
                .name("Flowable-UI请求流程") // 设置部署流程的名称
                .deploy(); // 执行部署操作
        System.out.println("deployment.getId() = " + deployment.getId());//1
        System.out.println("deployment.getName() = " + deployment.getName());
        System.out.println(deployment);
    }

}
