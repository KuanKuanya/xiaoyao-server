package com.xiaoyao.logic.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoyao.logic.constants.GameConstants;
import com.xiaoyao.logic.entity.MailTemplateEntity;
import com.xiaoyao.logic.exception.BusinessException;
import com.xiaoyao.logic.exception.ErrorCode;
import com.xiaoyao.logic.mapper.MailTemplateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件模板服务
 * 提供全服邮件模板的管理功能
 *
 * <p>扩展点说明：
 * 实际的邮件发送逻辑应由外部服务实现（如MailService），
 * 本服务只负责模板的管理和状态维护
 *
 * @author xiaoyao
 */
@Slf4j
@Service
public class MailTemplateService {

    @Resource
    private MailTemplateMapper mailTemplateMapper;

    /**
     * 创建邮件模板（GM使用）
     *
     * @param template 邮件模板实体
     * @param gmId 操作的GM ID
     * @return 模板ID
     * @throws BusinessException 参数校验失败
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createMailTemplate(MailTemplateEntity template, Long gmId) {
        // 参数校验
        validateTemplateForCreate(template);

        // 设置初始状态
        template.setStatus(GameConstants.MAIL_TEMPLATE_STATUS_PENDING);
        template.setSendCount(0);
        template.setCreatedBy(gmId);

        // 插入数据库
        mailTemplateMapper.insert(template);

        log.info("[邮件模板] 创建模板 id={} title={} targetType={} gmId={}",
                template.getId(), template.getTitle(), template.getTargetType(), gmId);

        return template.getId();
    }

    /**
     * 更新邮件模板（GM使用）
     * 注意：只有待发送状态的模板才能更新
     *
     * @param template 邮件模板实体
     * @param gmId 操作的GM ID
     * @throws BusinessException 模板不存在、状态不允许或参数校验失败
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMailTemplate(MailTemplateEntity template, Long gmId) {
        // 检查模板是否存在
        MailTemplateEntity existingTemplate = getTemplateById(template.getId());

        // 只有待发送状态才能更新
        if (existingTemplate.getStatus() != GameConstants.MAIL_TEMPLATE_STATUS_PENDING) {
            throw BusinessException.of(ErrorCode.STATE_INVALID,
                    "只有待发送状态的模板才能更新 status=%d", existingTemplate.getStatus());
        }

        // 参数校验
        validateTemplateForUpdate(template);

        // 设置更新人
        template.setUpdatedBy(gmId);

        // 更新数据库
        mailTemplateMapper.updateById(template);

        log.info("[邮件模板] 更新模板 id={} title={} gmId={}",
                template.getId(), template.getTitle(), gmId);
    }

    /**
     * 标记模板为已发送
     * 注意：实际的邮件发送逻辑由外部服务实现
     *
     * @param templateId 模板ID
     * @param sendCount 实际发送数量
     * @throws BusinessException 模板不存在或状态不允许
     */
    @Transactional(rollbackFor = Exception.class)
    public void markAsSent(Long templateId, Integer sendCount) {
        // 检查模板是否存在
        MailTemplateEntity template = getTemplateById(templateId);

        // 检查状态
        if (template.getStatus() == GameConstants.MAIL_TEMPLATE_STATUS_SENT) {
            throw BusinessException.of(ErrorCode.MAIL_TEMPLATE_ALREADY_SENT,
                    "模板已发送 id=%d", templateId);
        }

        if (template.getStatus() == GameConstants.MAIL_TEMPLATE_STATUS_REVOKED) {
            throw BusinessException.of(ErrorCode.MAIL_TEMPLATE_REVOKED,
                    "模板已撤回 id=%d", templateId);
        }

        // 更新状态
        template.setStatus(GameConstants.MAIL_TEMPLATE_STATUS_SENT);
        template.setSendTime(LocalDateTime.now());
        template.setSendCount(sendCount != null ? sendCount : 0);

        mailTemplateMapper.updateById(template);

        log.info("[邮件模板] 标记为已发送 id={} sendCount={}", templateId, sendCount);
    }

