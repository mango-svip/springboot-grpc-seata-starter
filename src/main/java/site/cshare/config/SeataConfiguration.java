package site.cshare.config;

import io.seata.spring.annotation.GlobalTransactionScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Optional;

@EnableConfigurationProperties(SeataGrpcConfig.class)
public class SeataConfiguration {

    private static Logger log = LoggerFactory.getLogger(SeataConfiguration.class);

    @Bean
    public SeataGrpcConfig seataGrpcConfig(Environment environment) {
        String serviceGroup = this.getProperty(environment, "seata.grpc.service-group", "seata.grpc.serviceGroup");
        String applicationId = this.getProperty(environment, "seata.grpc.application-id", "seata.grpc.applicationId");
        return new SeataGrpcConfig(applicationId, serviceGroup);
    }

    private String getProperty(final Environment environment, final String propertyName, final String otherPropertyName) {
        return Optional.ofNullable(environment.getProperty(propertyName)).orElseGet(() -> environment.getProperty(otherPropertyName));
    }

    @Bean
    @ConditionalOnBean(SeataGrpcConfig.class)
    public GlobalTransactionScanner globalTransactionScanner(SeataGrpcConfig seataGrpcConfig) {
        if (seataGrpcConfig.getApplicationId() == null || seataGrpcConfig.getServiceGroup() == null) {
            throw new RuntimeException("applicationId and serviceGroup must be setted");
        }
        if (log.isDebugEnabled()) {
            log.debug("seataGrpcConfig = {}", seataGrpcConfig);
        }
        GlobalTransactionScanner globalTransactionScanner = new GlobalTransactionScanner(seataGrpcConfig.getApplicationId(), seataGrpcConfig.getServiceGroup());
        return globalTransactionScanner;
    }
}
