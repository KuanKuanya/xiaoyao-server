package com.xiaoyao.logic.notice;

import com.mybatisflex.core.query.QueryWrapper;
import com.xiaoyao.logic.common.constants.GameConstants;
import com.xiaoyao.logic.common.exception.BusinessException;
import com.xiaoyao.logic.common.exception.ErrorCode;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 公告服务
 * 提供公告的增删改查和筛选功能
 *
 * @author xiaoyao
 */
@Slf4j
@Service
public class NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 获取玩家当前应该看到的公告列表
     *
     * @param platform 平台类型（wechat/douyin/ios/android）
     * @param appVersion 客户端版本号
     * @return 公告列表（按优先级降序）
     */
    public List<NoticeEntity> getPlayerNotices(String platform, String appVersion) {
        validatePlatform(platform);

        LocalDateTime now = LocalDateTime.now();

        // 查询指定平台的有效公告
        List<NoticeEntity> notices = noticeMapper.selectActiveNoticesByPlatform(now, platform);

        // 按版本号筛选
        if (StringUtils.hasText(appVersion)) {
            notices = notices.stream()
                    .filter(notice -> isVersionMatch(appVersion, notice.getMinVersion(), notice.getMaxVersion()))
                    .collect(Collectors.toList());
        }

        log.debug("[公告服务] 查询玩家公告 platform={} version={} count={}", platform, appVersion, notices.size());

        return notices;
    }

    /**
     * 获取登录时应该弹窗的公告
     *
     * @param platform 平台类型
     * @param appVersion 客户端版本号
     * @return 弹窗公告列表
     */
    public List<NoticeEntity> getLoginPopupNotices(String platform, String appVersion) {
        validatePlatform(platform);

        LocalDateTime now = LocalDateTime.now();

        // 查询弹窗公告
        List<NoticeEntity> popupNotices = noticeMapper.selectPopupNotices(now);

        // 按平台和版本号筛选
        popupNotices = popupNotices.stream()
                .filter(notice -> isPlatformMatch(platform, notice.getPlatforms()))
                .filter(notice -> isVersionMatch(appVersion, notice.getMinVersion(), notice.getMaxVersion()))
                .collect(Collectors.toList());

        log.info("[公告服务] 查询弹窗公告 platform={} count={}", platform, popupNotices.size());

        return popupNotices;
    }

    /**
     * 获取强制阅读的公告
     *
     * @param platform 平台类型
     * @param appVersion 客户端版本号
     * @return 强制阅读公告列表
     */
    public List<NoticeEntity> getForceReadNotices(String platform, String appVersion) {
        validatePlatform(platform);

        LocalDateTime now = LocalDateTime.now();

        // 查询强制阅读公告
        List<NoticeEntity> forceReadNotices = noticeMapper.selectForceReadNotices(now);

        // 按平台和版本号筛选
        forceReadNotices = forceReadNotices.stream()
                .filter(notice -> isPlatformMatch(platform, notice.getPlatforms()))
                .filter(notice -> isVersionMatch(appVersion, notice.getMinVersion(), notice.getMaxVersion()))
                .collect(Collectors.toList());

        log.info("[公告服务] 查询强制阅读公告 platform={} count={}", platform, forceReadNotices.size());

        return forceReadNotices;
    }

    /**
     * 根据ID查询公告
     *
     * @param noticeId 公告ID
     * @return 公告实体
     * @throws BusinessException 公告不存在
     */
    public NoticeEntity getNoticeById(Long noticeId) {
        NoticeEntity notice = noticeMapper.selectOneById(noticeId);
        if (notice == null || notice.getIsDeleted() == GameConstants.DELETED_YES) {
            throw BusinessException.of(ErrorCode.NOTICE_NOT_FOUND, "公告不存在: %d", noticeId);
        }
        return notice;
    }

    /**
     * 查询指定类型的公告
     *
     * @param noticeType 公告类型
     * @return 公告列表
     */
    public List<NoticeEntity> getNoticesByType(Integer noticeType) {
        validateNoticeType(noticeType);
        return noticeMapper.selectByType(noticeType);
    }

    /**
     * 创建公告（GM使用）
     *
     * @param notice 公告实体
     * @param gmId 操作的GM ID
     * @return 公告ID
     * @throws BusinessException 参数校验失败
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createNotice(NoticeEntity notice, Long gmId) {
        // 参数校验
        validateNoticeForCreate(notice);

        // 设置创建人
        notice.setCreatedBy(gmId);

        // 插入数据库
        noticeMapper.insert(notice);

        log.info("[公告服务] 创建公告 id={} type={} title={} gmId={}",
                notice.getId(), notice.getNoticeType(), notice.getTitle(), gmId);

        return notice.getId();
    }

    /**
     * 更新公告（GM使用）
     *
     * @param notice 公告实体
     * @param gmId 操作的GM ID
     * @throws BusinessException 公告不存在或参数校验失败
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateNotice(NoticeEntity notice, Long gmId) {
        // 检查公告是否存在
        NoticeEntity existingNotice = getNoticeById(notice.getId());

        // 参数校验
        validateNoticeForUpdate(notice);

        // 设置更新人
        notice.setUpdatedBy(gmId);

        // 更新数据库
        noticeMapper.update(notice);

        log.info("[公告服务] 更新公告 id={} title={} gmId={}",
                notice.getId(), notice.getTitle(), gmId);
    }

    /**
     * 删除公告（逻辑删除，GM使用）
     *
     * @param noticeId 公告ID
     * @param gmId 操作的GM ID
     * @throws BusinessException 公告不存在
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteNotice(Long noticeId, Long gmId) {
        // 检查公告是否存在
        NoticeEntity notice = getNoticeById(noticeId);

        // 设置删除标记
        notice.setIsDeleted(GameConstants.DELETED_YES);
        notice.setDeletedBy(gmId);
        notice.setDeletedAt(LocalDateTime.now());

        // 更新数据库
        noticeMapper.update(notice);

        log.info("[公告服务] 删除公告 id={} gmId={}", noticeId, gmId);
    }

    /**
     * 查询所有公告（GM后台使用）
     *
     * @param includeDeleted 是否包含已删除的
     * @return 公告列表
     */
    public List<NoticeEntity> getAllNotices(boolean includeDeleted) {
        QueryWrapper query = QueryWrapper.create();

        if (!includeDeleted) {
            query.where("is_deleted = ?", GameConstants.DELETED_NO);
        }

        query.orderBy("priority DESC, created_at DESC");

        return noticeMapper.selectListByQuery(query);
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 校验平台参数
     */
    private void validatePlatform(String platform) {
        if (!StringUtils.hasText(platform)) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "平台参数不能为空");
        }
    }

    /**
     * 校验公告类型
     */
    private void validateNoticeType(Integer noticeType) {
        if (noticeType == null || noticeType < 1 || noticeType > 4) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "公告类型无效: %d", noticeType);
        }
    }

    /**
     * 校验创建公告的参数
     */
    private void validateNoticeForCreate(NoticeEntity notice) {
        if (notice == null) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "公告对象不能为空");
        }

        if (!StringUtils.hasText(notice.getTitle())) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "公告标题不能为空");
        }

        if (!StringUtils.hasText(notice.getContent())) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "公告内容不能为空");
        }

        validateNoticeType(notice.getNoticeType());

        if (notice.getStartTime() == null || notice.getEndTime() == null) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "公告生效时间和结束时间不能为空");
        }

        if (notice.getStartTime().isAfter(notice.getEndTime())) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "公告生效时间不能晚于结束时间");
        }

        if (!StringUtils.hasText(notice.getPlatforms())) {
            notice.setPlatforms(GameConstants.PLATFORM_ALL);
        }

        // 设置默认值
        if (notice.getPriority() == null) {
            notice.setPriority(0);
        }
        if (notice.getIsPopup() == null) {
            notice.setIsPopup(GameConstants.NO);
        }
        if (notice.getIsForceRead() == null) {
            notice.setIsForceRead(GameConstants.NO);
        }
    }

    /**
     * 校验更新公告的参数
     */
    private void validateNoticeForUpdate(NoticeEntity notice) {
        validateNoticeForCreate(notice);
    }

    /**
     * 判断平台是否匹配
     *
     * @param playerPlatform 玩家平台
     * @param noticePlatforms 公告目标平台
     * @return 是否匹配
     */
    private boolean isPlatformMatch(String playerPlatform, String noticePlatforms) {
        if (!StringUtils.hasText(noticePlatforms)) {
            return true;
        }

        // "all" 表示所有平台
        if (GameConstants.PLATFORM_ALL.equals(noticePlatforms)) {
            return true;
        }

        // 判断是否包含玩家的平台
        return noticePlatforms.contains(playerPlatform);
    }

    /**
     * 判断版本号是否匹配
     *
     * @param playerVersion 玩家版本号
     * @param minVersion 最小版本号
     * @param maxVersion 最大版本号
     * @return 是否匹配
     */
    private boolean isVersionMatch(String playerVersion, String minVersion, String maxVersion) {
        if (!StringUtils.hasText(playerVersion)) {
            return true;
        }

        // 如果没有限制版本号，则匹配
        if (!StringUtils.hasText(minVersion) && !StringUtils.hasText(maxVersion)) {
            return true;
        }

        try {
            // 简单的版本号比较（假设版本号格式为 x.y.z）
            if (StringUtils.hasText(minVersion) && compareVersion(playerVersion, minVersion) < 0) {
                return false;
            }

            if (StringUtils.hasText(maxVersion) && compareVersion(playerVersion, maxVersion) > 0) {
                return false;
            }

            return true;

        } catch (Exception e) {
            log.warn("[公告服务] 版本号比较失败 player={} min={} max={}",
                    playerVersion, minVersion, maxVersion, e);
            return true; // 比较失败则通过
        }
    }

    /**
     * 比较版本号
     *
     * @param version1 版本号1
     * @param version2 版本号2
     * @return 比较结果（-1: version1 < version2, 0: 相等, 1: version1 > version2）
     */
    private int compareVersion(String version1, String version2) {
        String[] v1Parts = version1.split("\\.");
        String[] v2Parts = version2.split("\\.");

        int maxLength = Math.max(v1Parts.length, v2Parts.length);

        for (int i = 0; i < maxLength; i++) {
            int v1 = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
            int v2 = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;

            if (v1 < v2) {
                return -1;
            } else if (v1 > v2) {
                return 1;
            }
        }

        return 0;
    }
}
