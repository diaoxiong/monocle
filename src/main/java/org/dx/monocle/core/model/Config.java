package org.dx.monocle.core.model;

import java.util.List;
import lombok.Data;

/**
 * @author lizifeng
 * @date 2021/12/9 10:41
 */
@Data
public class Config {

    private String workspace;

    private List<RepositoryConfig> repositoryConfigs;

    private String webhook;

    private String secret;

    private String textPrefix;

    private String textPostfix;

    private String msgSenderName;
}