    /**
     * 撤回邮件模板
     * 注意：只有待发送状态的模板才能撤回
     *
     * @param templateId 模板ID
     * @param gmId 操作的GM ID
     * @throws BusinessException 模板不存在或状态不允许
     */
    @Transactional(rollbackFor = Exception.class)
    public void revokeMailTemplate(Long templateId, Long gmId) {
        // 检查模板是否存在
        MailTemplateEntity template = getTemplateById(templateId);

        // 只有待发送状态才能撤回
        if (template.getStatus() != GameConstants.MAIL_TEMPLATE_STATUS_PENDING) {
            throw BusinessException.of(ErrorCode.STATE_INVALID,
                    "只有待发送状态的模板才能撤回 status=%d", template.getStatus());
        }

        // 更新状态
        template.setStatus(GameConstants.MAIL_TEMPLATE_STATUS_REVOKED);
        template.setUpdatedBy(gmId);

        mailTemplateMapper.updateById(template);

        log.info("[邮件模板] 撤回模板 id={} gmId={}", templateId, gmId);
    }

    /**
     * 删除邮件模板（逻辑删除，GM使用）
     *
     * @param templateId 模板ID
     * @param gmId 操作的GM ID
     * @throws BusinessException 模板不存在
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteMailTemplate(Long templateId, Long gmId) {
        // 检查模板是否存在
        MailTemplateEntity template = getTemplateById(templateId);

        // 设置删除标记
        template.setIsDeleted(GameConstants.DELETED_YES);
        template.setDeletedBy(gmId);
        template.setDeletedAt(LocalDateTime.now());

        mailTemplateMapper.updateById(template);

        log.info("[邮件模板] 删除模板 id={} gmId={}", templateId, gmId);
    }

    /**
     * 根据ID查询模板
     *
     * @param templateId 模板ID
     * @return 模板实体
     * @throws BusinessException 模板不存在
     */
    public MailTemplateEntity getTemplateById(Long templateId) {
        MailTemplateEntity template = mailTemplateMapper.selectById(templateId);
        if (template == null || template.getIsDeleted() == GameConstants.DELETED_YES) {
            throw BusinessException.of(ErrorCode.MAIL_TEMPLATE_NOT_FOUND,
                    "邮件模板不存在: %d", templateId);
        }
        return template;
    }

    /**
     * 查询待发送的邮件模板
     * 供定时任务使用
     *
     * @return 待发送模板列表
     */
    public List<MailTemplateEntity> getPendingTemplates() {
        return mailTemplateMapper.selectPendingTemplates();
    }

    /**
     * 查询当前有效的邮件模板
     *
     * @return 有效模板列表
     */
    public List<MailTemplateEntity> getActiveTemplates() {
        LocalDateTime now = LocalDateTime.now();
        return mailTemplateMapper.selectActiveTemplates(now);
    }

    /**
     * 查询指定目标类型的模板
     *
     * @param targetType 目标类型
     * @return 模板列表
     */
    public List<MailTemplateEntity> getTemplatesByTargetType(Integer targetType) {
        validateTargetType(targetType);
        return mailTemplateMapper.selectByTargetType(targetType);
    }

    /**
     * 查询所有邮件模板（GM后台使用）
     *
     * @param includeDeleted 是否包含已删除的
     * @return 模板列表
     */
    public List<MailTemplateEntity> getAllTemplates(boolean includeDeleted) {
        LambdaQueryWrapper<MailTemplateEntity> wrapper = new LambdaQueryWrapper<>();

        if (!includeDeleted) {
            wrapper.eq(MailTemplateEntity::getIsDeleted, GameConstants.DELETED_NO);
        }

        wrapper.orderByDesc(MailTemplateEntity::getCreatedAt);

        return mailTemplateMapper.selectList(wrapper);
    }

