package com.xiaoyao.logic.skill;

import com.xiaoyao.common.proto.SkillInfo;
import com.xiaoyao.common.proto.SkillListResp;
import com.xiaoyao.common.proto.SkillResp;
import com.xiaoyao.logic.config.GameConfigService;
import com.xiaoyao.logic.config.SkillConfigEntity;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 技能服务
 *
 * @author xiaoyao
 */
@Slf4j
@Service
public class SkillService {

    @Resource
    private GameConfigService configService;

    /** 玩家技能数据缓存 (暂用内存，后续对接数据库) */
    private final Map<Long, List<SkillInfo>> playerSkills = new ConcurrentHashMap<>();

    /** 玩家已装备主动技能 */
    private final Map<Long, List<Integer>> playerEquippedActive = new ConcurrentHashMap<>();

    /** 玩家已装备心法 */
    private final Map<Long, Integer> playerEquippedMental = new ConcurrentHashMap<>();

    /** 主动技能槽位数量 */
    private static final int MAX_ACTIVE_SLOTS = 4;

    /**
     * 获取技能列表
     */
    public SkillListResp getSkillList(long playerId) {
        SkillListResp resp = new SkillListResp();

        List<SkillInfo> skills = playerSkills.getOrDefault(playerId, new ArrayList<>());

        // 如果玩家没有技能，初始化默认技能
        if (skills.isEmpty()) {
            initDefaultSkills(playerId, skills);
        }

        resp.setSkills(skills);
        resp.setEquippedActive(playerEquippedActive.getOrDefault(playerId, new ArrayList<>()));
        resp.setEquippedMental(playerEquippedMental.get(playerId));

        return resp;
    }

    /**
     * 学习技能
     */
    public SkillResp learnSkill(long playerId, int skillId) {
        SkillResp resp = new SkillResp();

        // 验证技能是否存在
        SkillConfigEntity skillConfig = configService.getSkillConfig(skillId);
        if (skillConfig == null) {
            resp.setSuccess(false);
            resp.setMessage("技能不存在配置");
            return resp;
        }

        List<SkillInfo> skills = playerSkills.computeIfAbsent(playerId, k -> new ArrayList<>());

        // 检查是否已学习
        for (SkillInfo skill : skills) {
            if (skill.getSkillId() == skillId) {
                resp.setSuccess(false);
                resp.setMessage("技能已学习");
                return resp;
            }
        }

        // TODO: 检查学习条件 (如境界、消耗)

        // 学习新技能
        SkillInfo newSkill = new SkillInfo();
        newSkill.setSkillId(skillId);
        newSkill.setLevel(1);
        newSkill.setEquipped(false);
        newSkill.setExp(0);
        newSkill.setUpgradeExp(100); // 初始升级经验
        skills.add(newSkill);

        resp.setSuccess(true);
        resp.setSkillId(skillId);
        resp.setNewLevel(1);
        resp.setMessage("学习成功");

        return resp;
    }

    /**
     * 升级技能
     */
    public SkillResp upgradeSkill(long playerId, int skillId) {
        SkillResp resp = new SkillResp();

        List<SkillInfo> skills = playerSkills.get(playerId);
        if (skills == null) {
            resp.setSuccess(false);
            resp.setMessage("技能不存在");
            return resp;
        }

        SkillInfo target = null;
        for (SkillInfo skill : skills) {
            if (skill.getSkillId() == skillId) {
                target = skill;
                break;
            }
        }

        if (target == null) {
            resp.setSuccess(false);
            resp.setMessage("技能不存在");
            return resp;
        }

        SkillConfigEntity config = configService.getSkillConfig(skillId);
        if (config == null)
            return resp; // Should not happen

        if (config.getMaxLevel() != null && target.getLevel() >= config.getMaxLevel()) {
            resp.setSuccess(false);
            resp.setMessage("技能已满级");
            return resp;
        }

        // TODO: 检查升级材料

        target.setLevel(target.getLevel() + 1);
        target.setUpgradeExp((int) (target.getUpgradeExp() * 1.5));

        resp.setSuccess(true);
        resp.setSkillId(skillId);
        resp.setNewLevel(target.getLevel());
        resp.setMessage("升级成功");

        return resp;
    }

    /**
     * 装备技能
     */
    public SkillResp equipSkill(long playerId, int skillId) {
        SkillResp resp = new SkillResp();

        List<SkillInfo> skills = playerSkills.get(playerId);
        if (skills == null) {
            // Try load default
            getSkillList(playerId);
            skills = playerSkills.get(playerId);
        }

        if (skills == null) {
            resp.setSuccess(false);
            resp.setMessage("技能不存在");
            return resp;
        }

        SkillInfo target = null;
        for (SkillInfo skill : skills) {
            if (skill.getSkillId() == skillId) {
                target = skill;
                break;
            }
        }

        if (target == null) {
            resp.setSuccess(false);
            resp.setMessage("技能未学习");
            return resp;
        }

        SkillConfigEntity config = configService.getSkillConfig(skillId);
        if (config == null) {
            resp.setSuccess(false);
            resp.setMessage("技能配置错误");
            return resp;
        }

        // 装备逻辑区分类型
        if (config.getSkillType() == 1) { // 主动
            List<Integer> equipped = playerEquippedActive.computeIfAbsent(playerId, k -> new ArrayList<>());
            if (equipped.size() >= MAX_ACTIVE_SLOTS) {
                resp.setSuccess(false);
                resp.setMessage("技能槽已满");
                return resp;
            }

            if (!equipped.contains(skillId)) {
                equipped.add(skillId);
                target.setEquipped(true);
            }
        } else if (config.getSkillType() == 3) { // 心法
            playerEquippedMental.put(playerId, skillId);
            target.setEquipped(true);
            // TODO: 卸载旧心法状态
        }

        resp.setSuccess(true);
        resp.setSkillId(skillId);
        resp.setMessage("装备成功");

        return resp;
    }

    /**
     * 卸下技能
     */
    public SkillResp unequipSkill(long playerId, int skillId) {
        SkillResp resp = new SkillResp();

        SkillConfigEntity config = configService.getSkillConfig(skillId);
        if (config == null)
            return resp;

        if (config.getSkillType() == 1) {
            List<Integer> equipped = playerEquippedActive.get(playerId);
            if (equipped != null) {
                equipped.remove(Integer.valueOf(skillId));
            }
        } else if (config.getSkillType() == 3) {
            playerEquippedMental.remove(playerId, skillId);
        }

        List<SkillInfo> skills = playerSkills.get(playerId);
        if (skills != null) {
            for (SkillInfo skill : skills) {
                if (skill.getSkillId() == skillId) {
                    skill.setEquipped(false);
                    break;
                }
            }
        }

        resp.setSuccess(true);
        resp.setSkillId(skillId);
        resp.setMessage("卸下成功");

        return resp;
    }

    private void initDefaultSkills(long playerId, List<SkillInfo> skills) {
        // 初始给几个基础技能 (ID 101, 301)
        int[] defaultSkillIds = { 101, 301 };

        for (int id : defaultSkillIds) {
            SkillConfigEntity cfg = configService.getSkillConfig(id);
            if (cfg != null) {
                SkillInfo s = new SkillInfo();
                s.setSkillId(id);
                s.setLevel(1);
                s.setEquipped(false);
                s.setExp(0);
                s.setUpgradeExp(100);
                skills.add(s);
            }
        }

        if (!skills.isEmpty()) {
            playerSkills.put(playerId, skills);
        }
    }
}
