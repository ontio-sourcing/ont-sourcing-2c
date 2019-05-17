package com.ontology.sourcing2c.controller;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.common.Helper;
import com.google.gson.Gson;
import com.ontology.sourcing2c.dao.contract.Contract;
import com.ontology.sourcing2c.model.contract.cyano.*;
import com.ontology.sourcing2c.model.contract.input.InputWrapper;
import com.ontology.sourcing2c.model.util.Result;
import com.ontology.sourcing2c.service.ContractService;
import com.ontology.sourcing2c.service.oauth.OAuthService;
import com.ontology.sourcing2c.service.ontid_server.OntidServerService;
import com.ontology.sourcing2c.service.time.bctsp.TspService;
import com.ontology.sourcing2c.service.util.*;
import com.ontology.sourcing2c.util.GlobalVariable;
import com.ontology.sourcing2c.util.exp.ErrorCode;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api/v1/c/")
public class ContractController {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(ContractController.class);
    private Gson gson = new Gson();

    //
    private PropertiesService propertiesService;
    //
    private TspService tspService;
    private SyncService syncService;
    private ValidateService validateService;
    //
    private OAuthService oauthService;
    private ContractService contractService;
    private OntidServerService ontidServerService;
    //
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public ContractController(PropertiesService propertiesService,
                              TspService tspService,
                              SyncService syncService,
                              ValidateService validateService,
                              OAuthService oauthService,
                              ContractService contractService,
                              OntidServerService ontidServerService,
                              KafkaTemplate<String, Object> kafkaTemplate) {
        //
        this.propertiesService = propertiesService;
        //
        this.tspService = tspService;
        this.syncService = syncService;
        this.validateService = validateService;
        //
        this.oauthService = oauthService;
        this.contractService = contractService;
        this.ontidServerService = ontidServerService;
        //
        this.kafkaTemplate = kafkaTemplate;
    }

    //
    @PostConstruct
    public void postConstructor() {
        //
        logger.info("ContractController PostConstruct start ...");
    }

