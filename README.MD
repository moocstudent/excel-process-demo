最终,经过测试,不管是websocket的实时数据返回,以及另一个接口的调用查询,都跟excel的Upload操作是有阻塞的关系,不过通过eventBus的调用可以实时地记录本次的上传具体信息,虽然没办法做到实时返回进度,但是记录失败条数,以及记录每次导入的是按哪些条件/每次批量大小还是可用的.

前端页面代码: https://github.com/moocstudent/html-excel-test
