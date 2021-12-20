package org.dx.monocle.core.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import org.dx.monocle.core.operator.GitOperator;
import org.dx.monocle.core.model.Config;
import org.dx.monocle.core.model.RepositoryConfig;
import org.dx.monocle.utils.ConfigUtil;
import org.dx.monocle.utils.MsgUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lizifeng
 * @date 2021/12/9 10:10
 */
public class CheckDiffTask extends AbstractCheckTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(CheckDiffTask.class);

    @Override
    public void run() {
        logger.info("checkDiffTask is running...");
        Config config = ConfigUtil.getConfig();

        try {
            Map<String, Set<String>> diffMap = new HashMap<>(16);
            List<RepositoryConfig> repositoryConfigList = config.getRepositoryConfigs();
            for (RepositoryConfig repositoryConfig : repositoryConfigList) {
                GitOperator gitOperator = new GitOperator(repositoryConfig.getRepositoryName());
                gitOperator.fetch();
                gitOperator.remotePruneOrigin();
                Set<String> remoteBranchSet = gitOperator.getRemoteBranch(repositoryConfig.getDestBranchPrefix());
                filterBranch(remoteBranchSet, repositoryConfig.getRepositoryName());
                Set<String> diffBranchSet = gitOperator.compareDiff(repositoryConfig.getSourceBranch(), remoteBranchSet);

                if (CollectionUtil.isNotEmpty(diffBranchSet)) {
                    diffMap.put(repositoryConfig.getRepositoryName(), diffBranchSet);
                }
            }

            if (MapUtil.isNotEmpty(diffMap)) {
                String msg = MsgUtil.buildMsg(diffMap);
                MsgUtil.sendMsg(msg);
                pushResendTask(diffMap);
            }
        } catch (Exception e) {
            logger.error("compare diff error", e);
        }
    }

    private void filterBranch(Set<String> remoteBranchSet, String repositoryName) {
        if (checkedMap.get(repositoryName) != null) {
            remoteBranchSet.removeAll(checkedMap.get(repositoryName));
        }
    }
}
