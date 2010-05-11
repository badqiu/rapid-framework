/**
 * author: badqiu
 */
var SimpleTable = function(form,pageNumber,pageSize,sortColumns) {
	this.pageNumber = pageNumber;
	this.pageSize = pageSize;
	this.sortColumns = sortColumns;
	this.form = form;
	
	$('#pageNumber').val(pageNumber);	
	$('#pageSize').val(pageSize);	
	$('#sortColumns').val(sortColumns);
	
	_this = this;
	$("#"+form+" .gridTable .gridBody .tableHeader th[sortColumn]").click(function() {
		//handle click sort header
		var column = $(this).attr('sortColumn');
		if(SimpleTableUtils.getSortDirection(sortColumns,column) == 'asc') {
			_this.toggleSort("");
		}else if(SimpleTableUtils.getSortDirection(sortColumns,column) == 'desc') {
			_this.toggleSort(column + " asc");
		}else {
			_this.toggleSort(column + " desc");
		}
	}).mouseover(function() {
		$(this).toggleClass('tableHeaderSortHover',true);
	}).mouseout(function() {
		$(this).toggleClass('tableHeaderSortHover',false);
	});
	
	// add 'desc' or 'asc' class to sorted tableHeader
	var sortInfos = SimpleTableUtils.getSortInfos(sortColumns);
	for(var i = 0; i < sortInfos.length; i++) {
		var info = sortInfos[i];
		var selector = "#"+form+' .gridTable .tableHeader th[sortColumn="'+info.column+'"]';
		var order = info.order ? info.order : 'asc';
		$(selector).addClass("sort " + order.toLowerCase());
	}
	
	//handle highlight
	$("#"+form+" .gridTable .gridBody .tableBody tr").mouseover(function() {
		$(this).toggleClass('highlight',true);
	}).mouseout(function() {
		$(this).toggleClass('highlight',false);
	});
	
};
SimpleTable.prototype = {
	doJump : function(pageNumber,pageSize,sortColumns) {
		//pageNumber = pageNumber || this.pageNumber;		
		//pageSize = pageSize || this.pageSize;		
		//sortColumns = sortColumns || this.sortColumns ;	
		
		//$('#pageNumber').val(pageNumber);	
		//$('#pageSize').val(pageSize);	
		//$('#sortColumns').val(sortColumns);
		//alert("pageNumber:"+pageNumber+" pageSize:"+pageSize+" sortColumns:"+sortColumns+" this.form:"+this.form);
		SimpleTableUtils.fireSubmit(this.form);
		//document.getElementById(this.form).submit();	
	},
	togglePage : function(pageNumber) {
		$('#pageNumber').val(pageNumber);
		this.doJump(pageNumber,null,null);
	},
	togglePageSize : function(pageSize) {
		$('#pageSize').val(pageSize);
		this.doJump(null,pageSize,null);
	},
	toggleSort : function(sortColumns) {
		$('#sortColumns').val(sortColumns);
		this.doJump(null,null,sortColumns);
	}
};

// static methods
var SimpleTableUtils = {
	getSortInfos : function(sortColumns) {
		if(!sortColumns) return []; 
		var results = [];
		var sorts = sortColumns.split(",");
		for(var i = 0; i < sorts.length; i++) {
			var columnAndOrder = sorts[i].split(/\s+/);
			var column = columnAndOrder[0];
			var order = columnAndOrder.length > 1 ? columnAndOrder[1] : null;
			
			var sortInfo = new Object();
			sortInfo.column = $.trim(column);
			sortInfo.order = $.trim(order);
			
			results.push(sortInfo);
		}
		return results;
	},
	getSortDirection : function(defaultSortColumns,currentColumn) {
		var infos = SimpleTableUtils.getSortInfos(defaultSortColumns);
		for(var i = 0; i < infos.length; i++) {
			var info = infos[i];
			var order = info.order ? info.order : 'asc';
			if(info.column == currentColumn) {
				return order;
			}
		}
		return null;
	},
	fireSubmit : function(form) {
	    var form = document.getElementById(form);
	    if (form.fireEvent) { //for ie
	    	if(form.fireEvent('onsubmit'))
	    		form.submit();
	    } else if (document.createEvent) { // for dom level 2
			var evt = document.createEvent("HTMLEvents");
	      	//true for can bubble, true for cancelable
	      	evt.initEvent('submit', false, true); 
	      	form.dispatchEvent(evt);
	      	
	      	if(navigator.userAgent.indexOf('Chrome') >= 0) {
	      		form.submit();
	      	}
	    }
	}
}