package com.dc.proposer2.sidecar.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ConsumerConfig {

    public static final String LINE_EXCHANGE = "line-exchange";

    @Value("${server.port}")
    private int port;

    @Bean
    public Queue proposerQueue() {
        String queueName = "proposer-queue-" + port;
        return new Queue(queueName, true, false, false);
    }

    @Bean
    public Binding binding(Queue proposerQueue, FanoutExchange lineExchange) {
        return BindingBuilder.bind(proposerQueue).to(lineExchange);
    }

    @Bean
    public FanoutExchange lineExchange() {
        return new FanoutExchange(LINE_EXCHANGE, true, false);
    }
}
