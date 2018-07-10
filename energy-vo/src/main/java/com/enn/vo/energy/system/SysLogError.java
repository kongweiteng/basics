package com.enn.vo.energy.system;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 *
 * </p>
 *
 * @author kongweiteng
 * @since 2018-06-27
 */
@TableName("sys_log_error")
@ApiModel(value = "sys_log_error")
@Data
public class SysLogError extends Model<SysLogError> {


    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Ignore
    private Long id;
    /**
     * 错误代码
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String msg;
    /**
     * 错误详情
     */
    private String error;
    /**
     * 项目名称
     */
    private String project;

    /**
     * 时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

    private String request;

    private String ip;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysLogError{" +
                "id=" + id +
                ", code=" + code +
                ", msg=" + msg +
                ", error=" + error +
                ", project=" + project +
                "}";
    }
}
