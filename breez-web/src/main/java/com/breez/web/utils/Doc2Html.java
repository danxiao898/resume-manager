package com.breez.web.utils;

/**
 * WORD转HTML doc格式
 * POI版本: 3.10-FINAL
 * */

import java.io.*;

import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;

public class Doc2Html {

    private static final short ENTER_ASCII = 13; //回车
    private static final short SPACE_ASCII = 32; // 空格
    private static final short TABULATION_ASCII = 9; // TAB

    public static int beginPosi = 0;
    public static int endPosi = 0;
    public static int beginArray[];
    public static int endArray[];
    public static String htmlTextArray[];
    public static boolean tblExist = false;

    /**
     * 解析DOC
     *
     * @param fileName 文件名
     * @param  isAllHtml 全部为HMTL
     * @param  tmpImgDir 临时目录,图片
     * @param  tmpImgUrl 临时链接,图片
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
        HWPFDocument doc = new HWPFDocument(in);
        Range rangetbl = doc.getRange();
        TableIterator it = new TableIterator(rangetbl);
        int num = 100;

        beginArray = new int[num];
        endArray = new int[num];
        htmlTextArray = new String[num];
        int length = doc.characterLength();
        if(length==0)return "";
        PicturesTable pTable = doc.getPicturesTable();
        SummaryInformation sif = doc.getSummaryInformation();
        String title = "DOC文件预览";
        if (null != sif) {
            title = doc.getSummaryInformation().getTitle();
        }
        StringBuffer htmlText=new StringBuffer("");
        if(isAllHtml){
            htmlText.append("<html><head><title>").append(title).append("</title></head><body>");
        }
        if (it.hasNext()) {
            analysisTables(it, rangetbl);
        }

        int cur = 0;
        String tempString = "";
        int index=0;
        for (int i = 0; i < length - 1; i++) {
            Range range = new Range(i, i + 1, doc);
            CharacterRun cr = range.getCharacterRun(0);
            if (tblExist) {
                if (i == beginArray[cur]) {
                    htmlText.append(tempString).append(htmlTextArray[cur]);
                    tempString = "";
                    i = endArray[cur] - 1;
                    cur++;
                    continue;
                }
            }
            if (pTable.hasPicture(cr)) {
                htmlText.append(tempString);
                String picFileName=analysisPicture(pTable, cr,tmpImgDir,index++);
                htmlText.append("<img src=\"").append(tmpImgUrl.endsWith("/")?tmpImgUrl:(tmpImgUrl+"/")).append(picFileName).append("\" style='width:100%;height:100%;border:0px;'/>");
                tempString = "";
            } else {
                Range range2 = new Range(i + 1, i + 2, doc);
                CharacterRun cr2 = range2.getCharacterRun(0);
                char c = cr.text().charAt(0);
                if (c == ENTER_ASCII) {
                    tempString += "<br/>";
                }
                else if (c == SPACE_ASCII)
                    tempString += " ";
                else if (c == TABULATION_ASCII)
                    tempString += "    ";
                boolean flag = compareCharStyle(cr, cr2);
                if (flag)
                    tempString += cr.text();
                else {
                    StringBuffer fontStyle1 = new StringBuffer("<span style=\"font-family:");
                    fontStyle1.append(cr.getFontName() ).append(";font-size:").append( cr.getFontSize() / 2).append( "pt;");
                    if (cr.isBold())
                        fontStyle1.append("font-weight:bold;");
                    if (cr.isItalic())
                        fontStyle1.append("font-style:italic;");
                    htmlText.append(fontStyle1.toString()).append("\">") .append(tempString).append(cr.text()).append("</span>");
                    fontStyle1.delete(0,fontStyle1.length());
                    tempString = "";
                }
            }
        }
        htmlText .append(tempString);
        if(isAllHtml){htmlText .append("</body></html>");}
        return htmlText.toString();
    }

    /**
     * 解析表格
     * @param  it
     * @param  rangetbl DOC段
     * @throws Exception
     */
    static void analysisTables(TableIterator it, Range rangetbl)
            throws Exception {
        int counter=0;
        while (it.hasNext()) {
            tblExist = true;
            StringBuffer htmlTextTbl = new StringBuffer();
            Table tb = (Table) it.next();
            beginPosi = tb.getStartOffset();
            endPosi = tb.getEndOffset();
            beginArray[counter] = beginPosi;
            endArray[counter] = endPosi;
            htmlTextTbl .append("<table border=\"1\" style=\"width:100%;font-size:14px;\" class=\"display table table-striped table-bordered table-hover table-checkable dataTable no-footer\">");
            for (int i = 0; i < tb.numRows(); i++) {
                TableRow tr = tb.getRow(i);
                String trCls=(i%2==1)?"odd":"even";
                htmlTextTbl .append("<tr class='").append(trCls).append("'>");
                for (int j = 0; j < tr.numCells(); j++) {
                    TableCell td = tr.getCell(j);
                    int cellWidth = td.getWidth();
                    for (int k = 0; k < td.numParagraphs(); k++) {
                        Paragraph para = td.getParagraph(k);
                        String s = para.text().toString().trim();
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
            htmlTextArray[counter++] = htmlTextTbl.toString();
        }
    }

    /**
     * 图片解析
     *
     * @param  pTable WORD中的图片域
     * @param  cr
     * @param  path 临时路径
     * @return String 图片文件名
     * @throws Exception
     */
    static String analysisPicture(PicturesTable pTable, CharacterRun cr, String path, int index)
            throws Exception {
        // 图片对象
        Picture pic = pTable.extractPicture(cr, false);
        // 图片文件名
        String afileName = "dzpic_"+System.currentTimeMillis()+"_"+index+".jpg";
        OutputStream out = new FileOutputStream(new File((path.endsWith("/")?path:(path+ "/")) + afileName));
        pic.writeImageContent(out);
        out.close();
        return afileName;
    }
    /**
     * 切换文字样式
     * @param  cr1
     * @param  cr2
     * @return boolean
     * */
    static boolean compareCharStyle(CharacterRun cr1, CharacterRun cr2) {
        boolean flag = false;
        if (cr1.isBold() == cr2.isBold() && cr1.isItalic() == cr2.isItalic()
                && cr1.getFontName().equals(cr2.getFontName())
                && cr1.getFontSize() == cr2.getFontSize()) {
            flag = true;
        }
        return flag;
    }
    /**
     * 测试
     * */
    public static void main(String args[]) {
        try {
            String htmlDoc=analysisDocument("/Users/wuhualu/Downloads/陆燕娟的简历.doc", true, "/Users/wuhualu/Documents/uploadtemp/","/Users/wuhualu/Documents/uploadtemp/");
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


