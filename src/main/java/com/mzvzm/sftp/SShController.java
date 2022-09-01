package com.mzvzm.sftp;

import com.mzvzm.sftp.helper.SftpHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("ssh")
public class SShController {
    public final SftpHelper sftpHelper;

    public SShController(SftpHelper sftpHelper) {
        this.sftpHelper = sftpHelper;
    }

    @PostMapping("/get")
    public String get() {
        ArrayList<String> strings = sftpHelper.downSftpFile();
        return strings.toString();
    }
}
