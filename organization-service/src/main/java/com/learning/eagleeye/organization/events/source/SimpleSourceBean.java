package com.learning.eagleeye.organization.events.source;

import com.learning.eagleeye.organization.events.model.OrganizationChangeModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleSourceBean {

    private Source source;

    @Autowired
    public SimpleSourceBean(Source source) {
        this.source = source;
    }

    public void publishOrganizationChange(String action, String orgId) {
        log.debug("Sending organization {} message for id: [{}]", action, orgId);
        OrganizationChangeModel change = new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action,
                orgId
        );

        source.output().send(MessageBuilder.withPayload(change).build());
    }
}
