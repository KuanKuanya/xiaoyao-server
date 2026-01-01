package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * 技能操作请求
 */
@Data
@ProtobufClass
public class SkillReq {
    /** 技能ID */
    private int skillId;
}
