package com.keven.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * word文档阅读
 * Created by Administrator on 2016/11/14.
 */
public class ApachePoiUtil {

    //    private static String docPath = Environment.getExternalStorageDirectory() + "/DownloadDoc/";
    private Context mContext;
    private static ApachePoiUtil mUtil;
    private String docPath;
    private String savePath;
    private String savePathX;

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        if (mUtil == null)
            mUtil = new ApachePoiUtil();
        mUtil.mContext = context;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mUtil.docPath = Environment.getExternalStorageDirectory() + "/Documents/DownloadDoc/";
            mUtil.savePath = Environment.getExternalStorageDirectory() + "/Documents/ConvertedHtml/";
            mUtil.savePathX = Environment.getExternalStorageDirectory() + "/Documents/ConvertedHtmlX/";
        } else {
            mUtil.docPath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/DownloadDoc/";
            mUtil.savePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/ConvertedHtml/";
            mUtil.savePathX = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/ConvertedHtmlX/";
        }

        new File(mUtil.docPath).mkdirs();
        new File(mUtil.savePath).mkdirs();
        new File(mUtil.savePathX).mkdirs();
        Log.e("ApachePoiUtil.init()", mUtil.docPath);
    }

    public static ApachePoiUtil getInstance() {
        return mUtil;
    }

    /**
     * 转换html
     *
     * @param docName
     * @param convertHandler
     */
    public void convert2Html(final String docName, final ConvertHandler convertHandler) {
        final String outPutFile = docName.substring(0, docName.indexOf(".")) + ".html";
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(docPath + docName));

                    WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                            DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

                    //设置图片路径
                    wordToHtmlConverter.setPicturesManager(new PicturesManager() {
                        public String savePicture(byte[] content,
                                                  PictureType pictureType, String suggestedName,
                                                  float widthInches, float heightInches) {
                            String name = docName.substring(0, docName.indexOf("."));
                            return name + "/" + suggestedName;
                        }
                    });

                    //保存图片
                    List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
                    if (pics != null) {
                        for (int i = 0; i < pics.size(); i++) {
                            Picture pic = pics.get(i);
                            System.out.println(pic.suggestFullFileName());
                            try {
                                String name = docName.substring(0, docName.indexOf("."));
                                if (!(new File(savePath + name).exists()))
                                    new File(savePath + name).mkdirs();
                                pic.writeImageContent(new FileOutputStream(savePath + name + "/"
                                        + pic.suggestFullFileName()));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    wordToHtmlConverter.processDocument(wordDocument);
                    Document htmlDocument = wordToHtmlConverter.getDocument();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    DOMSource domSource = new DOMSource(htmlDocument);
                    StreamResult streamResult = new StreamResult(out);

                    TransformerFactory tf = TransformerFactory.newInstance();
                    Transformer serializer = tf.newTransformer();
                    serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
                    serializer.setOutputProperty(OutputKeys.INDENT, "yes");
                    serializer.setOutputProperty(OutputKeys.METHOD, "html");
                    serializer.transform(domSource, streamResult);
                    out.close();
                    //保存html文件
                    writeFile(new String(out.toByteArray()), outPutFile, convertHandler);
                } catch (TransformerException e1) {
                    e1.printStackTrace();
                    convertHandler.onError(e1);
                } catch (IOException e2) {
                    e2.printStackTrace();
                    convertHandler.onError(e2);
                } catch (ParserConfigurationException e3) {
                    e3.printStackTrace();
                    convertHandler.onError(e3);
                }
            }
        }.start();
    }

    /**
     * 转换至xhtml
     *
     * @param docName
     * @param convertHandler
     */
    public void convert2HtmlX(final String docName, final ConvertHandler convertHandler) {
        final String outPutFile = docName.substring(0, docName.indexOf(".")) + ".html";
        final String name = docName.substring(0, docName.indexOf("."));
        new Thread() {
            @Override
            public void run() {
                super.run();
                InputStream input = null;
                try {
                    input = new FileInputStream(docPath + docName);
                    XWPFDocument document = new XWPFDocument(input);
                    XHTMLOptions options = XHTMLOptions.create();
                    File imageFolder = new File(savePathX + name + "/");
                    // 设置图片生成路径
                    options.setExtractor(new FileImageExtractor(imageFolder));
                    // 设置图片链接，去掉后使用相对路径
                    options.URIResolver(new FileURIResolver(imageFolder));
                    OutputStream out = new FileOutputStream(new File(savePathX + outPutFile));
                    XHTMLConverter.getInstance().convert(document, out, options);
                    out.close();
                    input.close();
                    convertHandler.onSuccess(savePathX + outPutFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    convertHandler.onError(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    convertHandler.onError(e);
                }
            }
        }.start();
    }

    /**
     * 写入文件s
     *
     * @param content
     * @param path
     * @param convertHandler
     */
    private void writeFile(String content, String path, ConvertHandler convertHandler) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            File file = new File(savePath, path);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
            bw.write(content);
            convertHandler.onSuccess(savePath + path);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ie) {
            }
        }
    }

    public String getSavePath(boolean isX) {
        return isX ? savePathX : savePath;
    }

    public String getDocPath() {
        return docPath;
    }

    public interface ConvertHandler {
        void onSuccess(String filepath);
        void onError(Exception e);
    }
}