package org.codeme.im.imforward.listener;

import org.codeme.im.imforward.event.NeedStartReconnectEvent;
import org.codeme.im.imforward.service.impl.InnerImReconnectionService;
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
    InnerImReconnectionService imReconnectionService;

    @Override
    public void onApplicationEvent(NeedStartReconnectEvent event) {
        imReconnectionService.start();
    }
}
