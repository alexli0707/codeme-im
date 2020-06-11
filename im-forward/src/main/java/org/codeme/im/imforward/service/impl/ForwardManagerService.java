package org.codeme.im.imforward.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.codeme.im.imcommon.constant.RedisKeyConstant;
import org.codeme.im.imcommon.model.vo.ProtocolMsg;
import org.codeme.im.imforward.config.IMForwardProperties;
import org.codeme.im.imforward.constant.IMForwardConstant;
import org.codeme.im.imforward.netty.inner.client.InnerImClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;


/**
 * CuratorService
 *
 * @author walker lee
 * @date 2020/6/2
 */
@Service
@Slf4j
public class ForwardManagerService {

    private CuratorFramework client;
    private PathChildrenCache imServerCache;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    IMForwardProperties imForwardProperties;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    ConcurrentHashMap<String, InnerImClient> innerImClientHashMap = new ConcurrentHashMap<>();

    PathChildrenCacheListener imServerCacheListener = new PathChildrenCacheListener() {
        @Override
        public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
            if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                String path = pathChildrenCacheEvent.getData().getPath();
                log.info("remove child:" + pathChildrenCacheEvent.getData().getPath());
                if (path.contains("lock")) {
                    log.info("ignore lock");
                } else {
                    String serverId = getServerIdFromPath(path);
                    InnerImClient innerImClient = innerImClientHashMap.get(serverId);
                    if (null != innerImClient) {
                        innerImClient.close();
                        innerImClientHashMap.remove(serverId);
                    }
                }
            } else if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                String path = pathChildrenCacheEvent.getData().getPath();
                if (path.contains("lock")) {
                    log.info("ignore lock");
                } else {
                    // 获取节点数据, 建立内部连接
                    String url = new String(pathChildrenCacheEvent.getData().getData());
                    String serverId = getServerIdFromPath(path);
                    InnerImClient innerImClient = applicationContext.getBean(InnerImClient.class);
                    innerImClient.setInnerNettyServerId(serverId);
                    innerImClient.setInnerNettyUrl(url);
                    innerImClient.start();
                    innerImClientHashMap.put(serverId, innerImClient);
                    log.info("add child:" + path);
                }
            }
        }
    };


    private void init() {
        this.client = CuratorFrameworkFactory.newClient(imForwardProperties.getZookeeperUrl(),
                new ExponentialBackoffRetry(1000, 5));
        this.imServerCache = new PathChildrenCache(this.client, IMForwardConstant.IM_SERVICE_ZK_DIR_PATH, true);
    }

    public void reconnectByServerId(String innerNettyServerId) throws InterruptedException {
        InnerImClient innerImClient = innerImClientHashMap.get(innerNettyServerId);
        if (null != innerImClient) {
            innerImClient.reconnect();
        }
    }

    public boolean forwardMsg(ProtocolMsg protocolMsg, long receiverId) {
        String innerServerId = this.getReceiverSocketServer(receiverId);
        if (StringUtils.isEmpty(innerServerId)) {
            log.error("当前用户不在线");
            return false;
        }
        return innerImClientHashMap.get(innerServerId).sendMsg(protocolMsg);
    }

    /**
     * 启动Curator
     *
     * @return
     * @throws InterruptedException
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        init();
        startZK();
    }

    private void startZK() {
        this.client.start();
        this.imServerCache.getListenable().addListener(imServerCacheListener);
        try {
            this.imServerCache.start(PathChildrenCache.StartMode.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getServerIdFromPath(String path) {
        return path.substring(path.lastIndexOf(File.separator) + 1);
    }


    /**
     * 获取接收者长连接服务实例名,返回为空则用户不在线或者没有建立连接
     *
     * @param receiverId
     * @return
     */
    private String getReceiverSocketServer(long receiverId) {
        return String.valueOf(redisTemplate.opsForValue().get(RedisKeyConstant.getUserSocketBelong(receiverId)));
    }


}
