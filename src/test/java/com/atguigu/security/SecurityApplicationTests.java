package com.atguigu.security;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.junit.Before;
import org.junit.jupiter.api.Test;

//@SpringBootTest
class SecurityApplicationTests {
    ProcessEngineConfiguration cfg = null;

    @Test
    public void contextLoads() {
        // 获取流程引擎对象
        ProcessEngine processEngine = cfg.buildProcessEngine();
        // 部署流程 获取RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()// 创建Deployment对象
                .addClasspathResource("hoilday-request.bpmn20.xml") // 添加流程部署文件
                .name("请求流程") // 设置部署流程的名称
                .deploy(); // 执行部署操作
        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
        System.out.println(deployment);
    }

    @Before
    public void before() {
        cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://localhost:3306/spring_security?serverTimezone=UTC&nullCatalogMeansCurrent=true")
                .setJdbcUsername("root")
                .setJdbcPassword("root")
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                //如果数据库中表结构不存在则新建
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

    }

}
