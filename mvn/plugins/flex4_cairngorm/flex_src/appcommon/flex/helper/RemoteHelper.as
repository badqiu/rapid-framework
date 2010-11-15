
/**
 *@author hunhun
 *@qq  104104702
 */
package appcommon.flex.helper
{
	import mx.rpc.remoting.RemoteObject;
	import com.adobe.cairngorm.business.ServiceLocator;
	public class RemoteHelper  
	{
		/**
		 *
		 * @param serviceId
		 * @return the serviceObeject you need
		 *
		 */	 	
		public static function getById(serviceId:String):Object
		{
			return (ServiceLocator.getInstance())[serviceId];
		}

	}
}

