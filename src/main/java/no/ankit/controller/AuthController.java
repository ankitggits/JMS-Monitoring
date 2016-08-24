package no.ankit.controller;

import no.ankit.dto.AuthRequest;
import no.ankit.util.MessageUtility;
import no.ankit.util.TestUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by AB75448 on 18.08.2016.
 */
@RestController
public class AuthController {

    @Autowired
    @Qualifier("domainListenerContainerPayment")
    DefaultMessageListenerContainer container1;

    @Autowired
    @Qualifier("domainListenerContainerCustomer")
    DefaultMessageListenerContainer container2;

    @Autowired
    @Qualifier("domainListenerContainerMerchant")
    DefaultMessageListenerContainer container3;

    @RequestMapping(value= "/login", method= POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        if(authRequest.getUsername().equalsIgnoreCase("admin") && authRequest.getPassword().equalsIgnoreCase("admin") ){
            container1.start();
            container2.start();
            container3.start();
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value= "/logout", method= GET, produces = "application/json")
    public ResponseEntity<?> logout() {
        container1.stop();
        container2.stop();
        container3.stop();
        MessageUtility.correlationMap.clear();
        MessageUtility.operationMap.clear();
        TestUtility.performanceResults.clear();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
