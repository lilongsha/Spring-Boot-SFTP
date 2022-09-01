package com.mzvzm.sftp.pool;

import com.jcraft.jsch.ChannelSftp;
import com.mzvzm.sftp.factory.SftpFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SftpPool {
    private final GenericObjectPool<ChannelSftp> pool;

    public SftpPool(SftpFactory factory) {
        this.pool = new GenericObjectPool<>(factory, factory.getSftpConfig().getPool());
        // 在从对象池获取对象时是否检测对象有效(true : 是) , 配置true会降低性能；
        this.pool.setTestOnBorrow(true);
        // 在向对象池中归还对象时是否检测对象有效(true : 是) , 配置true会降低性能
        this.pool.setTestOnReturn(true);
        // 连接是否被空闲连接回收器(如果有)进行检验.如果检测失败,则连接将被从池中去除
        this.pool.setTestWhileIdle(true);
    }

    public ChannelSftp borrowObject() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            log.info("获取sftp连接池连接失败："+e.getMessage());
            return null;
        }
    }

    public void returnObject(ChannelSftp channelSftp) {
        if (channelSftp != null) {
            pool.returnObject(channelSftp);
        }
        log.info("回收连接池连接" + channelSftp);
    }
}
