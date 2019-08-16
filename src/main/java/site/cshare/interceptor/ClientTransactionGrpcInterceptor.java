package site.cshare.interceptor;

import io.grpc.*;
import io.seata.core.context.RootContext;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcGlobalClientInterceptor
public class ClientTransactionGrpcInterceptor implements ClientInterceptor {

    Logger log = LoggerFactory.getLogger(ClientTransactionGrpcInterceptor.class);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        String xid = RootContext.getXID();
        if(log.isDebugEnabled()) {
            log.debug("Current xid = [{}]", xid);
        }
        ClientCall<ReqT, RespT> clientCall = channel.newCall(methodDescriptor, callOptions);
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(clientCall) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                if(xid != null) {
                    Metadata.Key<String> KEY_XID = Metadata.Key.of(RootContext.KEY_XID, Metadata.ASCII_STRING_MARSHALLER);
                    headers.put(KEY_XID, xid);
                }
                super.start(responseListener, headers);
            }
        };
    }
}
