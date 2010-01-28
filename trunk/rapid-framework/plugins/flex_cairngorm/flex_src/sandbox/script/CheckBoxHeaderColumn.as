package examples.script
{
import mx.controls.dataGridClasses.DataGridColumn;

[Event(name="click", type="flash.events.MouseEvent")]

public class CheckBoxHeaderColumn extends DataGridColumn
{
   public function CheckBoxHeaderColumn(columnName:String=null)
   {
    super(columnName);
   }
   /**is the checkbox selected**/
   public var selected:Boolean = false;
  
}
}


