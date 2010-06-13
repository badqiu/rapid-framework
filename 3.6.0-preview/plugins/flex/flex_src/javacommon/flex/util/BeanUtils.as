package javacommon.flex.util
{
	import mx.collections.ArrayCollection;
	import mx.utils.ObjectUtil;
	
	public class BeanUtils
	{
		public function BeanUtils()
		{
			//TODO: implement function
		}

		public static function copyProperties(dest:Object, orig:Object):void{
			if(orig is ArrayCollection){
				return;
			}
			var classInfo:Object = ObjectUtil.getClassInfo(orig);
            var properties:Array = classInfo.properties;
            for (var j:int = 0; j < properties.length; j++)
            {
            	var prop:* = properties[j];
            	if(dest.hasOwnProperty(prop.toString())){
            		if(orig[prop] is Date){
            			dest[prop] = orig[prop];
            		}else{
	            		var type:String = orig[prop] == null ? "null" : typeof(orig[prop]);
	            		
	            		if("object" == type){
	            			copyProperties(dest[prop],orig[prop]);
	            		}else{
	            			try {
	            				dest[prop] = orig[prop];
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