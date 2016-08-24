package no.ankit.dto;

import lombok.*;

/**
 * Created by AB75448 on 18.08.2016.
 */
@Getter
@Setter
@AllArgsConstructor
public class ListenerConcurrency {

    private int domainListenerConcurrency;
    private int specificListenerConcurrency;

}
