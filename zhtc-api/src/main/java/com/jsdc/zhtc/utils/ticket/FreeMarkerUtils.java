package com.jsdc.zhtc.utils.ticket;

import cn.hutool.core.codec.Base64;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.w3c.dom.Document;
import org.xhtmlrenderer.swing.Java2DRenderer;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * ClassName: FreeMarkerUtils <br/>
 * Description: <br/>
 * date: 2022/1/13 8:54<br/>
 *
 * @author bn<br       />
 */
public class FreeMarkerUtils {

    private static String getTemplate(String template, Map<String, Object> map) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);
        String templatePath = FreeMarkerUtils.class.getResource("/").getPath().replaceAll("%20", " ") + "/templates";
        cfg.setDirectoryForTemplateLoading(new File(templatePath));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        Template temp = cfg.getTemplate(template);
        StringWriter stringWriter = new StringWriter();
        temp.process(map, stringWriter);
        stringWriter.flush();
        stringWriter.close();
        return stringWriter.getBuffer().toString();
    }

    public static String turnImage(String template, Map<String, Object> map, HttpServletResponse response) throws Exception {
        String html = getTemplate(template, map);

        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(bin);
        Java2DRenderer renderer = new Java2DRenderer(document, 580, 600);
        renderer.getSharedContext().setReplacedElementFactory(new Base64ImgReplacedElementFactory());
        renderer.getSharedContext().getTextRenderer().setSmoothingThreshold(1);
        BufferedImage img = renderer.getImage();
//        ImageIO.write(img, "BMP", new File("C:\\Users\\Administrator\\Desktop\\filename.bmp"));

//        Thumbnails.of(new File("C:\\Users\\Administrator\\Desktop\\filename.bmp"))
//                .scale(1f) //图片大小（长宽）压缩比例 从0-1，1表示原图
//                .outputQuality(0.5f) //图片质量压缩比例 从0-1，越接近1质量越好
//                .toOutputStream(new FileOutputStream("C:\\Users\\Administrator\\Desktop\\filename%.bmp"));

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(img, "bmp", stream);
        String base64 = Base64.encode(stream.toByteArray());
        stream.flush();
        stream.close();
        return base64;
    }
}
