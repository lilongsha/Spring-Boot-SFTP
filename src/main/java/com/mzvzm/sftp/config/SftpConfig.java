package com.mzvzm.sftp.config;

import com.jcraft.jsch.ChannelSftp;
import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
public class SftpConfig {
    private String protocol;
    private String host;
    private int port;
    private String username;
    private String password;
    private String root;
    private String localPath;
    private String fileBackupPaths;
    private String remotePaths;
    private Integer sessionConnectTimeout;
    private Integer channelConnectedTimout;
    private Pool pool = new Pool();
    @Value("${sftp.config.sftpClientProtocol}")
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Value("${sftp.config.sftpClientHost}")
    public void setHost(String host) {
        this.host = host;
    }

    @Value("${sftp.config.sftpClientPort}")
    public void setPort(int port) {
        this.port = port;
    }

    @Value("${sftp.config.sftpClientUsername}")
    public void setUsername(String username) {
        this.username = username;
    }

    @Value("${sftp.config.sftpClientPassword}")
    public void setPassword(String password) {
        this.password = password;
    }

    @Value("${sftp.config.sftpClientRoot}")
    public void setRoot(String root) {
        this.root = root;
    }

    @Value("${sftp.config.localPaths}")
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    @Value("${sftp.config.fileBackupPaths}")
    public void setFileBackupPaths(String fileBackupPaths) {
        this.fileBackupPaths = fileBackupPaths;
    }

    @Value("${sftp.config.remotePaths}")
    public void setRemotePaths(String remotePaths) {
        this.remotePaths = remotePaths;
    }

    @Value("${sftp.config.sftpClientSessionConnectionTimeout}")
    public void setSessionConnectTimeout(Integer sessionConnectTimeout) {
        this.sessionConnectTimeout = sessionConnectTimeout;
    }

    @Value("${sftp.config.sftpClientChannelConnectedTimout}")
    public void setChannelConnectedTimout(Integer channelConnectedTimout) {
        this.channelConnectedTimout = channelConnectedTimout;
    }

    public static class Pool extends GenericObjectPoolConfig<ChannelSftp> {
        public Pool() {
            super();
        }

        @Override
        public int getMaxTotal() {
            return super.getMaxTotal();
        }

        @Override
        public void setMaxTotal(int maxTotal) {
            super.setMaxTotal(maxTotal);
        }

        @Override
        public int getMaxIdle() {
            return super.getMaxIdle();
        }

        @Override
        public int getMinIdle() {
            return super.getMinIdle();
        }

        @Override
        public void setMaxIdle(int maxIdle) {
            super.setMaxIdle(maxIdle);
        }

        @Override
        public void setMinIdle(int minIdle) {
            super.setMinIdle(minIdle);
        }
    }
}