    @PostMapping("/attestation/put")
    public ResponseEntity<Result> putAttestation(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("putAttestation");

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");
        required.add("filehash");
        required.add("type");
        required.add("metadata");
        required.add("context");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        InputWrapper iw = gson.fromJson(gson.toJson(obj), InputWrapper.class);

        //
        String access_token = iw.getAccessToken();
        String filehash = iw.getFilehash();
        String type = iw.getType();

        //
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("metadata", iw.getMetadata());
        jsonObject.put("context", iw.getContext());
        jsonObject.put("signature", iw.getSignature());
        String detail = jsonObject.toJSONString();

        //
        try {

            //
            String ontid = oauthService.getContentUser(access_token);

            //
            Contract contract = new Contract();
            contract.setOntid(ontid);
            contract.setFilehash(filehash);
            contract.setDetail(detail);
            contract.setType(type);
            contract.setCreateTime(new Date());

            //
            Map<String, Object> map = tspService.getTimeStampMap(filehash);
            //
            long timestamp = (long) map.get("timestamp");
            String timestampSign = map.get("timestampSign").toString();
            //
            contract.setTimestamp(new Date(timestamp * 1000L));
            contract.setTimestampSign(timestampSign);

            //
            String key = contractService.contractToDigestForKey(contract);
            String value = contractService.contractToDigestForValue(contract);

            //
            contract.setContractKey(key);

            //
            contractService.saveToLocal(ontid, contract);

            // todo
            // https://github.com/ontio-cyano/integration-docs/blob/master/cn/%E9%92%B1%E5%8C%85%E5%AF%B9%E6%8E%A5-%E9%92%B1%E5%8C%85%E6%89%93%E5%BC%80DApp.md
            Arg a = new Arg();
            a.setName(key);
            a.setValue(value);

            List<Arg> l1 = new ArrayList<>();
            l1.add(a);

            Function f = new Function();
            f.setOperation("putRecord");
            f.setArgs(l1);

            List<Function> l2 = new ArrayList<>();
            l2.add(f);

            InvokeConfig ic = new InvokeConfig();
            ic.setFunctions(l2);
            ic.setContractHash(Helper.reverse(propertiesService.codeAddr));
            ic.setGasLimit(20000);
            ic.setGasPrice(500);

            Params pr = new Params();
            pr.setInvokeConfig(ic);

            CyanoRequest cq = new CyanoRequest();
            cq.setAction("invoke");
            cq.setVersion("v1.0.0");
            cq.setId(UUID.randomUUID().toString());
            cq.setParams(pr);

            //
            rst.setResult(cq);
            rst.setErrorAndDesc(ErrorCode.SUCCESSS);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/attestation/hash")
    public ResponseEntity<Result> selectByOntidAndHash(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("selectByOntidAndHash");

        //
        Set<String> required = new HashSet<>();
        required.add("hash");
        required.add("access_token");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String hash = (String) obj.get("hash");
        String accessToken = (String) obj.get("access_token");

        //
        String ontid = "";
        try {
            ontid = oauthService.getContentUser(accessToken);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        // ontid 也要作为条件，否则查到别人的了
        List<Contract> list = contractService.selectByOntidAndHash(ontid, hash);

        //
        rst.setResult(list);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);

    }

    @PostMapping("/attestation/hash/delete")
    public ResponseEntity<Result> deleteByOntidAndHash(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("deleteByOntidAndHash");

        //
        Set<String> required = new HashSet<>();
        required.add("hash");
        required.add("access_token");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String hash = (String) obj.get("hash");
        String accessToken = (String) obj.get("access_token");

        //
        String ontid = "";
        try {
            ontid = oauthService.getContentUser(accessToken);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        // ontid 也要作为条件
        try {
            contractService.deleteByOntidAndHash(ontid, hash);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        rst.setResult(true);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);

    }

    @PostMapping("/attestation/history")
    public ResponseEntity<Result> getHistory(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getHistory");

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");
        required.add("pageNum");
        required.add("pageSize");
        if (obj.containsKey("type")) {
            required.add("type");
        }

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String accessToken = (String) obj.get("access_token");
        int pageNum = Integer.parseInt(obj.get("pageNum").toString());
        int pageSize = Integer.parseInt(obj.get("pageSize").toString());
        //
        String type = "";
        if (obj.containsKey("type")) {
            type = (String) obj.get("type");
        }

        //
        String ontid = "";
        try {
            ontid = oauthService.getContentUser(accessToken);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
        //
        List<Contract> list = contractService.getHistoryByOntid(ontid, pageNum, pageSize, type);
        //
        rst.setResult(list);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/attestation/count")
    public ResponseEntity<Result> count(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("count");

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String accessToken = (String) obj.get("access_token");

        //
        String ontid = "";
        try {
            ontid = oauthService.getContentUser(accessToken);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
        //
        Integer count = contractService.count(ontid);
        //
        rst.setResult(count);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/attestation/explorer")
    public ResponseEntity<Result> getExplorer(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getExplorer");

        //
        Set<String> required = new HashSet<>();
        required.add("pageNum");
        required.add("pageSize");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        int pageNum = Integer.parseInt(obj.get("pageNum").toString());
        int pageSize = Integer.parseInt(obj.get("pageSize").toString());

        //
        List<Contract> list = contractService.getExplorerHistory(GlobalVariable.CURRENT_CONTRACT_TABLE_NAME, pageNum, pageSize);
        //
        rst.setResult(list);
        //
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);

        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/attestation/explorer/hash")
    public ResponseEntity<Result> getExplorerHash(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getExplorerHash");

        //
        Set<String> required = new HashSet<>();
        required.add("hash");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String hash = (String) obj.get("hash");

        // ontid 也要作为条件，否则查到别人的了
        List<Contract> list = contractService.selectByHash(hash);

        //
        rst.setResult(list);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);

    }

}