    /**
     * 查询指定时间范围内的模板
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 模板列表
     */
    public List<MailTemplateEntity> getTemplatesByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "时间范围参数不能为空");
        }

        if (startTime.isAfter(endTime)) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "开始时间不能晚于结束时间");
        }

        return mailTemplateMapper.selectByTimeRange(startTime, endTime);
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 校验创建模板的参数
     */
    private void validateTemplateForCreate(MailTemplateEntity template) {
        if (template == null) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "邮件模板对象不能为空");
        }

        if (!StringUtils.hasText(template.getTitle())) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "邮件标题不能为空");
        }

        if (!StringUtils.hasText(template.getContent())) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "邮件内容不能为空");
        }

        validateTargetType(template.getTargetType());

        if (template.getStartTime() == null || template.getEndTime() == null) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "邮件生效时间和结束时间不能为空");
        }

        if (template.getStartTime().isAfter(template.getEndTime())) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "邮件生效时间不能晚于结束时间");
        }

        if (template.getValidDays() == null || template.getValidDays() <= 0) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "有效天数必须大于0");
        }

        // 校验附件格式
        validateAttachments(template.getAttachments());

        // 校验目标条件格式（如果是条件筛选类型）
        if (template.getTargetType() == GameConstants.MAIL_TARGET_CONDITION) {
            validateTargetCondition(template.getTargetCondition());
        }

        // 设置默认值
        if (!StringUtils.hasText(template.getSenderName())) {
            template.setSenderName("系统");
        }
    }

    /**
     * 校验更新模板的参数
     */
    private void validateTemplateForUpdate(MailTemplateEntity template) {
        validateTemplateForCreate(template);
    }

    /**
     * 校验目标类型
     */
    private void validateTargetType(Integer targetType) {
        if (targetType == null || targetType < 1 || targetType > 3) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "目标类型无效: %d", targetType);
        }
    }

    /**
     * 校验附件格式
     * 附件格式应为JSON数组: [{"itemId": "xxx", "count": 10}, ...]
     */
    private void validateAttachments(String attachments) {
        if (!StringUtils.hasText(attachments)) {
            return; // 允许没有附件
        }

        try {
            // 简单校验JSON格式（实际项目中应使用JSON库解析校验）
            if (!attachments.trim().startsWith("[") || !attachments.trim().endsWith("]")) {
                throw BusinessException.of(ErrorCode.MAIL_ATTACHMENT_INVALID,
                        "附件格式错误，应为JSON数组");
            }

            // TODO: 使用Jackson或Gson解析并校验具体字段

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("[邮件模板] 附件格式校验失败", e);
            throw BusinessException.of(ErrorCode.MAIL_ATTACHMENT_INVALID,
                    "附件格式错误: " + e.getMessage());
        }
    }

    /**
     * 校验目标条件格式
     * 条件格式应为JSON对象: {"minLevel": 10, "maxLevel": 50, ...}
     */
    private void validateTargetCondition(String targetCondition) {
        if (!StringUtils.hasText(targetCondition)) {
            throw BusinessException.of(ErrorCode.MAIL_TARGET_CONDITION_INVALID,
                    "条件筛选类型必须提供目标条件");
        }

        try {
            // 简单校验JSON格式（实际项目中应使用JSON库解析校验）
            if (!targetCondition.trim().startsWith("{") || !targetCondition.trim().endsWith("}")) {
                throw BusinessException.of(ErrorCode.MAIL_TARGET_CONDITION_INVALID,
                        "目标条件格式错误，应为JSON对象");
            }

            // TODO: 使用Jackson或Gson解析并校验具体字段

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("[邮件模板] 目标条件格式校验失败", e);
            throw BusinessException.of(ErrorCode.MAIL_TARGET_CONDITION_INVALID,
                    "目标条件格式错误: " + e.getMessage());
        }
    }
}
