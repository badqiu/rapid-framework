package appcommon.flex.util

{
	public class ConvertUtils {
		public static function toNumber(v : Object) : Number {
			if(v) return Number(v)
			else return NaN;
		}
	}
}