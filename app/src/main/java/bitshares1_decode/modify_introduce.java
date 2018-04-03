package bitshares1_decode;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class modify_introduce {

	/**
	 * 添加自定义数据结构注意事项
	 * 1. 必须继承ByteSerializable
	 * 2. 实现的public  Field[]  getOrderedFieldName()
	 * 示例如下： 
	 * List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(TYPE));		
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Field[] ret_fields = new Field[fields.size()];
		fields.toArray(ret_fields);
		return ret_fields;
	 * 3. 构造函数必须有无参构造函数。
	 * 4. 如果类型里有定长数组需要定义辅助成员 命名规范是 函数名加Size,同时有最终赋值用的函数 命名规范是set+函数名+Array
	 * 示例如下：
	 *    Character[] testchararray ;
	 *    public static final int testchararraySize =5 ;
	 *    
    public void settestchararrayArray(Object[] array) {
    	for(int i =0;i<testchararraySize;++i){
    		this.testchararray[i] = (Character)array[i];
    	}
    }
	 *    且默认构造函数里必须初始化。
	 *   
	 * 5. 有collection类型的数据结构类似LIST SET MAP 必须在默认构造函数中插入一个初值。
	 * 
	 */
}
