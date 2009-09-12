<#include "/macro.include"/> 
<#include "/custom.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?lower_case>
<#assign actionExtension = "do"> 
/**
 * [${table.tableAlias}] author by $YourName$
 * @include "../../extclient/RowExpander.js"
 * @include "../../extclient/gridToExcel.js"
 * @include "../../extclient/SearchField.js"
 */
 
Ext.namespace('${basepackage}');
Ext.namespace('${basepackage}.${classNameLower}');

/**
 * 查询表单
 * @class ${basepackage}.${classNameLower}.queryformpanel
 * @extends Ext.form.FormPanel
 */
${basepackage}.${classNameLower}.queryformpanel = Ext.extend(Ext.form.FormPanel,{
	initComponent:function() {
		Ext.apply(this,{
	        labelAlign:'right',
	        labelWidth:80,
	        defaultType:'textfield',
	        bodyStyle:'padding:20px;',
	        defaults:{width:290},
	        items:[{
	            xtype:'panel',
	            html:'请在下面输入查询条件：',
	            width:370,
	            border:false,
	            style:'padding:10 0 0 3;margin:0 0 20 10;border-bottom:1px solid #ccc;font-size:14px;font-weight:bold;'
	        	}
	        	,{
	            xtype:'panel',
	            layout:'column',
	            width:400,
	            border:false,
	            defaults:{border:false}
	        	}
	        	<#list table.notPkColumns as column>
	        	,{xtype:'textfield',fieldLabel:'${column.columnAlias}',name:'s_${column.columnNameLower}',width:288}
	        	</#list>
	            ]
	    });
		${basepackage}.${classNameLower}.queryformpanel.superclass.initComponent.call(this);
	}
});

/**
 * 查询窗口
 * @class ${basepackage}.${classNameLower}.querywin
 * @extends Ext.Window
 */
${basepackage}.${classNameLower}.querywin = Ext.extend(Ext.Window,{
	initComponent:function() {
		Ext.apply(this,{
	        title:'高级查询',
	        width:455,
	        height:395,
	        modal:true,
	        closeAction:'hide',
	        layout:'fit'
	    });
		${basepackage}.${classNameLower}.querywin.superclass.initComponent.call(this);
	}
});

/**
 * 内容表单
 * @class ${basepackage}.${classNameLower}.dtlformpanel
 * @extends Ext.form.FormPanel
 */
${basepackage}.${classNameLower}.dtlformpanel = Ext.extend(Ext.form.FormPanel,{
	initComponent:function() {
		Ext.apply(this,{
	        labelWidth:100,
	        labelAlign:'right',
	        frame:true,
//	        bodyStyle:'padding:10px',
	        autoScroll:true,//滚动条
			items:[{
		            xtype:'panel',
		            layout:'column',
		            width:400,
		            border:false,
		            defaults:{border:false}
		        	}
	            	<#list table.columns as column>
					<#if column.pk>
	        		,{xtype:'hidden',fieldLabel:'${column.columnAlias}',name:'${column.columnNameLower}',width:288}
	        		<#else>
	        		,{xtype:'textfield',fieldLabel:'${column.columnAlias}',name:'${column.columnNameLower}',width:288}
	        		</#if>
	        		</#list>
	        ]
	    });
	    ${basepackage}.${classNameLower}.dtlformpanel .superclass.initComponent.call(this);
	}
	
});

/**
 * 表单窗口
 * @class ${basepackage}.${classNameLower}.dtlwin
 * @extends Ext.Window
 */		
${basepackage}.${classNameLower}.dtlwin =  Ext.extend(Ext.Window,{
	initComponent:function() {
		Ext.apply(this,{
	        width:535,
	        height:400,
	        layout:'fit',
	        border:false,
	        closeAction:'hide',
	        modal:true,
	        maximizable:true,
	        constrain: true,
	        collapsible:true
	    });
		${basepackage}.${classNameLower}.dtlwin.superclass.initComponent.call(this);
	}
});


/**
 * 主表格入口
 * @class ${basepackage}.${classNameLower}
 * @extends Ext.grid.GridPanel
 */
