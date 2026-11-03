package com.mes.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String DEVICE_DATA_EXCHANGE = "mes.device.data.exchange";
    public static final String DEVICE_DATA_QUEUE = "mes.device.data.queue";
    public static final String DEVICE_DATA_ROUTING_KEY = "device.data";

    @Bean
    public Queue deviceDataQueue() {
        return QueueBuilder.durable(DEVICE_DATA_QUEUE).build();
    }

    @Bean
    public DirectExchange deviceDataExchange() {
        return new DirectExchange(DEVICE_DATA_EXCHANGE, true, false);
    }

    @Bean
    public Binding deviceDataBinding(Queue deviceDataQueue, DirectExchange deviceDataExchange) {
        return BindingBuilder.bind(deviceDataQueue).to(deviceDataExchange).with(DEVICE_DATA_ROUTING_KEY);
    }
}
