package com.shiro.demo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @TableName menu_info
 */
@TableName(value = "menu_info")
@Data
public class MenuInfoEntity extends BaseEntity implements Serializable {

    /**
     * ID:菜单id
     */
    @TableId
    private Long menuId;

    /**
     * 父菜单id
     */
    private Long parentMenuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单类型catalog:目录,menu:菜单,button:按钮
     */
    private String menuType;

    /**
     * 菜单位置:left 左侧，top 顶部、right 右侧、button 底部
     */
    private String menuPosition;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 业务域编码
     */
    private String businessDomainCode;

    /**
     * 系统域编码
     */
    private Long appDomainCode;

    /**
     * 菜单图标
     */
    private String icon;

    private Integer level;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 删除标记;0 未删除
     */
    private Long delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", menuId=").append(menuId);
        sb.append(", parentMenuId=").append(parentMenuId);
        sb.append(", menuName=").append(menuName);
        sb.append(", menuType=").append(menuType);
        sb.append(", permission=").append(permission);
        sb.append(", path=").append(path);
        sb.append(", businessDomainCode=").append(businessDomainCode);
        sb.append(", appDomainCode=").append(appDomainCode);
        sb.append(", icon=").append(icon);
        sb.append(", sort=").append(sort);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}