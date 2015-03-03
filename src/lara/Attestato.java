package lara;

import com.samskivert.mustache.Template;
import org.apache.commons.io.IOUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class Attestato {

    private final Template template;

    public Attestato(Template template) {
        this.template = template;
    }

    public void writePdf(Map<String, Object> bindings, OutputStream out) throws Exception{

        ITextRenderer renderer = new ITextRenderer();
        String html = template.execute(bindings);
        renderer.setDocumentFromString(html);

        renderer.getSharedContext().setReplacedElementFactory(new ReplacedElementFactoryImpl(renderer.getOutputDevice()));

        renderer.layout();
        renderer.createPDF(out);
        renderer.finishPDF();
    }
}
