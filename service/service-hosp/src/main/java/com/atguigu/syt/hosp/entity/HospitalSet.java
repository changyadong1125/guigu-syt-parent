package com.atguigu.syt.hosp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 医院设置表
 * </p>
 *
 * @author atguigu
 * @since 2023-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HospitalSet implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 医院名称
     */
    private String hosname;

    /**
     * 医院编号
     */
    private String hoscode;

    /**
     * api基础路径
     */
    private String apiUrl;

    /**
     * 签名秘钥
     */
    private String signKey;

    /**
     * 联系人
     */
    private String contactsName;

    /**
     * 联系人手机
     */
    private String contactsPhone;

    /**
     * 状态 1可用 0禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除(1:已删除，0:未删除)
     */
    private Integer isDeleted;


}
