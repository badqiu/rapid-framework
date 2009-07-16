/**
 * @include "../../extclient/RowExpander.js"
 * @include "../../extclient/gridToExcel.js"
 * @include "../../extclient/SearchField.js"
 */
 
/*
 * [Userinfo] author by $YourName$
 */

Ext.BLANK_IMAGE_URL = "../../widgets/ext-2.2.1/resources/images/default/s.gif";
Ext.QuickTips.init();
Ext.form.Field.prototype.msgTarget = 'qtip';
 

Ext.namespace('com.awd');
Ext.namespace('com.awd.userinfo');

/**
 * 查询表单
 * @class com.awd.userinfo.queryformpanel
 * @extends Ext.form.FormPanel
 */
com.awd.userinfo.queryformpanel = Ext.extend(Ext.form.FormPanel,{
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

	        	,{xtype:'textfield',fieldLabel:'username',name:'s_username',width:288}
	        	,{xtype:'textfield',fieldLabel:'userphone',name:'s_userphone',width:288}
	            ]
	    });
		com.awd.userinfo.queryformpanel.superclass.initComponent.call(this);
	}
});

/**
 * 查询窗口
 * @class com.awd.userinfo.querywin
 * @extends Ext.Window
 */
com.awd.userinfo.querywin = Ext.extend(Ext.Window,{
	initComponent:function() {
		Ext.apply(this,{
	        title:'高级查询',
	        width:455,
	        height:395,
	        modal:true,
	        closeAction:'hide',
	        layout:'fit'
	    });
		com.awd.userinfo.querywin.superclass.initComponent.call(this);
	}
});

/**
 * 内容表单
 * @class com.awd.userinfo.dtlformpanel
 * @extends Ext.form.FormPanel
 */
com.awd.userinfo.dtlformpanel = Ext.extend(Ext.form.FormPanel,{
	initComponent:function() {
		Ext.apply(this,{
	        labelWidth:100,
	        labelAlign:'right',
	        frame:true,
//	        bodyStyle:'padding:10px',
	        id:'dtlformpanel',
	        autoScroll:true,//滚动条
			items:[{
		            xtype:'panel',
		            layout:'column',
		            width:400,
		            border:false,
		            defaults:{border:false}
		        	}

	        		,{xtype:'textfield',fieldLabel:'username',name:'username',width:288}
	        		,{xtype:'textfield',fieldLabel:'userphone',name:'userphone',width:288}
	        ]
	    });
	    com.awd.userinfo.dtlformpanel .superclass.initComponent.call(this);
	}
	
});

/**
 * 表单窗口
 * @class com.awd.userinfo.dtlwin
 * @extends Ext.Window
 */		
com.awd.userinfo.dtlwin =  Ext.extend(Ext.Window,{
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
		com.awd.userinfo.dtlwin.superclass.initComponent.call(this);
	}
});

/**
 * 搜索域类扩展
 * @class com.awd.userinfo.searchfield
 * @extends Ext.app.SearchField
 */
com.awd.userinfo.searchfield = Ext.extend(Ext.app.SearchField,{
	initComponent:function() {
		Ext.apply(this,{
	                width: 130
	    });
		com.awd.userinfo.searchfield.superclass.initComponent.call(this);
	}
	
});

/**
 * 主表格入口
 * @class com.awd.userinfo
 * @extends Ext.grid.GridPanel
 */
