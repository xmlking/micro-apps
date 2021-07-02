# Resiliency

An application is resilient if it can recover quickly from failures.<br/>
Failures can be caused by software failures, hardware failures, connection failures.

We should care for **Server Experience (SX)** , same way we care for **User Experience (UX)** when dealing with microservices.<br/>
Write non-blocking APIs with *Adaptive Concurrency Limits* and flow control implementation. 

Resilience cannot be added as afterthought; it needs to be part of the design from the beginning.

## Adaptive Automatic Flow Control

In microservice systems, **availability** can be ensured through a variety of measures, such as *fault tolerance* and *flow limiting*. 
These two measures are collectively called flow control. In the current mainstream system design, the flow control rules are 
usually setting manually and statically, which leads the rules cannot be dynamically adjusted according to the flow shape, 
and difficult to utilize the full performance of the system. To mitigate this problem, an adaptive dynamic flow control 
algorithm is proposed. The algorithm is modeled on the system's monitoring data and current flow, which real-time calculating 
the flow limiting threshold, and implements fine-grained service adaptive flow control to improve the resource utilization.

## References 
- [Performance Under Load](https://netflixtechblog.medium.com/performance-under-load-3e6fa9a60581)
- [Alibaba Sentinel](https://sentinelguard.io/en-us/)
