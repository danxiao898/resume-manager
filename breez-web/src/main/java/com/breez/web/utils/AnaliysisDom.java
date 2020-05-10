package com.breez.web.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
/**
 * 分析文档的格式是doc还是docx
 * */
public class AnaliysisDom {

    public static String analiysisDom(String path,String filename,HttpServletRequest request){
        String uri=null;
        String realPath=null;
        String inputpath=path+filename;
        String outputname=(filename.substring(0,filename.lastIndexOf('.')))+".html";
        String outputpath=path+outputname;
        File imgfile=new File(path+filename.substring(0,filename.lastIndexOf('.')));
        String string=imgfile.getPath();
        if (!imgfile.exists()){
            imgfile.mkdirs();
        }
        File outputFile = new File(outputpath);
        if (outputFile.exists()){

        }else {
            if (inputpath.endsWith(".doc") || inputpath.endsWith(".DOC")){
                try {
                    String s = Doc2Html.analysisDocument(inputpath, true, imgfile.getPath(), imgfile.getPath());
                    File file=new File(outputpath);
                    if (file.exists()){
                        file.createNewFile();
                    }
                    FileWriter fileWriter=new FileWriter(file);
                    BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                    bufferedWriter.write(s,0,s.length()-1);
                    bufferedWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (inputpath.endsWith(".docx") || inputpath.endsWith("DOCX")){
                try {
                    String s = Docx2Html.analysisDocument(inputpath, true, imgfile.getPath()+"/", imgfile.getPath()+"/");
                    File file=new File(outputpath);
                    if (file.exists()){
                        file.createNewFile();
                    }
                    FileWriter fileWriter=new FileWriter(file);
                    BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                    bufferedWriter.write(s,0,s.length()-1);
                    bufferedWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        realPath = request.getSession().getServletContext().getRealPath(outputname);
        System.out.println(realPath);
        uri=request.getScheme() + "://" + request.getServerName() + ":" +
                request.getServerPort() +"/"+ request.getContextPath() +outputname;
        return uri;
    }
}


