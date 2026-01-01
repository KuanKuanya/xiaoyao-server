package com.xiaoyao.logic.config;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import com.xiaoyao.logic.config.dto.MapConfigVO;

/**
 * 游戏配置 Action
 * <p>
 * 提供配置数据查询接口
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@Component
@ActionController(ConfigCmd.cmd)
public class ConfigAction {

    @Resource
    private GameConfigService configService;

    /**
     * 获取所有境界配置
     */
    @ActionMethod(ConfigCmd.getRealms)
    public ConfigResp getRealms() {
        List<RealmConfigEntity> list = configService.getAllRealmConfigs();
        ConfigResp resp = new ConfigResp();
        resp.setRealms(list);
        return resp;
    }

    /**
     * 获取所有地图配置
     */
    @ActionMethod(ConfigCmd.getMaps)
    public ConfigResp getMaps() {
        List<MapConfigVO> list = configService.getAllMapConfigVOs();
        ConfigResp resp = new ConfigResp();
        resp.setMaps(list);
        return resp;
    }

    /**
     * 获取所有怪物配置
     */
    @ActionMethod(ConfigCmd.getMonsters)
    public ConfigResp getMonsters() {
        List<MonsterConfigEntity> list = configService.getAllMonsterConfigs();
        ConfigResp resp = new ConfigResp();
        resp.setMonsters(list);
        return resp;
    }

    /**
     * 获取所有物品配置
     */
    @ActionMethod(ConfigCmd.getItems)
    public ConfigResp getItems() {
        List<ItemConfigEntity> list = configService.getAllItemConfigs();
        ConfigResp resp = new ConfigResp();
        resp.setItems(list);
        return resp;
    }

    /**
     * 获取所有技能配置
     */
    @ActionMethod(ConfigCmd.getSkills)
    public ConfigResp getSkills() {
        List<SkillConfigEntity> list = configService.getAllSkillConfigs();
        ConfigResp resp = new ConfigResp();
        resp.setSkills(list);
        return resp;
    }

    /**
     * 获取所有配置 (一次性加载)
     */
    @ActionMethod(ConfigCmd.getAll)
    public ConfigResp getAll() {
        ConfigResp resp = new ConfigResp();
        resp.setRealms(configService.getAllRealmConfigs());
        resp.setMaps(configService.getAllMapConfigVOs());
        resp.setMonsters(configService.getAllMonsterConfigs());
        resp.setItems(configService.getAllItemConfigs());
        resp.setSkills(configService.getAllSkillConfigs());
        return resp;
    }
}
