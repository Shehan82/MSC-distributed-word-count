package com.dc.proposer2.sidecar.controller;

import com.dc.proposer2.sidecar.exception.CallNotPermittedRabbitMQException;
import com.dc.proposer2.sidecar.service.ProposerSidecarService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ProposerSidecarController {

    private static final Logger LOG = LoggerFactory.getLogger(ProposerSidecarController.class);


    private final ProposerSidecarService proposerSidecarService;

    public ProposerSidecarController(ProposerSidecarService proposerSidecarService) {
        this.proposerSidecarService = proposerSidecarService;
    }

    @RabbitListener(queues = "proposer-queue-8095", ackMode = "MANUAL")
    public void calculateWordCount(String line, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            this.proposerSidecarService.calculateWordCount(line);
            channel.basicAck(tag, false);
            LOG.info("Manually acknowledged and removed from the queue");
        } catch (CallNotPermittedRabbitMQException e) {
            channel.basicNack(tag, false, true);
//            this.proposerSidecarService.notifyCoordinatorOfProposerFailure();
            LOG.info("Manually not acknowledged and re-queued");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            channel.basicNack(tag, false, true);
            LOG.info("General exception : Manually not acknowledged and re-queued");
        }
    }


}
