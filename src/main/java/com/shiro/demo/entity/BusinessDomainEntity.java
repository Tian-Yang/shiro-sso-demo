package com.shiro.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName business_domain
 */
@TableName(value ="business_domain")
@Data
public class BusinessDomainEntity implements Serializable {
    /**
     * 
     */
    @TableId
    private String businessDomainCode;

    /**
     * 
     */
    private String businessDomainName;

    /**
     * 
     */
    private String businessDomainDesc;

    /**
     * 认证隔离范围 business_domain：业务域级别隔离 tenant: 租户级别隔离
     */
    private String authIsolationScope;

    /**
     * 
     */
    private Long createBy;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Long updateBy;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private String delFlag;

    /**
     * 
     */
    private Date dataTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        BusinessDomainEntity other = (BusinessDomainEntity) that;
        return (this.getBusinessDomainCode() == null ? other.getBusinessDomainCode() == null : this.getBusinessDomainCode().equals(other.getBusinessDomainCode()))
            && (this.getBusinessDomainName() == null ? other.getBusinessDomainName() == null : this.getBusinessDomainName().equals(other.getBusinessDomainName()))
            && (this.getBusinessDomainDesc() == null ? other.getBusinessDomainDesc() == null : this.getBusinessDomainDesc().equals(other.getBusinessDomainDesc()))
            && (this.getAuthIsolationScope() == null ? other.getAuthIsolationScope() == null : this.getAuthIsolationScope().equals(other.getAuthIsolationScope()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
            && (this.getDataTime() == null ? other.getDataTime() == null : this.getDataTime().equals(other.getDataTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getBusinessDomainCode() == null) ? 0 : getBusinessDomainCode().hashCode());
        result = prime * result + ((getBusinessDomainName() == null) ? 0 : getBusinessDomainName().hashCode());
        result = prime * result + ((getBusinessDomainDesc() == null) ? 0 : getBusinessDomainDesc().hashCode());
        result = prime * result + ((getAuthIsolationScope() == null) ? 0 : getAuthIsolationScope().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getDataTime() == null) ? 0 : getDataTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", businessDomainCode=").append(businessDomainCode);
        sb.append(", businessDomainName=").append(businessDomainName);
        sb.append(", businessDomainDesc=").append(businessDomainDesc);
        sb.append(", authIsolationScope=").append(authIsolationScope);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", dataTime=").append(dataTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}