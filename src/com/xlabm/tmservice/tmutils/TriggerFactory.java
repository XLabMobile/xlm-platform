package com.xlabm.tmservice.tmutils;

import java.util.HashMap;

/**
 * XLAB Mobile Version 0.1 Alpha Release
 * User: SID@XLABM
 * Date: 5/21/13
 * Time: 6:45 AM
 * Responsibility of Class:
 */
public class TriggerFactory {

    public Trigger makeTrigger(String qid, String type, HashMap<String,
            String> params) {
        if (type.equals("GeoTrigger")) {
            return new GeoTrigger(qid, params);
        } else if (type.equals("TimeTrigger")) {
            return new TimerTrigger(qid, params);
        } else if (type.equals("default")) {
            return new DefaultTrigger(qid, params);
        } else
            return new DefaultTrigger(qid, params);

    }
}
