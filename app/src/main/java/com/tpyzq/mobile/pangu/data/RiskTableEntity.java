package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 风险测评试题
 */
public class RiskTableEntity {

    public String PAPER_TYPE;            //试卷类型 1：客户风险测评
    public String QUESTION_TYPE;         //试题类型0：客观情况 1：主观意识 2：择优选择
    public String QUESTION_NO;          //问题编号
    public String ORGAN_FLAG;           //机构标志 1：个人
    public String ORDER_NO;             //顺序号
    public String QUESTION_CONTENT;     //试题内容
    public String QUESTION_SCORE;       //题目分值
    public String REMARK;               //备注
    public String QUESTION_KIND;        //试题类型0：单选 1：多选 2：可编辑题
    public List<OPTION_ANSWER> OPTION_ANSWER;

    public class OPTION_ANSWER {

        public String QUESTION_NO;      //
        public String ANSWER_NO;        //问题编号
        public String REMARK;           //备注
        public String ANSWER_CONTENT;   //题目分值
    }

}