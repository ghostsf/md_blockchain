package com.mindata.blockchain.core.controller;

import com.mindata.blockchain.core.bean.BaseData;
import com.mindata.blockchain.core.bean.ResultGenerator;
import com.mindata.blockchain.core.requestbody.InstructionBody;
import com.mindata.blockchain.core.service.InstructionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 区块body内单个指令的controller
 * @author wuweifeng wrote on 2018/3/7.
 */
@RestController
@RequestMapping("/instruction")
public class InstructionController {
    @Resource
    private InstructionService instructionService;

    /**
     * 构建一条指令，传入各必要参数
     * @param instructionBody instructionBody
     * @return
     * 用私钥签名后的指令
     */
    @PostMapping
    public BaseData build(InstructionBody instructionBody) throws Exception {
        if (!instructionService.checkKeys(instructionBody)) {
             return ResultGenerator.genFailResult("公私钥不是一对");
        }
        return ResultGenerator.genSuccessResult(instructionService.build(instructionBody));
    }
}
