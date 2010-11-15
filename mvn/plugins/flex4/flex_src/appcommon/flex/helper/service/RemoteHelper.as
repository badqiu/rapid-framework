
/**
 *@author hunhun
 *@qq  104104702
 */
package appcommon.flex.helper.service
{
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class RemoteHelper
	{

		public static var service:RemoteObject=new RemoteObject();
		private static var onResultHandler:Function=null;
		private static var onFaultHandler:Function=null;

		public function RemoteHelper()
		{
			throw new Error("can't be instantiated !");
		}
		/**传入远程对象的id进行构造，返回一个静态的远程对象实例
		 * */
		public static function hand(remoteObjectId:String, resultHandler:Function=null, faultHandler:Function=null):RemoteObject
		{
			service.destination=remoteObjectId;
			service.endpoint="../messagebroker/amf";
			service.showBusyCursor=true;
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
			var data:Object=Object(event.result);
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

