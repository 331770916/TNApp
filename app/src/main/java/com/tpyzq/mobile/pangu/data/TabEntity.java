package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/9/27 13:52
 */
public class TabEntity {

    /**
     * code : 200
     * type : SUCCESS
     * message : [{"id":"57ea0416e4b0dc598b5d7622","dt":1474953979000,"title":"战略转型成效显著 平安银行斩获\u201c最佳产品创新银行\u201d大奖"},{"id":"57e9eed0e4b0dc598b5d66f8","dt":1474948281000,"title":"\u201c2016第十二届北京金博会\u201d领导寄语"},{"id":"57e9f4aee4b0dc598b5d6d23","dt":1474947960000,"title":"30余金融大咖建言昆明打造区域性国际金融中心"},{"id":"57e9ea27e4b0dc598b5d61cc","dt":1474947323000,"title":"ppp模式需各方参与"},{"id":"57e9e96ce4b0dc598b5d5f4f","dt":1474947015000,"title":"手机让发展中国家金融弯道超车：跳过现金和网点时代"},{"id":"57e9e5b7e4b0dc598b5d558e","dt":1474945825000,"title":"p2p背后的财富故事"},{"id":"57e9faa1e4b0dc598b5d71a4","dt":1474944540000,"title":"地王热高烧不退 警惕房企高位加杠杆风险"},{"id":"57e9e22ae4b0dc598b5d43f5","dt":1474943160000,"title":"上汽集团cfo谷峰或将离职：大象转身半年流失两位高管"},{"id":"57e9e1fde4b0dc598b5d42e0","dt":1474943160000,"title":"郭家耀: 短线或缺乏资金进场 大市或进一步下试23,000点支持"},{"id":"57e9e151e4b0dc598b5d3ed4","dt":1474942440000,"title":"人保系高管流失潮 待遇逊同业"}]
     */

    private String code;
    private String type;
    /**
     * id : 57ea0416e4b0dc598b5d7622
     * dt : 1474953979000
     * title : 战略转型成效显著 平安银行斩获“最佳产品创新银行”大奖
     */

    private List<MessageBean> message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MessageBean> getMessage() {
        return message;
    }

    public void setMessage(List<MessageBean> message) {
        this.message = message;
    }

    public static class MessageBean {
        private String id;
        private long dt;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
