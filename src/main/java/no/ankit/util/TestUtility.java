package no.ankit.util;

import no.ankit.dto.PerformanceResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AB75448 on 11.08.2016.
 */
public class TestUtility {

    public static final List<PerformanceResponse> performanceResults = new ArrayList<>();

    public static double mean(List<Long> m) {
        double sum = 0;
        for (int i = 0; i < m.size(); i++) {
            sum += m.get(i);
        }
        return sum / m.size();
    }

    public static double median(List<Long> m) {
        int middle = m.size()/2;
        if (m.size()%2 == 1) {
            return m.get(middle);
        } else {
            return (m.get(middle-1) + m.get(middle)) / 2;
        }
    }


}
