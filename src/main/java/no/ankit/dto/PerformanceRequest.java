package no.ankit.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by AB75448 on 17.08.2016.
 */
@Getter
@Setter
public class PerformanceRequest{
    private int requests;
    private int concurrency;
    private String url;
    private String msg;
    private int sourceListenerConcurrency;
    private int destListenerConcurrency;
}
