package com.alibaba.tctools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.XMLHelper;
import cn.org.rapid_framework.generator.util.XMLHelper.NodeData;

/**
 * 读取generator.xml的配置项信息
 * @author lai.zhoul
 * @date    2011-7-8
 */
public class GeneratorConfigUtil {


	public static Map<String, String> parse(File file) throws IOException, SAXException {
		Map<String, String> result = new HashMap<String, String>();
		FileInputStream in=new FileInputStream(file);
		NodeData nd = new XMLHelper().parseXML(in);
		List<NodeData> list = nd.childs;	//根结点的孩子
		List<NodeData> resultList=list.subList(2, list.size());//去掉前两项非key映射
		//System.out.println(resultList);
		//遍历所有key的映射，返回map
		for (NodeData item : resultList) {
			result.put(item.attributes.get("key"), item.nodeValue);
		}	
		System.out.println("[DEBUG] parsing 配置文件：key映射="+result);
		in.close();
		return result;
	}
	
	
}
