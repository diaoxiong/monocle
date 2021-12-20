package org.dx.monocle.core.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import org.dx.monocle.core.operator.GitOperator;
import org.dx.monocle.core.model.RepositoryConfig;
import org.dx.monocle.utils.ConfigUtil;
import org.dx.monocle.utils.MsgUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lizifeng
 * @date 2021/12/10 17:05
 */
public class ResendTask extends AbstractCheckTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(CheckDiffTask.class);

    private final Map<String, Set<String>> diffMap;

    public ResendTask(Map<String, Set<String>> diffMap) {
        this.diffMap = diffMap;
    }

    @Override
    public void run() {
        logger.info("resendTask start");
        try {
            // checkedMap中删除diffMap
            for (Map.Entry<String, Set<String>> entry : diffMap.entrySet()) {
                checkedMap.get(entry.getKey()).removeAll(entry.getValue());
            }

            // 重新compare
            Map<String, Set<String>> newDiffMap = new HashMap<>(16);
            for (Map.Entry<String, Set<String>> entry : diffMap.entrySet()) {
                String repositoryName = entry.getKey();
                GitOperator gitOperator = new GitOperator(repositoryName);
                RepositoryConfig repositoryConfig = ConfigUtil.getRepositoryConfig(repositoryName);
                Set<String> diffBranchSet = gitOperator.compareDiff(repositoryConfig.getSourceBranch(), entry.getValue());
                if (CollectionUtil.isNotEmpty(diffBranchSet)) {
                    newDiffMap.put(repositoryName, diffBranchSet);
                }
            }

            // sendMsg
            if (MapUtil.isNotEmpty(newDiffMap)) {
                String msg = MsgUtil.buildMsg(newDiffMap);
                MsgUtil.sendMsg(msg);
                pushResendTask(newDiffMap);
            }
        } catch (Exception e) {
            logger.error("recheck diff error", e);
        }
    }
}
