# netty in action
netty & springboot2 搭建分布式IM应用. 从0到1,循序渐进.

# 项目模块
* im-client: im客户端
* im-server: im服务,主要是围绕链接的建立与管理,设计成可以多实例扩展部署.
* im-common: 通用内容的抽取,包括消息的类型与处理
* im-api: 提供rest-api的服务,预留部署的灵活程度与im-server模块拆分



# 本地
注册帐号
```
  curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{
    "username": "admin",
    "password": "111111",
    "confirmed_password": "111111"
  }' 'http://localhost:8031/api/oauth/register'
```

授权
```
     curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{
       "username": "admin",
       "password": "111111"
     }' 'http://localhost:8031/api/oauth'
```

```
