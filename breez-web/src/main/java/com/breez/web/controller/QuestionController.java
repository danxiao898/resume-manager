package com.breez.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.breez.base.result.BreezResult;
import com.breez.web.entities.Question;
import com.breez.web.entities.QuestionRecords;
import com.breez.web.entities.QuestionType;
import com.breez.web.entities.QuestionRecord;
import com.breez.web.service.QuestionRecordService;
import com.breez.web.service.QuestionService;
import com.breez.web.service.QuestionTypeService;
import com.breez.web.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/question")
public class QuestionController {

    private String HTML_PREFIX = "system/question/";

    @Autowired
    QuestionService questionService;

    @Autowired
    QuestionTypeService questionTypeService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    QuestionRecordService questionRecordService;


    @PreAuthorize("hasAuthority('sys:question:list')")
    @GetMapping(value = {"/", ""})
    public String user() {
        return HTML_PREFIX + "question-list";
    }

    /**
     * 分页查询
     * @param page 分页对象 size，current
     * @param question 查询条件
     * @return
     */
    @PreAuthorize("hasAuthority('sys:question:list')")
    @PostMapping("/page")
    @ResponseBody
    public BreezResult page(Page<Question> page, Question question, String questionTypeName) {

        if (questionTypeName != null && questionTypeName.trim().length() > 0) {
            QuestionType questionType = new QuestionType();
            questionType.setName(questionTypeName);
            question.setQuestionType(questionType);
        }

        IPage<Question> questionIPage = questionService.selectPage(page, question);
        System.out.println(questionIPage.getRecords());
        return BreezResult.ok(questionIPage);
    }

    @PreAuthorize("hasAnyAuthority('sys:question:edit','sys:question:add')")
    @GetMapping(value = {"/form","/form/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {
        //1.查询用户信息，包含了用户所拥有的角色
        Question question = questionService.findById(id);
        model.addAttribute("question", question);

        List<QuestionType> questionTypes = questionTypeService.list();
        model.addAttribute("questionTypes", questionTypes);

        return HTML_PREFIX + "question-form";
    }

    @PreAuthorize("hasAnyAuthority('sys:question:edit','sys:question:add')")
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT}, value = "")
    public String saveOrUpdate(Question question) {
        System.out.println(question);
        //1.保存用户表
        questionService.saveOrUpdate(question);
        return "redirect:/question";
    }

    @PreAuthorize("hasAuthority('sys:question:delete')")
    @DeleteMapping("/{id}")
    @ResponseBody
    public BreezResult deleteById(@PathVariable Long id) {
        //删除
        questionService.removeById(id);
        return BreezResult.ok();
    }

    @GetMapping(value = {"/test","/test/{userId}"})
    public String getAllQuestion(@PathVariable(required = false) Long userId, Model model, boolean disableToolBar) {

        if (disableToolBar) {
            model.addAttribute("disableToolBar", true);
        }

        if(userId == null) {
            userId = sysUserService.getCurrentUser().getId();
        }

        List<QuestionRecord> questionRecords = questionRecordService.selectByUserId(userId);

        if(questionRecords != null && questionRecords.size() > 0) {
            model.addAttribute("questionRecords", questionRecords);

            //统计答对的问题个数
            Integer rightCount = 0;
            for (QuestionRecord questionRecord : questionRecords) {
                if(questionRecord.getAnswer().equals(questionRecord.getQuestion().getAnswer())) {
                    rightCount++;
                }
            }
            model.addAttribute("rightCount", rightCount);

            return HTML_PREFIX + "my-answer";
        } else {
            List<Question> questions = questionService.list();

            model.addAttribute("questions", questions);
            model.addAttribute("uid", sysUserService.getCurrentUser().getId());
            System.out.println(questions);

            return HTML_PREFIX + "my-question";
        }



    }

    @PostMapping("/answer")
    public  String answer(QuestionRecords questionRecords) {

        System.out.println(questionRecords);

        questionRecordService.saveBatch(questionRecords.getQuestionRecords());
        return "redirect:/question/test";
    }


}
