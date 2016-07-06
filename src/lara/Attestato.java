package lara;

import com.samskivert.mustache.Template;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.swing.NaiveUserAgent;

import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class Attestato {

    private final String baseURL;
    private final Template template;
    private final Map<String, Object> bindings = new LinkedHashMap<>();

    public Attestato(String baseURL, Template template) {
        this.baseURL = baseURL;
        this.template = template;
    }

    public void writePdf(OutputStream out) throws Exception{

        ITextRenderer renderer = new ITextRenderer();
        String html = template.execute(bindings);
        renderer.setDocumentFromString(html);

        renderer.getSharedContext().setBaseURL(baseURL);
        NaiveUserAgent uac = new NaiveUserAgent();
        uac.setBaseURL(baseURL);

        renderer.getSharedContext().setUserAgentCallback(uac);
        renderer.getSharedContext().setReplacedElementFactory(new ReplacedElementFactoryImpl(renderer.getOutputDevice()));

        renderer.layout();
        renderer.createPDF(out);
        renderer.finishPDF();
    }

    public void bind(String key, String value) {
        bindings.put(key, value);
    }
}
