package com.xiaoyao.logic.config.mapper;

import com.xiaoyao.logic.config.dto.MapConfigVO;
import com.xiaoyao.logic.config.proto.MapConfigProto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * 地图配置映射器
 * <p>
 * 使用 MapStruct 自动生成 Entity/VO 到 Proto 的转换代码
 * 编译时生成实现类，类型安全且高性能
 * </p>
 *
 * @author xiaoyao
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapConfigProtoMapper {

    /**
     * 将 MapConfigVO 转换为 MapConfigProto
     *
     * @param vo 数据库实体的 VO 对象
     * @return 网络协议对象
     */
    @Mapping(source = "id", target = "mapId")
    @Mapping(source = "isImmortal", target = "immortal")
    MapConfigProto toProto(MapConfigVO vo);

    /**
     * 批量转换
     *
     * @param vos VO 列表
     * @return Proto 列表
     */
    List<MapConfigProto> toProtoList(List<MapConfigVO> vos);
}
