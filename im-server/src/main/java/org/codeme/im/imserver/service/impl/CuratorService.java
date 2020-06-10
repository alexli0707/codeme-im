package org.codeme.im.imserver.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.codeme.im.imserver.config.IMServerProjectProperties;
import org.codeme.im.imserver.constant.IMServerConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * CuratorService
 *
 * @author walker lee
 * @date 2020/6/2
 */
@Service
@Slf4j
public class CuratorService implements Closeable, LeaderSelectorListener {

    private CuratorFramework client;
    private LeaderSelector leaderSelector;

    @Autowired
    IMServerProjectProperties imServerProjectProperties;


    private void init() {
        this.client = CuratorFrameworkFactory.newClient(imServerProjectProperties.getZookeeperUrl(),
                new ExponentialBackoffRetry(1000, 5));
        this.leaderSelector = new LeaderSelector(this.client, IMServerConstant.IM_SERVICE_ZK_DIR_PATH, this);
    }

    /**
     * 启动Curator
     *
     * @return
     * @throws InterruptedException
     */
    public void register() {
        init();
        try {
            startZK();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startZK() {
        this.client.start();
        this.leaderSelector.setId(imServerProjectProperties.getZkId());
        this.leaderSelector.start();
        this.client.getUnhandledErrorListenable().addListener(errorsListener);
    }


    @Override
    public void close() throws IOException {
        this.leaderSelector.close();
        this.client.close();
    }

    @Override
    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
        try {
            this.client.create().withMode(CreateMode.EPHEMERAL).forPath(IMServerConstant.IM_SERVICE_ZK_DIR_PATH + File.separator + imServerProjectProperties.getZkId(), imServerProjectProperties.getImInnerServerUrl().getBytes());
            this.client.create().withMode(CreateMode.EPHEMERAL).forPath(IMServerConstant.IM_OUTER_SERVICE_ZK_DIR_PATH + File.separator + imServerProjectProperties.getZkId(), imServerProjectProperties.getImServerUrl().getBytes());
            log.info("zk成功注册:{} |{}", imServerProjectProperties.getZkId(), imServerProjectProperties.getImServerUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {

    }

    UnhandledErrorListener errorsListener = new UnhandledErrorListener() {
        @Override
        public void unhandledError(String message, Throwable e) {
            log.error("Unrecoverable error: " + message, e);
            try {
                close();
            } catch (IOException ioe) {
                log.warn("Exception when closing.", ioe);
            }
        }
    };
}
