package com.quinlan.system.service.impl;



import com.quinlan.common.constant.CacheConstants;
import com.quinlan.common.core.redis.RedisCache;
import com.quinlan.common.core.text.Convert;
import com.quinlan.common.utils.StringUtils;
import com.quinlan.system.domain.SysConfig;
import com.quinlan.system.mapper.SysConfigMapper;
import com.quinlan.system.service.ISysConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class SysConfigServiceImpl implements ISysConfigService {

    @Resource
    private RedisCache redisCache;

    @Resource
    private SysConfigMapper configMapper;

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey){
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = configMapper.selectConfig(config);
        if (StringUtils.isNotNull(retConfig)) {
            redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaEnabled() {

        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled)){
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey){
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }

}
