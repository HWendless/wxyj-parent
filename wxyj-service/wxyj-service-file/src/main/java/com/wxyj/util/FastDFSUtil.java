package com.wxyj.util;

import com.wxyj.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FastDFSUtil {
    /**
     * 加载Treaker连接信息
     *
     */

    static {
        try {
            //查找classpath下的文件路径
            String filename = new ClassPathResource("fdfs_client.conf").getPath();
            ClientGlobal.init(filename);
//            ClientGlobal clientGlobal =
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     * @param
     */
    public static String[] upload (FastDFSFile fastDFSFile) throws Exception {
        //附加参数
        NameValuePair [] meta_list= new NameValuePair[1];
        meta_list[0]=new NameValuePair("author",fastDFSFile.getAuthor());
        //创建一个Tracker访问的客户端对象TrackerClient


        TrackerServer trackerServer = getTrackerServer();

        //通过TrackerClient的链接信息可以获得Storage的链接信息，创建StorageClient对象存储Storage的链接信息

        StorageClient storageClient = getStrorageClient(trackerServer);

        /**
         * 通过StorageClient访问Storage，实现文件上传，并且获取文件上传
         * 1.上传文件的字节数组
         * 2.文件的扩展名 jpg
         * 3.附加参数 比如：拍摄地址： 北京
         */
        /**
         * uploads[]
         *      uploads[0]:文件上传所存储的Storage 的组名字 ，group1
         *      uploads[1]：文件存储到storage的名字 /
         */
       String [] uploads = storageClient.upload_file(fastDFSFile.getContent(),fastDFSFile.getExt(),null);
        return uploads;

    }

    /**
     * 获取文件信息
     * @param groupName 文件的组名 group1
     * @param remoteFileName 文件的存储路径名字
     */
    public static  FileInfo getFile (String groupName, String remoteFileName) throws  Exception{
        //创建一个TrackerClient对象，通过TrackerClient 对象访问TrackerService
        TrackerClient trackerClient = new TrackerClient();
        //
        TrackerServer trackerServer = trackerClient.getConnection();

        StorageClient storageClient = new StorageClient(trackerServer,null);
        //返会文件的ip，文件的大小，
        return storageClient.get_file_info(groupName,remoteFileName);


    }

    public static InputStream downloadFile(String groupName, String remoteFileName) throws  Exception{

        //创建一个TrackerClient对象，通过TrackerClient 对象访问TrackerService
        TrackerClient trackerClient = new TrackerClient();
        //
        TrackerServer trackerServer = trackerClient.getConnection();

        StorageClient storageClient = new StorageClient(trackerServer,null);

        byte [] buffer = storageClient.download_file(groupName,remoteFileName);

        return new ByteArrayInputStream(buffer);

    }

    public static void deleteFile(String groupName, String remoteFileName) throws  Exception{
        //创建一个TrackerClient对象，通过TrackerClient 对象访问TrackerService
        TrackerClient trackerClient = new TrackerClient();
        //
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer,null);
        //删除
        storageClient.delete_file(groupName,remoteFileName);
    }

    public static StorageServer  getStorages() throws  Exception {
        //创建一个TrackerClient对象，通过TrackerClient 对象访问TrackerService
        TrackerClient trackerClient = new TrackerClient();
        //
        TrackerServer trackerServer = trackerClient.getConnection();

        //获取Storage信息
        return trackerClient.getStoreStorage(trackerServer);

    }
    public static ServerInfo[] getServerInfo(String groupName, String remoteFileName)  throws  Exception{

        //创建一个TrackerClient对象，通过TrackerClient 对象访问TrackerService
        TrackerClient trackerClient = new TrackerClient();
        //
        TrackerServer trackerServer = trackerClient.getConnection();

        return trackerClient.getFetchStorages(trackerServer,groupName,remoteFileName);


    }

//    public static  void main(String [] args) throws  Exception{
//        FileInfo fileInfo = getFile("group1","M00/00/00/wKg8zF4cGbWAVJujAAAiwa3P4dA064.jpg");
//        System.out.println(fileInfo.getSourceIpAddr());
//        System.out.println(fileInfo.getFileSize());
//        InputStream is = downloadFile("group1","M00/00/00/wKg8zF4cGbWAVJujAAAiwa3P4dA064.jpg");
//        FileOutputStream os = new FileOutputStream("D:/1.jpg");
//        //定义一个缓存区
//
//        byte[] buffer = new byte[1024];
//        while (is.read(buffer)!=-1)
//        {
//            os.write(buffer);
//        }
//        os.flush();
//        os.close();
//        is.close();
//    }

    public static  TrackerServer getTrackerServer() throws  Exception {
        TrackerClient trackerClient = new TrackerClient();

        //通过TrackerClient访问TrackerServer服务，获取连接信息

        TrackerServer trackerServer = trackerClient.getConnection();

        //通过TrackerClient的链接信息可以获得Storage的链接信息，创建StorageClient对象存储Storage的链接信息

        return  trackerServer;

    }

    public static  StorageClient getStrorageClient(TrackerServer trackerServer) {
       return    new StorageClient(trackerServer,null);
    }

    public static void main(String[] args) throws  Exception{
        getServerInfo("group1","M00/00/00/wKg8zF4cGbWAVJujAAAiwa3P4dA064.jpg");
    }
}