${basepackage}.${classNameLower}Grid = Ext.extend(Ext.grid.GridPanel,{
    initComponent:function() {
    	this.pageSize=10;
    	this.ds = new Ext.data.Store({
	        url:'../${className}/extlist.do',
	        reader:new Ext.data.JsonReader({
	            root:'list',
	            totalProperty:'totalSize',
	            id:'id'
		        }
		        ,[<#list table.columns as column>'${column.columnNameLower}',</#list>]
	        ),
	        baseParams:{
	            limit:this.pageSize
	        },
	        remoteSort:true
	    });
	    
	    //行扩展
	    this.expander = new Ext.grid.RowExpander({
	        tpl : new Ext.Template(
	            '<p style="margin-left:70px"><b>字典内容:</b> {kvalue}</p><br>'
	        )
	    });
	    
	    this.sm = new Ext.grid.CheckboxSelectionModel();
		this.cm = new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer(),
		    this.sm,
		    this.expander
		    <#list table.notPkColumns as column>
	        ,{header:'${column.columnAlias}',width:100,sortable:true,dataIndex:'${column.columnNameLower}'}
	        </#list>
		]);
		
		/**
		 * 扩展类的构建开始
		 */
		Ext.apply(this,{
			store:this.ds
	        ,sm:this.sm
	        ,cm: this.cm
			,plugins:this.expander
			,collapsible: true
			,viewConfig:{forceFit:true}
	        ,bbar:new Ext.PagingToolbar({
	            pageSize:this.pageSize,
	            store:this.ds,
	            displayInfo:true
	        })
	        , tbar:[
	        	{text:'新增',cls:'x-btn-text-icon',iconCls:'addicon',handler:this.add${className},scope:this},'-'
	        	,{text:'修改',cls:'x-btn-text-icon',iconCls:'editicon',handler:this.edit${className},scope:this},'-'
	        	,{text:'删除',cls:'x-btn-text-icon',iconCls:'deleteicon',handler:this.delete${className},scope:this},'-'
	        	,{text:'查询',id:'btn-query',cls:'x-btn-text-icon',iconCls:'queryicon',handler:this.buildQueryWin,scope:this}
	        	,'->'
	        	,'搜索范围：'
				,{xtype:'combo',
	            fieldLabel:'搜索范围',
	            emptyText:'请选择...',
	            name:'field_type',
	            hiddenName:'field_type',
	            store:new Ext.data.ArrayStore({
        			fields: ['name','code'],
        			data: [<#list table.notPkColumns as column>['${column.columnAlias}', '${column.columnNameLower}']<#if column_has_next>,</#if></#list>]
        		}),
	            displayField:'name',
	            valueField:'code',
	            forceSelection: false,
	            selectOnFocus: true,
	            editable: false,
	            triggerAction: 'all',
	            allowBlank:true,
	            mode: 'local',
	            width:120
	            ,listeners: {          
          			select:{fn:function(object,record,index){
          				this.getTopToolbar().items.get("searchfld").getStore().baseParams['field_type'] = object.getValue();
          			},scope:this}
          		}    
	        	},{xtype:"searchfield",itemId:"searchfld",width: 130,store:this.ds}
	        ]
		});
		//调用父类构建函数
        ${basepackage}.${classNameLower}Grid.superclass.initComponent.call(this);
        //加载数据
        this.store.load({params:{start:0}});
        
 		//扩展类的详细弹出窗口
 		this.dtlformpanel = new ${basepackage}.${classNameLower}.dtlformpanel();
 		this.dtlwin =  new ${basepackage}.${classNameLower}.dtlwin({items:this.dtlformpanel,buttons:[{
	            text:'保存',
	            handler:this.save${className},
	            scope:this
	        },{
	            text:'取消',
	            handler:function(){this.dtlwin.hide();},
	            scope:this
	        }]});
	    
	    //扩展类的查询弹出窗口
	    this.queryformpanel = new ${basepackage}.${classNameLower}.queryformpanel();
	    this.querywin =  new ${basepackage}.${classNameLower}.querywin({items:this.queryformpanel,buttons:[{
	            text:'确定',
	            handler:this.queryOrder,
	            scope:this
	        },{
	            text:'取消',
	            handler:function(){this.querywin.hide();},
	            scope:this
	        }]});
	    //双击操作
 		this.on({"dblclick":this.dblclick});
 		//右键菜单监听
 		this.addListener('rowcontextmenu', this.onMessageContextMenu);
    }
    
   /**
    * 构建函数结束
    */

	//右键菜单
    ,onMessageContextMenu : function (grid, rowIndex, e) {
		e.stopEvent();
		var coords = e.getXY();
		var record = grid.getStore().getAt(rowIndex);
		var messageContextMenu = new Ext.menu.Menu({
			id: 'messageContextMenu',
			items: [{icon:'../../images/edit.gif',text: '编辑',handler: rgtEdit,scope: this},
	        		{id: 'delete',icon:'../../images/delete.gif',handler: rgtDelete,text: '删除'
	        }]
	    });
	    //右键编辑
	    function rgtEdit() {
	            		messageContextMenu.hide();
				        this.dtlwin.setTitle('修改${table.tableAlias}');
				        this.dtlwin.show();
				        this.dtlformpanel.form.reset();
				        this.dtlformpanel.form.loadRecord(record);
				        this.dtlformpanel.url = '../${className}/extupdate.do';
	    };
	    //右键删除
		function rgtDelete() {
			messageContextMenu.hide();
			if (!record||record.length == 0) {
				Ext.Msg.alert("提示", "请先选择要删除的�记录");
				return;
			}
			Ext.MessageBox.confirm('确认删除','确定要删除这些记录?',function(btn){
				if (btn == 'yes'){
						Ext.Ajax.request({
						url:'../${className}/extdelete.do?ids='+record.data.${table.idColumn.columnNameLower},
						method:'POST',
						success:function(response){
							var data = Ext.util.JSON.decode(response.responseText);
							if (data.success == true){
								grid.getStore().remove(record);
								grid.getView().refresh();
							}
							else{
								Ext.MessageBox.alert('警告',data.msg);
							}
							 grid.getStore().reload();
						},
						scope:this
					});
				}},this);
		};
		messageContextMenu.showAt([coords[0], coords[1]]);
		e.preventDefault();//to disable the standard browser context menu
	}
	
	//双击事件
    ,dblclick :function(){
	    	var sm = this.getSelectionModel();
	   		var record=null;
			try{
				record=sm.getSelected();
				if(record==null){
					return;
				}
			}
			catch(e){
				try{
					record=sm.selection.record();
				}
				catch(ex){}
			}
	    	this.showWinForm(record);
	}
	//双击打开窗口
    ,showWinForm:function(record){
        this.dtlwin.setTitle('修改${table.tableAlias}');
        this.dtlwin.show();
        this.dtlformpanel.form.reset();
        this.dtlformpanel.form.loadRecord(record);
        this.dtlformpanel.url = '../${className}/extupdate.do';
    }
    
    //新建窗口
    ,add${className} : function(){
        this.dtlwin.setTitle('新建${table.tableAlias}');
        this.dtlwin.show();
        this.dtlformpanel.form.reset();
	    this.dtlformpanel.url = '../${className}/extsave.do';
	}
	
	//编辑操作
    ,edit${className}:function(){
    	var records = this.getSelectionModel().getSelections();//单选
    	
	   if (records.length!=1) {
			Ext.Msg.alert("提示", "请先选择要修改的记录");
			return;
		}
	    this.dtlwin.setTitle('修改${table.tableAlias}');
	    this.dtlwin.show();
	    this.dtlformpanel.form.reset();
	    this.dtlformpanel.form.loadRecord(records[0]);
	    this.dtlformpanel.url = '../${className}/extupdate.do';

    }
    
    //删除操作
    ,delete${className}:function(){
    	var records = this.getSelectionModel().getSelections();
		if (!records||records.length == 0) {
			Ext.Msg.alert("提示", "请先选择要删除的�记录");
			return;
		}
		Ext.MessageBox.confirm('确认删除','确定要删除这些记录?',function(btn){
			if (btn == 'yes'){
				Ext.Ajax.request({
					url:'../${className}/extdelete.do?ids='+this.getRecordArrayUtils(records, '${table.idColumn.columnNameLower}'),
		            method:'POST',
		            success:function(response){
		                var data = Ext.util.JSON.decode(response.responseText);
		                if (data.success == true){
			                for(var i = 0; i < records.length; i++) {
							 	this.getStore().remove(records[i]);
			                    this.getView().refresh();
							 }
							 this.getStore().reload();
		                }
		                else{
		                    Ext.MessageBox.alert('警告',data.msg);
		                }
		            },
		            scope:this
		        });
			}
		},this);
    }
    
    //保存操作
    ,save${className}:function(){
		if (this.dtlformpanel.form.isValid() == false){
	        return;
	    }
	    this.dtlformpanel.form.submit({
	        url:this.dtlformpanel.url,
	        success:function(form,action){
	        	Ext.MessageBox.alert('警告',action.result.msg);
	            this.dtlwin.hide();
	          	this.getStore().reload();
	        },
	        scope:this,
	        failure:function(form,action){
	            Ext.MessageBox.alert('警告',action.result.msg);
	        }
	    })
	
    }
    //新建查询窗口
    ,buildQueryWin: function(){
    	this.querywin.setTitle('查询');
        this.querywin.show();
        this.getTopToolbar().items.get("searchfld").setValue("");
        this.getTopToolbar().items.get("searchfld").getStore().baseParams['field_type']=null;
    }
    //查询操作
    ,queryOrder:function(){
    	<#list table.notPkColumns as column>
    	this.getStore().baseParams['s_${column.columnNameLower}'] = this.queryformpanel.form.findField('s_${column.columnNameLower}').getRawValue();
		</#list>
		this.getStore().load();
		this.querywin.hide();
    }
    //工具类
    ,getRecordArrayUtils : function(records,field) {
		var result = [];
		for(var i = 0; i < records.length; i++) {
			result.push(records[i].get(field));
		}
		return result;
	}

});
 
/**
 * 注册主表格的xtype
 */
Ext.reg('${classNameLower}', ${basepackage}.${classNameLower}Grid);


