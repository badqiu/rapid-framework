# 介绍 #
应用struts2主要是使用零配置及增加了一种result-type: DirectResult

# DirectResult扩展 #

rapid对struts2的**唯一扩展**,增加了一种result-type: DirectResult
该result-type的作用是可以java代码中指定返回页.
```
	public String show() {
		//do somethins ...
		return "/pages/Blog/show.jsp"; //这里为forward
	}
```

如果是要**重定向(redirect)**,在前面加个感叹号即可
```
	public String save() {
		blogManager.save(blog);
		return "!/pages/Blog/list.do"; //这里为redirect
	}
```

并且你如果觉得比较混淆,也可以不使用!,直接使用 "forward:" 及"redirect:"前缀
```
return "forward:/pages/Blog/list.do";
return "redirect:/pages/Blog/list.do";
```

# 零配置 #
使用struts2自带的struts.enable.SlashesInActionNames即可实现零配置.
生成的URL规则为: /namesapce/Object/method.do
注意Object首个字符为大写.

以下为配置
```
    <constant name="struts.enable.SlashesInActionNames" value="true" />

	<package name="custom-default" extends="struts-default">
        <result-types>
            <result-type name="direct" class="cn.org.rapid_framework.struts2.dispatcher.DirectResult"/>
        </result-types>
        <default-interceptor-ref name="paramsPrepareParamsStack"/>
  	</package>
  	
  	<!-- 修改为你自己的namespace -->
    <package name="default" namespace="/system" extends="custom-default">
		
		<!-- 通过URL访问的路径是 /namesapce/Entity/method.do -->
		<action name="*/*" method="{2}" class="com.company.system.action.{1}Action">
		    <result name="*" type="direct">通过Action直接返回，这一行无需修改</result>
		</action>
						
    </package>
```