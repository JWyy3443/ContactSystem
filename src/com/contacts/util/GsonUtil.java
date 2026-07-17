package com.contacts.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gson工具类
 *
 * 【类说明】
 * - 提供Gson实例的全局获取
 * - 配置了LocalDateTime类型的序列化/反序列化适配器
 * - 简化JSON序列化操作
 *
 * 【Gson说明】
 * - Gson是Google提供的JSON处理库
 * - 用于Java对象与JSON字符串之间的转换
 * - serialize: 对象 -> JSON字符串
 * - deserialize: JSON字符串 -> 对象
 *
 * 【配置说明】
 * - 注册了LocalDateTime类型的适配器
 * - 日期格式：yyyy-MM-dd'T'HH:mm:ss（如 2024-01-15T10:30:00）
 * - 全局日期格式设置
 *
 * 【使用场景】
 * - Servlet层：将Java对象转换为JSON响应
 * - 前端数据交互：对象与JSON的互相转换
 *
 * 【使用示例】
 * Gson gson = GsonUtil.getInstance();
 * String json = gson.toJson(user);  // 对象转JSON
 * User user = gson.fromJson(json, User.class);  // JSON转对象
 */
public class GsonUtil {

    /** 日期时间格式化器，格式：yyyy-MM-dd'T'HH:mm:ss */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /** 全局Gson单例实例 */
    private static final Gson GSON = new GsonBuilder()
            // 注册LocalDateTime类型的序列化/反序列化适配器
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            // 设置全局日期格式
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            // 创建Gson实例
            .create();

    /**
     * 获取全局Gson实例
     *
     * 【功能】
     * - 返回配置好的Gson单例
     * - 每次调用返回同一实例
     *
     * 【返回】
     * - 配置了LocalDateTime适配器的Gson实例
     *
     * @return Gson实例
     */
    public static Gson getInstance() {
        return GSON;
    }

    /**
     * LocalDateTime类型适配器
     *
     * 【功能】
     * - 实现LocalDateTime与JSON的相互转换
     * - 序列化：LocalDateTime -> JSON字符串
     * - 反序列化：JSON字符串 -> LocalDateTime
     *
     * 【序列化格式】
     * - 输入：LocalDateTime对象
     * - 输出：格式化的时间字符串，如 "2024-01-15T10:30:00"
     *
     * 【反序列化格式】
     * - 输入：时间字符串
     * - 输出：LocalDateTime对象
     * - 支持null值处理
     */
    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

        /**
         * 序列化：LocalDateTime转JSON
         *
         * 【参数】
         * - src: LocalDateTime对象
         * - typeOfSrc: 类型信息
         * - context: 序列化上下文
         *
         * 【返回值】
         * - null值返回JsonNull
         * - 正常值返回格式化的JSON字符串
         */
        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) {
                // null值返回JsonNull
                return JsonNull.INSTANCE;
            }
            // 使用格式化器将LocalDateTime转换为字符串
            return new JsonPrimitive(src.format(FORMATTER));
        }

        /**
         * 反序列化：JSON转LocalDateTime
         *
         * 【参数】
         * - json: JSON元素
         * - typeOfT: 目标类型
         * - context: 反序列化上下文
         *
         * 【返回值】
         * - null值或空字符串返回null
         * - 正常值返回LocalDateTime对象
         *
         * 【异常】
         * - 格式错误抛出JsonParseException
         */
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (json == null || json.isJsonNull()) {
                // JSON为null或JsonNull，返回null
                return null;
            }

            String dateStr = json.getAsString();
            if (dateStr == null || dateStr.trim().isEmpty()) {
                // 字符串为空或空白，返回null
                return null;
            }

            // 使用格式化器解析字符串为LocalDateTime
            return LocalDateTime.parse(dateStr, FORMATTER);
        }
    }
}
