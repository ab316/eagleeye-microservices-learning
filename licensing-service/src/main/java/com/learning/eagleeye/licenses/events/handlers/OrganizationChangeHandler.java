package com.learning.eagleeye.licenses.events.handlers;

import com.learning.eagleeye.licenses.events.Channels;
import com.learning.eagleeye.licenses.events.model.OrganizationChangeModel;
import com.learning.eagleeye.licenses.repository.OrganizationRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(Channels.class)
@Slf4j
public class OrganizationChangeHandler {

    @Autowired
    private OrganizationRedisRepository repository;

    @StreamListener(Channels.INBOUND_ORG_CHANGES)
    public void onOrganizationChange(OrganizationChangeModel changeModel) {
        log.debug("Received organization [{}] event for id: [{}]", changeModel.getAction(), changeModel.getOrganizationId());
        switch (changeModel.getAction()) {
            case "UPDATE":
            case "DELETE":
                repository.deleteById(changeModel.getOrganizationId());
                break;
            case "SAVE":
                break;
            default:
                log.error("Invalid action [{}]", changeModel.getType());
        }
    }
}
