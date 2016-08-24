package no.ankit.dto;

import lombok.*;

/**
 * Created by AB75448 on 17.08.2016.
 */
@Getter
@Setter
public class PerformanceResponse {

    private String performanceId;
    private MinMax rest;
    private MinMax reqSendRec;
    private MinMax serviceExe;
    private MinMax resSendRec;
    private MinMax messaging;
    private double completedIn;
    private double mean;
    private double mediun;
    private String status;
    private String message;
    private int requests;
    private int concurrency;
    private int sourceListenerConcurrency;
    private int destListenerConcurrency;

}

