package com.learning.eagleeye.licenses.events.sink;

import com.learning.eagleeye.licenses.events.model.OrganizationChangeModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Sink.class)
@Slf4j
public class SimpleSinkBean {

    @StreamListener(Sink.INPUT)
    public void loggerSink(OrganizationChangeModel changeModel) {
        log.debug("Received organization change event for id: [{}]", changeModel.getOrganizationId());
    }
}
