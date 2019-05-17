package com.ontology.sourcing2c.service;

import ch.qos.logback.classic.Logger;
import com.github.ontio.account.Account;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.network.exception.ConnectorException;
import com.github.ontio.sdk.exception.SDKException;
import com.ontology.sourcing2c.dao.Event;
import com.ontology.sourcing2c.dao.contract.*;
import com.ontology.sourcing2c.mapper.EventMapper;
import com.ontology.sourcing2c.mapper.contract.*;
import com.ontology.sourcing2c.service.util.ChainService;
import com.ontology.sourcing2c.service.util.PropertiesService;
import com.ontology.sourcing2c.util.GlobalVariable;
import com.ontology.sourcing2c.util._hash.Sha256Util;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

@Service
public class ContractService {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(ContractService.class);

    //
    private PropertiesService propertiesService;
    private ChainService chainService;
    //
    private ContractMapper contractMapper;
    private ContractIndexMapper contractIndexMapper;
    private ContractOntidMapper contractOntidMapper;
    //
    private EventMapper eventMapper;

    //
    @Autowired
    public ContractService(PropertiesService propertiesService,
                           ChainService chainService,
                           ContractMapper contractMapper,
                           ContractIndexMapper contractIndexMapper,
                           ContractOntidMapper contractOntidMapper,
                           EventMapper eventMapper) {
        //
        this.propertiesService = propertiesService;
        this.chainService = chainService;
        //
        this.contractMapper = contractMapper;
        this.contractIndexMapper = contractIndexMapper;
        this.contractOntidMapper = contractOntidMapper;
        //
        this.eventMapper = eventMapper;

    }

    // public Map<String, String> putAttestation(Contract contract) throws Exception {
    //
    //     List paramList = new ArrayList<>();
    //     paramList.add("putRecord".getBytes());
    //
    //     List args = new ArrayList();
    //     // key
    //     args.add(contractToDigestForKey(contract));  // 上链时，只把指定field的组合后的hash
    //     // value
    //     args.add(contractToDigestForValue(contract));  // 上链时，只把指定field的组合后的hash
    //
    //     //
    //     paramList.add(args);
    //     byte[] params = BuildParams.createCodeParamsScript(paramList);
    //
    //     // 先查询是不是项目方，有没有设置指定的payer地址和合约地址
    //
    //     //
    //     // String s2 = payer.getAddressU160().toBase58();
    //
    //     //
    //     Map<String, String> map = invokeContract(Helper.reverse(codeAddr), null, params, payer, 76220L, GlobalVariable.DEFAULT_GAS_PRICE, false);
    //
    //     //
    //     String txhash = map.get("txhash");
    //     String result = map.get("result");
    //     // System.out.println(result);  // true
    //
    //     //
    //     return map;
    // }
    //
    // public String getContract(Contract contract, Account payer) throws Exception {
    //
    //     List paramList = new ArrayList<>();
    //     paramList.add("getRecord".getBytes());
    //
    //     List args = new ArrayList();
    //     args.add(contractToDigestForKey(contract));
    //
    //     paramList.add(args);
    //     byte[] params = BuildParams.createCodeParamsScript(paramList);
    //
    //     // 先查询是不是项目方，有没有设置指定的payer地址和合约地址
    //     ContractCompany contractCompany = contractCompanyMapper.findByOntid(contract.getCompanyOntid());
    //     if (contractCompany != null) {
    //         payer = GlobalVariable.getInstanceOfAccount(contractCompany.getPrikey());
    //         codeAddr = Address.AddressFromVmCode(contractCompany.getCodeAddr()).toHexString();
    //     }
    //
    //     //
    //     Map<String, String> map = invokeContract(Helper.reverse(codeAddr), null, params, payer, chainService.ontSdk.DEFAULT_GAS_LIMIT, GlobalVariable.DEFAULT_GAS_PRICE, true);
    //
    //     //
    //     String txhash = map.get("txhash");
    //     String result = map.get("result");
    //
    //     //
    //     String s1 = JSON.parseObject(result).getString("Result");
    //     byte[] s2 = Helper.hexToBytes(s1);
    //     String value = new String(s2);
    //
    //     //
    //     return value;
    // }

    //
    public Map<String, String> invokeContract(String codeAddr, String method, byte[] params, Account payerAcct, long gaslimit, long gasprice, boolean preExec) throws Exception {

        //
        if (payerAcct == null) {
            throw new SDKException("params should not be null");
        }
        if (gaslimit < 0 || gasprice < 0) {
            throw new SDKException("gaslimit or gasprice should not be less than 0");
        }

        //
        Map<String, String> map = new HashMap<String, String>();

        //
        Transaction tx = chainService.ontSdk.vm().makeInvokeCodeTransaction(codeAddr, method, params, payerAcct.getAddressU160().toBase58(), gaslimit, gasprice);
        // System.out.println(tx);
        // com.github.ontio.core.payload.InvokeCode@fecc2faa

        //
        chainService.ontSdk.addSign(tx, payerAcct);

        // String s = payerAcct.getAddressU160().toBase58();

        //
        String rawdata = tx.toHexString();
        String txhash = tx.hash().toString();
        //
        map.put("txhash", txhash);
        map.put("rawdata", rawdata);  // TODO

        //
        if (preExec) {

            for (int retry = 0; retry < 5; retry++) {
                //
                Object result = null;
                try {
                    result = chainService.ontSdk.getConnect().sendRawTransactionPreExec(rawdata);
                } catch (ConnectorException | IOException e) {
                    logger.error(e.getMessage());
                    //
                    chainService.switchOntSdk();
                    //
                    continue;
                }
                //
                map.put("result", result.toString());
                //
                break;
            }

        } else {

            for (int retry = 0; retry < 5; retry++) {
                //
                boolean result = false;
                try {
                    result = chainService.ontSdk.getConnect().sendRawTransaction(rawdata);
                } catch (ConnectorException | IOException e) {
                    logger.error(e.getMessage());
                    //
                    chainService.switchOntSdk();
                    //
                    continue;
                }
                //
                map.put("result", Boolean.toString(result));
                //
                break;
            }
        }

        //
        return map;
    }

