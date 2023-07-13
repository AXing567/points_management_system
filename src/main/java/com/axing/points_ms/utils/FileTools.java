package com.axing.points_ms.utils;

import java.io.File;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.utils
 * @className: FileTools
 * @author: Axing
 * @description: TODO
 * @date: 2023/7/11 15:41
 * @version: 1.0
 */
public class FileTools {
    /**
     * @param oldFileNamePath:
     * @param newFileNamePath:
     * @return String
     * @author Axing
     * @description 输入旧文件名和新文件名，返回新文件名（旧文件名可含路径）
     * @date 2023/7/11 15:41
     */
    public static String renameFile(String oldFileNamePath, String newFileNamePath) {
        // 创建一个File对象，表示旧文件名
        File file = new File(oldFileNamePath);
        // 初始化文件扩展名为空
        String extension = "";
        // 获取文件名中最后一个点（即文件扩展名）的索引
        int i = file.getName().lastIndexOf('.');
        // 如果文件扩展名存在，则将其保存在extension变量中
        if (i > 0) {
            extension = file.getName().substring(i);
        }
        // 创建一个新的File对象，表示新文件的完整路径，包括新文件名和旧文件的扩展名
        String newPath = new File(file.getParent(), newFileNamePath + extension).getAbsolutePath();
        // 创建一个新的File对象，表示新文件的完整路径
        File newFile = new File(newPath);
        // 检查新文件的父文件夹是否存在
        if(!newFile.getParentFile().exists()){
            // 如果不存在，则递归创建所有缺少的文件夹
            newFile.getParentFile().mkdirs();
        }
        // 返回新文件的完整路径
        return newPath;
    }
}
