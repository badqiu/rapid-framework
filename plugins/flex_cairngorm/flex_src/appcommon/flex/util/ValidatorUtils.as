package appcommon.flex.util
{
	import mx.validators.Validator;
	
	
	public class ValidatorUtils
	{
		public function ValidatorUtils()
		{
		}

		public static function validateAll(validators:Array) : Boolean
		{
			var errors : Array =  Validator.validateAll(validators);
			if(errors.length > 0) {
				return false;
			}else {
				return true;
			}
		}
	}
}