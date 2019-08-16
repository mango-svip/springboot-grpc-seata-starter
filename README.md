# springboot-grpc-seata-starter
springboot-grpc-seata-starter

###实现原理
```
基于grpc interceptor

    在客户端发送请求之前进行拦截，置入全局事务ID
    
         Metadata.Key<String> KEY_XID = Metadata.Key.of(RootContext.KEY_XID, Metadata.ASCII_STRING_MARSHALLER);
         headers.put(KEY_XID, xid);
        
    服务器收到请求处理之前进行拦截，取出全局事务ID,并调动Seata的方法绑定到当前线程内
          
        Metadata.Key<String> key_xid = Metadata.Key.of(RootContext.KEY_XID, Metadata.ASCII_STRING_MARSHALLER);
        final String rpcXid = metadata.get(key_xid);
        RootContext.bind(rpcXid);
        
    这样就可以加入seata控制的全局事务了

```
---
[seata]: https://github.com/seata/seata


有关seata的内容可以看这里 [seata] 
