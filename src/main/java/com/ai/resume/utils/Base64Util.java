package com.ai.resume.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author ws
 * @date 2026/4/19 17:04
 */
public class Base64Util {

    /**
     * URL 安全的 Base64 编码（无填充，将 + 替换为 -，/ 替换为 _）
     */
    public static String encodeUrlSafe(String text) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * URL 安全的 Base64 解码
     */
    public static String decodeUrlSafe(String encoded) {
        byte[] decoded = Base64.getUrlDecoder().decode(encoded);
        return new String(decoded, StandardCharsets.UTF_8);
    }
}
