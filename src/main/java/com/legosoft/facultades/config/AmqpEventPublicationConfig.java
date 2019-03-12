package com.legosoft.facultades.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AmqpEventPublicationConfig {

    @Value("${axon.amqp.exchange}")
    String exchangeName;

    @Bean
    public Exchange exchange(){
        return ExchangeBuilder.topicExchange(exchangeName).build();
    }

    @Bean
    public Queue queue(){
        return new Queue("facultades", true);
    }

    @Bean
    public Binding binding(){
        return new Binding("facultades", Binding.DestinationType.QUEUE, exchangeName,"*", null);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("192.168.20.109");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setPort(5673);
        return connectionFactory;
    }


    @Bean
    @Required
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory());
        admin.setAutoStartup(true);
        admin.declareExchange(exchange());
        admin.declareQueue(queue());
        admin.declareBinding(binding());
        return admin;
    }

    @Bean
    RabbitTransactionManager transactionManager(){
        RabbitTransactionManager txMgr = new RabbitTransactionManager(connectionFactory());
        return txMgr;
    }

    @Bean
    public Exchange exchangeCqrs(){
        return ExchangeBuilder.topicExchange("ExchangeCQRS").build();
    }

    @Bean
    public Queue queueCqrs(){
        return new Queue("cqrs", true);
    }

    @Bean
    public Binding bindingCqrs(){
        return new Binding("cqrs", Binding.DestinationType.QUEUE, "ExchangeCQRS","*", null);
    }

    @Bean
    @Required
    public RabbitAdmin rabbitAdminCqrs() {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory());
        admin.setAutoStartup(true);
        admin.declareExchange(exchangeCqrs());
        admin.declareQueue(queueCqrs());
        admin.declareBinding(bindingCqrs());
        return admin;
    }
}
