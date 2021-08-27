package com.tanklab.mathless.utils;

import com.tanklab.mathless.pojo.FileTreeNode;
import com.tanklab.mathless.pojo.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static java.lang.Runtime.getRuntime;

public class FileOperatorUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileOperatorUtil.class);

    public static Message create(String path, boolean isFile) throws IOException, InterruptedException {
        File file = new File(path);
        if (file.exists()) return new Message(false, "创建失败，目标已经存在");

        boolean flag = true;
        if (!isFile) {
            if (!file.mkdirs()) flag = false;
            else {
                Runtime runtime = getRuntime();
                String command = "chmod -R 777 " + file.getAbsolutePath();
                Process process = runtime.exec(command);
                process.waitFor();
            }
        } else {
            if (!file.getParentFile().exists()) {
                logger.info("文件的目录不存在，创建目录");
                if (!file.getParentFile().mkdirs()) {
                    logger.error("创建目标文件所在目录失败");
                    return new Message(false, "创建文件的所在目录失败");
                }
            }
            try {
                if (!file.createNewFile()) flag = false;
                else {
                    file.setWritable(true, false);
                    file.setReadable(true, false);
                    file.setExecutable(true, false);
                }
            } catch (IOException e) {
                logger.error("创建文件失败: " + e.getMessage());
                return new Message(false, e.getMessage());
            }
        }

        if (flag)
            return new Message(true, "创建成功");
        else
            return new Message(false, "创建失败");
    }

    public static Message delete(String path) {
        File file = new File(path);
        if (!file.exists()) return new Message(false, "需要删除的文件/文件夹不存在");

        boolean flag = true;
        if (file.isFile()) {
            if (!file.delete()) flag = false;

        } else {
            // 删除文件夹下的所有文件（包括子目录）
            File[] fs = file.listFiles();
            for (File f : fs) {
                // 删除子文件
                Message message = delete(f.getAbsolutePath());
                flag = message.isSuccess();
            }
            if (!file.delete()) flag = false;
        }

        if (flag) return new Message(true, "删除成功");
        else return new Message(false, "删除失败");
    }

    public static Message upload(String userDir, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            if (fileName != null) {
                File filePath = new File(userDir, fileName);
                file.transferTo(filePath);
                filePath.setWritable(true, false);
                filePath.setReadable(true, false);
                filePath.setExecutable(true, false);
                return new Message(true, userDir + File.separator + fileName);
            } else {
                return new Message(false, "请上传文件");
            }
        } catch (IOException e) {
            return new Message(false, "上传失败: " + e.getMessage());
        }
    }

    // 压缩文件 / 文件夹
    public static boolean compress(String filePath, String zipName) {
        // 被压缩的文件
        File fileToZip = new File(filePath);
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        try {
            File f = new File(fileToZip.getParentFile().getAbsolutePath() +
                    File.separator + zipName);
            // 压缩结果输出，即压缩包
            fos = new FileOutputStream(f);

            logger.info("压缩" + fileToZip.getParentFile().getAbsolutePath() + File.separator + zipName);

            zipOut = new ZipOutputStream(fos);
            zip(fileToZip, zipOut, "");

            zipOut.close();
            fos.close();
            logger.info("压缩文件成功");

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("压缩失败: " + e.getMessage());
            return false;
        }
        return true;
    }

    // 递归压缩文件
    private static void zip(File fileToZip, ZipOutputStream zipOut, String path) throws IOException {

        //不压缩隐藏文件夹
        if (fileToZip.isHidden()) return;

        // 判断压缩对象如果是一个文件夹
        if (fileToZip.isDirectory()) {

            // 遍历文件夹子目录，进行递归的zipFile
            File[] files = fileToZip.listFiles();

            //解决空文件夹问题
            if (files.length == 0) {
                ZipEntry zipEntry = new ZipEntry(path + fileToZip.getName() + "/");
                zipOut.putNextEntry(zipEntry);
            }

            for (File file : files) {
                zip(file, zipOut, path + fileToZip.getName() + File.separator);
            }
            // 如果当前递归对象是文件夹，加入ZipEntry之后就返回
            return;
        }

        //如果当前的fiieToZip是一个文件，将其以字节码形式压缩到压缩包里面
        FileInputStream fis = new FileInputStream(fileToZip);

        // 创建压缩文件中的条目。
        ZipEntry zipEntry = new ZipEntry(path + fileToZip.getName());
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    // 解压文件
    public static boolean decompress(String zipDir, String destDir) {
        return false;
    }

    // 输出文件结构树
    public static List<FileTreeNode> getFileTree(String path) {
        if (!new File(path).exists()) {
            logger.error("文件/文件夹不存在");
            return null;
        }
        String basePath = new File(path).getAbsolutePath() + File.separator;
        return traverseFiles(basePath, new File(path));
    }

    public static List<FileTreeNode> traverseFiles(String basePath, File file) {
        List<FileTreeNode> baseTreeNodes = new ArrayList<>();
        File[] childFiles = file.listFiles();
        if (childFiles != null) {
            for (File listFile : childFiles) {
                FileTreeNode baseTreeNode = new FileTreeNode();
                baseTreeNode.setName(listFile.getName());
                baseTreeNode.setIfDir(listFile.isDirectory());
                String filePath = listFile.getAbsolutePath();
                baseTreeNode.setPath(filePath.substring(filePath.indexOf(basePath) + basePath.length()));
                //baseTreeNode.setLength(listFile.length());
                baseTreeNode.getChildren().addAll(traverseFiles(basePath, listFile));
                baseTreeNodes.add(baseTreeNode);
            }
        }
        return baseTreeNodes;
    }

    //随机生成指定位数的字符串
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    // 文件内容转为字符串
    public static Message file2String(File file) {
        String ret = null;
        try {
            if (file.exists()) {
                byte[] data = new byte[(int) file.length()];
                boolean result;
                FileInputStream inputStream = null;
                inputStream = new FileInputStream(file);
                int len = inputStream.read(data);
                inputStream.close();
                ret = new String(data);
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            return new Message(false, ex.getMessage());
        } finally {
            return new Message(true, ret);
        }
    }

    // 将时间戳转换为具体时间格式
    public static String stampStrToDateStr(String stampStr) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(stampStr)));
    }

    public static void saveAsFileWriter(String filePath, String content) {

        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(filePath);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //自动拼接成路径，避免一长段代码
    public static String concatPaths(String... paths){
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < paths.length; i++){
            stringBuffer.append(paths[i]);
            if(i != paths.length - 1) stringBuffer.append(File.separator);
        }
        return stringBuffer.toString();
    }

    // 列出某个文件夹内的文件夹/文件名
    public static Message listFiles(String path){
        File root = new File(path);
        if(!root.exists()) return new Message(false, "文件夹不存在");
        if(!root.isDirectory()) return new Message(false, "不是文件夹");
        File[] files = root.listFiles();
        List<String> fileList = new ArrayList<>();
        for(File f : files){
            fileList.add(f.getName());
        }
        return new Message(true, "操作成功", fileList);
    }

    //文件复制
    public static Message copy(String source, String dest) throws IOException {
        FileChannel in = new FileInputStream(source).getChannel();
        FileChannel out = new FileOutputStream(dest).getChannel();
        out.transferFrom(in, 0, in.size());
        return new Message(true, "操作成功");
    }

    //解压
    public static void unzip(String path, String target) throws IOException {
        File targetfolder = new File(target);
        ZipInputStream zi = new ZipInputStream(new FileInputStream(path));
        ZipEntry ze = null;
        FileOutputStream fo = null;
        byte[] buff = new byte[1024];
        int len;
        while((ze =  zi.getNextEntry())!=null)
        {
            File _file = new File(targetfolder,ze.getName());
            if(!_file.getParentFile().exists()) _file.getParentFile().mkdirs();
            if(ze.isDirectory())
            {
                _file.mkdir();
            }
            else //file
            {
                fo = new FileOutputStream(_file);
                while((len=zi.read(buff))>0) fo.write(buff, 0, len);
                fo.close();
            }
            zi.closeEntry();
        }
        zi.close();
    }

}