package com.xiaoyao.logic.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 公告实体
 * 对应数据库表: t_notice
 *
 * @author xiaoyao
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("t_notice")
public class NoticeEntity extends BaseEntity {

    /**
     * 公告ID
     */
    @Id
    private Long id;

    /**
     * 类型: 1-系统公告 2-活动公告 3-更新公告 4-紧急公告
     */
    private Integer noticeType;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容 (支持富文本)
     */
    private String content;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 横幅图片URL
     */
    private String bannerUrl;

    /**
     * 优先级 (越大越靠前)
     */
    private Integer priority;

    /**
     * 是否弹窗: 0-否 1-是
     */
    private Integer isPopup;

    /**
     * 是否强制阅读: 0-否 1-是
     */
    private Integer isForceRead;

    /**
     * 生效时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 目标平台: all/wechat/douyin/ios/android
     */
    private String platforms;

    /**
     * 最小版本号
     */
    private String minVersion;

    /**
     * 最大版本号
     */
    private String maxVersion;

    // 公共字段 (version, is_deleted, created_at, updated_at等) 已由 BaseEntity 提供
}
