package no.ankit.util;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AB75448 on 16.08.2016.
 */
public class SseUtility {
    public static final List<SseEmitter> emitters = new ArrayList<>();
}
