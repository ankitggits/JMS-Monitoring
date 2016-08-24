package no.ankit.controller;

import no.ankit.dto.ListenerConcurrency;
import no.ankit.dto.ListenerConcurrencyRequest;
import no.ankit.dto.ListenerConcurrencyResponse;
import no.ankit.util.MessageUtility;
import com.google.common.collect.Lists;
import no.dnb.vaap.common.messaging.domain.MessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by AB75448 on 16.08.2016.
 */
@RestController
public class MessageController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value="/suggest/{searchText}", method= GET)
    public List<String> handleOpen(@PathVariable("searchText") String searchText) {
        List<String> suggessions = new ArrayList<>();
        MessageUtility.operationMap.keySet().stream().filter(opcode-> opcode.toLowerCase().contains(searchText.trim().toLowerCase())).forEach(filteredKey->suggessions.add(filteredKey));
        MessageUtility.correlationMap.keySet().stream().filter(correlation-> correlation.toLowerCase().contains(searchText.trim().toLowerCase())).forEach(filteredKey->suggessions.add(filteredKey));
        return suggessions;
    }

    @RequestMapping(value="/find/{value}", method= GET)
    public List<MessageEntity> findMessageEntities(@PathVariable("value") String value) {
        value = value.trim();
        return Lists.reverse(value.toLowerCase().startsWith("payment") || value.toLowerCase().startsWith("customer") || value.toLowerCase().startsWith("merchant")?MessageUtility.operationMap.get(value):MessageUtility.correlationMap.get(value));
    }

    @RequestMapping(value="/find/{start}/{end}", method= GET)
    public List<MessageEntity> findMessageEntities(@PathVariable("start") Integer start, @PathVariable("end") Integer end) {
        List<MessageEntity> messageEntities = Lists.newArrayList();
        MessageUtility.correlationMap.values().stream().forEach(valuesList->{
                messageEntities.addAll(valuesList);
        });
        messageEntities.sort((o1, o2) -> {
            try {
                return MessageUtility.FORMATTER.parse(o2.getTimeStamp()).compareTo(MessageUtility.FORMATTER.parse(o1.getTimeStamp()));
            } catch (ParseException e) {
                e.printStackTrace();
                return 1;
            }
        });

        return messageEntities;
    }

    @RequestMapping(value="/listener/concurrency", method= POST)
    public ResponseEntity<ListenerConcurrencyResponse> findListenerConcurrency(@RequestBody ListenerConcurrencyRequest request) {
        ListenerConcurrency source = restTemplate.getForObject(request.getSource(),ListenerConcurrency.class);
        ListenerConcurrency dest = restTemplate.getForObject(request.getDestination(),ListenerConcurrency.class);
        return new ResponseEntity<ListenerConcurrencyResponse>(new ListenerConcurrencyResponse(source,dest), HttpStatus.OK);
    }



}

