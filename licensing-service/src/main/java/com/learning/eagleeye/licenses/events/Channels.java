package com.learning.eagleeye.licenses.events;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Channels {
    String INBOUND_ORG_CHANGES = "inboundOrgChanges";

    @Input(INBOUND_ORG_CHANGES)
    SubscribableChannel organizations();
}