    // 发到链上的key
    public String contractToDigestForKey(Contract contract) {
        String k = contract.getOntid() + contract.getFilehash() + contract.getTimestamp();
        return Sha256Util.sha256(k);
    }

    // 发到链上的value
    public String contractToDigestForValue(Contract contract) {
        String v = contract.getOntid() + contract.getDetail() + contract.getTimestamp() + contract.getTimestampSign();
        return Sha256Util.sha256(v);
    }

    // 后期如果需要验证
    // public boolean verifyContractOnBlockchain(Contract contract, Account payer) throws Exception {
    //     String valueLocal = contractToDigestForValue(contract);
    //     String valueOnBlockchain = getContract(contract, payer);
    //     return valueOnBlockchain.equals(valueLocal);
    // }

    //
    public List<Contract> getHistoryByOntid(String ontid, int pageNum, int pageSize, String type) {
        //
        String tableName = getIndex(ontid).getName();
        //
        int start = (pageNum - 1) * pageSize;
        int offset = pageSize;
        //
        List<Contract> list;
        if (StringUtils.isEmpty(type)) {
            list = contractMapper.selectByOntidPageNumSize(tableName, ontid, start, offset);
        } else {
            list = contractMapper.selectByOntidPageNumSizeAndType(tableName, ontid, start, offset, type);
        }
        return addHeight(list);
    }

    //
    public List<Contract> getExplorerHistory(String tableName, int pageNum, int pageSize) {
        int start = (pageNum - 1) * pageSize;
        int offset = pageSize;
        List<Contract> list = contractMapper.selectByPageNumSize(tableName, start, offset);
        return addHeight(list);
    }

    //
    public List<Contract> selectByOntidAndHash(String ontid, String hash) {
        String tableName = getIndex(ontid).getName();
        List<Contract> list = contractMapper.selectByOntidAndHash(tableName, ontid, hash);
        return addHeight(list);
    }

    //
    public void deleteByOntidAndHash(String ontid, String hash) {
        String tableName = getIndex(ontid).getName();
        contractMapper.deleteByOntidAndHash(tableName, ontid, hash);
    }

    //
    public List<Contract> selectByHash(String hash) {

        // TODO 目前只支持从当前表查询
        List<Contract> list = contractMapper.selectByHash(GlobalVariable.CURRENT_CONTRACT_TABLE_NAME, hash);
        return addHeight(list);
    }

    //
    public Integer count(String ontid) {
        String tableName = getIndex(ontid).getName();
        return contractMapper.count(tableName, ontid);
    }

    // 跨表添加height信息
    private List<Contract> addHeight(List<Contract> list) {
        //
        if (list == null || list.size() == 0)
            return list;
        //
        List<Contract> newlist = new ArrayList<>();
        for (Contract c : list) {
            Event e = eventMapper.findByTxhash(c.getTxhash());
            if (e != null) {
                Integer height = e.getHeight();
                c.setHeight(height);
            } else {
                c.setHeight(0);  // TODO
            }
            newlist.add(c);
        }
        return newlist;
    }

    // TODO @Transactional  ??
    private ContractOntid getRecord(String ontid) {

        //
        ContractOntid existed = contractOntidMapper.findFirstByOntidOrderByCreateTimeAsc(ontid);
        //
        if (existed != null) {
            return existed;
        }
        //
        ContractOntid record = new ContractOntid();
        record.setOntid(ontid);
        record.setCreateTime(new Date());
        record.setContractIndex(GlobalVariable.CURRENT_CONTRACT_TABLE_INDEX);

        // TODO 这里存在一个很大bug，若同一时间一个ontid发来多个请求，在数据库记录任何一个之前，都会返回null，于是后面都会创建并写入数据库，同一个ontid就会有多条记录
        //        ContractOntid existed = contractOntidMapper.findByOntid(ontid);
        // contractOntidMapper.save(record);
        contractOntidMapper.saveIfIgnore(ontid, record.getContractIndex(), record.getCreateTime());
        //
        return record;
    }

    private ContractIndex getIndex(String ontid) {
        ContractOntid record = getRecord(ontid);
        ContractIndex contractIndex = contractIndexMapper.selectByPrimaryKey(record.getContractIndex());
        return contractIndex;
    }

    // 写入数据库
    public void saveToLocal(String ontid, Contract contract) {
        contractMapper.insert(getIndex(ontid).getName(), contract);
    }

    // 写入数据库，batch insert
    public void saveToLocalBatch(String ontid, List<Contract> contractList) {
        contractMapper.insertBatch(getIndex(ontid).getName(), contractList);
    }

}
