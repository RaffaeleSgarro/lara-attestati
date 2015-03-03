package lara;

import com.lowagie.text.Image;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextReplacedElementFactory;
import org.xhtmlrenderer.render.BlockBox;

import javax.imageio.ImageIO;
import java.io.InputStream;

public class ReplacedElementFactoryImpl extends ITextReplacedElementFactory {

    public ReplacedElementFactoryImpl(ITextOutputDevice outputDevice) {
        super(outputDevice);
    }

    @Override
    public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
        Element e = box.getElement();

        if (e != null && e.getTagName().equals("img")) {
            try (InputStream in = getClass().getResourceAsStream("/" + e.getAttribute("src"))) {
                byte[] png = IOUtils.toByteArray(in);
                Image iTextImage = Image.getInstance(png);
                iTextImage.scaleAbsolute(cssWidth, cssHeight);
                FSImage fsImage = new ITextFSImage(iTextImage);

                return new ITextImageElement(fsImage);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        } else {
            return super.createReplacedElement(c, box, uac, cssWidth, cssHeight);
        }
    }
}
