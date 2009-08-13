var SimpleTable = function(form,pageNumber,pageSize,sortColumns) {
	this.pageNumber = pageNumber;
	this.pageSize = pageSize;
	this.sortColumns = sortColumns;
	this.form = form;
	_this = this;
	
	$(".gridTable .gridBody .tableHeader th[sortColumn]").click(function() {
		//handle click sort header
		var column = $(this).attr('sortColumn');
		if(SimpleTable.isOrderByAsc(sortColumns,column)) {
			_this.doJump(null,null,column + " desc" );
		}else {
			_this.doJump(null,null,column + " asc");
		}
	}).mouseover(function() {
		$(this).toggleClass('tableHeaderSortHover',true);
	}).mouseout(function() {
		$(this).toggleClass('tableHeaderSortHover',false);
	});
	
	// add 'desc' or 'asc' class to sorted tableHeader
	var sortInfos = SimpleTable.getSortInfos(sortColumns);
	for(var i = 0; i < sortInfos.length; i++) {
		var info = sortInfos[i];
		var selector = '.gridTable .tableHeader th[sortColumn="'+info.column+'"]';
		var order = info.order ? info.order : 'asc';
		$(selector).addClass("sort " + order.toLowerCase());
	}
	
	//handle highlight
	$(".gridTable .gridBody .tableBody tr").mouseover(function() {
		$(this).toggleClass('highlight',true);
	}).mouseout(function() {
		$(this).toggleClass('highlight',false);
	});
};
// static methods
SimpleTable.getSortInfos = function(sortColumns) {
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
}
SimpleTable.isOrderByAsc = function(defaultSortColumns,currentColumn) {
	var infos = SimpleTable.getSortInfos(defaultSortColumns);
	for(var i = 0; i < infos.length; i++) {
		var info = infos[i];
		var order = info.order ? info.order : 'asc';
		if(info.column == currentColumn && 'desc' == info.order) {
			return false;
		}
	}
	return true;
}

SimpleTable.fireSubmit = function(form) {
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

SimpleTable.prototype = {
	doJump : function(pageNumber,pageSize,sortColumns) {
		pageNumber = pageNumber || this.pageNumber;		
		pageSize = pageSize || this.pageSize;		
		sortColumns = sortColumns || this.sortColumns ;	
		
		$('#pageNumber').val(pageNumber);	
		$('#pageSize').val(pageSize);	
		$('#sortColumns').val(sortColumns);
		//alert("pageNumber:"+pageNumber+" pageSize:"+pageSize+" sortColumns:"+sortColumns+" this.form:"+this.form);
		SimpleTable.fireSubmit(this.form);
		//document.getElementById(this.form).submit();	
	},
	togglePage : function(page) {
		this.doJump(page,null,null);
	},
	togglePageSize : function(pageSize) {
		this.doJump(null,pageSize,null);
	},
	toggleSort : function(sortColumns) {
		this.doJump(null,null,sortColumns);
	}
};