package javacommon.flex.util
{
	import flash.display.Sprite;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	
	public class AppCrudUtils
	{
		public function AppCrudUtils()
		{
			//TODO: implement function
		}

		public static function confirmDelete(selectedList:ArrayCollection,yesHandler :Function,parent:Sprite = null):void{
			if(selectedList.length <= 0){
			     Alert.show("Please select record");
			     return;
			}
			
	     	Alert.show("Are you sure to detele selected record?","warning",Alert.YES|Alert.NO,parent,function(event:CloseEvent):void {
		     	if(event.detail == Alert.YES){
		     		yesHandler();
		    	}
	     	});
		}
	}
}