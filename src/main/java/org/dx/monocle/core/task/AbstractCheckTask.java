package org.dx.monocle.core.task;

import org.dx.monocle.utils.ScheduleUtil;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author lizifeng
 * @date 2021/12/14 15:07
 */
public abstract class AbstractCheckTask {

    protected static Map<String, Set<String>> checkedMap = new ConcurrentHashMap<>(16);

    protected void pushResendTask(Map<String, Set<String>> diffMap) {
        // merge checkedMap
        for (Map.Entry<String, Set<String>> entry : diffMap.entrySet()) {
            if (checkedMap.get(entry.getKey()) == null) {
                checkedMap.put(entry.getKey(), entry.getValue());
            } else {
                checkedMap.get(entry.getKey()).addAll(entry.getValue());
            }
        }

        ScheduleUtil.SCHEDULED_THREAD_POOL_EXECUTOR.schedule(new ResendTask(diffMap), 12L, TimeUnit.HOURS);
    }
}
