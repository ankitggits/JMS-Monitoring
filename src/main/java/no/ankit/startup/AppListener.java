package no.ankit.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * Created by AB75448 on 19.08.2016.
 */
@Component
public class AppListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    @Qualifier("domainListenerContainerPayment")
    DefaultMessageListenerContainer container1;

    @Autowired
    @Qualifier("domainListenerContainerCustomer")
    DefaultMessageListenerContainer container2;

    @Autowired
    @Qualifier("domainListenerContainerMerchant")
    DefaultMessageListenerContainer container3;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent  applicationStartedEvent) {
        System.out.println("context refresh event is being called");
        container1.stop();
        container2.stop();
        container3.stop();
    }
}
