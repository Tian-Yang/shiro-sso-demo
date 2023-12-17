package com.shiro.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiro.demo.entity.MemberInfoEntity;
import com.shiro.demo.vo.member.MemberListQueryReqVO;
import com.shiro.demo.vo.member.MemberListQueryRespVO;
import com.shiro.demo.vo.role.RoleListQueryReqVO;
import com.shiro.demo.vo.role.RoleListQueryRespVO;

/**
* @author nmtz
* @description 针对表【member_info】的数据库操作Mapper
* @createDate 2023-12-10 16:51:34
* @Entity generator.domain.MemberInfoEntity
*/
public interface MemberInfoMapper extends BaseMapper<MemberInfoEntity> {

    IPage<MemberListQueryRespVO> pageQuery(IPage<MemberListQueryReqVO> page);

}




