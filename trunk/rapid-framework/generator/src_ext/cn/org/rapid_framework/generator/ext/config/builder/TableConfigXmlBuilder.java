package cn.org.rapid_framework.generator.ext.config.builder;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.SAXException;

import cn.org.rapid_framework.generator.ext.ibatis.model.TableConfig;
import cn.org.rapid_framework.generator.ext.ibatis.model.TableConfig.ColumnConfig;
import cn.org.rapid_framework.generator.ext.ibatis.model.TableConfig.OperationConfig;
import cn.org.rapid_framework.generator.ext.ibatis.model.TableConfig.ParamConfig;
import cn.org.rapid_framework.generator.ext.ibatis.model.TableConfig.ResultMapConfig;
import cn.org.rapid_framework.generator.ext.ibatis.model.TableConfig.SqlConfig;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.XMLHelper;
import cn.org.rapid_framework.generator.util.XMLHelper.NodeData;

public class TableConfigXmlBuilder {
	
    public TableConfig parseFromXML(InputStream reader) throws SAXException, IOException {
        NodeData nodeData = new XMLHelper().parseXML(reader);
        TableConfig config = new TableConfig();
        
        // table
        BeanHelper.copyProperties(config, nodeData.attributes);
        
        for(NodeData child : nodeData.childs) {
            // table/operation
            if("operation".equals(child.nodeName)) {
                OperationConfig target = new OperationConfig();
                BeanHelper.copyProperties(target, child.attributes);
                for(NodeData opChild : child.childs) {
                    // table/operation/extraparams
                    if("extraparams".equals(opChild.nodeName)) {
                        // table/operation/extraparams/param
                        for(NodeData paramNode : opChild.childs) {
                            ParamConfig mp = new ParamConfig();
                            BeanHelper.copyProperties(mp, paramNode.attributes);
                            target.extraparams.add(mp);
                        }
                    }else {
                        BeanHelper.setProperty(target, opChild.nodeName, opChild.innerXML);
                    }
                }
                config.operations.add(target);
            }
            // table/column
            if("column".equals(child.nodeName)) {
                ColumnConfig target = new ColumnConfig();
                BeanHelper.copyProperties(target, child.attributes);
                config.columns.add(target);
            }
            // table/sql
            if("sql".equals(child.nodeName)) {
                SqlConfig target = new SqlConfig();
                BeanHelper.copyProperties(target, child.attributes);
                target.setSql(child.innerXML);
                config.includeSqls.add(target);
            }
            // table/resultmap
            if("resultmap".equals(child.nodeName)) {
                ResultMapConfig target = new ResultMapConfig();
                BeanHelper.copyProperties(target, child.attributes);
                // table/resultmap/column
                for(NodeData c : child.childs) {
                    if("column".equals(c.nodeName)) {
                        ColumnConfig column = new ColumnConfig();
                        BeanHelper.copyProperties(column, c.attributes);
                        target.getColumns().add(column);
                    }
                }
                config.resultMaps.add(target);
            }
        }
        return config;
    }
    
}
