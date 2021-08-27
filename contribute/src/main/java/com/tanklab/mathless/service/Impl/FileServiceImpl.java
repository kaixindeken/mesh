package com.tanklab.mathless.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.tanklab.mathless.controller.user.UserControllerApi;
import com.tanklab.mathless.pojo.FileTreeNode;
import com.tanklab.mathless.pojo.Message;
import com.tanklab.mathless.pojo.UserInfo;
import com.tanklab.mathless.pojo.bo.FileBO;
import com.tanklab.mathless.service.FileService;
import com.tanklab.mathless.utils.CmdExecutor;
import com.tanklab.mathless.utils.FileOperatorUtil;
import com.tanklab.mathless.utils.RedisKeyUtil;
import com.tanklab.mathless.utils.RedisOperatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private RedisOperatorUtil redisOperatorUtil;

//    @Resource
//    private HostHolder hostHolder;

    @Resource
    private UserControllerApi userControllerApi;

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    static {
        // 只适用于Linux运行环境
        boolean enbale = false; // 默认情况下不开启
        if(enbale){
            try{
                String rootPassword = ""; // nfs服务器的root密码
                String Device = ""; // 位于nfs服务器下，要挂载的路径
                String Dir= "userspace"; // 挂载点 ， 默认挂载在项目userspaces下
                String mountCmd = String.format("mount -t nfs %s %s",Device,Dir);
                String out = CmdExecutor.builder(rootPassword).errRedirect(false).sudoCmd(mountCmd).exec();
                System.out.println(out);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Message create(String relativePath, boolean isFile) throws IOException, InterruptedException {
//        String userDir = hostHolder.getUser().getUserDir();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String userDir = (String) userData.get("userDir");
        String filePath = userDir + File.separator + relativePath;
        return FileOperatorUtil.create(filePath, isFile);
    }

    @Override
    public List<FileTreeNode> getFileTree(String relativePath){
//        String userDir = hostHolder.getUser().getUserDir();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String userDir = (String) userData.get("userDir");
        String fileAbsolutePath = userDir + File.separator + relativePath;
        return FileOperatorUtil.getFileTree(fileAbsolutePath);
    }

    @Override
    public Message delete(String relativePath) {
//        String userDir = hostHolder.getUser().getUserDir();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String userDir = (String) userData.get("userDir");
        String filePath = userDir + File.separator + relativePath;
        return FileOperatorUtil.delete(filePath);
    }

    @Override
    public Message upload(MultipartFile file) {
//        String userDir = hostHolder.getUser().getUserDir();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String userDir = (String) userData.get("userDir");
        return FileOperatorUtil.upload(userDir, file);

    }

    @Override
    public void download(String relativePath, HttpServletResponse response) {

//        String userDir = hostHolder.getUser().getUserDir();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String userDir = (String) userData.get("userDir");
        String fileAbsolutePath = userDir + File.separator + relativePath;

        File file = new File(fileAbsolutePath);
        if (!file.exists()) {
            logger.error("文件不存在");
            return;
        }

        // 如果是文件夹，先压缩
        if (file.isDirectory()) {
            FileOperatorUtil.compress(fileAbsolutePath, file.getName() + ".zip");
            file = new File(fileAbsolutePath + ".zip");
        }

        byte[] buffer = new byte[1024];
        int len = 0;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setHeader("Content-type", "application/octet-stream");
            //response.setContentType("application/x-msdownload");

            // 设置响应头，控制浏览器下载该文件，目前中文会乱码
            response.setHeader("content-disposition", "attachment;filename="
                    + URLEncoder.encode(file.getName(), "GBK"));

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ServletOutputStream sos = response.getOutputStream();
            while ((len = bis.read(buffer)) != -1) sos.write(buffer, 0, len);

            sos.flush();
            sos.close();

        } catch (IOException e) {
            return;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    return;
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    return;
                }
            }
        }
        logger.info("下载成功");
        return;
    }

    @Override
    public Message saveFile(FileBO fileBO) {
        // redis 缓存，随后定时送回db
//        UserInfo userInfo = hostHolder.getUser();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String userDir = (String) userData.get("userDir");
        String key = RedisKeyUtil.getFileKey((String) userData.get("id"), fileBO.getRelativePath());
        String fileAbsolutePath = userDir + File.separator + fileBO.getRelativePath();
        redisOperatorUtil.set(key, fileBO.getContent());
        FileOperatorUtil.saveAsFileWriter(fileAbsolutePath, fileBO.getContent());
        return new Message(true, "操作成功");
    }

    @Override
    public Message getFileContent(String relativePath) {
//        UserInfo userInfo = hostHolder.getUser();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String userDir = (String) userData.get("userDir");
        String key = RedisKeyUtil.getFileKey((String) userData.get("id"), relativePath);
        String fileAbsolutePath = userDir + File.separator + relativePath;
        File file = new File(fileAbsolutePath);

        // 先判断redis中有没有对应的key
        if(redisOperatorUtil.keyIsExist(key)){
            String fileContent = (String) redisOperatorUtil.get(key);
            return new Message(true, fileContent);
        } else { // 未命中，去数据库读，然后更新redis缓存
            if(!file.exists() || file.isDirectory()){
                return new Message(false, "请检查是否为正确的文件路径");
            }
            Message message = FileOperatorUtil.file2String(file);
            if(message.isSuccess()){
                redisOperatorUtil.set(key, message.getInfo());
            }
            return message;
        }
    }

    private List<JSONObject> readFromFile(String filePath) {

        File file = new File(filePath);
        File[] files = file.listFiles();
        List fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o2, File o1) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });
        List<JSONObject> ret = new ArrayList<JSONObject>();
        for (File file1 : files) {
            JSONObject ret2 = new JSONObject();
            ret2.put("dateTime", FileOperatorUtil.stampStrToDateStr(file1.getName()));
            ret2.put("fileContent", FileOperatorUtil.file2String(file1));
            ret.add(ret2);
        }
        return ret;
    }

    private static String getLatestFileName(String path) {
        ArrayList<String> fileNameList = new ArrayList<>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        if (tempList.length == 0) {
            return null;
        }
        for (int i = 0; i < tempList.length; i++) {
            fileNameList.add(tempList[i].getName());
        }
        fileNameList.sort(Comparator.reverseOrder());
        return fileNameList.get(0);
    }

    /**
    private boolean judgeDirty(String key){
        String judge_key = "$is_dirty" + key;
        if(redisTemplate.opsForValue().get(judge_key) == null) return false;
        return (Boolean) redisTemplate.opsForValue().get(judge_key);
    }
    private void setDirty(String key,Boolean dirty){
        String judge_key = "$is_dirty" + key;
        redisTemplate.opsForValue().set(judge_key, dirty);
    }*/

    @Override
    public String NewFunction(String realName, String lang) throws IOException {
//        String usr_dir = hostHolder.getUser().getUserDir();
        LinkedHashMap<String, Object> userData =
                (LinkedHashMap<String, Object>) userControllerApi.getUserProfile().getData();
        String userDir = (String) userData.get("userDir");
        String rpath = userDir.split(File.separator)[2];
        FileOperatorUtil.unzip(
                File.separator + "models" + File.separator + lang + ".zip",
                File.separator + "deployspaces" + File.separator + rpath + File.separator);
        File newFunc = new File(
                File.separator + "deployspaces" + File.separator + rpath + File.separator + lang);
        newFunc.renameTo(new File(
                File.separator + "deployspaces" + File.separator + rpath + File.separator + realName));
        File deployYml = new File(
                File.separator + "deployspaces" + File.separator + rpath + File.separator + realName + File.separator + "deploy.yml");
        deployYml.createNewFile();
        String depContent = "functions:\n" +
                "  " + realName + ":\n" +
                "    lang: " + lang + "\n" +
                "    handler: ./function\n" +
                "    image: 192.168.1.6:5555/model/" + realName + "\n";
        FileOutputStream fosd = new FileOutputStream(deployYml, true);
        fosd.write(depContent.getBytes());
        fosd.close();
        File ciYml = new File(
                File.separator + "deployspaces" + File.separator + rpath + File.separator + realName + File.separator + ".gitlab-ci.yml");
        ciYml.createNewFile();
        String ciContent = "include:\n" +
                "  - project: 'ops/ci-template'\n" +
                "    file: 'Template/base.yml'\n";
        FileOutputStream fosc = new FileOutputStream(ciYml, true);
        fosc.write(ciContent.getBytes());
        fosc.close();
        return File.separator + "deployspaces" + File.separator + rpath + File.separator + realName + File.separator + "function";
    }
}
