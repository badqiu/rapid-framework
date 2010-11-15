<#include "/custom.include">
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>   
<#assign classNameLowerCase = className?lower_case>   
<#assign pkJavaType = table.idColumn.javaType>   

package ${basepackage}.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;
import cn.org.rapid_framework.web.scope.Flash;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

@Controller
@RequestMapping("/${classNameLowerCase}")
public class ${className}Controller {
    //Ĭ�϶�������,example: username desc,createTime asc
    protected static final String DEFAULT_SORT_COLUMNS = null; 
    
    private ${className}Manager ${classNameFirstLower}Manager;
    
    private final String LIST_ACTION = "redirect:/${classNameLowerCase}";
        
    /** 
     * ����setXXXX()����,spring�Ϳ���ͨ��autowire�Զ����ö�������,ע���Сд
     **/
    public void set${className}Manager(${className}Manager manager) {
        this.${classNameFirstLower}Manager = manager;
    }

    /**
     * ������@ModelAttribute�ķ��������ڱ�controller�ķ�������ǰִ��,���Դ��һЩ�������,��ö��ֵ
     */
    @ModelAttribute
    public void init(ModelMap model) {
        model.put("now", new java.sql.Timestamp(System.currentTimeMillis()));
    }
    
    /** �б� */
    @RequestMapping
    public String index(ModelMap model,${className}Query query,HttpServletRequest request,HttpServletResponse response) {
        Page page = this.${classNameFirstLower}Manager.findPage(query);
        
        model.addAllAttributes(toModelMap(page, query));
        return "/${className?lower_case}/index";
    }
    
    /** �������� */
    @RequestMapping(value="/new")
    public String _new(ModelMap model,${className} ${classNameFirstLower},HttpServletRequest request,HttpServletResponse response) throws Exception {
        model.addAttribute("${classNameFirstLower}",${classNameFirstLower});
        return "/${classNameLowerCase}/new";
    }
    
    /** ��ʾ */
    @RequestMapping(value="/{id}")
    public String show(ModelMap model,@PathVariable ${pkJavaType} id) throws Exception {
        ${className} ${classNameFirstLower} = (${className})${classNameFirstLower}Manager.getById(id);
        model.addAttribute("${classNameFirstLower}",${classNameFirstLower});
        return "/${classNameLowerCase}/show";
    }
    
    /** �༭ */
    @RequestMapping(value="/{id}/edit")
    public String edit(ModelMap model,@PathVariable ${pkJavaType} id) throws Exception {
        ${className} ${classNameFirstLower} = (${className})${classNameFirstLower}Manager.getById(id);
        model.addAttribute("${classNameFirstLower}",${classNameFirstLower});
        return "/${classNameLowerCase}/edit";
    }
    
    /** �������� */
    @RequestMapping(method=RequestMethod.POST)
    public String create(ModelMap model,${className} ${classNameFirstLower},HttpServletRequest request,HttpServletResponse response) throws Exception {
        ${classNameFirstLower}Manager.save(${classNameFirstLower});
        Flash.current().success("�����ɹ�"); //�����Flash�е�����,����һ��http��������Ȼ���Զ�ȡ����,error()������ʾ������Ϣ
        return LIST_ACTION;
    }
    
    /** ������� */
    @RequestMapping(value="/{id}",method=RequestMethod.PUT)
    public String update(ModelMap model,@PathVariable ${pkJavaType} id,HttpServletRequest request,HttpServletResponse response) throws Exception {
        ${className} ${classNameFirstLower} = (${className})${classNameFirstLower}Manager.getById(id);
        bind(request,${classNameFirstLower});
        ${classNameFirstLower}Manager.update(${classNameFirstLower});
        Flash.current().success("���³ɹ�");
        return LIST_ACTION;
    }
    
    /** ɾ�� */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public String delete(ModelMap model,@PathVariable ${pkJavaType} id) {
        ${classNameFirstLower}Manager.removeById(id);
        Flash.current().success("ɾ���ɹ�");
        return LIST_ACTION;
    }

    /** ����ɾ�� */
    @RequestMapping(method=RequestMethod.DELETE)
    public String batchDelete(ModelMap model,@RequestParam("items") ${pkJavaType}[] items) {
        for(int i = 0; i < items.length; i++) {
            ${classNameFirstLower}Manager.removeById(items[i]);
        }
        Flash.current().success("ɾ���ɹ�");
        return LIST_ACTION;
    }
    
}

