package org.dx.monocle.core.model;

import lombok.Data;

/**
 * @author lizifeng
 * @date 2021/12/9 10:39
 */
@Data
public class RepositoryConfig {

    private String repositoryName;

    private String sourceBranch;

    private String destBranchPrefix;
}
