package com.ontology.sourcing2c;

import com.github.ontio.account.Account;
import com.github.ontio.common.Helper;
import com.github.ontio.network.exception.ConnectorException;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import com.google.gson.Gson;
import com.ontology.sourcing2c.model.event.EventPojo;
import com.ontology.sourcing2c.model.event.Notify;
import com.ontology.sourcing2c.service.ContractService;
import com.ontology.sourcing2c.service.util.ChainService;
import com.ontology.sourcing2c.util.GlobalVariable;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "file:/Volumes/Data/_work/201802_Ontology/ONTSouring/ont-sourcing-2c/config/application-local.properties")
public class ContractTests04 {

    //
    private Gson gson = new Gson();

    //
    @Autowired
    ChainService chainService;
    @Autowired
    ContractService contractService;

    // 付款的数字钱包
    private Account payerAccount = GlobalVariable.getInstanceOfAccount("6a62d116e416246f974229eee7d1b0894d8c2ab70446856e85e35b7f5d37adef");

    // 公共合约_test
    private String codeAddr = "e2510ed1044503faf6e3e66b98372606bbeae38f";

    @Test
    public void example01() throws Exception {

        List paramList = new ArrayList<>();
        paramList.add("putRecord".getBytes());

        List args = new ArrayList();

        // args.add("7467b431e3acc8861f6a10a9b312de99f0e4b532de423cc5df2ff10addab0375");
        // args.add("e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927102c67");

        // args.add("ca4e952e3f96fb4c1b800153bf6d2c8c648b88371f613d13ea8ac75f29048f29");
        // args.add("3fa48bd8e69f2ee23f9a668bb1c687aecd74faaf6a09cf7b8547f8a8f3609bf9");
        // txhash 69a68994f96a0bb408f8e28bd0573407d15a87cd7fbccb7c1537085ba669fce9

        args.add("d074f682be104081482da7277160796762880f4dcc3546c060dabc2f2585d4ff");
        args.add("03081ca971bfb2d0b79caf228af09cba00637a9a3da3c6e9c8bd1fc85b6ca1ed");
        // txhash 3c6de3c717bda6a054f96d4fcb46a8b65152320afa008b01a4919c9335f96f2e

        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        //
        Map<String, String> map = contractService.invokeContract(Helper.reverse(codeAddr), null, params, payerAccount, 76220L, GlobalVariable.DEFAULT_GAS_PRICE, false);

        //
        String txhash = map.get("txhash");
        System.out.println(txhash);
        // da0a97b7e8e4c3d49645bbdf8579d8badfcc8aeb8a45384ec7dc8209e184d50a

        String result = map.get("result");
        System.out.println(result);
        // true
    }

    @Test
    public void example02() throws ConnectorException, IOException, DecoderException {

        Object rst = chainService.ontSdk.getConnect().getSmartCodeEvent("da0a97b7e8e4c3d49645bbdf8579d8badfcc8aeb8a45384ec7dc8209e184d50a");

        //
        String ntfy = rst.toString();
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
        EventPojo ep = gson.fromJson(ntfy, EventPojo.class);

        //
        Notify n0 = ep.getNotify().get(0);

        //
        List<String> states = n0.getStates();

        //
        String s1 = states.get(0);
        String n1 = new String(Helper.hexToBytes(s1));
        System.out.println(n1);
        // putRecord

        String s2 = states.get(1);
        String n2 = new String(Helper.hexToBytes(s2));
        System.out.println(n2);
        // 7467b431e3acc8861f6a10a9b312de99f0e4b532de423cc5df2ff10addab0375

        String s3 = states.get(2);
        String n3 = new String(Helper.hexToBytes(s3));
        System.out.println(n3);
        // e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927102c67
    }


    @Test
    public void example03() throws ConnectorException, IOException, DecoderException {

        Object rst = chainService.ontSdk.getConnect().getSmartCodeEvent("8ec363b0a9710803af00a354661fc4d87f90720d355952b441b4d5c100768fac");
        System.out.println(rst);
    }

    @Test
    public void example05() throws ConnectorException, IOException {

        for (int i = 0; i < 5; i++) {

            // Object obj = chainService.ontSdk.getConnect().getSmartCodeEvent(0);
            Object obj = chainService.ontSdk.getConnect().getSmartCodeEvent(1790451);
            System.out.println(obj.toString());

            chainService.switchOntSdk();
        }
    }


}
