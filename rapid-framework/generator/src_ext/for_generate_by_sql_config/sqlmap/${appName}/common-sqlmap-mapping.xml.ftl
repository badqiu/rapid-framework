<?xml version="1.0" encoding="GB2312" ?>
<!DOCTYPE sqlMap PUBLIC "-//iBATIS.com//DTD SQL Map 2.0//EN" "http://ibatis.apache.org/dtd/sql-map-2.dtd">

<!-- ==========================================================  -->
<!-- Configuration for ibatis sqlmap mapping.                    -->
<!-- ==========================================================  -->

<sqlMap namespace="paygw">
    <!-- ============================================= -->
    <!-- RESULT MAPS                                   -->
    <!-- ============================================= -->

    <!-- result map for Money class -->
    <resultMap id="RM-MONEY" class="com.iwallet.biz.common.util.money.Money">
        <result property="cent" columnIndex="1" jdbcType="NUMBER" nullValue="0"/>
    </resultMap>

    <!-- result map for Money class -->
    <resultMap id="paygw.RM-MONEY" class="com.iwallet.biz.common.util.money.Money">
        <result property="cent" columnIndex="1" jdbcType="NUMBER" nullValue="0"/>
    </resultMap>
    
    <!-- ============================================= -->
    <!-- mapped statements for SEQUENCE                -->
    <!-- ============================================= -->
    <!-- mapped statement for plugin.seqIbatisClassName.seq.operationName -->
    <select id="seq.mappedStatementId" resultClass="long">
    <![CDATA[
        seq.mappedStatementSql
    ]]>
    </select>
</sqlMap>