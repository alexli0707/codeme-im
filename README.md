# netty in action
netty & springboot2 搭建分布式IM应用. 从0到1,循序渐进.

## 前言
> 曾经用NIO写过商业化IM的android端sdk,也用NIO写过自己的玩具IM,在读完<netty in action>这本书之后觉得用netty来管理和维护长链接的socket来看代码会更优雅,入门更容易.再结合日常开发用的springboot的框架与docker容器化,想从0到1实现一个部署方便的分布式IM服务



 

## 预计的章节(对应分支)
* [x] 1.init以及建立心跳(`chapter1_heartbeat`)
* [x] 2.注册授权以及连接鉴权与管理(`chapter2_authorization_authentication`)
* [x] 3.客户端断线重连与服务端连接管理 (`chapter3_client_reconnection`)
* [x] 4.发送文本消息与指定单聊(`chapter4_p2p_chat`)
* [x] 5.api建群,拉群以及群文本聊天(`chapter5_chatroom_chat`)





## 项目模块
* im-client: im客户端                                                                                                    
* im-server: im服务,主要是围绕链接的建立与管理,设计成可以多实例扩展部署.
* im-common: 通用内容的抽取,包括消息的类型与处理
* im-api: 提供rest-api的服务,预留部署的灵活程度与im-server模块拆分



## 本地
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

创建群

```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'Authorization: Bearer 40ef82a6537ccad2c256e98b6de5fe13' -d '{
       "title": "技术交流群"
     }' 'http://localhost:8031/api/chatroom'
```    

邀请进群

```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'Authorization: Bearer 40ef82a6537ccad2c256e98b6de5fe13' -d '{
       "chatroom_id": 1265169045146738693,
       "invitee":[2,3] 
     }' 'http://localhost:8031/api/chatroom/invite'

```


