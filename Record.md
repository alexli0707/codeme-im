# coding过程的问题与解决

* 线程死锁:写授权的时候需要在Handler中获取bean实例, 一开始采用获取实现`ApplicationContextAware`接口的类回调方法获取ApplicationContext实例并调用`getBean`方法获取,但是在使用过程中会偶发在`channelActive`被调用时`context`为空的情况,同时也觉得这种写法比较丑陋.之后用构造方式传参将ApplicationContext从`Server`以及`Client`传入`Handler`,这时在`Handler`里构造方法中获取`bean`出现了死锁,通过visualvm找到死锁线程的运行行,然后通过google 找到解决(https://stackoverflow.com/questions/48442286/deadlock-in-spring-boot-applications-postconstruct-method).
