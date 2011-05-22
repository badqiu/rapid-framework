
/**
 * 用于数字型的DataGridColumn组件，修复按数字排序sort以及列为null时显示为0的bug
 * @author hunhun
 *
 * */
package common.base
{

	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.utils.ObjectUtil;

	public class NumericDataGridColumn extends DataGridColumn
	{
		public static const NUMERIC:String="N";

		public function NumericDataGridColumn(columnName:String=null)
		{
			super(columnName);
			initCompare(NUMERIC);
		}

		function initCompare(numeric:Object):void
		{
			if (numeric == NUMERIC)
			{
				sortCompareFunction=numericCompare;
			}
		}

		/**
		 * Pull the numbers from the objects and call the implementation. TAKEN FROM mx.collections.SortField
		 */
		private function numericCompare(a:Object, b:Object):int
		{
			var fa:Number;
			try
			{
				fa=dataField == null ? Number(a) : Number(a[dataField]);
			}
			catch (error:Error)
			{
			}

			var fb:Number;
			try
			{
				fb=dataField == null ? Number(b) : Number(b[dataField]);
			}
			catch (error:Error)
			{
			}

			return ObjectUtil.numericCompare(fa, fb);
		}


		/**
		 *
		 *当属性为null时，显示" "
		 * */
		override public function itemToLabel(data:Object):String
		{
			if (!data)
				return " ";

			//如果列为null,则返回空字符串
			if (!data[dataField])
			{
				return " ";
			}

			if (labelFunction != null)
				return labelFunction(data, this);

			if (typeof(data) == "object" || typeof(data) == "xml")
			{
				try
				{
					if (!hasComplexFieldName)
						data=data[dataField];
					else
						data=deriveComplexColumnData(data);
				}
				catch (e:Error)
				{
					data=null;
				}
			}

			if (data is String)
				return String(data);

			try
			{
				return data.toString();
			}
			catch (e:Error)
			{
			}

			return " ";
		}
	}


}

