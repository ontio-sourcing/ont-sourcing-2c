package com.ontology.sourcing2c.util._crypt;


import ch.qos.logback.classic.Logger;
import com.ontology.sourcing2c.util._codec.Base64ConvertUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * AES工具类，用来和前端通信加密
 * 加密向量IV_STRING必须是16位字符串，
 * Util里面也有AES了，这个设置了加密向量的AES，建议用这个
 */

@Component
public class AESUtil {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(AESUtil.class);

    private SecurePropertiesService sc;

    /**
     * 偏移量,必须是16位字符串
     */
    private String IV_STRING;

    @Autowired
    public AESUtil(SecurePropertiesService securePropertiesService) {
        sc = securePropertiesService;
        IV_STRING = sc.getAesIv();
    }

    /**
     * 产生随机密钥(这里产生密钥必须是16位)
     */
    public String generateKey() {
        String key = UUID.randomUUID().toString();
        key = key.replace("-", "").substring(0, 16);// 替换掉-号
        return key;
    }

    /**
     * 和util里面的区别是增加了ivParameterSpec，这个AES算法也要指定一样才能加解密
     * @param key
     * @param content
     * @return
     */
    public String encryptData(String key, String content) {
        byte[] encryptedBytes = new byte[0];
        try {
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            // 注意，为了能与 iOS 统一
            // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
            byte[] enCodeFormat = key.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            byte[] initParam = IV_STRING.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            encryptedBytes = cipher.doFinal(byteContent);
            // 同样对加密后数据进行 base64 编码

            return Base64ConvertUtil.encode(encryptedBytes);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public String decryptData(String key, String content) {
        try {
            // base64 解码
            byte[] encryptedBytes = Base64ConvertUtil.decodeAndReturnBytes(content);
            byte[] enCodeFormat = key.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
            byte[] initParam = IV_STRING.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] result = cipher.doFinal(encryptedBytes);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
