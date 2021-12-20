package org.dx.monocle.core.executor.impl;

import cn.hutool.core.io.IoUtil;
import org.dx.monocle.core.executor.CmdExecutor;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lizifeng
 * @date 2021/12/9 11:41
 */
public class CmdJavaExecutor implements CmdExecutor {

    private final Logger logger = LoggerFactory.getLogger(CmdJavaExecutor.class);

    private final String dir;

    public CmdJavaExecutor(String dir) {
        this.dir = dir;
    }

    @Override
    public String execute(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd, null, new File(dir));
            return IoUtil.read(process.getInputStream(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("execute cmd error, cmd=[{}]", cmd, e);
            throw new RuntimeException(e);
        }
    }
}
