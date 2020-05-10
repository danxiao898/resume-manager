/**
 * WORD转HTML docx格式
 * POI版本: 3.10-FINAL
 * */
package com.breez.web.utils;
import java.io.*;
import java.util.List;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class Docx2Html {
    /**
     * 解析DOC
     *
     * @param fileName 文件名
     * @param  isAllHtml 全部为HMTL
     * @param  tmpImgDir 临时目录,不包含文件名
     * @param  tmpImgUrl 临时链接,图片 链接不包含文件名,通常这个参数可以传一个相对路径
     * @throws Exception
     */
    public static String analysisDocument(String fileName,boolean isAllHtml,String tmpImgDir,String tmpImgUrl) throws Exception{
        InputStream in = new FileInputStream(new File(fileName));
        return analysisDocument(in, isAllHtml, tmpImgDir, tmpImgUrl);
    }
    /**
     * 解析DOC
     */
    public static String analysisDocument(InputStream in,boolean isAllHtml,String tmpImgDir,String tmpImgUrl) throws Exception {
        XWPFDocument doc = new XWPFDocument(in);
        StringBuffer buffer=new StringBuffer();
        List<IBodyElement> eles= doc.getBodyElements();
        for(IBodyElement el:eles){
            String name=el.getElementType().name();
            if(name.equals("表格")){//表格
                XWPFTable table=(XWPFTable)el;
                buffer.append(analysisTable(table));
            }else {//文本
                XWPFParagraph graph=(XWPFParagraph)el;
                List<XWPFRun> runs=graph.getRuns();
                ParagraphAlignment alignment=graph.getAlignment();
                String align="left";
                if(alignment.equals(ParagraphAlignment.CENTER)){
                    align="center";
                }else if(alignment.equals(ParagraphAlignment.RIGHT)){
                    align="right";
                }
                buffer.append("<div style='text-align:").append(align);

                StringBuffer pBuffer=new StringBuffer();
                int index=0;
                for(XWPFRun run:runs){
                    index++;
                    List<XWPFPicture> pics=run.getEmbeddedPictures();
                    if(null!=pics){
                        for(XWPFPicture pic:pics){
                            index++;
                            XWPFPictureData pictureData=pic.getPictureData();
                            byte[] picBytes=pictureData.getData();
                            String picName="dzpic_"+System.currentTimeMillis()+"_"+index+".jpg";
                            File file=new File(tmpImgDir+"/"+picName);
                            OutputStream os =new FileOutputStream(file);
                            os.write(picBytes);
                            os.close();
                            pBuffer.append("<img src='").append(tmpImgUrl).append(picName).append("'border=0'/>");
                        }
                    }
                    String text=run.getText(run.getTextPosition());
                    if(null!=text){
                        String color=null==run.getColor()?"000":run.getColor();
                        int fontsize=run.getFontSize()==-1?15:run.getFontSize();
                        pBuffer.append("<span style='color:#").append(color).append(";font-size:").append(fontsize).append("px;'>");
                        pBuffer.append(text).append("</span>");
                    }
                }
                buffer.append(";'>").append(pBuffer.toString()).append("</div>");
            }
        }
        return buffer.toString();
    }
    /**
     * 解析表格
     * @param tb 表格 对象
     * @return String
     * @throws Exception
     */
    static String analysisTable(XWPFTable tb)
            throws Exception {
        StringBuffer htmlTextTbl = new StringBuffer();
        htmlTextTbl .append("<table border=\"1\" style=\"width:100%;font-size:14px;\" class=\"display table table-striped table-bordered table-hover table-checkable dataTable no-footer\">");
        List<XWPFTableRow> rows=tb.getRows();
        int rowCount=rows.size();
        for (int i = 0; i < rowCount; i++) {
            XWPFTableRow tr = rows.get(i);
            String trCls=(i%2==1)?"odd":"even";
            htmlTextTbl .append("<tr class='").append(trCls).append("'>");

            List<XWPFTableCell> cells=tr.getTableCells();
            int cellCount=cells.size();
            for (int j = 0; j < cellCount; j++) {
                XWPFTableCell td = tr.getCell(j);
                List<XWPFParagraph> cellGraphs=  td.getParagraphs();
                for (int k = 0; k < cellGraphs.size(); k++) {
                    XWPFParagraph para = cellGraphs.get(k);
                    String s =para.getText()==null?"": para.getText().trim();
                    if (s == "") {
                        s = "&nbsp;";
                    }
                    if(i==0){
                        htmlTextTbl.append("<th class='td-center'>").append(s).append("</th>");
                    }else{
                        htmlTextTbl.append("<td class='td-center'>").append(s).append("</td>");
                    }

                }
            }
            htmlTextTbl.append("</tr>");

        }
        htmlTextTbl.append("</table>");
        return htmlTextTbl.toString();
    }

    public static void main(String args[]) {
        try {
            String htmlDoc=analysisDocument("/Users/wuhualu/Documents/一种微风智慧运维管理系统的研发.docx", true, "/Users/wuhualu/Documents/uploadtemp/","/Users/wuhualu/Documents/uploadtemp/");
            File file=new File("/Users/wuhualu/Documents/uploadtemp/text.html");
            if (file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter=new FileWriter(file);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.write(htmlDoc,0,htmlDoc.length()-1);
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

