 /* Copyright 2008 [Pomer], Inc. All rights reserved.
 * Website: http://www.pomer.org.cn/
 */
package appcommon.flex
{

	
	import com.adobe.cairngorm.control.FrontController;
	import com.company.project.user_info.UserInfoRegister;

	public class GlobalController extends FrontController
	{
		public function GlobalController()
		{
			initialiseCommands();
		}

		public function initialiseCommands():void
		{
			UserInfoRegister.initialiseCommands(this);
		}


	}
}