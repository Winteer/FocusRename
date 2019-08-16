package Medical;

import Focus.getFocusUtil;
import com.alibaba.fastjson.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 1、json文件中汉字改为首字母大写
 * 2、统一图片格式
 */
public class medicalUtil {


    /**
     * 重命名windows屏保图片文件
     *
     * @param DocPath Windows屏保路径
     * @param OutPath 导出路径
     */
    public static void ReNameFile(String DocPath, String OutPath) {
        String fileName = "";
        String newFileName = "";
        File file = null;
        file = new File(DocPath);
        if(!file.isDirectory()){
        }else if(file.isDirectory()){
            File[] files =  file.listFiles();
            for(File tmpFile : files){
                //遍历每个对象，只有为文件时才进行处理
                if(tmpFile.isFile()){
                    System.out.println(tmpFile.getAbsolutePath());
                    fileName = tmpFile.getName(); //当前文件名
                    System.out.println(fileName.indexOf(".json"));
                    if(fileName.indexOf(".json") != -1){
                        List<JSONObject> newList = new ArrayList<JSONObject>();
                        System.out.println("进行json处理！");
                        String JsonStr = jsonUtils.readJsonFile(tmpFile.getAbsolutePath());
                        JSONObject jsonObject = JSONObject.parseObject(JsonStr);
                        List<JSONObject> list = (List<JSONObject>) jsonObject.get("shapes");
                        System.out.println(list.size());
                        //遍历文件内的每个对象，统计以及修改相应的数据
                        for(JSONObject obj : list){
                            String label = "";
                            JSONObject newObj = obj;
                            System.out.println(newObj.get("label"));
                            label=getPinYinHeadChar(newObj.get("label").toString()).toUpperCase();
                            newObj.put("label",label);
                            newList.add(newObj);
                        }
                        jsonObject.put("shapes",list);
                        /****************************/
                        // 汉字转换为首字母大写 完成
                        // 写入新的json文件
                        /****************************/
                        jsonUtils.createJsonFile(jsonObject.toJSONString(),"E:\\modify",fileName);
                        System.out.println(jsonObject.toJSONString());
                    }else{
                        System.out.println("进行图片处理！");
                    }
//                    renameFile(DocPath,OutPath, fileName, newFileName);
                }
            }
        }
        System.out.println("=============");
        System.out.println("   转换完毕！");
        System.out.println("=============");
    }

    /**
     * 文件重命名
     *
     * @param path    原文件路径
     * @param outPath 导出文件路径
     * @param oldname 原文件名
     * @param newname 新文件名
     */
    public static void renameFile(String path, String outPath, String oldname, String newname) {
        if (!oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "\\" + oldname);
            File newfile = new File(outPath + "\\" + newname);
            File dir = newfile.getParentFile(); //获取文件夹
            if (!dir.exists()) { //如果导出文件夹不存在，则创建
                dir.mkdirs();
            }
            if (!oldfile.exists()) {
                return;//重命名文件不存在
            }
            if (newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                System.out.println(newname + "已经存在！");
            else {
                try {
                    //复制到导出路径，原路径下文件依然存在
                    FileUtils.copyFile(oldfile, newfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //下面的方式为剪切方式，剪切后，原路径下没有图片文件
                //oldfile.renameTo(newfile);
            }
        } else {
            System.out.println("新文件名和旧文件名相同...");
        }
    }

    /**
     * 得到 全拼
     *
     * @param src
     * @return
     */
    public static String getPingYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符
                if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];
                } else {
                    t4 += java.lang.Character.toString(t1[i]);
                }
            }
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4;
    }

    /**
     * 得到中文首字母
     *
     * @param str
     * @return
     */
    public static String getPinYinHeadChar(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    /**
     * 将字符串转移为ASCII码
     *
     * @param cnStr
     * @return
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }

    public static void main(String[] args) {
        String cnStr = "智慧医疗";
        System.out.println(getPingYin(cnStr).toUpperCase());
        System.out.println(getPinYinHeadChar(cnStr).toUpperCase());
        medicalUtil.ReNameFile("E:\\test", "E:\\modify");
    }
}
