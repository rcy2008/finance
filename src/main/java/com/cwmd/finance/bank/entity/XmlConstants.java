package com.cwmd.finance.bank.entity;

public class XmlConstants {

	/**
	 * 生成XML节点及参数常量
	 */
	
	/**
	 * 根节点  message
	 */
	public static final String ROOT_ELEMENT = "message";
	
	/**
	 * 报文头节点  head
	 */
	public static final String HEAD_ELEMENT = "head";
	
	/**
	 * 报文体节点  body
	 */
	public static final String BODY_ELEMENT = "body";
	
	/**
	 * 节点  field
	 */
	public static final String FIELD_ELEMENT = "field";
	
	/**
	 * 节点 field-list
	 */
	public static final String FIELD_LIST_ELEMENT = "field-list";

	/**
	 * 节点属性  name
	 */
	public static final String ATTR_NAME = "name";
	
	/**
	 * 节点加密熟悉  pin 
	 */
	public static final String ATTR_PIN = "pin";
	
	/**
	 * XML缩进数量  2
	 */
	public static final Integer INTENT_SIZE = 2;
	
	
	/**
	 * 文件编码方式常量
	 */
	
	/**
	 * UTF-8编码方式
	 */
	public static final String UTF8_ENCODING = "UTF-8";
	
	/**
	 * gb2312编码方式
	 */
	public static final String GB2312_ENCODING = "gb2312";
	
	/**
	 * GBK编码方式
	 */
	public static final String GBK_ENCODING = "GBK";
	
	/**
	 * 文件名设置常量
	 */
	
	/**
	 * 文件名分隔符
	 */
	public static final String FILENAME_DELIMITER = "_";
	
	/**
	 * 返回对账文件前缀
	 */
	public static final String DZ_PREFIX = "DZ";
	
	/**
	 * 返还现货交易冻结解冻文件前缀
	 */
	public static final String DJJD_PREFIX = "DJJD";
	
	/**
	 * 返还会员签约文件前缀
	 */
	public static final String HYQY_PREFIX = "HYQY";
	
	/**
	 * 返还出入金明细文件前缀
	 */
	public static final String CRJMX_PREFIX = "CRJMX";
	
	/**
	 * 现货代扣非交易费用文件前缀
	 */
	public static final String DKFY_PREFIX = "DKFY";
	
	/**
	 * 现货代扣非交易费用上传文件后缀
	 */
	public static final String REQ_SUFFIX = ".REQ";
	
	/**
	 * 现货代扣非交易费用结果文件后缀
	 */
	public static final String ANS_SUFFIX = ".ANS";
	
	/**
	 * 算法常量
	 */
	
	/** 
     * DESede密钥算法 
     * */  
    public static final String ENCRYPT_DESEDE = "DESede";
    
    /** 
     * DES密钥算法 
     * */  
    public static final String ENCRYPT_DES = "DES";
    
    /**
     * RSA密钥算法 
     */
    public static final String ENCRYPT_RSA = "RSA";
    
    /**
     * MD5withRSA密钥算法 
     */
    public static final String ENCRYPT_MD5_RSA = "MD5withRSA";
    
    /**
     * HTTP传递参数常量
     */
    /**
     * 传递参数xml
     */
    public static final String REQ_PARAM_XML = "xml";
    
    /**
     * 传递参数signature
     */
    public static final String REQ_PARAM_SIGNATURE = "signature";
    
    /**
     * 请求URL
     */
    public static final String URL = "url";
    
    /**
     * 请求密钥类型 key_type
     */
    public static final String KEY_TYPE = "key_type";
    
    /**
     * 传递参数符号 "="
     */
    public static final String EQUAL_SIGN = "=";
    
    /**
     * 传递参数符号 "&"
     */
    public static final String AND_SIGN = "&";
    
    /**
     * 传递参数符号 "?"
     */
    public static final String QUESTION_SIGN = "?";
    
    /**
     * HTTP协议
     */
    public static final String HTTP_PROTOCOL = "http";
    
    /**
     * 客户编号 
     */
    public static final String MCH_NO = "MCH_NO";
    
    /**
     * 商户名称
     */
    public static final String MCH_NAME = "MCH_NAME";
    
    /**
     * 商户结算账户
     */
    public static final String SETTLEMENT_ACCOUNT = "SETTLEMENT_ACCOUNT";
    
    /**
     * 商户一般账户
     */
    public static final String COMMON_ACCOUNT = "COMMON_ACCOUNT";
    
    /**
     * 银行发送DES与RSA密钥URL
     */
    public static final String BANK_KEYS_URL = "BANK_KEYS_URL";
    
    /**
     * 银行交易URL
     */
    public static final String TRADE_URL = "TRADE_URL";

    /**
     * DESede密钥
     * 加密报文密钥
     */
    public static final String BANK_DES_EDE_KEYS = "BANK_DES_EDE_KEYS";
    
    /**
     * 银行RSA公钥
     * 用于验证报文签名
     */
    public static final String BANK_RSA_PUB_KEYS = "BANK_RSA_PUB_KEYS";
    
    /**
     * DESede密钥
     * 发送到银行进行加密
     */
    public static final String SELF_DES_EDE_KEYS = "SELF_DES_EDE_KEYS";

    /**
     * RSA公钥
     * 发送到银行
     */
    public static final String SELF_RSA_PUB_KEYS = "SELF_RSA_PUB_KEYS";

    /**
     * RSA密钥
     * 进行签名
     */
    public static final String SELF_RSA_PRI_KEYS = "SELF_RSA_PRI_KEYS";
    
    /**
     * 报文头常量
     */
    /**
     * 报文头version 100
     */
    public static final String HEAD_VERSION = "100";
    
    /**
     * 报文头 发送类型 0200
     */
    public static final String HEAD_SEND_TYPE = "0200";
    
    /**
     * 报文头 接收类型 0210
     */
    public static final String HEAD_RECEIVE_TYPE = "0210";
    
    /**
     * 报文头 渠道代号 30
     */
    public static final String HEAD_CHANNEL_NO = "30";
    
    /**
     * 报文头 渠道子编码 3001
     */
    public static final String HEAD_CHANNEL_SUB_NO = "3001";
    
    /**
     * 报文返回码 成功：000000000000  
     */
    public static final String HEAD_SUCCESS_CODE = "000000000000";
    
    /**
     * 报文返回成功信息   成功
     */
    public static final String HEAD_SUCCESS_MSG = "成功";
    
    /**
     * 报文发送方向类型 1-南储发送
     */
    public static final Integer SYSTEM_SEND = 1;
    
    /**
     * 报文发送方向类型 2-银行发送 
     */
    public static final Integer BANK_SEND = 2;
    
    /**
     * 银行访问链接超时时间 10000毫秒
     */
    public static final Integer BANK_CONNECTION_TIME_OUT = 10000;
    
    /**
     * 银行访问反馈超时时间 20000毫秒
     */
    public static final Integer BANK_READ_TIME_OUT = 20000;
    
    /**
     * 银行调用超时时间 60000毫秒
     */
    public static final Integer BANK_TRADE_TIMEOUT = 60000;
}
