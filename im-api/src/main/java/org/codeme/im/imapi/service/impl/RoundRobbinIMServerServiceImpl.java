package org.codeme.im.imapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.codeme.im.imapi.config.ApiProjectProperties;
import org.codeme.im.imapi.constant.ApiProjectConstant;
import org.codeme.im.imapi.service.RoundRobbinIMServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RoundRobbinIMServerServiceImpl
 * 实现简单的轮循分配连接到不同的服务上
 *
 * @author walker lee
 * @date 2020/6/10
 */
@Service
@Slf4j
public class RoundRobbinIMServerServiceImpl implements RoundRobbinIMServerService {

    @Autowired
    private ApiProjectProperties apiProjectProperties;

    private CuratorFramework client;
    private PathChildrenCache imServerCache;

    private volatile Integer imServerSize = 0;

    private ArrayList<String> imServerList = new ArrayList<>();

    private AtomicInteger roundRobbinInt = new AtomicInteger(0);


    @Override
    public String getIMServerUrlWithRoundRobbin() {
        int robbin = roundRobbinInt.incrementAndGet();
        String imServerUrl = imServerList.get(Math.abs(robbin % imServerSize));
        log.info("roundrobin get url:"+imServerUrl);
        return imServerUrl;
    }

    PathChildrenCacheListener imServerCacheListener = new PathChildrenCacheListener() {
        @Override
        public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
            if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                String path = pathChildrenCacheEvent.getData().getPath();
                log.info("remove child:" + pathChildrenCacheEvent.getData().getPath());
                if (path.contains("lock")) {
                    log.info("ignore lock");
                } else {
                    String imServerUrl = new String(pathChildrenCacheEvent.getData().getData());
                    imServerList.remove(imServerUrl);
                    imServerSize--;
                }
            } else if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                String path = pathChildrenCacheEvent.getData().getPath();
                if (path.contains("lock")) {
                    log.info("ignore lock");
                } else {
                    // 获取节点数据, 建立内部连接
                    String imServerUrl = new String(pathChildrenCacheEvent.getData().getData());
                    imServerList.add(imServerUrl);
                    imServerSize++;
                    log.info("add child:" + path);
                }
            }
        }
    };

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

    private void init() {
        this.client = CuratorFrameworkFactory.newClient(apiProjectProperties.getZookeeperUrl(),
                new ExponentialBackoffRetry(1000, 5));
        this.imServerCache = new PathChildrenCache(this.client, ApiProjectConstant.IM_OUTER_SERVICE_ZK_DIR_PATH, true);
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
}
