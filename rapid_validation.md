# 最快速的表单验证框架 RapidValidation #

  * 简洁,快速的验证语法
  * 无需编写验证提示信息(当然也支持自定义提示信息)
  * 支持组合验证
  * Ajax支持
  * 错误消息在指定地方显示
  * 兼容prototype.js 1.3+所有版本及jquery.js
  * 基于标准的Html属性(class)添加验证,易于其它标准的jsp taglib集成
  * 特殊应用场景支持,如:密码确认,结束日期必须大于开始日期

# 在线demo #

http://www.rapid-framework.org.cn/demo/rapid-validation/doc/index.html

# 项目网站 #
http://code.google.com/p/rapid-validation/

# 应用网站 #
javaeye现就是使用本表单验证,有其独特优势:快速.

# helloworld示例 #

**引用文件**
```
<script src="prototype.js" type="text/javascript"></script>
<script src="validation_cn.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="style_min.css"/>
```
prototype.js是所有的基础
validation\_cn.js真正的验证框架文件
可以添加style\_min.css中的样式声明,也可以把style\_min.css中的样式声明引入到你的框架js文件中去.
**表单验证**

<!-- 为form增加required-validate class,标识需要验证form -->
```
<form id='helloworld' action="#">
	helloworld:</br>
	<!--通过class添加验证: required表示不能为空,min-length-15表示最小长度为15 -->
	<textarea name='content' class='required min-length-15'></textarea></br>
	<input type='submit' value='Submit'/> 
	<input type='reset' value='Reset'/>
</form>
<script>
  new Validation('helloworld')
</script>
```
在要检查的域中通过class属性来声明被检查域的限制条件,例如上面的class='required min-length-15'表示这是一个非空,并且最小长度是15的域,在编辑域失去焦点时,会自动检查,如果输入不满足上述条件,则产生错误提示.