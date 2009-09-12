
 
package javacommon.flex.util
{
	import mx.formatters.DateFormatter;
	
	public class DateConvertUtils
	{
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