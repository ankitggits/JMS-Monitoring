package no.ankit.controller;

import no.ankit.dto.PerformanceRequest;
import no.ankit.dto.PerformanceResponse;
import no.ankit.service.PerformanceService;
import no.ankit.util.TestUtility;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by AB75448 on 17.08.2016.
 */
@RestController
public class PerformanceController {

    @Autowired
    PerformanceService service;

    @RequestMapping(value= "/performance", method= POST, consumes = "application/json", produces = "application/json")
    public PerformanceResponse handleOpen(@RequestBody PerformanceRequest performanceRequest) {
        PerformanceResponse performanceResponse = service.test(performanceRequest);
        performanceResponse.setPerformanceId(UUID.randomUUID().toString());
        TestUtility.performanceResults.add(performanceResponse);
        return performanceResponse;
    }

    @RequestMapping(value= "/performance", method= GET, produces = "application/json")
    public List<PerformanceResponse> handleOpen() {
        return Lists.reverse(TestUtility.performanceResults);
    }

    @RequestMapping(value= "/performance/{performanceId}", method= DELETE, produces = "application/json")
    public ResponseEntity<?> handleDelete(@PathVariable String performanceId) {
        ListIterator<PerformanceResponse> listIterator = TestUtility.performanceResults.listIterator();
        while(listIterator.hasNext()){
            if(listIterator.next().getPerformanceId().equals(performanceId)){
                listIterator.remove();
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}