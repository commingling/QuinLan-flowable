package com.quinlan.system.service;

public interface ISysConfigService {

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    public String selectConfigByKey(String configKey);

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    public boolean selectCaptchaEnabled();

}
