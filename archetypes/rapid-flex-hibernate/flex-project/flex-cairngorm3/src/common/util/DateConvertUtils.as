
/**
 *@author badqiu
 *@author hunhun 
 */
package common.util
{
	import mx.formatters.DateFormatter;
	
	public class DateConvertUtils
	{
		public  static const  DATE_FORMAT : String = "YYYY-MM-DD";
		public  static const  DATE_TIME_FORMAT : String = "YYYY-MM-DD J:NN:SS";
		
		public function DateConvertUtils()
		{
		}
		
		public static function format(date:Date,formatString:String):String
		{
			if(date == null){
				return null;
			}
			var df:DateFormatter = new DateFormatter();
			df.formatString = formatString; 
			return df.format(date);
		}
		
	}
}