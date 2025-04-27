package com.dc.coordinator.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfig {

    public static final String LINE_EXCHANGE = "line-exchange";

    @Bean
    public FanoutExchange lineExchange() {
        return new FanoutExchange(LINE_EXCHANGE, true, false);
    }
}