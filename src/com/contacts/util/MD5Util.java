package com.contacts.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 *
 * 【类说明】
 * - 提供MD5单向哈希加密功能
 * - 用于密码加密存储
 *
 * 【MD5算法说明】
 * - MD5（Message-Digest Algorithm 5）是一种哈希算法
 * - 将任意长度的数据转换为128位的哈希值
 * - 具有不可逆性：无法从哈希值还原原始数据
 * - 具有固定性：相同输入总是产生相同输出
 *
 * 【安全说明】
 * - MD5已被证明不够安全（可被暴力破解）
 * - 对于高安全需求，建议使用BCrypt等更强算法
 * - 本项目使用MD5作为基础密码保护
 *
 * 【使用场景】
 * - 用户注册时对密码进行加密
 * - 用户登录时对输入密码加密后比对
 *
 * 【输出格式】
 * - 返回32位十六进制字符串
 * - 例如： plaintext -> "5f4dcc3b5aa765d61d8327deb882cf99"
 */
public class MD5Util {

    /**
     * 对明文进行MD5加密
     *
     * 【功能】
     * - 将输入字符串转换为MD5哈希值
     * - 返回32位十六进制表示
     *
     * 【处理流程】
     * 1. 判断输入是否为空，为空直接返回null
     * 2. 获取MD5算法实例
     * 3. 将输入字符串转换为UTF-8字节数组
     * 4. 执行哈希计算
     * 5. 将结果字节数组转换为十六进制字符串
     *
     * 【异常处理】
     * - NoSuchAlgorithmException: MD5算法不存在（不会发生）
     * - UnsupportedEncodingException: UTF-8编码不支持（不会发生）
     * - 发生异常时抛出RuntimeException
     *
     * @param plainText 明文密码（未加密）
     * @return MD5哈希值（32位十六进制字符串），输入为null时返回null
     */
    public static String encrypt(String plainText) {
        // 【第一步】空值检查
        if (plainText == null) {
            return null;
        }

        try {
            // 【第二步】获取MD5算法实例
            // MessageDigest是Java提供的哈希算法封装类
            // "MD5"是算法名称
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 【第三步】执行哈希计算
            // 将字符串转换为UTF-8字节数组后计算哈希
            byte[] digest = md.digest(plainText.getBytes("UTF-8"));

            // 【第四步】构建十六进制字符串
            // MD5结果为16字节，每个字节转换为2位十六进制
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                // %02x: 格式化为2位十六进制，不足前面补0
                sb.append(String.format("%02x", b));
            }

            // 【第五步】返回哈希值字符串
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            // MD5算法理论上始终可用，如果发生异常将其包装为运行时异常
            throw new RuntimeException("MD5 encryption failed", e);
        } catch (java.io.UnsupportedEncodingException e) {
            // UTF-8编码始终可用，同样包装为运行时异常
            throw new RuntimeException("UTF-8 encoding not supported", e);
        }
    }
}
