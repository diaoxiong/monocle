package org.dx.monocle.core.operator;

import cn.hutool.core.util.StrUtil;
import org.dx.monocle.core.executor.CmdExecutor;
import org.dx.monocle.core.executor.impl.CmdJavaExecutor;
import org.dx.monocle.utils.ConfigUtil;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lizifeng
 * @date 2021/12/14 14:34
 */
public class GitOperator {

    private final String repositoryName;

    private final String dir;

    public GitOperator(String repositoryName) {
        this.repositoryName = repositoryName;
        this.dir = ConfigUtil.getConfig().getWorkspace() + File.separator + this.repositoryName;
    }

    public Set<String> getRemoteBranch(String destBranchPrefix) {
        CmdExecutor cmdExecutor = new CmdJavaExecutor(dir);
        String result = cmdExecutor.execute("git branch -r");
        String prefix = "origin/" + destBranchPrefix;
        return Arrays.stream(result.split("\n")).map(String::trim).filter(s -> s.startsWith(prefix)).collect(Collectors.toSet());
    }

    public Set<String> compareDiff(String sourceBranch, Set<String> remoteBranchList) {
        Set<String> diffBranchSet = new HashSet<>();
        for (String remoteBranch : remoteBranchList) {
            CmdExecutor cmdExecutor = new CmdJavaExecutor(dir);
            String cmd = "git diff origin/" + sourceBranch + " " + remoteBranch;
            String result = cmdExecutor.execute(cmd);
            boolean hasDiff = StrUtil.isNotBlank(result);
            if (hasDiff) {
                diffBranchSet.add(remoteBranch);
            }
        }
        return diffBranchSet;
    }

    public void fetch() {
        CmdExecutor cmdExecutor = new CmdJavaExecutor(dir);
        cmdExecutor.execute("git fetch");
    }

    public void remotePruneOrigin() {
        CmdExecutor cmdExecutor = new CmdJavaExecutor(dir);
        cmdExecutor.execute("git remote prune origin");
    }
}