com.awd.userinfoGrid = Ext.extend(Ext.grid.GridPanel,{
    initComponent:function() {
    	this.pageSize=10;
    	this.ds = new Ext.data.Store({
	        url:'./extlist.do',
	        reader:new Ext.data.JsonReader({
	            root:'list',
	            totalProperty:'totalSize',
	            id:'id'
		        }
		        ,['id','username','userphone',]
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

	        ,{header:'username',width:100,sortable:true,dataIndex:'username'}
	        ,{header:'userphone',width:100,sortable:true,dataIndex:'userphone'}
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
	        	{text:'新增',cls:'x-btn-text-icon',icon:'../../images/add.gif',handler:this.addUserinfo,scope:this},'-'
	        	,{text:'修改',cls:'x-btn-text-icon',icon:'../../images/edit.gif',handler:this.editUserinfo,scope:this},'-'
	        	,{text:'删除',cls:'x-btn-text-icon',icon:'../../images/delete.gif',handler:this.deleteUserinfo,scope:this},'-'
	        	,{text:'查询',id:'btn-query',cls:'x-btn-text-icon',icon:'../../images/query.gif',handler:this.buildQueryWin,scope:this}
				,{text:'导出到Excel', cls:'x-btn-text-icon',icon:'../../images/export.gif',handler:this.exporttoexcel,scope:this}
	        	,'->'
	        	,'搜索范围：'
				,{xtype:'combo',
	            fieldLabel:'搜索范围',
	            emptyText:'请选择...',
	            name:'field_type',
	            id:'fieldtype',
	            hiddenName:'field_type',
	            store:new Ext.data.Store({
	                proxy:new Ext.data.HttpProxy({url:'./extfield.do'}),
	                reader:new Ext.data.JsonReader({}, ['code','name']),
	                autoLoad:false
	            }),
	            displayField:'name',
	            valueField:'code',
	            forceSelection: false,
	            selectOnFocus: true,
	            editable: false,
	            triggerAction: 'all',
	            allowBlank:true,
	            mode: 'remote',
	            width:120
	            ,listeners: {          
          			select:function(object,record,index){
          				Ext.getCmp("searchfld").getStore().baseParams['field_type'] = this.value;
          			}
          		}    
	        	},new com.awd.userinfo.searchfield({id:'searchfld',store:this.ds})
	        ]
		});
		//调用父类构建函数
        com.awd.userinfoGrid.superclass.initComponent.call(this);
        //加载数据
        this.store.load({params:{start:0}});
        
 		//扩展类的详细弹出窗口
 		this.dtlformpanel = new com.awd.userinfo.dtlformpanel();
 		this.dtlwin =  new com.awd.userinfo.dtlwin({items:this.dtlformpanel,buttons:[{
	            text:'保存',
	            handler:this.saveUserinfo,
	            scope:this
	        },{
	            text:'取消',
	            handler:function(){this.dtlwin.hide();},
	            scope:this
	        }]});
	    
	    //扩展类的查询弹出窗口
	    this.queryformpanel = new com.awd.userinfo.queryformpanel();
	    this.querywin =  new com.awd.userinfo.querywin({items:this.queryformpanel,buttons:[{
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

    //导出excel方法
	,exporttoexcel:function(){
        var vExportContent = this.getExcelXml();
        if (Ext.isIE6 || Ext.isIE7 || Ext.isSafari || Ext.isSafari2 || Ext.isSafari3) {
            var fd=Ext.get('frmDummy');
            if (!fd) {
                fd=Ext.DomHelper.append(Ext.getBody(),{tag:'form',method:'post',id:'frmDummy',action:'../../exportexcel.jsp', target:'_blank',name:'frmDummy',cls:'x-hidden',cn:[
                    {tag:'input',name:'exportContent',id:'exportContent',type:'hidden'}
                ]},true);
            }
            fd.child('#exportContent').set({value:vExportContent});
            fd.dom.submit();
        } else {
            document.location = 'data:application/vnd.ms-excel;base64,'+Base64.encode(vExportContent);
        }
	}
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
				        this.dtlwin.setTitle('修改Userinfo');
				        this.dtlwin.show();
				        this.dtlformpanel.form.reset();
				        this.dtlformpanel.form.loadRecord(record);
				        this.dtlformpanel.url = './extupdate.do';
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
						url:'./extdelete.do?ids='+record.data.id,
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
        this.dtlwin.setTitle('修改Userinfo');
        this.dtlwin.show();
        this.dtlformpanel.form.reset();
        this.dtlformpanel.form.loadRecord(record);
        this.dtlformpanel.url = './extupdate.do';
    }
    
    //新建窗口
    ,addUserinfo : function(){
        this.dtlwin.setTitle('新建Userinfo');
        this.dtlwin.show();
        this.dtlformpanel.form.reset();
	    this.dtlformpanel.url = './extsave.do';
	}
	
	//编辑操作
    ,editUserinfo:function(){
    	var records = this.getSelectionModel().getSelections();//单选
    	
	   if (records.length!=1) {
			Ext.Msg.alert("提示", "请先选择要修改的记录");
			return;
		}
	    this.dtlwin.setTitle('修改Userinfo');
	    this.dtlwin.show();
	    this.dtlformpanel.form.reset();
	    this.dtlformpanel.form.loadRecord(records[0]);
	    this.dtlformpanel.url = './extupdate.do';

    }
    
    //删除操作
    ,deleteUserinfo:function(){
    	var records = this.getSelectionModel().getSelections();
		if (!records||records.length == 0) {
			Ext.Msg.alert("提示", "请先选择要删除的�记录");
			return;
		}
		Ext.MessageBox.confirm('确认删除','确定要删除这些记录?',function(btn){
			if (btn == 'yes'){
				Ext.Ajax.request({
					url:'./extdelete.do?ids='+this.getRecordArrayUtils(records, 'id'),
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
    ,saveUserinfo:function(){
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
        Ext.getCmp("fieldtype").clearValue();
        Ext.getCmp("searchfld").setValue("");
        Ext.getCmp("searchfld").getStore().baseParams['field_type']=null;
    }
    //查询操作
    ,queryOrder:function(){
    	this.getStore().baseParams['s_id'] = this.queryformpanel.form.findField('s_id').getRawValue();
    	this.getStore().baseParams['s_username'] = this.queryformpanel.form.findField('s_username').getRawValue();
    	this.getStore().baseParams['s_userphone'] = this.queryformpanel.form.findField('s_userphone').getRawValue();
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
Ext.reg('userinfo', com.awd.userinfoGrid);


