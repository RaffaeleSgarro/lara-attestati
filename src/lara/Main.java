package lara;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    public static void main(String... args) throws Exception {

        InputStream in = Attestato.class.getResourceAsStream("/attestato.html");
        String html = IOUtils.toString(in, "UTF-8");
        in.close();

        Template template = Mustache.compiler()
                .defaultValue("{{{name}}}")
                .compile(html);

        File baseDir = new File("attestati");

        // if (baseDir.exists())
        //    throw new RuntimeException("Directory " + baseDir + " already exists!");

        FileUtils.forceMkdir(baseDir);

        // Loop over records
        OutputStream out = new FileOutputStream(new File(baseDir, "test.pdf"));
        Map<String, Object> bindings = new LinkedHashMap<>();

        Attestato attestato = new Attestato(template);
        attestato.writePdf(bindings, out);

        out.flush();
        out.close();
    }

}
