package com.quinlan.system.mapper;

import com.quinlan.system.domain.SysConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 参数配置 数据层
 */
@Mapper
public interface SysConfigMapper {

    /**
     * 查询参数配置信息
     *
     * @param config 参数配置信息
     * @return 参数配置信息
     */
    public SysConfig selectConfig(SysConfig config);

}
