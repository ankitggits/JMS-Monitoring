package no.ankit.util;

import no.dnb.vaap.common.messaging.domain.MessageEntity;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AB75448 on 16.08.2016.
 */
public class MessageUtility {

    public static final Map<String, List<MessageEntity>>  correlationMap;
    public static final Map<String, List<MessageEntity>>  operationMap;
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    static{
        correlationMap = new LinkedHashMap<>();
        operationMap = new LinkedHashMap<>();
    }

}
