package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 风险测评试题
 */
public class RiskTableEntity {

    public String PAPER_TYPE;
    public String QUESTION_TYPE;
    public String QUESTION_NO;
    public String ORGAN_FLAG;
    public String ORDER_NO;
    public String QUESTION_CONTENT;
    public String QUESTION_SCORE;
    public String REMARK;
    public String QUESTION_KIND;
    public List<OPTION_ANSWER> OPTION_ANSWER;

    public class OPTION_ANSWER {

        public String QUESTION_NO;
        public String ANSWER_NO;
        public String REMARK;
        public String ANSWER_CONTENT;
    }

}