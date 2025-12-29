package com.xiaoyao.logic.skill;

import com.xiaoyao.common.proto.SkillInfo;
import com.xiaoyao.common.proto.SkillListResp;
import com.xiaoyao.common.proto.SkillResp;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 技能服务
 * <p>
 * TODO: 接入数据库后替换内存存储
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
public class SkillService {

    /** 玩家技能数据缓存 */
    private static final Map<Long, List<SkillInfo>> PLAYER_SKILLS = new ConcurrentHashMap<>();

    /** 玩家已装备主动技能 */
    private static final Map<Long, List<Integer>> PLAYER_EQUIPPED_ACTIVE = new ConcurrentHashMap<>();

    /** 玩家已装备心法 */
    private static final Map<Long, Integer> PLAYER_EQUIPPED_MENTAL = new ConcurrentHashMap<>();

    /** 主动技能槽位数量 */
    private static final int MAX_ACTIVE_SLOTS = 4;

    /**
     * 获取技能列表
     */
    public SkillListResp getSkillList(long playerId) {
        SkillListResp resp = new SkillListResp();

        List<SkillInfo> skills = PLAYER_SKILLS.getOrDefault(playerId, new ArrayList<>());
        resp.setSkills(skills);
        resp.setEquippedActive(PLAYER_EQUIPPED_ACTIVE.getOrDefault(playerId, new ArrayList<>()));
        resp.setEquippedMental(PLAYER_EQUIPPED_MENTAL.get(playerId));

        return resp;
    }

    /**
     * 学习技能
     */
    public SkillResp learnSkill(long playerId, int skillId) {
        SkillResp resp = new SkillResp();

        List<SkillInfo> skills = PLAYER_SKILLS.computeIfAbsent(playerId, k -> new ArrayList<>());

        // 检查是否已学习
        for (SkillInfo skill : skills) {
            if (skill.getSkillId() == skillId) {
                resp.setSuccess(false);
                resp.setMessage("技能已学习");
                return resp;
            }
        }

        // 学习新技能
        SkillInfo newSkill = new SkillInfo();
        newSkill.setSkillId(skillId);
        newSkill.setLevel(1);
        newSkill.setEquipped(false);
        newSkill.setExp(0);
        newSkill.setUpgradeExp(100);
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

        List<SkillInfo> skills = PLAYER_SKILLS.get(playerId);
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

        List<SkillInfo> skills = PLAYER_SKILLS.get(playerId);
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

        // 装备主动技能
        List<Integer> equipped = PLAYER_EQUIPPED_ACTIVE.computeIfAbsent(playerId, k -> new ArrayList<>());
        if (equipped.size() >= MAX_ACTIVE_SLOTS) {
            resp.setSuccess(false);
            resp.setMessage("技能槽已满");
            return resp;
        }

        if (!equipped.contains(skillId)) {
            equipped.add(skillId);
            target.setEquipped(true);
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

        List<Integer> equipped = PLAYER_EQUIPPED_ACTIVE.get(playerId);
        if (equipped != null) {
            equipped.remove(Integer.valueOf(skillId));
        }

        List<SkillInfo> skills = PLAYER_SKILLS.get(playerId);
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
}
