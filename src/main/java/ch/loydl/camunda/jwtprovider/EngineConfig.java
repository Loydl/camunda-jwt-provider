package ch.loydl.camunda.jwtprovider;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by hloydl on 15.06.2017.
 */
@Component
public class EngineConfig {

    private static final Logger LOG = LoggerFactory.getLogger(EngineConfig.class);

    /**
     * Retrieve a shared process engine (internally via JNDI).
     *
     * @return the default process engine
     */
    @Bean
    public ProcessEngine processEngine() {

        ProcessEngine pe = BpmPlatform.getDefaultProcessEngine();
        if(pe != null) {
            return pe;
        }
        pe = ProcessEngines.getDefaultProcessEngine(false);
        if (pe != null) {
            return pe;
        }
        String errMessage = "Default Process engine not available";
        LOG.error(errMessage);
        return null;
    }

    /**
     * Retrieve the identity service to authenticate name and password
     * @return the identity service
     */
    @Bean
    public IdentityService identityService() {
        return processEngine().getIdentityService();
    }

}
