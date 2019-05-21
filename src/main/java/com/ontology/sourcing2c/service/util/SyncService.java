package com.ontology.sourcing2c.service.util;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSON;
import com.github.ontio.common.Helper;
import com.github.ontio.network.exception.ConnectorException;
import com.ontology.sourcing2c.dao.Event;
import com.ontology.sourcing2c.dao.contract.Contract;
import com.ontology.sourcing2c.mapper.EventMapper;
import com.ontology.sourcing2c.mapper.SyncMapper;
import com.ontology.sourcing2c.model.SyncPojo;
import com.ontology.sourcing2c.model.event.EventPojo;
import com.ontology.sourcing2c.model.event.Notify;
import com.ontology.sourcing2c.service.ContractService;
import com.ontology.sourcing2c.util.ThreadUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class SyncService {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(SyncService.class);

    //
    private PropertiesService propertiesService;
    private ContractService contractService;

    //
    private EventMapper eventMapper;
    private SyncMapper syncMapper;

    //
    private ChainService chainService;

    @Autowired
    public SyncService(PropertiesService propertiesService, ContractService contractService, EventMapper eventMapper, SyncMapper syncMapper, ChainService chainService) {
        //
        this.propertiesService = propertiesService;
        this.contractService = contractService;
        //
        this.eventMapper = eventMapper;
        this.syncMapper = syncMapper;
        //
        this.chainService = chainService;
    }

    //
    public void confirmTx(String txhash) {

        ThreadUtil.getInstance().submit(new Runnable() {

            @Override
            public void run() {

                for (int retry = 0; retry < 5; retry++) {
                    try {
                        Thread.sleep(6 * 1000);
                        Object event = chainService.ontSdk.getConnect().getSmartCodeEvent(txhash);
                        while (event == null || StringUtils.isEmpty(event)) {
                            // sdk.addAttributes(ontId,password,key,valueType,value);
                            Thread.sleep(6 * 1000);
                            event = chainService.ontSdk.getConnect().getSmartCodeEvent(txhash);
                        }
                        // 获取交易所在的区块高度
                        int height = chainService.ontSdk.getConnect().getBlockHeightByTxHash(txhash);
                        //
                        Event exist = eventMapper.findByTxhash(txhash);
                        if (exist == null) {
                            //
                            String eventStr = JSON.toJSONString(event);
                            Event record = new Event();
                            record.setTxhash(txhash);
                            record.setEvent(eventStr);
                            record.setHeight(height);
                            record.setCreateTime(new Date());
                            eventMapper.save(record);
                        }
                        //
                        break;
                    } catch (ConnectorException | IOException e) {
                        //
                        // logger.error(e.getMessage());
                        logger.error(e.getMessage());

                        //
                        chainService.switchOntSdk();
                        //
                        continue;
                    } catch (Exception e) {
                        //
                        logger.error(e.getMessage());
                        //
                        break;
                    }
                }
            }
        });

    }

    /**
     * 同步链上信息
     */
    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public void synchronizeData() {

        //
        logger.info("synchronizeData schedule : {}", Thread.currentThread().getName());

        //
        int h = 0;

        //
        int blockHeight = 0;
        try {
            blockHeight = chainService.ontSdk.getConnect().getBlockHeight();
        } catch (ConnectorException | IOException e) {
            logger.error("getBlockHeight error {}", e.getMessage());
            return;
        }


        int startHeight;

        //
        if (syncMapper.findAll().size() == 0) {
            SyncPojo s = new SyncPojo();
            s.setComplete(blockHeight);
            s.setCreateTime(new Date());
            syncMapper.save(s);
            return;
        }

        //
        SyncPojo s0 = syncMapper.findAll().get(0);
        Integer completeheight = s0.getComplete();
        startHeight = completeheight + 1;


        logger.info("最大块高：{}", blockHeight);
        logger.info("开始块高：{}", startHeight);

        //
        if (startHeight >= blockHeight) {
            logger.info("startHeight >= blockHeight, try later ...");
            return;
        }

        //
        for (h = startHeight; h <= blockHeight; h++) {

            //
            logger.info("开始处理块高:{}", h);

            //
            String eventsStr = "";
            try {
                Object eventsRst = chainService.ontSdk.getConnect().getSmartCodeEvent(h);
                eventsStr = eventsRst.toString();
            } catch (ConnectorException | IOException e) {
                logger.error("getSmartCodeEvent error {}", e.getMessage());
                return;
            }

            //
            if (StringUtils.isEmpty(eventsStr)) {
                logger.error("eventsStr is empty, height is {}", h);
                continue;
            }

            //
            List<EventPojo> eventsPojo = JSON.parseArray(eventsStr, EventPojo.class);

            //
            if (eventsPojo == null) {
                logger.error("eventsPojo is null, height is {}, eventsStr is {}", h, eventsStr);
                continue;
            }

            //
            if (eventsPojo.size() <= 0) {
                logger.error("eventsPojo size <= 0, height is {}", h);
                continue;
            }

            //
            for (EventPojo ep : eventsPojo) {

                try {
                        /*
     {
    "GasConsumed":10000000,
    "Notify":[
        {
            "States":[
                "7075745265636f7264",
                "37343637623433316533616363383836316636613130613962333132646539396630653462353332646534323363633564663266663130616464616230333735",
                "65383134373562323565343966323736373532326433333230353763336536626231313434633834326463653437393133646338323232393237313032633637"
            ],
            "ContractAddress":"e2510ed1044503faf6e3e66b98372606bbeae38f"
        },
        {
            "States":[
                "transfer",
                "AGbL9NGxBosqRxiD34ZXPHigeti5AZBnfP",
                "AFmseVrdL9f9oyCzZefL9tG6UbviEH9ugK",
                10000000
            ],
            "ContractAddress":"0200000000000000000000000000000000000000"
        }
    ],
    "TxHash":"da0a97b7e8e4c3d49645bbdf8579d8badfcc8aeb8a45384ec7dc8209e184d50a",
    "State":1
}
                         */

                    //
                    String txhash = ep.getTxHash();

                    //
                    List<Notify> l = ep.getNotify();
                    for (Notify n : l) {

                        // 比较合约地址
                        if (!propertiesService.codeAddr.equals(n.getContractAddress())) {
                            logger.debug("codeAddr not correct ... {}", n.getContractAddress());
                            continue;
                        }

                        //
                        List<String> states = n.getStates();

                        // 比较方法名
                        String s1 = states.get(0);
                        String n1 = new String(Helper.hexToBytes(s1));
                        // System.out.println(n1);
                        // putRecord
                        if (!"putRecord".equals(n1)) {
                            logger.debug("method name not correct ... {}", n1);
                            continue;
                        }

                        // 比较key
                        String s2 = states.get(1);
                        String n2 = new String(Helper.hexToBytes(s2));
                        //
                        logger.info("n2 is {}, txhash is {}", n2, txhash);
                        //
                        if (n2.contains("String:")) {
                            n2 = n2.replace("String:", "");
                        }
                        // System.out.println(n2);
                        // 7467b431e3acc8861f6a10a9b312de99f0e4b532de423cc5df2ff10addab0375

                        //
                        Contract c = contractService.selectByContractKey(n2);
                        if (c != null && StringUtils.isEmpty(c.getTxhash())) {
                            contractService.updateByContractKey(txhash, 0, n2, new Date());
                        }

                        //
                        Event exist = eventMapper.findByTxhash(txhash);
                        if (exist == null) {
                            String eventStr = JSON.toJSONString(ep);
                            Event record = new Event();
                            record.setTxhash(txhash);
                            record.setEvent(eventStr);
                            record.setHeight(h);
                            record.setCreateTime(new Date());
                            eventMapper.save(record);
                        }
                    }

                } catch (Exception e) {
                    //
                    logger.error("{},{}", e.getMessage(), ep.toString());
                    // TODO rollback
                    // if (i != 0) {
                    //     Map<String, Object> map = new HashMap<>();
                    //     map.put("height", i);
                    //     ElasticsearchUtil.addData(map, indexName, heightType, "startHeight");
                    // }
                }
            }

        }

        //
        s0.setComplete(blockHeight);
        s0.setUpdateTime(new Date());
        syncMapper.save(s0);

    }
}
