package com.atguigu.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 
 * 
 * @author catherine sun
 * @email catherine.sun20@gmail.com
 * @date 2022-07-10 21:17:41
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Brand ID
	 */
	@TableId
	private Long brandId;
	/**
	 * Brand name
	 */
	@NotBlank(message="Brand name must not be blank.")
	private String name;
	/**
	 * Brand logo storage address
	 */
	@NotEmpty
	@URL(message="Must be a valid url.")
	private String logo;
	/**
	 * Brand description
	 */
	private String descript;
	/**
	 * Whether brand appears or not in brand management system
	 */
	private Integer showStatus;
	/**
	 * Brand name initial
	 */
	@NotEmpty
	@Pattern(regexp = "/^[a-zA-Z]$/",message = "An initial must be an alphabet letter")//自定义校验规则
	private String firstLetter;
	/**
	 * Sort
	 */
	@NotNull
	@Min(value=0,message="Sorting number must be an integer greater than or equal to 0.")
	private Integer sort;

}
