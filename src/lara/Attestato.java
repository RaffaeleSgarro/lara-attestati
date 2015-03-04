package lara;

import com.samskivert.mustache.Template;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class Attestato {

    private final Template template;
    private final Map<String, Object> bindings = new LinkedHashMap<>();

    public Attestato(Template template) {
        this.template = template;
    }

    public void writePdf(OutputStream out) throws Exception{

        ITextRenderer renderer = new ITextRenderer();
        String html = template.execute(bindings);
        renderer.setDocumentFromString(html);

        renderer.getSharedContext().setReplacedElementFactory(new ReplacedElementFactoryImpl(renderer.getOutputDevice()));

        renderer.layout();
        renderer.createPDF(out);
        renderer.finishPDF();
    }

    public void bind(String key, String value) {
        bindings.put(key, value);
    }
}
