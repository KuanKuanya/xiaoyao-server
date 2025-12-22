package com.xiaoyao.common.proto;

import lombok.Data;
import java.util.List;

/**
 * 技能列表响应
 */
@Data
public class SkillListResp {
    /** 技能列表 */
    private List<SkillInfo> skills;
    /** 已装备的主动技能 */
    private List<Integer> equippedActive;
    /** 已装备的心法 */
    private Integer equippedMental;
}
