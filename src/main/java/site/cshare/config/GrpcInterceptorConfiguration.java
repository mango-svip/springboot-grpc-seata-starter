package site.cshare.config;

import site.cshare.interceptor.ClientTransactionGrpcInterceptor;
import site.cshare.interceptor.ServerTransactionGrpcInterceptor;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfigureBefore({GrpcServerAutoConfiguration.class})
public class GrpcInterceptorConfiguration {

    Logger log = LoggerFactory.getLogger(ServerTransactionGrpcInterceptor.class);

    @Bean
    @ConditionalOnMissingBean(ServerTransactionGrpcInterceptor.class)
    public ServerTransactionGrpcInterceptor serverTransactionGrpcInterceptor() {
        if(log.isDebugEnabled()) {
            log.debug("Pre-Registering service serverTransactionGrpcInterceptor");
        }
        return new ServerTransactionGrpcInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean(ClientTransactionGrpcInterceptor.class)
    public ClientTransactionGrpcInterceptor clientTransactionGrpcInterceptor() {
        if(log.isDebugEnabled()) {
            log.debug("Pre-Registering service clientTransactionGrpcInterceptor");
        }
        return new ClientTransactionGrpcInterceptor();
    }

}
