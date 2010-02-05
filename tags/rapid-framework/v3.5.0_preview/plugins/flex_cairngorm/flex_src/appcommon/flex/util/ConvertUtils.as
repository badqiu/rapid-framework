package appcommon.flex.util

{
	public class ConvertUtils {
		
		public static function toNumber(v : Object) : Number {
			if(v) return Number(v)
			else return NaN;
		}
		
		public static function getNaNIfNull(v : Object) : Number {
			if(v == null) 
				return NaN;
			else 
				return Number(v)
		}
		
		public static function getNullIfNaN(v : Object) : Number {
			if(v == null) return null;
			if(v is Number) {
				if(isNaN(v as Number)) 
					return null;
				else
					return Number(v);
			}else {
				throw new Error("value must is Number");
			}
		}
	}
}