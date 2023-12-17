package com.shiro.demo.controller;

import cn.hutool.core.util.PageUtil;
import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.context.AuthContext;
import com.shiro.demo.service.MemberInfoService;
import com.shiro.demo.util.CharacterUtil;
import com.shiro.demo.util.PasswordGenerator;
import com.shiro.demo.vo.PageResp;
import com.shiro.demo.vo.member.*;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 认证系统 Member API
 * Author TianYang
 * Date 2023/12/7 14:35
 */
@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberInfoService memberInfoService;


    /**
     * 用户列表(分页查询)
     *
     * @param memberListQueryReqVO
     * @return
     */
    @PostMapping("/list")
    public CommonResp<PageResp<MemberListQueryRespVO>> list(@Validated @RequestBody MemberListQueryReqVO memberListQueryReqVO) {
        return CommonResp.success(PageResp.parsePage(memberInfoService.pageQuery(memberListQueryReqVO)));
    }

    /**
     * 新增用户
     *
     * @param memberAddVO
     * @return
     */
    @PostMapping("/add")
    public CommonResp<MemberAddRespVO> add(@Validated @RequestBody MemberAddReqVO memberAddVO) {
        return CommonResp.success(memberInfoService.addMemberInfo(memberAddVO));
    }

    /**
     * 更新用户
     *
     * @return
     */
    @PutMapping("/update")
    public CommonResp<Void> update() {
        return CommonResp.success();
    }

    /**
     * 删除用户
     *
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResp<Void> delete() {
        return CommonResp.success();
    }

    /**
     * 生成用户名
     *
     * @param nameGenerateVO
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    @PostMapping("/generateName")
    public CommonResp<String> generateName(@Validated @RequestBody MemberNameGenerateVO nameGenerateVO) throws BadHanyuPinyinOutputFormatCombination {
        String chineseName = nameGenerateVO.getChineseName();
        return CommonResp.success(memberInfoService.generateName(chineseName));
    }

    /**
     * 生成密码
     * 生成该账号密码，需包含数字（5个）、大小写字母（2个）、符号（一个@ 或者*）三种构成。8位，自动生成。组成顺序随机
     *
     * @return
     */
    @PostMapping("/generatePassword")
    public CommonResp<String> generatePassword() {
        log.info("memberId:{}", AuthContext.getMemberId());
        return CommonResp.success(PasswordGenerator.generatePassword());
    }


}
