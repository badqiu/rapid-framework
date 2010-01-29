package appcommon.flex.util
{
	import mx.collections.ArrayCollection;
	import mx.collections.IViewCursor;
	import mx.utils.ObjectUtil;
	
	public class CollectionUtils
	{
		public function CollectionUtils()
		{
		}
		
		public static function select(cursor:IViewCursor,propertyName :String):ArrayCollection {
			if(cursor == null) return new ArrayCollection();
			
			var result : ArrayCollection = new ArrayCollection();
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
		
		public static function selectIfTrue(cursor:IViewCursor,propertyName :String):ArrayCollection {
			if(cursor == null) return new ArrayCollection();
			
			var result : ArrayCollection = new ArrayCollection();
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
		/**
		 * 判断list中的对象是否存在
		 */
		public static function existsByPropertyEqual(list:ArrayCollection,forCompareObject:Object,equalPropertyName:String):Boolean {
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
	
		/**
		 * 假如list中与比较的对象的属性值相等,则更新List中某一个对象，
		 */
		public static function updateListItemIfPropertyEqual(list:ArrayCollection,forCompareObject:Object,equalPropertyName:String):Boolean {
			var hasUpdated : Boolean = false;
			var cursor : IViewCursor =  list.createCursor();
			while(!cursor.afterLast) {
				var item:Object = cursor.current;
				if(forCompareObject[equalPropertyName] == item[equalPropertyName]) {
					cursor.remove();
					cursor.insert(forCompareObject);
					hasUpdated = true;
				}
				cursor.moveNext();
			}
			return hasUpdated;
		}
				
	}
}