# struts 1 #

对struts 1的扩展主要是实现零配置. 主要是struts 1本身提供的通配符功能配置rapid的扩展.

以下为struts-config.xml的配置内容.
```
	<!-- 
		struts通配符配置,第一个*为{1},第二个*为{2},依次类推,具体可以查看http://struts.apache.org/1.2.9/userGuide/building_controller.html#action_mapping_wildcards
		如请求路径为/system/User.do,那会将type组成如下字符串
		com.creawor.demo.system.web.action.UserAction
	-->
	<action-mappings>
		<action path="/*/*/*"
			type="com.company.{1}.web.action.{2}Action"
			name="com.company.{1}.web.form.{2}Form" scope="request"
			validate="false" parameter="{3}" attribute="{2}Form">
		</action>
	</action-mappings>
```

配置文件实现零配置,再配合直接在方法中return new ActionForward(),而不是使用 return mapping.findForward("name");,代码如下
```
	public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
								HttpServletResponse response) {
		return new ActionForward("/pages/Blog/create.jsp");
	}
```


# rapid扩展 #
主要是使配置文件的name节点支持使用完整的类名,而不需要将form bean分开配置.
扩展类为DynamicModuleConfig
```
		<action path="/*/*/*"
			type="com.company.{1}.web.action.{2}Action"
			name="com.company.{1}.web.form.{2}Form" scope="request"
			validate="false" parameter="{3}" attribute="{2}Form">
		</action>
<!--注: name节点现在是完整的类路径,此处为rapid扩展 -->
```