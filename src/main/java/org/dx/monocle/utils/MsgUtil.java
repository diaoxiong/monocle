package org.dx.monocle.utils;

import org.dx.monocle.core.model.Config;
import org.dx.monocle.core.msg.sender.LarkMsgSender;
import org.dx.monocle.core.msg.sender.MsgSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author lizifeng
 * @date 2021/12/13 13:50
 */
public class MsgUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgUtil.class);

    private static final Map<String, MsgSender> MSG_SENDER_MAP = new HashMap<>();

    static {
        MSG_SENDER_MAP.put("lark", new LarkMsgSender());
    }

    public static void sendMsg(String msg) {
        Config config = ConfigUtil.getConfig();
        MSG_SENDER_MAP.get(config.getMsgSenderName()).sendMsg(msg);
        LOGGER.info(msg);
    }

    public static String buildMsg(Map<String, Set<String>> diffMap) {
        Config config = ConfigUtil.getConfig();
        StringBuilder stringBuilder = new StringBuilder(config.getTextPrefix());
        for (Map.Entry<String, Set<String>> entry : diffMap.entrySet()) {
            stringBuilder.append(entry.getKey()).append(": \n");
            for (String branch : entry.getValue()) {
                stringBuilder.append("    ").append(branch.substring(7)).append("\n");
            }
        }
        stringBuilder.append(config.getTextPostfix());
        return stringBuilder.toString();
    }
}
