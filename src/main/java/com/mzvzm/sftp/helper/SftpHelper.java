package com.mzvzm.sftp.helper;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.mzvzm.sftp.config.SftpConfig;
import com.mzvzm.sftp.pool.SftpPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Vector;

@Slf4j
@Component
public class SftpHelper {
    private final SftpConfig sftpConfig;
    private final SftpPool pool;

    public SftpHelper(SftpPool pool, SftpConfig sftpConfig) {
        this.pool = pool;
        this.sftpConfig = sftpConfig;
    }

    public ArrayList<String> downSftpFile() {
        log.info("获取SFTP连接信息");
        ChannelSftp sftp = pool.borrowObject();
        log.info("登录成功，从连接池获取SFTP连接信息" + sftp);
        ArrayList<String> fileList = new ArrayList<String>();
        if (sftp == null) {
            log.info("从连接池获取SFTP连接信息为NULL");
        } else {
            try {
                sftp.cd(sftpConfig.getRemotePaths());
                log.info("SFTP登录进入目录：" + sftpConfig.getRemotePaths());
                Vector<ChannelSftp.LsEntry> list = sftp.ls("*.txt");
                if (list.size() > 0) {
                    for (ChannelSftp.LsEntry entity : list) {
                        String filename = entity.getFilename();
                        log.info("遍历SFTP文件---------" + filename);
                        String routeFileName = sftpConfig.getRemotePaths()+filename;
                        //下载
                        File localFile = new File(sftpConfig.getLocalPath()+filename);
                        OutputStream outputStream = Files.newOutputStream(localFile.toPath());
                        sftp.get(routeFileName, outputStream);
                        outputStream.close();
                        //备份下载
                        File localbackupFile = new File(sftpConfig.getFileBackupPaths()+filename);
                        OutputStream osputStream = Files.newOutputStream(localbackupFile.toPath());
                        sftp.get(routeFileName, osputStream);
                        osputStream.close();
                        log.info("下载文件："+filename);
                        fileList.add(sftpConfig.getLocalPath()+filename);
                        //删除文件
                        sftp.rm(filename);
                    }
                } else {
                    log.info("本次遍历文件为空");
                }
            } catch (SftpException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                pool.returnObject(sftp);
            }
        }

        return fileList;
    }
}
