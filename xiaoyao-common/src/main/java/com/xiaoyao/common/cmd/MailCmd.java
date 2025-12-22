package com.xiaoyao.common.cmd;

/**
 * 邮件模块命令
 *
 * @author xiaoyao
 */
public interface MailCmd {
    
    /** 主命令 */
    int cmd = CmdModule.MAIL;
    
    /** 获取邮件列表 */
    int getList = 1;
    
    /** 读取邮件 */
    int read = 2;
    
    /** 领取附件 */
    int claim = 3;
    
    /** 删除邮件 */
    int delete = 4;
    
    /** 一键领取 */
    int claimAll = 5;
    
    /** 一键删除已读 */
    int deleteRead = 6;
}
