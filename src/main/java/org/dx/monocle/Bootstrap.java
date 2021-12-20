package org.dx.monocle;

import org.dx.monocle.core.task.CheckDiffTask;
import org.dx.monocle.utils.ScheduleUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author lizifeng
 * @date 2021/12/9 10:06
 */
public class Bootstrap {

    public static void main(String[] args) {
        ScheduleUtil.SCHEDULED_THREAD_POOL_EXECUTOR.scheduleAtFixedRate(new CheckDiffTask(), 3L, 60L, TimeUnit.SECONDS);
    }
}
