
package javacommon.flex.base{
	[Bindable]
    public class BaseEntity{
    	public static var DATE_FORMAT : String = "YYYY-MM-DD";
		public static var DATE_TIME_FORMAT : String = "YYYY-MM-DD J:NN:SS";
		
        public function BaseEntity(){
        }

        public var selected: Boolean = false;

    }

}
