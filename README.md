# register-demo
1.项目介绍：\
注册中心:register-server\
服务组件：register-client\
共通部分：register-common\
功能介绍:\
register-server：
服务注册：\
服务下线：\
微服务存活状态监控：实时监控注册中心中的服务实例是否存活。标准：通过心跳机制查询最近一次心跳时间，如果超过了过期时间就认为服务宕机摘除注册表中的注册数据。\
自我保护机制：目的是为了保护注册服务不因为当注册中心机器出现了故障，导致网络有了问题，会导致大量心跳没法发送过来，为此会出现大量服务实例因为超过过期时间儿导致被从注册中心摘除的情况。\

register-client:
服务注册：\
服务下线：\
心跳发送：\

