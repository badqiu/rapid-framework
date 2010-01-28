package examples.script
{


import flash.events.MouseEvent;

import mx.controls.CheckBox;
import mx.controls.DataGrid;
import mx.events.DataGridEvent;
public class CheckBoxHeaderRenderer extends CheckBox
{



public function CheckBoxHeaderRenderer()
   {
    super();
    //addEventListener("click", clickHandler);
   }
private var _data:CheckBoxHeaderColumn;
override public function get data():Object
{
   return _data;
}

override public function set data(value:Object):void
{
   _data = value as CheckBoxHeaderColumn;
   DataGrid(listData.owner).addEventListener(DataGridEvent.HEADER_RELEASE, sortEventHandler);
   selected = _data.selected;
}

private function sortEventHandler(event:DataGridEvent):void
{
   if (event.itemRenderer == this)
    event.preventDefault();
}
override protected function clickHandler(event:MouseEvent):void
{
   super.clickHandler(event);
   data.selected = selected;
   data.dispatchEvent(event);
}



}
}

