 /* Copyright 2008 [Pomer], Inc. All rights reserved.
 * Website: http://www.pomer.org.cn/
 */
package appcommon.flex
{

	import com.adobe.cairngorm.control.FrontController;
	import com.company.project.user_info.command.ListUserInfoCommand;
	import com.company.project.user_info.command.*;
	import com.company.project.user_info.event.DeleteUserInfoEvent;
	import com.company.project.user_info.event.ListUserInfoEvent;
	import com.company.project.user_info.event.SaveUserInfoEvent;



	public class GlobalController extends FrontController
	{
		public function GlobalController()
		{
			initialiseCommands();
		}

		public function initialiseCommands():void
		{
			addCommand(ListUserInfoEvent.EVENT_NAME,ListUserInfoCommand);
			addCommand(SaveUserInfoEvent.EVENT_NAME,SaveUserInfoCommand);
			addCommand(DeleteUserInfoEvent.EVENT_NAME,DeleteUserInfoCommand);
		}


	}
}