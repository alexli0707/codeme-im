package org.codeme.im.imclient.listener;

import org.codeme.im.imclient.event.NeedStartReconnectEvent;
import org.codeme.im.imclient.service.impl.ImReconnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * NeedStartReconnectEventListener
 *
 * @author walker lee
 * @date 2020/5/25
 */
@Component
public class NeedStartReconnectEventListener implements ApplicationListener<NeedStartReconnectEvent> {

    @Autowired
    ImReconnectionService imReconnectionService;

    @Override
    public void onApplicationEvent(NeedStartReconnectEvent event) {
        imReconnectionService.start();
    }
}
