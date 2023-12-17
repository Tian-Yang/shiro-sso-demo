package com.shiro.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiro.demo.entity.MenuInfoEntity;
import com.shiro.demo.vo.menu.MenuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author nmtz
 * @description 针对表【menu_info】的数据库操作Mapper
 * @createDate 2023-12-12 19:30:15
 * @Entity generator.domain.MenuInfoEntity
 */
public interface MenuInfoMapper extends BaseMapper<MenuInfoEntity> {
    List<MenuVO> queryAllMenus(@Param("menuPosition") String menuPosition,@Param("businessDomainCode")String businessDomainCode,@Param("tenantId") Long tenantId);

    List<MenuVO> queryAllMenusByRoleId(@Param("roleId") Long roleId,@Param("businessDomainCode")String businessDomainCode,@Param("tenantId") Long tenantId);

    List<MenuVO> queryAccessibleMenus(@Param("memberId")Long memberId,@Param("menuPosition") String menuPosition,@Param("businessDomainCode")String businessDomainCode,@Param("tenantId") Long tenantId);


}




