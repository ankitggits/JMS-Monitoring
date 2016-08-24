package no.ankit.controller;

import no.ankit.util.SseUtility;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by AB75448 on 14.08.2016.
 */
@RestController
public class ServerSentController {

    @RequestMapping(value="/emitter/open", method= GET)
    public SseEmitter handleOpen() throws IOException {
        SseEmitter emitter = new SseEmitter(600000L);
        SseUtility.emitters.add(emitter);
        return emitter;
    }

    @RequestMapping(value="/emitter/close", method= GET)
    public String handleClose() throws IOException {
        SseUtility.emitters.stream().forEach((emitter->{
            try {
                emitter.complete();
            }catch (Exception e){

            }
        }));
        return "emitter closed";
    }

}
