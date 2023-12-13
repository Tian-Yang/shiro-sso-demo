package com.shiro.demo.controller;

import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.util.CharacterUtil;
import com.shiro.demo.util.PasswordGenerator;
import com.shiro.demo.vo.member.MemberNameGenerateVO;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证系统 Member API
 * Author TianYang
 * Date 2023/12/7 14:35
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/query")
    public CommonResp<Void> query() {
        return CommonResp.success();
    }

    @PostMapping("/add")
    public CommonResp<Void> add() {
        return CommonResp.success();
    }

    @PutMapping("/update")
    public CommonResp<Void> update() {
        return CommonResp.success();
    }

    @DeleteMapping("/delete")
    public CommonResp<Void> delete() {
        return CommonResp.success();
    }

    /**
     *
     * @param nameGenerateVO
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    @PostMapping("/generateName")
    public CommonResp<String>  generateName(@Validated @RequestBody MemberNameGenerateVO nameGenerateVO) throws BadHanyuPinyinOutputFormatCombination {
        String chineseName = nameGenerateVO.getChineseName();
       String pinyin = CharacterUtil.chineseToPinying(chineseName);
        return CommonResp.success(pinyin);
    }

    /**
     * 生成密码
     * 生成该账号密码，需包含数字（5个）、大小写字母（2个）、符号（一个@ 或者*）三种构成。8位，自动生成。组成顺序随机
     * @return
     */
    @PostMapping("/generatePassword")
    public CommonResp<String> generatePassword(){
        return CommonResp.success(PasswordGenerator.generatePassword());
    }


}
