package cn.org.rapid_framework.generator.ext.tableconfig.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import cn.org.rapid_framework.generator.Generator;
import cn.org.rapid_framework.generator.GeneratorConstants;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.ext.tableconfig.builder.TableConfigXmlBuilder;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.GLogger;

public class AirTest extends GeneratorTestCase {
    
    public void setUp() throws Exception {
    	super.setUp();
        GLogger.perfLogLevel = GLogger.DEBUG;
        GLogger.logLevel = GLogger.DEBUG;
        GeneratorProperties.setProperty(GeneratorConstants.GG_IS_OVERRIDE.code, "true");
        GeneratorProperties.setProperty(GeneratorConstants.GENERATOR_SQL_RESULTCLASS_SUFFIX.code, "DO");
        
//        GeneratorProperties.setProperty(GeneratorConstants.JDBC_URL, "jdbc:mysql://mypay1.devdb.alipay.net:3306/mcenter?useUnicode=true&characterEncoding=UTF-8");
//        GeneratorProperties.setProperty(GeneratorConstants.JDBC_DRIVER, "com.mysql.jdbc.Driver");
//        GeneratorProperties.setProperty(GeneratorConstants.JDBC_USERNAME, "merchant");
//        GeneratorProperties.setProperty(GeneratorConstants.JDBC_PASSWORD, "merchant");
        
        GeneratorProperties.setProperty("basepackage", "com.alipay.mquery.common.dal.air");
        GeneratorProperties.setProperty("appName", "rapid");
        
        g = new Generator();
        g.setSourceEncoding("GBK");
        g.setOutputEncoding("GBK");
        g.setOutRootDir("./target/temp/generate_by_sql_config");
    }
    
    public void test_genereate_by_sql_config() throws Exception {
//        genereate_by_table_config("trade_base.xml");
//        genereate_by_table_config("mcenter_air_ext.xml");
//        genereate_by_table_config("mcenter_creditpay.xml");
//        genereate_by_table_config("mcenter_account_log.xml");
//        genereate_by_table_config("system_partner_query_trade_base.xml");
//        genereate_by_table_config("trade_fund_bill.xml");
//        genereate_by_table_config("mcenter_air_ext.xml");
    }

    private void genereate_by_table_config(String tableConfigFilename)
                                                                      throws IOException,
                                                                      FileNotFoundException,
                                                                      Exception,
                                                                      SQLException {
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql_config"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig/"+tableConfigFilename);
        TableConfig t = new TableConfigXmlBuilder().parseFromXML(file);
        GeneratorModel gm = MetaTableTest.newFromTable(t);
        g.generateBy(gm.templateModel, gm.filePathModel);
        
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql"));
        for(Sql sql : t.getSqls()) {
            GeneratorModel sqlGM = MetaTableTest.newFromSql(sql,t);
            g.generateBy(sqlGM.templateModel, sqlGM.filePathModel);
        }
    }

}
