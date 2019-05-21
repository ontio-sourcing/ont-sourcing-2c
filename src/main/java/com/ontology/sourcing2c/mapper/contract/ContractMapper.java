package com.ontology.sourcing2c.mapper.contract;

import com.ontology.sourcing2c.dao.contract.Contract;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ContractMapper {

    int insert(@Param("tableName") String tableName, @Param("record") Contract record);

    // int insertSelective(@Param("tableName") String tableName, @Param("record") Contract record);
    //
    // void insertBatch(@Param("tableName") String tableName, @Param("contractList") List<Contract> contractList);

    //
    List<Contract> selectByOntidAndHash(@Param("tableName") String tableName, @Param("ontid") String ontid, @Param("hash") String hash);

    //
    List<Contract> deleteByOntidAndHash(@Param("tableName") String tableName, @Param("ontid") String ontid, @Param("hash") String hash);

    //
    List<Contract> selectByHash(@Param("tableName") String tableName, @Param("hash") String hash);

    //
    List<Contract> selectByContractKey(@Param("tableName") String tableName, @Param("hash") String hash);

    //
    List<Contract> selectByUUID(@Param("tableName") String tableName, @Param("uuidStr") String uuidStr);

    //
    String selectCyanoInfoByContractKey(@Param("tableName") String tableName, @Param("hash") String hash);

    //
    Integer updateByContractKey(@Param("tableName") String tableName, @Param("txhash") String txhash, @Param("status") Integer status, @Param("contractKey") String contractKey,
                                @Param("updateTime") Date updateTime);

    //
    Integer updateByUUID(@Param("tableName") String tableName, @Param("txhash") String txhash, @Param("status") Integer status, @Param("uuidStr") String uuidStr,
                                @Param("updateTime") Date updateTime);

    //
    int count(@Param("tableName") String tableName, @Param("ontid") String ontid);

    List<Contract> selectByOntidPageNumSize(@Param("tableName") String tableName, @Param("ontid") String ontid, @Param("start") int start, @Param("offset") int offset);

    List<Contract> selectByOntidPageNumSizeAndType(@Param("tableName") String tableName,
                                                   @Param("ontid") String ontid,
                                                   @Param("start") int start,
                                                   @Param("offset") int offset,
                                                   @Param("type") String type);

    //
    List<Contract> selectByPageNumSize(@Param("tableName") String tableName, @Param("start") int start, @Param("offset") int offset);

    //
    Integer deleteByCreateTime(@Param("tableName") String tableName, @Param("createTime") Date createTime);
}