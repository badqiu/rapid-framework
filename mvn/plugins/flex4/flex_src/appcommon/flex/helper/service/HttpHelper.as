
/**
 *@author hunhun
 *@qq  104104702
 */

package appcommon.flex.helper.service
{
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	public class HttpHelper
	{
		
		public static var service:HTTPService=new HTTPService();
		private static var onResultHandler:Function=null;
		private static var onFaultHandler:Function=null;
		
		public function  HttpHelper(){
			throw new Error("can't be instantiated !");
		}
		
		public static function hand(url:String, method:String="GET",resultFormat:String="object",resultHandler:Function=null, faultHandler:Function=null):HTTPService{
			service.url=url;
			service.method=method;
			service.resultFormat=resultFormat;
			onResultHandler=resultHandler;
			onFaultHandler=faultHandler;
			service.addEventListener(ResultEvent.RESULT, OnResult);
			service.addEventListener(FaultEvent.FAULT, onFault);
			return service;
		}
		
		/**将远程服务得到的数据传给data对象 由resultHandler方法处理
		 * */
		private static function OnResult(event:ResultEvent):void
		{
			var data:Object=event.result;
			if (onResultHandler != null)
			{
				onResultHandler(data);
			}
		}
		
		private static function onFault(event:FaultEvent):void
		{
			if (onFaultHandler != null)
			{
				onFaultHandler();
			}
			else
			{
				Alert.show("程序出错啦 :" + "/n" + event.message.toString());
			}
		}
	}
}