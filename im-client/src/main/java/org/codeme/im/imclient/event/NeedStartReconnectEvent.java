package org.codeme.im.imclient.event;

import org.springframework.context.ApplicationEvent;

/**
 * NeedStartReconnectEvent
 *
 * @author walker lee
 * @date 2020/5/25
 */
public class NeedStartReconnectEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public NeedStartReconnectEvent(Object source) {
        super(source);
    }
}
