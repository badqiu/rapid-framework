package javacommon.flex.util
{
	import mx.collections.ArrayCollection;
	import mx.collections.IViewCursor;
	import mx.utils.ObjectUtil;
	
	public class CollectionUtils
	{
		public function CollectionUtils()
		{
		}
		
		public static function select(array:ArrayCollection,propertyName :String):ArrayCollection {
			if(array == null) return new ArrayCollection();
			
			var result : ArrayCollection = new ArrayCollection();
			var cursor:IViewCursor = array.createCursor();
  	    	while(!cursor.afterLast){
  	    		var obj: Object = cursor.current;
  	    		if(!obj.hasOwnProperty(propertyName)) {
  	    			throw new Error("object not hasOwnProperty="+propertyName+" objectClass="+ObjectUtil.getClassInfo(obj).name);
  	    		}
  	    		result.addItem(obj[propertyName]);
  	    		cursor.moveNext();
  	    	}
  	    	return result;
		}
		
		public static function selectIfTrue(array:ArrayCollection,propertyName :String):ArrayCollection {
			if(array == null) return new ArrayCollection();
			
			var result : ArrayCollection = new ArrayCollection();
			var cursor:IViewCursor = array.createCursor();
  	    	while(!cursor.afterLast){
  	    		var obj: Object = cursor.current;
            	if(!obj.hasOwnProperty(propertyName)) {
  	    			throw new Error("object not hasOwnProperty="+propertyName+" objectClass="+ObjectUtil.getClassInfo(obj).name);
  	    		}
  	    		if(obj[propertyName])
  	    			result.addItem(obj);
  	    		cursor.moveNext();
  	    	}
  	    	return result;
		}
		
		public static function removeByPropertyEqual(list:ArrayCollection,equalPropertyName :String,propertyValus:Array):void {
			var cursor : IViewCursor =  list.createCursor();
			while(!cursor.afterLast) {
				var obj:Object = cursor.current;
				if(propertyValus.indexOf(obj[equalPropertyName]) >= 0) {
					cursor.remove();
				}else {
					cursor.moveNext();
				}
			}
		}
		
		public static function isPropertyEqual(list:ArrayCollection,forCompareObject:Object,equalPropertyName:String):Boolean {
			var cursor : IViewCursor =  list.createCursor();
			while(!cursor.afterLast) {
				var item:Object = cursor.current;
				if(forCompareObject[equalPropertyName] == item[equalPropertyName]) {
					return true;
				}
				cursor.moveNext();
			}
			return false;
		}
		
	}
}