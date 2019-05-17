package com.ontology.sourcing2c.service.util;

import com.ontology.sourcing2c.dao.contract.ContractTypes;
import com.ontology.sourcing2c.service.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ValidateService {

    //
    private OAuthService oauthService;
    private PropertiesService propertiesService;

    //
    @Autowired
    public ValidateService(OAuthService oauthService, PropertiesService propertiesService) {
        //
        this.oauthService = oauthService;
        this.propertiesService = propertiesService;
    }

    private void validateOntid(String ontid) throws Exception {
        //
        if (StringUtils.isEmpty(ontid))
            throw new Exception("ontid is empty.");
        //
        if (ontid.length() != 42)
            throw new Exception("ontid length should be 42.");
        //
        if (!ontid.toLowerCase().contains("did:ont:A".toLowerCase()))
            throw new Exception("ontid format invalid.");
        //
        // isExistedOntid(ontid);
    }

    private void validateFilehash(String filehash) throws Exception {
        //
        if (StringUtils.isEmpty(filehash))
            throw new Exception("filehash is empty.");
        //
        if (filehash.length() != 64)
            throw new Exception("filehash length should be 64.");
    }

    private void validateTxhash(String txhash) throws Exception {
        //
        if (StringUtils.isEmpty(txhash))
            throw new Exception("txhash is empty.");
        //
        if (txhash.length() != 64)
            throw new Exception("txhash length should be 64.");
    }

    private void validateHash(String hash) throws Exception {
        //
        if (StringUtils.isEmpty(hash))
            throw new Exception("hash is empty.");
        //
        if (hash.length() != 64)
            throw new Exception("hash length should be 64.");
    }

    public void validateParamsKeys(LinkedHashMap<String, Object> obj, Set<String> required) throws Exception {
        Iterator<String> requiredIterator = required.iterator();
        while (requiredIterator.hasNext()) {
            String next = requiredIterator.next();
            if (!obj.containsKey(next))
                throw new Exception(next + " is missing.");
        }
    }

    public void validateParamsValues(LinkedHashMap<String, Object> obj) throws Exception {

        if (obj.containsKey("path_key")) {
            String path_key = (String) obj.get("path_key");
            if (StringUtils.isEmpty(path_key)) {
                throw new Exception("path_key is empty.");
            }
        }

        if (obj.containsKey("username")) {
            String username = (String) obj.get("username");
            //
            if (StringUtils.isEmpty(username)) {
                throw new Exception("username is empty.");
            }
            // TODO is existed
        }

        if (obj.containsKey("password")) {
            String password = (String) obj.get("password");
            if (StringUtils.isEmpty(password)) {
                throw new Exception("password is empty.");
            }
        }

        if (obj.containsKey("passphrase")) {
            String passphrase = (String) obj.get("passphrase");
            if (StringUtils.isEmpty(passphrase)) {
                throw new Exception("passphrase is empty.");
            }
        }

        if (obj.containsKey("attribute")) {
            String attribute = (String) obj.get("attribute").toString();
            if (StringUtils.isEmpty(attribute)) {
                throw new Exception("attribute is empty.");
            }
        }

        if (obj.containsKey("controlPassword")) {
            String controlPassword = (String) obj.get("controlPassword");
            if (StringUtils.isEmpty(controlPassword)) {
                throw new Exception("controlPassword is empty.");
            }
        }

        //
        if (obj.containsKey("detail")) {
            // detail 肯定是一个数组
            Object o = obj.get("detail");
            if (o instanceof List<?>) {
                return;
            } else {
                throw new Exception("detail should be List<?>.");
            }
        }

        if (obj.containsKey("type")) {
            String type = (String) obj.get("type");
            if (!ContractTypes.contains(type) && !StringUtils.isEmpty(type)) {
                throw new Exception("type " + type + " is incorrect.");
            }
        }

        if (obj.containsKey("filehash")) {
            String filehash = (String) obj.get("filehash");
            validateFilehash(filehash);
        }

        if (obj.containsKey("txhash")) {
            String txhash = (String) obj.get("txhash");
            validateTxhash(txhash);
        }

        if (obj.containsKey("hash")) {
            String hash = (String) obj.get("hash");
            validateHash(hash);
        }

        if (obj.containsKey("ontid")) {
            String ontid = (String) obj.get("ontid");
            validateOntid(ontid);
        }

        if (obj.containsKey("controlOntid")) {
            String controlOntid = (String) obj.get("controlOntid");
            validateOntid(controlOntid);
        }

        if (obj.containsKey("user_ontid")) {
            String user_ontid = (String) obj.get("user_ontid");
            //
            if (!StringUtils.isEmpty(user_ontid)) {
                //
                if (user_ontid.length() != 42)
                    throw new Exception("user_ontid length should be 42.");
                //
                if (!user_ontid.toLowerCase().contains("did:ont:A".toLowerCase()))
                    throw new Exception("user_ontid format invalid.");
            }
        }

        if (obj.containsKey("pageNum")) {
            Integer pageNum = Integer.valueOf(obj.get("pageNum").toString());
            if (StringUtils.isEmpty(pageNum))
                throw new Exception("pageNum not valid.");
        }

        if (obj.containsKey("pageSize")) {
            Integer pageSize = Integer.valueOf(obj.get("pageSize").toString());
            if (StringUtils.isEmpty(pageSize) || pageSize > 10)
                throw new Exception("pageSize not valid.");
        }

        if (obj.containsKey("access_token")) {
            String access_token = (String) obj.get("access_token");
            if (StringUtils.isEmpty(access_token)) {
                throw new Exception("access_token is empty.");
            }
            // 验证 token
            oauthService.verify(access_token);
        }

        // 司法链
        if (obj.containsKey("userType")) {
            String userType = (String) obj.get("userType");
            if (StringUtils.isEmpty(userType)) {
                throw new Exception("userType is empty.");
            }
            //
            if (!"PERSON".equals(userType) && !"ENTERPRISE".equals(userType)) {
                throw new Exception("userType is incorrect.");
            }
        }

        if (obj.containsKey("certType")) {
            String certType = (String) obj.get("certType");
            if (StringUtils.isEmpty(certType)) {
                throw new Exception("certType is empty.");
            }
            //
            if (!"IDENTITY_CARD".equals(certType) && !"UNIFIED_SOCIAL_CREDIT_CODE".equals(certType) && !"ENTERPRISE_REGISTERED_NUMBER".equals(certType)) {
                throw new Exception("cert_type is incorrect.");
            }
        }

        if (obj.containsKey("certName")) {
            String certName = (String) obj.get("certName");
            if (StringUtils.isEmpty(certName)) {
                throw new Exception("certName is empty.");
            }
        }

        if (obj.containsKey("certNo")) {
            String certNo = (String) obj.get("certNo");
            //
            if (StringUtils.isEmpty(certNo)) {
                throw new Exception("certNo is empty.");
            }
            //
            if (certNo.length() != 15 && certNo.length() != 18) {
                throw new Exception("certNo length incorrect.");
            }

            // TODO 其它类型的证件号
            // if (obj.containsKey("certType")) {
            //     String certType = (String) obj.get("certType");
            //     if ("IDENTITY_CARD".equals(certType)) {
            //         if (certNo.length() != 15 && certNo.length() != 18) {
            //             throw new Exception("certNo length incorrect.");
            //         }
            //     }
            // }
        }

        if (obj.containsKey("prikey")) {
            String prikey = (String) obj.get("prikey");
            //
            if (StringUtils.isEmpty(prikey)) {
                throw new Exception("prikey is empty.");
            }
            //
            if (prikey.length() != 64)
                throw new Exception("prikey length should be 64.");
        }

        if (obj.containsKey("code_addr")) {
            String code = (String) obj.get("code_addr");
            //
            if (StringUtils.isEmpty(code)) {
                throw new Exception("code_addr is empty.");
            }
        }



        //
        if (obj.containsKey("filelist")) {
            ArrayList<Map<String, Object>> filelist = (ArrayList<Map<String, Object>>) obj.get("filelist");
            //
            if (filelist.size() == 0) {
                throw new Exception("filelist contains no elements.");
            }
            // TODO
            if (filelist.size() >= 30) {
                throw new Exception("filelist contains too many elements. max is 30.");
            }
            //
            for (Map<String, Object> item : filelist) {
                // 检查 key
                if (!item.containsKey("filehash")) {
                    throw new Exception("param filehash is missing.");
                }
                if (!item.containsKey("type")) {
                    throw new Exception("param type is missing.");
                }
                if (!item.containsKey("detail")) {
                    throw new Exception("param detail is missing.");
                }
                // 检查 value
                if (item.containsKey("filehash")) {
                    String filehash = item.get("filehash").toString();
                    validateHash(filehash);
                }
                if (item.containsKey("type")) {
                    String type = item.get("type").toString();
                    if (!ContractTypes.contains(type)) {
                        throw new Exception("type " + type + " is incorrect.");
                    }
                }
                if (item.containsKey("detail")) {
                    // detail 肯定是一个数组
                    Object o = item.get("detail");
                    if (o instanceof List<?>) {
                        return;
                    } else {
                        throw new Exception("detail should be List<?>.");
                    }
                }
            }
        }

        //
        if (obj.containsKey("ont_password")) {
            String ont_password = (String) obj.get("ont_password");
            //
            if (StringUtils.isEmpty(ont_password)) {
                throw new Exception("ont_password is empty.");
            }
            //
            if (!propertiesService.ontPassword.equals(ont_password)) {
                throw new Exception("ont_password is incorrect.");
            }
        }

        //
        if (obj.containsKey("phone_cn")) {
            String phone_cn = (String) obj.get("phone_cn");
            if (StringUtils.isEmpty(phone_cn)) {
                throw new Exception("phone_cn is empty.");
            }
            //
            if (!StringUtils.startsWithIgnoreCase(phone_cn, "86*")) {
                throw new Exception("phone_cn should start with 86* then phone number.");
            }
            //
            if (phone_cn.length() != 14) {
                throw new Exception("phone_cn length is incorrect.");
            }
            //
            String s = phone_cn.replace("86*", "");
            if (!s.matches("^[0-9]*$") || !StringUtils.startsWithIgnoreCase(s, "1")) {
                throw new Exception("phone_cn is incorrect.");
            }
        }
    }
}
