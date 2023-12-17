package com.shiro.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiro.demo.entity.RoleInfoEntity;
import com.shiro.demo.vo.role.RoleListQueryReqVO;
import com.shiro.demo.vo.role.RoleListQueryRespVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author TianYang
 * @description 针对表【role_info】的数据库操作Mapper
 * @createDate 2023-12-13 14:49:16
 * @Entity generator.domain.Roleinfo
 */
public interface RoleInfoMapper extends BaseMapper<RoleInfoEntity> {

    IPage<RoleListQueryRespVO> pageQuery(IPage<RoleListQueryReqVO> page);

}




