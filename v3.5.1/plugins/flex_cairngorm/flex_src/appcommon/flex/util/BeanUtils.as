package appcommon.flex.util
{
	import mx.collections.ArrayCollection;
	import mx.utils.ObjectUtil;
	
	public class BeanUtils
	{
		public function BeanUtils()
		{
			//TODO: implement function
		}

		public static function copyProperties(dest:Object, source:Object):void{
			if(source is ArrayCollection){
				return;
			}
			var classInfo:Object = ObjectUtil.getClassInfo(source);
            var properties:Array = classInfo.properties;
            for (var j:int = 0; j < properties.length; j++)
            {
            	var prop : * = properties[j];
            	if(dest.hasOwnProperty(prop.toString())){
            		if(source[prop] is Date){
            			dest[prop] = source[prop];
            		}else{
	            		var type:String = source[prop] == null ? "null" : typeof(source[prop]);
	            		
	            		if("object" == type){
	            			copyProperties(dest[prop],source[prop]);
	            		}else{
	            			try {
	            				dest[prop] = source[prop];
	            			}catch(e:Error){
	            				trace(e.message);
	            			}
	            		}
            		}
            	}
            }
		}
	}
}