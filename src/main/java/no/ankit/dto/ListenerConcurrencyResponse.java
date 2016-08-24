package no.ankit.dto;

import lombok.*;

/**
 * Created by AB75448 on 18.08.2016.
 */
@Getter
@Setter
@AllArgsConstructor
public class ListenerConcurrencyResponse {
    private ListenerConcurrency source;
    private ListenerConcurrency destination;
}
