package site.cshare.interceptor;

import io.grpc.*;
import io.seata.core.context.RootContext;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcGlobalServerInterceptor
public class ServerTransactionGrpcInterceptor implements ServerInterceptor {

    Logger log = LoggerFactory.getLogger(ServerTransactionGrpcInterceptor.class);

    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String xid = RootContext.getXID();
        Metadata.Key<String> key_xid = Metadata.Key.of(RootContext.KEY_XID, Metadata.ASCII_STRING_MARSHALLER);
        final String rpcXid = metadata.get(key_xid);
        if (log.isDebugEnabled()) {
            log.debug("xid in RootContext[{}] xid in RpcContext Request", rpcXid);
        }
        boolean bind = false;
        if (xid != null) {
            metadata.put(key_xid, xid);
        } else {
            if (rpcXid != null) {
                RootContext.bind(rpcXid);
                bind = true;
            }
            if (log.isDebugEnabled()) {
                log.debug("bind [{}] to RootContext");
            }
        }
        final boolean bindResult = bind;
        ServerCall.Listener<ReqT> reqTListener = serverCallHandler.startCall(serverCall, metadata);
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(reqTListener) {
            @Override
            public void onComplete() {
                super.onComplete();
                if (bindResult) {
                    String unbindXid = RootContext.unbind();
                    if (log.isDebugEnabled()) {
                        log.debug("unbind [{}] from RootContext");
                    }
                    if (!rpcXid.equalsIgnoreCase(unbindXid)) {
                        log.warn("xid in change during RPC from {} to {}", rpcXid, unbindXid);
                        if (unbindXid != null) {
                            RootContext.bind(unbindXid);
                            log.warn("bind [{}] back to RootContext", unbindXid);
                        }
                    }

                }

            }
        };
    }
}
