package com.atguigu.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author catherine sun
 * @email catherine.sun20@gmail.com
 * @date 2022-07-12 11:51:09
 */
@Data
@TableName("sms_spu_bounds")
public class SpuBoundsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private Long spuId;
	/**
	 * 成长积分
	 */
	private BigDecimal growBounds;
	/**
	 * 购物积分
	 */
	private BigDecimal buyBounds;
	/**
	 * 优惠生效情况（1111，从右到左）：
	 * 		0 - 无优惠，成长积分不赠送；1 - 无优惠，成长积分赠送
	 * 	    0 - 无优惠，购物积分不赠送；1 - 无优惠，购物积分赠送
	 * 	    0 - 有优惠，成长积分不赠送；1 - 有优惠，成长积分赠送
	 * 	    0 - 有优惠，购物积分不赠送；1 - 无优惠，购物积分赠送
	 */
	private Integer work;

}
