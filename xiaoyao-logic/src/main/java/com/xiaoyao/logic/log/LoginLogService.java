package com.xiaoyao.logic.log;

import com.xiaoyao.logic.entity.LoginLogEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 登录日志服务
 * 用于记录玩家登录信息，支持留存分析和异常检测
 *
 * @Slf4j
 * @Service
 * @RequiredArgsConstructor
 *                          public class LoginLogService {
 * 
 *                          private final
 *                          com.xiaoyao.logic.repository.LoginLogRepository
 *                          loginLogRepository;
 * 
 *                          /**
 *                          记录玩家登录
 *
 * @param playerId     玩家ID
 * @param platformType 平台类型 (0-游客 1-微信 2-抖音 3-iOS 4-Android)
 * @param deviceId     设备ID
 * @param clientIp     客户端IP
 * @return 登录日志ID
 */
public Long logLogin(Long playerId,Integer platformType,String deviceId,String clientIp){return logLogin(playerId,0,platformType,deviceId,null,null,null,clientIp);}

/**
 * 记录玩家登录（完整参数）
 *
 * @param playerId     玩家ID
 * @param loginType    登录类型 (0-正常登录 1-断线重连 2-切换账号)
 * @param platformType 平台类型
 * @param deviceId     设备ID
 * @param deviceModel  设备型号
 * @param osVersion    系统版本
 * @param appVersion   App版本
 * @param clientIp     客户端IP
 * @return 登录日志ID
 */
public Long logLogin(Long playerId,Integer loginType,Integer platformType,String deviceId,String deviceModel,String osVersion,String appVersion,String clientIp){try{LoginLogEntity loginLogEntity=new LoginLogEntity();loginLogEntity.setPlayerId(playerId);loginLogEntity.setLoginType(loginType);loginLogEntity.setPlatformType(platformType);loginLogEntity.setDeviceId(deviceId!=null?deviceId:"");loginLogEntity.setDeviceModel(deviceModel!=null?deviceModel:"");loginLogEntity.setOsVersion(osVersion!=null?osVersion:"");loginLogEntity.setAppVersion(appVersion!=null?appVersion:"");loginLogEntity.setClientIp(clientIp!=null?clientIp:"");loginLogEntity.setLoginTime(System.currentTimeMillis());loginLogEntity.setLogoutTime(null);loginLogEntity.setOnlineDuration(0);loginLogEntity.setCreatedAt(LocalDateTime.now());

loginLogEntity=loginLogRepository.save(loginLogEntity);

log.info("[登录日志] playerId={} platform={} device={} ip={} logId={}",playerId,platformType,deviceId,clientIp,loginLogEntity.getId());

return loginLogEntity.getId();

}catch(Exception e){log.error("[登录日志] 记录失败 playerId={}",playerId,e);return null;}}

/**
 * 记录玩家登出
 *
 * @param logId 登录日志ID
 */
public void logLogout(Long logId){if(logId==null){return;}

try{
// 查询登录记录
LoginLogEntity loginLog=loginLogRepository.findById(logId).orElse(null);if(loginLog==null){log.warn("[登出日志] 登录记录不存在 logId={}",logId);return;}

// 计算在线时长
long logoutTime=System.currentTimeMillis();int onlineDuration=(int)((logoutTime-loginLog.getLoginTime())/1000);

// 更新登出时间和在线时长
int rows=loginLogRepository.updateLogout(logId,logoutTime,onlineDuration);

if(rows>0){log.info("[登出日志] playerId={} logId={} 在线时长={}秒",loginLog.getPlayerId(),logId,onlineDuration);}

}catch(Exception e){log.error("[登出日志] 更新失败 logId={}",logId,e);}}

/**
 * 通过玩家ID记录登出（自动查找最后一次登录记录）
 *
 * @param playerId 玩家ID
 */
public void logLogoutByPlayerId(Long playerId){try{
// 查找最后一次登录记录
LoginLogEntity lastLogin=loginLogRepository.findLastLogin(playerId);if(lastLogin==null||lastLogin.getLogoutTime()!=null){
// 没有登录记录或已经登出
return;}

// 记录登出
logLogout(lastLogin.getId());

}catch(Exception e){log.error("[登出日志] 查找最后登录失败 playerId={}",playerId,e);}}

/**
 * 获取玩家当前的登录日志ID
 *
 * @param playerId 玩家ID
 * @return 登录日志ID (未登录返回null)
 */
public Long getCurrentLoginLogId(Long playerId){try{LoginLogEntity lastLogin=loginLogRepository.findLastLogin(playerId);if(lastLogin!=null&&lastLogin.getLogoutTime()==null){return lastLogin.getId();}}catch(Exception e){log.error("[登录日志] 获取当前登录ID失败 playerId={}",playerId,e);}return null;}}
