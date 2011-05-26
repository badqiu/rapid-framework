
/**
 * 可以用于Number属性的双向绑定的输入框组件
 *@author hunhun
 */
package common.base
{
	import flash.events.Event;

	import mx.utils.StringUtil;

	import spark.components.TextInput;

	[DefaultProperty("number")]
	public class TextInputNumber extends TextInput
	{
		private var _number:Number;

		public function TextInputNumber()
		{
			super();
			addEventListener("change", changeHandler);
		}

		private function changeHandler(event:Event):void
		{
			_number=Number(super.text);
			dispatchEvent(new Event("numberChanged"));
		}

		public function set number(value:Number):void
		{
			_number=value;
			super.text=isNaN(_number) ? "" : String(_number);
			dispatchEvent(new Event("numberChanged"));
		}

		[Bindable("numberChanged")]
		public function get number():Number
		{
			if (StringUtil.trim(super.text)=="") return NaN;
			else
				return _number;
		}
	}
}


