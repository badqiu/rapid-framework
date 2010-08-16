package cn.org.rapid_framework.generator.ext.ibatis.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import cn.org.rapid_framework.generator.Generator;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.StringHelper;

public class AirTest extends GeneratorTestCase {
    
    public void setUp() throws Exception {
        GLogger.perf = true;
        System.setProperty("gg.isOverride", "true");
        System.setProperty("generator.sql.resultClass.suffix", "DO");
        
        GeneratorProperties.setProperty("jdbc.url", "jdbc:mysql://mypay1.devdb.alipay.net:3306/mcenter?useUnicode=true&characterEncoding=UTF-8");
        GeneratorProperties.setProperty("jdbc.driver", "com.mysql.jdbc.Driver");
        GeneratorProperties.setProperty("jdbc.username", "merchant");
        GeneratorProperties.setProperty("jdbc.password", "merchant");
        
        GeneratorProperties.setProperty("basepackage", "com.alipay.mquery.common.dal.air");
        GeneratorProperties.setProperty("appName", "rapid");
        
        g = new Generator();
        g.setSourceEncoding("GBK");
        g.setOutputEncoding("GBK");
        g.setOutRootDir("./temp/generate_by_sql_config");
    }
    
    public void test_genereate_by_sql_config() throws Exception {
//        genereate_by_table_config("trade_base.xml");
        genereate_by_table_config("mcenter_air_ext.xml");
        genereate_by_table_config("mcenter_creditpay.xml");
        genereate_by_table_config("mcenter_account_log.xml");
//        genereate_by_table_config("trade_fund_bill.xml");
//        genereate_by_table_config("mcenter_air_ext.xml");
    }

    private void genereate_by_table_config(String tableConfigFilename)
                                                                      throws IOException,
                                                                      FileNotFoundException,
                                                                      Exception,
                                                                      SQLException {
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql_config"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/ibatis/"+tableConfigFilename);
        TableConfig t = TableConfig.parseFromXML(new FileInputStream(file));
        GeneratorModel gm = MetaTableTest.newFromTable(t);
        g.generateBy(gm.templateModel, gm.filePathModel);
        
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql"));
        for(Sql sql : t.getSqls()) {
            GeneratorModel sqlGM = MetaTableTest.newFromSql(sql,t);
            g.generateBy(sqlGM.templateModel, sqlGM.filePathModel);
        }
    }

}
