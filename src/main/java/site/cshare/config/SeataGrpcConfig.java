package site.cshare.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "seata.grpc")
public class SeataGrpcConfig {

    private static Logger log = LoggerFactory.getLogger(SeataGrpcConfig.class);

    private String applicationId;

    private String serviceGroup;

    public SeataGrpcConfig() {
    }

    public SeataGrpcConfig(String applicationId, String serviceGroup) {
        this.applicationId = applicationId;
        this.serviceGroup = serviceGroup;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    @Override
    public String toString() {
        return "SeataGrpcConfig{" +
            "applicationId='" + applicationId + '\'' +
            ", serviceGroup='" + serviceGroup + '\'' +
            '}';
    }
}
