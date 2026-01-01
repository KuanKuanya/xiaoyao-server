package com.xiaoyao.logic.config.mapper;

import com.xiaoyao.logic.config.proto.ConfigProto;
import com.xiaoyao.logic.config.dto.MapConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 配置响应映射器
 * <p>
 * 组装多种配置数据到协议对象
 * </p>
 *
 * @author xiaoyao
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ConfigProtoMapper {

    @Autowired
    protected MapConfigProtoMapper mapConfigProtoMapper;

    /**
     * 创建配置响应对象并设置地图配置
     *
     * @param mapVOs 地图配置 VO 列表
     * @return 配置协议对象
     */
    public ConfigProto createWithMaps(List<MapConfigVO> mapVOs) {
        ConfigProto proto = new ConfigProto();
        proto.setMaps(mapConfigProtoMapper.toProtoList(mapVOs));
        return proto;
    }

    // TODO: 后续添加其他配置类型的转换方法
}
