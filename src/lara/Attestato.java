package lara;

import org.apache.commons.io.IOUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextReplacedElementFactory;

import java.io.InputStream;
import java.io.OutputStream;

public class Attestato {

    public void writePdf(OutputStream out) throws Exception{

        InputStream in = Attestato.class.getResourceAsStream("/attestato.html");
        String html = IOUtils.toString(in, "UTF-8");
        in.close();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);

        renderer.getSharedContext().setReplacedElementFactory(new ReplacedElementFactoryImpl(renderer.getOutputDevice()));

        renderer.layout();
        renderer.createPDF(out);
        renderer.finishPDF();
    }
}
