package no.ankit.message;

import no.ankit.util.MessageUtility;
import no.ankit.util.SseUtility;
import no.dnb.vaap.common.messaging.domain.MessageEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AB75448 on 15.08.2016.
 */
public class TopicListener {

    public void listen(MessageEntity message) {
        if(MessageUtility.correlationMap.containsKey(message.getJMSCorrelationID())){
            MessageUtility.correlationMap.get(message.getJMSCorrelationID()).add(message);
        }else{
            List<MessageEntity> entityList = new ArrayList<>();
            entityList.add(message);
            MessageUtility.correlationMap.put(message.getJMSCorrelationID(), entityList);
        }

        if(MessageUtility.operationMap.containsKey(message.getOpCode())){
            MessageUtility.operationMap.get(message.getOpCode()).add(message);
        }else{
            List<MessageEntity> opEntityList = new ArrayList<>();
            opEntityList.add(message);
            MessageUtility.operationMap.put(message.getOpCode(), opEntityList);
        }
        System.out.println("Message received to topic:-"+message.toString());
        SseUtility.emitters.stream().forEach((emitter->{
            try {
                emitter.send(message);
            }catch (Exception e){

            }
        }));
    }

}
