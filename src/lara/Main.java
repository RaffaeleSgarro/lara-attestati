package lara;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class Main {

    private static final Logger log = LoggerFactory.getLogger("attestati");

    private final Configuration configuration;

    private CSVParser parser;
    private CSVRecord csv;

    public Main(Configuration configuration) {
        this.configuration = configuration;
    }

    /*
     * Configuration is loaded from conf.yaml and possibly overridden via the first
     * command line parameter.
     *
     * Configuration file encoding is platform default.
     */
    public static void main(String... args) throws Exception {

        String confFilename = args.length >= 1 ? args[0] : "conf.yaml";
        String confFileEncoding = args.length >= 2 ? args[1] : Charset.defaultCharset().name();

        log.info("Loading configuration from file {}, encoding {}", confFilename, confFileEncoding);

        YamlReader yaml = new YamlReader(new InputStreamReader(new FileInputStream(confFilename), confFileEncoding));
        Configuration configuration = yaml.read(Configuration.class);

        Main main = new Main(configuration);
        main.start();

        log.info("Program completed!");
    }

    public void start() throws Exception {

        String baseURL = configuration.getTemplateBaseURL();

        Template template = Mustache.compiler()
                .defaultValue("{{{name}}}")
                .compile(configuration.getTemplateText());

        log.info("Read data from CSV at " + configuration.getDataFilename());

        File destination = new File(configuration.getDestination());

        if (destination.exists())
            throw new RuntimeException("Directory " + destination
                    + " already exists and I'm too coward to overwrite user files!");

        FileUtils.forceMkdir(destination);

        log.info("Working directory is " + destination);

        Set<String> processed = new HashSet<>();

        parser = CSVFormat.DEFAULT
                .withHeader()
                .withIgnoreHeaderCase()
                .parse(configuration.openDataReader());

        for (CSVRecord _record : parser.getRecords()) {
            this.csv = _record;
            try {
                String id = field("id");
                String lastName = field("lastName");
                String firstName = field("firstName");

                if (processed.contains(id))
                    continue;

                processed.add(id);

                Attestato attestato = new Attestato(baseURL, template);
                attestato.bind("name",
                          beautify(firstName)
                        + " "
                        + beautify(lastName));

                if (parser.getHeaderMap().containsKey("qualifica")) {
                    attestato.bind("job_type", field("jobType"));
                }

                attestato.bind("birth_place", StringUtils.upperCase(field("birthPlace")));
                attestato.bind("birth_date", field("birthDate"));
                attestato.bind("odm", StringUtils.capitalize(field("odm")));
                attestato.bind("n", field("n"));
                attestato.bind("finish_date", field("finishDate"));

                File file = new File(destination, configuration.getPdfFilePrefix()
                        + beautify(lastName)
                        + " "
                        + beautify(firstName)
                        + " "
                        + id
                        + ".pdf");

                if (file.exists())
                    throw new RuntimeException("File already exists: " + file);

                OutputStream out = new FileOutputStream(file);
                attestato.writePdf(out);
                out.flush();
                out.close();

                log.info("Generated " + file);
            } catch (Exception e) {
                throw new RuntimeException("Error at line " + parser.getCurrentLineNumber(), e);
            }
        }

        parser.close();
    }

    private String field(String columnName) {
        String mappedColumnName = configuration.getMappedColumnName(columnName);

        if (csv.isMapped(mappedColumnName)) {
            return csv.get(mappedColumnName);
        } else {
            return null;
        }
    }
    
    private String beautify(String name) {
        return WordUtils.capitalize(StringUtils.lowerCase(name));
    }

}
