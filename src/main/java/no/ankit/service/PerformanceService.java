package no.ankit.service;

import no.ankit.dto.MinMax;
import no.ankit.dto.PerformanceRequest;
import no.ankit.dto.PerformanceResponse;
import no.ankit.util.TestUtility;
import no.dnb.vaap.common.messaging.monitor.TestComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;

/**
 * Created by AB75448 on 17.08.2016.
 */
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
public class PerformanceService {

    @Autowired
    RestTemplate restTemplate;

    private boolean isNotStarted = false;

    private StopWatch watch = new StopWatch();

    private volatile int successFlowCount = 0;

    private volatile long min = Long.MIN_VALUE;

    private volatile long max = Long.MAX_VALUE;

    private volatile long minSendRec, minServiceEx, minAckRec, minMessaging = Long.MIN_VALUE;

    private volatile long maxSendRec, maxServiceEx, maxAckRec, maxMessaging= Long.MAX_VALUE;

    public PerformanceResponse test(PerformanceRequest request){
        setup();
        PerformanceResponse response = new PerformanceResponse();
        CountDownLatch countDownLatch = new CountDownLatch(request.getRequests());
        ExecutorService executor = Executors.newFixedThreadPool(request.getConcurrency());
        CopyOnWriteArrayList<Long> messagingValues = new CopyOnWriteArrayList<>();
        for (int i = 1; i <= request.getRequests(); i++) {
            executor.submit(() -> {
                StopWatch internalWatch = new StopWatch();
                String msgToSend = request.getMsg();
                if(StringUtils.isEmpty(msgToSend)){
                    msgToSend = Thread.currentThread().getName();
                }
                String expectedMsg = "Hello ".concat(msgToSend);
                boolean isSuccess = false;
                internalWatch.start();
                TestComponent result = null;
                try {
                    result = restTemplate.getForObject(request.getUrl() + "/"+msgToSend, TestComponent.class);
                    if (result != null && result.getVal().equals(expectedMsg)) {
                        isSuccess = true;
                    } else {
                        System.out.println("Failed Result :-"+result);
                        isSuccess = false;
                    }
                } catch (Exception e) {
                    System.err.println("Error Result :-"+e.getCause());
                    e.printStackTrace();
                    isSuccess = false;
                }
                internalWatch.stop();
                if (isSuccess) {
                    incrementSuccess();
                    long timeTakenSendRec = result.getRecTime()-result.getSentTime();
                    long timeTakenServiceExecution = result.getAckSentTime() - result.getRecTime();
                    long timeTakenAckRec = result.getAckRecTime()-result.getAckSentTime();
                    long timeTakenMessaging = timeTakenSendRec+timeTakenAckRec;
                    messagingValues.add(timeTakenMessaging);
                    if(countDownLatch.getCount()==request.getRequests()){
                        minSendRec = maxSendRec = timeTakenSendRec;
                        minAckRec = maxAckRec = timeTakenAckRec;
                        minServiceEx = maxServiceEx = timeTakenServiceExecution;
                        minMessaging = maxMessaging = timeTakenMessaging;
                    }else{

                        if(timeTakenSendRec < minSendRec){
                            minSendRec = timeTakenSendRec;
                        }else if(timeTakenSendRec>maxSendRec){
                            maxSendRec = timeTakenSendRec;
                        }

                        if(timeTakenServiceExecution < minServiceEx){
                            minServiceEx = timeTakenServiceExecution;
                        }else if(timeTakenServiceExecution> maxServiceEx){
                            maxServiceEx = timeTakenServiceExecution;
                        }

                        if(timeTakenAckRec < minAckRec){
                            minAckRec = timeTakenAckRec;
                        }else if(timeTakenAckRec>maxAckRec){
                            maxAckRec = timeTakenAckRec;
                        }

                        if(timeTakenMessaging < minMessaging){
                            minMessaging = timeTakenMessaging;
                        }else if(timeTakenMessaging>maxMessaging){
                            maxMessaging = timeTakenMessaging;
                        }
                    }
					/*System.out.println("completed for msg:   " + msg + " ,item remaining to complete: "
							+ (countDownLatch.getCount() - 1)+" ,Time analysis: "
							+"  Send-Rec-"+timeTakenSendRec
							+" ,SERVICE EX-"+timeTakenServiceExecution
							+" ,ACK-REC-"+timeTakenAckRec
							+" ,Messaging-"+timeTakenMessaging
							);*/
                } else {
                    System.err.println("Failed for msg: " + msgToSend + " ,item remaining to complete: "
                            + (countDownLatch.getCount() - 1) + ",came: " + result);
                }
                long timeTaken = internalWatch.getTotalTimeMillis();
                if (countDownLatch.getCount() == request.getRequests()) {
                    min = max = timeTaken;
                } else {
                    if (timeTaken < min) {
                        min = timeTaken;
                    } else if (timeTaken > max) {
                        max = timeTaken;
                    }
                }
                countDownLatch.countDown();
            });
        }

        while (countDownLatch.getCount() != 0) {
            try {
                countDownLatch.await(4, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                try {
                    Thread.sleep(4000L);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

        executor.shutdown();
        watch.stop();

        if(request.getRequests() == successFlowCount){
            response.setStatus("success");
            response.setMessage("PROCESS COMPLETED SUCCESSFULLY");
            response.setRest(new MinMax(min,max));
            response.setReqSendRec(new MinMax(minSendRec,maxSendRec));
            response.setServiceExe(new MinMax(minServiceEx,maxServiceEx));
            response.setResSendRec(new MinMax(minAckRec,maxAckRec));
            response.setMessaging(new MinMax(minMessaging,maxMessaging));
            response.setCompletedIn(watch.getTotalTimeMillis());

            messagingValues.sort(Long::compareTo);
            double mean = TestUtility.mean(messagingValues);
            double mediun = TestUtility.median(messagingValues);
            response.setMean(mean);
            response.setMediun(mediun);

            System.out.println(
                    "--------------------------------PROCESS COMPLETED SUCCESSFULLY---------------------------------");
            System.out.println("------------------------------- Time analysis-MIN:-" + min + " MAX:-"
                    + max + "--------------------------------");
            System.out.println("---------------------------- Time analysis (SEND-REC)-MIN:-" + minSendRec + " MAX:-"
                    + maxSendRec + "-----------------------------");
            System.out.println("---------------------------- Time analysis (SERVICE EX)-MIN:-" + minServiceEx + " MAX:-"
                    + maxServiceEx + "----------------------------");
            System.out.println("---------------------------- Time analysis (ACK-REC)-MIN:-" + minAckRec + " MAX:-"
                    + maxAckRec + "-----------------------------");
            System.out.println("---------------------------- Time analysis (Messaging)-MIN:-" + minMessaging + " MAX:-"
                    + maxMessaging + "----------------------------");


            System.out.println(
                    "******************************************MEAN   : "+mean+"*********************************************************");
            System.out.println(
                    "******************************************MEDIAN : "+mediun+"*******************************************************");
        }else{
            response.setStatus("failed");
            System.out.println("***********************************************************************************************");
            if (isNotStarted) {
                response.setMessage("MQ NOT WORKING PROPERLY , MAY BE TIMEOUT");
                System.out.println("-----------------------------MQ NOT WORKING PROPERLY , MAY BE TIMEOUT--------------------------");
            } else if (successFlowCount == 0) {
                response.setMessage("PROCESS FAILED COMPLETELY");
                System.out.println("----------------------------------PROCESS FAILED COMPLETELY------------------------------------");
            } else if (successFlowCount != request.getRequests()) {
                response.setMessage("PROCESS PARTIALY COMPLETED("+ (request.getRequests() - successFlowCount) + "-FAILED)");
                System.out.println("-------------------------------PROCESS PARTIALY COMPLETED("
                        + (request.getRequests() - successFlowCount) + "-FAILED)-----------------------------");
            }
        }

        System.out.println(
                "_______________________________________________________________________________________________");
        System.out.println(
                "***********************************************************************************************");
        System.out.println("***************"+request.getRequests()+" REQUESTS WITH "+request.getConcurrency()+" CONCURRENT THREADS FINISHED IN Rest: " + watch.getTotalTimeMillis()
                +  " ***************");

        System.out.println(
                "******************************************************************************************************");

        response.setRequests(request.getRequests());
        response.setConcurrency(request.getConcurrency());
        response.setSourceListenerConcurrency(request.getSourceListenerConcurrency());
        response.setDestListenerConcurrency(request.getDestListenerConcurrency());
        return response;
    }

    private void setup(){

        System.out.println(
                "***********************************************************************************************");
        System.out.println(
                "----------------------------------------PROCESS STARTED----------------------------------------");
        System.out.println(
                "_______________________________________________________________________________________________");
        System.out.println(
                "***********************************************************************************************");
        watch.start();
    }

    private synchronized void incrementSuccess() {
        successFlowCount++;
    }

}
