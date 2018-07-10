package test;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.util.Lists;

/**
* @author kai.guo
* @version 创建时间：2018年6月12日 上午9:43:56
* @Description 类描述
*/
public class StreamTest {
	
	 public static void main(String[] args) {
		
		 /*List<BigDecimal> ageList=Lists.newArrayList();
		 ageList.add(new BigDecimal("21"));
		 ageList.add(new BigDecimal("11"));
		 ageList.add(new BigDecimal("32"));
		 ageList.add(new BigDecimal("53"));
		 ageList.add(new BigDecimal("37"));*/
		 List<Long> ageList=Lists.newArrayList();
		 ageList.add(21L);
		 ageList.add(11L);
		 ageList.add(32L);
		 ageList.add(53L);
		 ageList.add(37L);
		 
		 
		 Long result=ageList.stream().parallel().reduce(Long::sum).get();
		 System.out.println("result:=="+result);
		 
		 long l = Stream.iterate(1L, i -> i + 1).limit(300).parallel().reduce(Long::sum).get();
		    System.out.println(l);
		 
		 
	}
	

}
