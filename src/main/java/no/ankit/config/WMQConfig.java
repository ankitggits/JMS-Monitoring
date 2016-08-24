package no.ankit.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.connection.SingleConnectionFactory;

import javax.jms.ConnectionFactory;

/**
 * Created by AB75448 on 19.08.2016.
 */
@Configuration
@Profile("!local")
public class WMQConfig {

    @Value("${mq.connectionFactory:tcp://localhost:61616}")
    String connectionFactoryURL;

    @Bean
    ConnectionFactory connectionFactory() {
        return new SingleConnectionFactory(new ActiveMQConnectionFactory(connectionFactoryURL));
    }
}
