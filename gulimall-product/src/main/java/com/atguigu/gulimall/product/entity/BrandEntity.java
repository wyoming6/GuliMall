package com.atguigu.gulimall.product.entity;

import com.atguigu.common.valid.AddGroup;
import com.atguigu.common.valid.ListValue;
import com.atguigu.common.valid.UpdateGroup;
import com.atguigu.common.valid.UpdateStatusGroup;
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
	@NotNull(message = "Must designate brand ID when updating brand", groups = {UpdateGroup.class})
	@Null(message = "Must not designate brand ID when creating brand", groups = {AddGroup.class})
	@TableId
	private Long brandId;
	/**
	 * Brand name
	 */
	@NotBlank(message="Brand name must not be blank.", groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * Brand logo storage address
	 */
	@NotBlank(groups = {AddGroup.class})
	@URL(message="Must be a valid url.",groups = {AddGroup.class, UpdateGroup.class})
	private String logo;
	/**
	 * Brand description
	 */
	private String descript;
	/**
	 * Whether brand appears or not in brand management system
	 */
	@NotNull(groups = {AddGroup.class, UpdateStatusGroup.class})
	@ListValue(vals={0,1}, groups = {AddGroup.class, UpdateStatusGroup.class})
	private Integer showStatus;
	/**
	 * Brand name initial
	 */
	@NotEmpty(groups = {AddGroup.class})
	@Pattern(regexp = "^[a-zA-Z]$",message = "An initial must be an alphabet letter", groups = {AddGroup.class, UpdateGroup.class})//自定义校验规则
	private String firstLetter;
	/**
	 * Sort
	 */
	@NotNull(groups = {AddGroup.class})
	@Min(value=0,message="Sorting number must be an integer greater than or equal to 0.",groups = {AddGroup.class, UpdateGroup.class})
	private Integer sort;

}
