

/**
 *@author badqiu
 */
package common.util

{
	public class NumberConvertUtils {
		
		public static function toNumber(v : Object) : Number {
			if(v) return Number(v)
			else return NaN;
		}
		
		public static function format(v : Object) : String {
			if(v == null) return "";
			
			if(v is Number) {
				var num : Number = Number(v);
				if(isNaN(num)) 
					return "";
				else
					return String(num);
			}
			
			return String(v);
		}
	}
}