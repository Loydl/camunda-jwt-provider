package ch.loydl.camunda.jwtprovider;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class TestConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() {
        SpringProcessEngineConfiguration conf = new SpringProcessEngineConfiguration();
        conf.setProcessEngineName("default");
        conf.setDataSource(dataSource());
        conf.setTransactionManager(transactionManager());
        conf.setDatabaseSchemaUpdate("true");
        conf.setHistoryLevel(HistoryLevel.HISTORY_LEVEL_FULL);
        //conf.setJobExecutor(jobExecutor());
        //conf.setProcessEnginePlugins(processEnginePlugins());
        return conf;
    }

    /*
    @Bean
    public JobExecutor jobExecutor() {
        JobExecutor jobExecutor = new DefaultJobExecutor();
        jobExecutor.setWaitTimeInMillis(100);
        return jobExecutor;
    }*/


   /*
   @Bean
    public List<ProcessEnginePlugin> processEnginePlugins() {
        return Collections.singletonList(
                new SpringProcessEnginePlugin()
        );
    }*/

    @Bean
    public ProcessEngineFactoryBean processEngineFactoryBean() {
        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
        return factoryBean;
    }

    @Bean
    public ProcessEngine processEngine() {
        try {
            return processEngineFactoryBean().getObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ProcessEngine", e);
        }
    }

    @Bean
    ProcessEngineRule processEngineRule() {
        ProcessEngineRule processEngineRule = new ProcessEngineRule();
        processEngineRule.setProcessEngine(processEngine());
        return processEngineRule;
    }

    /*
    @Bean
    public RuntimeService runtimeService() {
        return processEngine().getRuntimeService();
    }
    */

    @Bean
    public IdentityService identityService() {
        return  processEngine().getIdentityService();
    }

}

