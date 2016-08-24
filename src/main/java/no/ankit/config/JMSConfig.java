package no.ankit.config;

import no.ankit.message.TopicListener;
import no.ankit.message.TopicMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JMSConfig {

	@Bean
	public TopicListener topicListener(){
		return new TopicListener();
	}

	@Bean
	public TopicMessageConverter topicMessageConverter(){
		return new TopicMessageConverter();
	}

	@Autowired
	private Environment environment;

	@Autowired
	ConnectionFactory connectionFactory;

	@Bean
	public DestinationResolver destResolver(){
		return new DynamicDestinationResolver();
	}

	@Bean
	public DefaultMessageListenerContainer domainListenerContainerPayment() {
		DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer() {
			{
				setConnectionFactory(connectionFactory);
				setMessageListener(new MessageListenerAdapter(topicListener()) {
					{
						setMessageConverter(topicMessageConverter());
						setDefaultListenerMethod(topicListener().getClass().getMethods()[0].getName());
					}
				});
				setDestinationName("VirtualTopic.LOCAL/VAAP01/PAYMENT");
				setPubSubDomain(true);
				setConcurrency("1");
				setDestinationResolver(destResolver());
			}
		};

		return messageListenerContainer;
	}

	@Bean
	public DefaultMessageListenerContainer domainListenerContainerCustomer() {
		DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer() {
			{
				setConnectionFactory(connectionFactory);
				setMessageListener(new MessageListenerAdapter(topicListener()) {
					{
						setMessageConverter(topicMessageConverter());
						setDefaultListenerMethod(topicListener().getClass().getMethods()[0].getName());
					}
				});
				setDestinationName("VirtualTopic.LOCAL/VAAP01/CUSTOMER");
				setPubSubDomain(true);
				setConcurrency("1");
				setDestinationResolver(destResolver());
			}
		};

		return messageListenerContainer;
	}

	@Bean
	public DefaultMessageListenerContainer domainListenerContainerMerchant() {
		DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer() {
			{
				setConnectionFactory(connectionFactory);
				setMessageListener(new MessageListenerAdapter(topicListener()) {
					{
						setMessageConverter(topicMessageConverter());
						setDefaultListenerMethod(topicListener().getClass().getMethods()[0].getName());
					}
				});
				setDestinationName("VirtualTopic.LOCAL/VAAP01/MERCHANT");
				setPubSubDomain(true);
				setConcurrency("1");
				setDestinationResolver(destResolver());
			}
		};

		return messageListenerContainer;
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	RequestContextListener requestContextListener(){
		return new RequestContextListener();
	}
	
}
