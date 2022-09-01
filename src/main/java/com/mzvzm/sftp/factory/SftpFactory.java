package com.mzvzm.sftp.factory;

import com.jcraft.jsch.*;
import com.mzvzm.sftp.config.SftpConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Slf4j
@Component
public class SftpFactory extends BasePooledObjectFactory<ChannelSftp> {
    private final SftpConfig sftpConfig;

    public SftpConfig getSftpConfig() {
        return sftpConfig;
    }

    public SftpFactory(SftpConfig sftpConfig) {
        this.sftpConfig = sftpConfig;
    }

    @Override
    public ChannelSftp create() {
        try {
            JSch jSch = new JSch();
            Session sshSession = jSch.getSession(sftpConfig.getUsername(), sftpConfig.getHost(), sftpConfig.getPort());
            sshSession.setPassword(sftpConfig.getPassword());
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect(sftpConfig.getSessionConnectTimeout());
            ChannelSftp channel = (ChannelSftp) sshSession.openChannel(sftpConfig.getProtocol());
            channel.connect(sftpConfig.getChannelConnectedTimout());
            log.info("连接SFTP状态：" + channel.isConnected());
            log.info("连接SFTPHash：" + channel.hashCode());
            return channel;
        } catch (JSchException e) {
            log.info("连接sftp失败：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PooledObject<ChannelSftp> wrap(ChannelSftp channelSftp) {
        return new DefaultPooledObject<>(channelSftp);
    }

    @Override
    public void destroyObject(PooledObject<ChannelSftp> p) throws Exception {
        ChannelSftp channelSftp = p.getObject();
        log.info("连接SFTP销毁Hash：" + channelSftp.hashCode());
        channelSftp.disconnect();
    }


}
