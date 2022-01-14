## redis中通过list模拟缓存管道实现一个服务生产、多个服务消费(这个版本通过消费者定时清空pipeline里面的数据)
>  product模块负责向redis的pipeline中生产Person对象，每隔5s向List左侧写入100个对象，提供/start-product和/stop-product两个接口来控制生产的进行。

> consumer01和consumer02两个模块从redis的pipeline的右侧取出Person对象并打印。两个消费者每隔5秒启动三个线程读取pipeline里面数据并一致消费到为空。
> 这两个模块提供/start-product和/stop-product两个接口来控制消费的进行。

## 在business模块openfeign调用consumer模块的接口


