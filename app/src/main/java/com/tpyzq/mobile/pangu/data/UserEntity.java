package com.tpyzq.mobile.pangu.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zhangwenbo on 2016/8/8.
 * 用户实体类
 */
public class UserEntity implements Parcelable {

    private String code;
    private String msg;
    private String session;
    private List<Data> data;

    private String userId;      //用户表主键


    private String Username;            //用户名
    private String Realname;            //真实姓名;
    private String Usertype;            //用户类型
    private String Token;               //登录交易获得的token值
    private String Isarrange;           //是否签署过电子签名约定书
    private String Isregister;           //是否注册  0是注册用户，1是未注册用户
    private String Scno;                //注册账号
    private String password;            //注册登录密码   //登录密码
    private String Typescno;            //账号类型（0:qq 1:微信 2:微博 3:手机）
    private String Mobile;              //手机号
    private String Mobile_TYPE;         //手机号类型:1中国移动、2中国电信、3中国联通
    private String Tradescno;           //交易账号（多个交易账号用逗号隔开存放在此字段）;
    private String Idcard;              //身份卡片
    private String Identityid;          //身份id
    private String Mount;               //是否在手机端安装并登录过?
    private String Regtime;             //注册时间（手机或WEB）
    private String Regchannel;          //注册来源（手机或WEB）
    private String Userstatus;          // 用户状态（默认为1001）：1001 最后登陆用户,1002 非最后登陆用户
    private String Islogin;             //是否已经登陆
    private String Realauth;            //是否认证，0未认证  1已认证 针对服务器端的导入用户
    private String Pickname;            //昵称，不要求唯一
    private String Pinyin;              //昵称的全拼（小写）
    private String Signature;           //个性签名 系统将该项信息保存为外部文本文件(Android 可以保存为XML)
    private String SEX;                 //性别    0男  1女
    private String Photo;               //用户头像原图地址
    private String Zoomphoto;           //头像压缩后的小图地址
    private String Bgurl;               //背景地址
    private String Interests;           //兴趣
    private String Birthdate;           //生日
    private String Hometown;            //家乡
    private String Religiousview;       //宗教
    private String Provinceid;          //用户所在省份ID
    private String Certification;       //双向认证
    private String Plugins;             //插件下载
    private String Legitimacy;          //合法性
    private String Keyboard;            //键盘
    private String RegisterID;            //注册标识ID
    private String refreshTime;         //刷新时间
    private String isInitUnregisterData;//0 未初始化 , 1 已初始化

    public String getIsInitUnregisterData() {
        return isInitUnregisterData;
    }

    public void setIsInitUnregisterData(String isInitUnregisterData) {
        this.isInitUnregisterData = isInitUnregisterData;
    }

    public String getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(msg);
        dest.writeString(session);
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>(){
        @Override
        public UserEntity createFromParcel(Parcel source) {
            UserEntity bean = new UserEntity();
            bean.code = source.readString();
            bean.msg  = source.readString() ;
            bean.session = source.readString();
            return bean;
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {
        private String SESSION;

        public void setSESSION(String SESSION) {
            this.SESSION = SESSION;
        }

        public String getSESSION() {
            return SESSION;
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTypescno() {
        return Typescno;
    }

    public void setTypescno(String typescno) {
        Typescno = typescno;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getMobile_TYPE() {
        return Mobile_TYPE;
    }

    public void setMobile_TYPE(String mobile_TYPE) {
        Mobile_TYPE = mobile_TYPE;
    }

    public String getTradescno() {
        return Tradescno;
    }

    public void setTradescno(String tradescno) {
        Tradescno = tradescno;
    }

    public String getIdcard() {
        return Idcard;
    }

    public void setIdcard(String idcard) {
        Idcard = idcard;
    }

    public String getIdentityid() {
        return Identityid;
    }

    public void setIdentityid(String identityid) {
        Identityid = identityid;
    }

    public String getMount() {
        return Mount;
    }

    public void setMount(String mount) {
        Mount = mount;
    }

    public String getRegtime() {
        return Regtime;
    }

    public void setRegtime(String regtime) {
        Regtime = regtime;
    }

    public String getRegchannel() {
        return Regchannel;
    }

    public void setRegchannel(String regchannel) {
        Regchannel = regchannel;
    }

    public String getUserstatus() {
        return Userstatus;
    }

    public void setUserstatus(String userstatus) {
        Userstatus = userstatus;
    }

    public String getIslogin() {
        return Islogin;
    }

    public void setIslogin(String islogin) {
        Islogin = islogin;
    }

    public String getRealauth() {
        return Realauth;
    }

    public void setRealauth(String realauth) {
        Realauth = realauth;
    }

    public String getPickname() {
        return Pickname;
    }

    public void setPickname(String pickname) {
        Pickname = pickname;
    }

    public String getPinyin() {
        return Pinyin;
    }

    public void setPinyin(String pinyin) {
        Pinyin = pinyin;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public String getSEX() {
        return SEX;
    }

    public void setSEX(String SEX) {
        this.SEX = SEX;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getZoomphoto() {
        return Zoomphoto;
    }

    public void setZoomphoto(String zoomphoto) {
        Zoomphoto = zoomphoto;
    }

    public String getBgurl() {
        return Bgurl;
    }

    public void setBgurl(String bgurl) {
        Bgurl = bgurl;
    }

    public String getInterests() {
        return Interests;
    }

    public void setInterests(String interests) {
        Interests = interests;
    }

    public String getBirthdate() {
        return Birthdate;
    }

    public void setBirthdate(String birthdate) {
        Birthdate = birthdate;
    }

    public String getHometown() {
        return Hometown;
    }

    public void setHometown(String hometown) {
        Hometown = hometown;
    }

    public String getReligiousview() {
        return Religiousview;
    }

    public void setReligiousview(String religiousview) {
        Religiousview = religiousview;
    }

    public String getProvinceid() {
        return Provinceid;
    }

    public void setProvinceid(String provinceid) {
        Provinceid = provinceid;
    }

    public static Creator<UserEntity> getCREATOR() {
        return CREATOR;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getRealname() {
        return Realname;
    }

    public void setRealname(String realname) {
        Realname = realname;
    }

    public String getUsertype() {
        return Usertype;
    }

    public void setUsertype(String usertype) {
        Usertype = usertype;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getIsarrange() {
        return Isarrange;
    }

    public void setIsarrange(String isarrange) {
        Isarrange = isarrange;
    }

    public String getIsregister() {
        return Isregister;
    }

    public void setIsregister(String isregister) {
        Isregister = isregister;
    }

    public String getScno() {
        return Scno;
    }

    public void setScno(String scno) {
        Scno = scno;
    }

    public String getCertification() {
        return Certification;
    }

    public void setCertification(String certification) {
        Certification = certification;
    }

    public String getPlugins() {
        return Plugins;
    }

    public void setPlugins(String plugins) {
        Plugins = plugins;
    }

    public String getLegitimacy() {
        return Legitimacy;
    }

    public void setLegitimacy(String legitimacy) {
        Legitimacy = legitimacy;
    }

    public String getKeyboard() {
        return Keyboard;
    }

    public void setKeyboard(String keyboard) {
        Keyboard = keyboard;
    }

    public String getRegisterID() {
        return RegisterID;
    }

    public void setRegisterID(String registerID) {
        RegisterID = registerID;
    }
}
