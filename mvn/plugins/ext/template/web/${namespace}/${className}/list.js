	<#include "macro.include"/> 
	<#include "custom.include"/> 
	<#assign className = table.className>   
	<#assign classNameLower = className?uncap_first> 
	
	<#macro generateId>
			<#list table.compositeIdColumns as column>
				<#t>${column.columnNameLower}
			</#list>
	</#macro>
	
	var actionObj = ${className}Manager;
	
	//JSON数据定义
    var dataStore = new Ext.data.Store( {
		proxy : new Ext.data.HttpProxy( {
			url : 'json.do'
		}),

		reader : new Ext.data.JsonReader( {
			root : 'list',//记录值
			totalProperty : 'totalSize',//记录总数
			id : '<@generateId/>'
		},[
			'<@generateId/>',
			<#list table.columns as column>
				<#if !column.htmlHidden>
					'${column.columnNameLower}'<#if column_has_next>,</#if>
				</#if>
			</#list>
		])
	});

    /*
	//xml数据定义
	var dataStore = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({url: 'xml.do'}),
        reader: new Ext.data.XmlReader({
               record: 'user',
               id: 'userId'
	           },['userId', 'userName', 'sex', 'age'])
    });
    */
    
    //多选框
    var sm = new Ext.grid.CheckboxSelectionModel();
   
    //列模型
    var colModel = new Ext.grid.ColumnModel([
    	sm,{header: '<@generateId/>',width:120, dataIndex: '<@generateId/>',menuDisabled:true,hidden: true},
    		
    		<#list table.columns as column>
				<#if !column.htmlHidden>
					{header: '${column.columnNameLower}',width:120, dataIndex: '${column.columnNameLower}',menuDisabled:true}<#if column_has_next>,</#if>
				</#if>
			</#list>
	]);
    colModel.defaultSortable=true;//默认情况下 列是可排序的
    
    //定义分页工具条
	var pagingBar = new Ext.PagingToolbar({
        pageSize: 10,
        store: dataStore,
        displayInfo: true,
        displayMsg: '显示 {0}-{1}条 / 共 {2} 条',
        emptyMsg: "无数据",

        items:[
            '-', {
            pressed: true,
            enableToggle:true,
            text: 'Show Preview',
            cls: 'x-btn-text-icon details',
            toggleHandler: function(btn, pressed){
                var view = grid.getView();
                view.showPreview = pressed;
                view.refresh();
            }
        }]
    });
  
    //表格渲染
	var grid = new Ext.grid.GridPanel({
	        store: dataStore,
	        cm: colModel,
	        width:800,
        	height:580,
        	title:'${className}列表',
        	sm:sm,//多选框
        	trackMouseOver:true, //鼠标移动样式变化
	        disableSelection:false,//是否允许选择
	        loadMask: true,//等待标记
	        collapsible : true,// 是否可以展开
            animCollapse : true,// 展开时是否有动画效果
        	bbar: pagingBar,//分页工具条
        	frame:true,
        	iconCls:'icon-grid',
        	
        	//工具条
        	tbar:[{
	            text:'添加',
	            tooltip:'添加',
	            iconCls:'add',//客户端的样式
	            handler:addCol
	        }, '-', {
	            text:'修改',
	            tooltip:'修改',
	            iconCls:'option',
	            handler:updateCol
	        },'-',{
	            text:'删除',
	            tooltip:'删除',
	            iconCls:'remove',
	            handler:deleteCol
	        }],

	        viewConfig: {
	            forceFit:true
	        }
	});
 
	//点击'增加'按钮执行的操作
	function addCol(){      
		var n = grid.getStore().getCount();// 获得总行数
		var formPanel = new Ext.form.FormPanel({
		        baseCls: 'x-plain',
		        labelWidth: 55,
		        url:'#',
		        defaultType: 'textfield',
				frame:true,
				
		        items: [
			        <#list table.columns as column>
					<#if !column.htmlHidden>
						{fieldLabel: '${column.columnNameLower}',name: '${column.columnNameLower}',allowBlank:false,anchor:'80%'}<#if column_has_next>,</#if>
					</#if>
					</#list>
		        ]
		    });
		    	       
		    var window = new Ext.Window({
				modal:true,//是否为模态
		        title: '新增${className}',
		        width: 400,
		        height:250,
		        minWidth: 300,
		        minHeight: 200,
		        layout: 'fit',
		        plain:true,
		        bodyStyle:'padding:5px;',
		        buttonAlign:'center',
		        items: formPanel,
		        buttons: [{
		            text: '提交',
					handler:function(){
						if (formPanel.form.isValid()){
						   var n = grid.getStore().getCount();// 获得总行数
						   var obj = formPanel.getForm().getValues();
						   actionObj.save${className}(obj,function(is) {
								if (is){
									Ext.MessageBox.alert('操作状态', '添加信息成功!',function(){
		                                window.hide();//隐藏对话框
										dataStore.load({params:{start:0, limit:10}});
									});
								} else {
									Ext.MessageBox.alert('操作状态', '添加信息失败!' );
								}
								window.hide();
							});
						 }else{
						 	Ext.Msg.alert('信息', '您输入的信息有误，请重新输入!');
						 }
															
		            }
		        },{
		            text: '取消',
					handler:function(){window.hide();}
		        }]
		    });
		    
		window.show();
	}
    
    //点击'删除'按钮，执行的操作
    function deleteCol() {
		var record = grid.getSelectionModel().getSelected();//返回值为Record 类型
		if (!record) {
			Ext.Msg.alert("提示", "请先选择要删除的行!");
			return;
		}
		if (record){
			Ext.MessageBox.confirm('确认删除','你真的要删除所选用户吗?',
			function(btn){
				if (btn == 'yes'){
					actionObj.remove${className}ById(record.get("<@generateId/>"),
					function(data){
						if (data){
							dataStore.remove(record);//更新界面,来删除数据
					        Ext.MessageBox.alert("操作状态","数据删除成功！");
					         //Ext.example.msg("操作状态","数据删除成功！");
							}else{
								Ext.MessageBox.alert("出错了!","单个用户数据删除失败!");
							}
					});
				}
			});
		}
	}
	
	//'修改'事件处理函数	
	function updateCol(){
		var record = grid.getSelectionModel().getSelected();//返回值为Record 类型
		if (!record) {
			Ext.Msg.alert("提示", "请先选择要修改的行!");
			return;
		}
		actionObj.get${className}ById(record.get("<@generateId/>"),function(obj) {
			if (obj == null) {
				alert("数据初始化错误");
			}else{
				var form = new Ext.form.FormPanel({
			        baseCls: 'x-plain',
			        labelWidth: 55,
			        url:'#',
			        defaultType: 'textfield',
			
			        items: [
			        {xtype:'hidden',name:'<@generateId/>',value:obj.<@generateId/>},
			        <#list table.columns as column>
					<#if !column.htmlHidden>
						{fieldLabel: '${column.columnNameLower}',name: '${column.columnNameLower}',value:obj.${column.columnNameLower},allowBlank:false,anchor:'80%'}<#if column_has_next>,</#if>
					</#if>
					</#list>
			       ]
			    });

			    var window = new Ext.Window({
					modal:true,//是否为模态
			        title: '修改${className}',
			        width: 400,
			        height:250,
			        minWidth: 300,
			        minHeight: 200,
			        layout: 'fit',
			        plain:true,
			        bodyStyle:'padding:5px;',
			        buttonAlign:'center',
			        items: form,

			        buttons: [{
			            text: '提交',
						handler:function(){
						   	//alert("您提交的值为："+form.getForm().getValues(true).replace(/&/g,', '));
						   	var obj = form.getForm().getValues();
							actionObj.update${className}(obj,function(is) {
								if (is) {
									Ext.MessageBox.alert('操作状态','修改信息成功!',function(){
		                                window.hide();
		                                dataStore.load({params:{start:0, limit:10}});
									});
								} else {
									Ext.MessageBox.alert("出错了!","修改数据失败!");
								}
							});						
			            }
			        },{
			            text: '取消',
						handler:function(){window.hide();}
			        }]
			    });
    			window.show(); 
   			}
		});
	}


	//呈现
	Ext.onReady(function(){
		Ext.QuickTips.init();//tip 提示
		Ext.form.Field.prototype.msgTarget = 'side';
		
		grid.render('editor-grid');
		dataStore.load({params:{start:0, limit:10}});
	});
	