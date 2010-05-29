package javacommon.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 定义标准的rest方法以对应实体对象的操作,以达到统一rest的方法名称,
 * 还可以避免子类需要重复编写@RequestMapping annotation.
 * 
 * 子类要实现某功能只需覆盖下面的方法即可.
 * 注意: 覆盖时请使用@Override,以确保不会发生错误
 * <pre>
 * /userinfo                => index()  
 * /userinfo/new            => _new()  
 * /userinfo/{id}           => show()  
 * /userinfo/{id}/edit      => edit()  
 * /userinfo        POST    => create()  
 * /userinfo/{id}   PUT     => update()  
 * /userinfo/{id}   DELETE  => delete()  
 * /userinfo        DELETE  => batchDelete()  
 * </pre>
 * 
 * @author badqiu
 */
public class BaseRestSpringController<Entity,PK> extends BaseSpringController{
	
	//特别说明: 由于spring的方法参数映射太过于灵活,如果以下参数不适应你,请自己修改参数并修改代码生成器模板
	// 如果你不喜欢 HttpServletRequest request,HttpServletResponse response作为方法参数，也请删除
	
	@RequestMapping
	public String index(ModelMap model,HttpServletRequest request,HttpServletResponse response,Entity entity) {
		throw new UnsupportedOperationException("not yet implement");
	}
	
	/** 进入新增 */
	@RequestMapping(value="/new")
	public String _new(ModelMap model,HttpServletRequest request,HttpServletResponse response,Entity entity) throws Exception {
		throw new UnsupportedOperationException("not yet implement");
	}
	
	/** 显示 */
	@RequestMapping(value="/{id}")
	public String show(ModelMap model,@PathVariable PK id) throws Exception {
		throw new UnsupportedOperationException("not yet implement");
	}
	
	/** 编辑 */
	@RequestMapping(value="/{id}/edit")
	public String edit(ModelMap model,@PathVariable PK id) throws Exception {
		throw new UnsupportedOperationException("not yet implement");
	}
	
	/** 保存新增 */
	@RequestMapping(method=RequestMethod.POST)
	public String create(ModelMap model,HttpServletRequest request,HttpServletResponse response,Entity entity) throws Exception {
		throw new UnsupportedOperationException("not yet implement");
	}
	
	/** 保存更新 */
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public String update(ModelMap model,@PathVariable PK id,HttpServletRequest request,HttpServletResponse response) throws Exception {
		throw new UnsupportedOperationException("not yet implement");
	}
	
	/** 删除 */
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public String delete(ModelMap model,@PathVariable PK id) {
		throw new UnsupportedOperationException("not yet implement");
	}

	/** 批量删除 */
	@RequestMapping(method=RequestMethod.DELETE)
	public String batchDelete(ModelMap model,@RequestParam("items") PK[] items) {
		throw new UnsupportedOperationException("not yet implement");
	}
}
