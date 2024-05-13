package com.jsdc.zhtc.utils.ticket;

import lombok.SneakyThrows;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;
import org.xhtmlrenderer.swing.ImageReplacedElement;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * ClassName: Base64ImgReplacedElementFactory <br/>
 * Description: <br/>
 * date: 2022/1/13 8:57<br/>
 *
 * @author bn<br       />
 */
public class Base64ImgReplacedElementFactory implements ReplacedElementFactory {
    @SneakyThrows
    @Override
    public ReplacedElement createReplacedElement(LayoutContext layoutContext, BlockBox blockBox, UserAgentCallback userAgentCallback, int width, int height) {
        Element e = blockBox.getElement();
        if (e == null) {
            return null;
        }
        String nodeName = e.getNodeName();
        if ("img".equals(nodeName)) {
            // 这里直接从标签获取base64图片的值，如果是地址的话需要通过地址去获取图片
            String attribute = e.getAttribute("src").replaceAll(" ", "");

            // 这里的width和height就是标签的css属性
            return new ImageReplacedElement(buildImage(attribute, userAgentCallback), width, height);
        }
        return null;
    }

    /**
     * 将base64编码解码并生成图像
     *
     * @param srcAttr 属性
     * @param uac     回调
     * @return FSImage
     */
    protected java.awt.Image buildImage(String srcAttr, UserAgentCallback uac) throws IOException {

        if (srcAttr.startsWith("data:image/")) {
            String b64encoded = srcAttr.substring(srcAttr.indexOf("base64,") + "base64,".length()
            );
            // 解码
            byte[] decodedBytes = Base64.getDecoder().decode(b64encoded);
            ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
            return ImageIO.read(bais);
        }
        return null;
    }

    @Override
    public void reset() {
    }

    @Override
    public void remove(Element element) {
    }

    @Override
    public void setFormSubmissionListener(FormSubmissionListener formSubmissionListener) {
    }
}
