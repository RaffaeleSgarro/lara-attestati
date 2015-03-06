package lara;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /**
     *
     * Invoke like this
     *
     * java lara.Main data.csv attestati
     *
     * @param args
     * @throws Exception
     */
    public static void main(String... args) throws Exception {

        if (args.length != 2) {
            throw new RuntimeException("Usage: java lara.Main 'attestati/data.csv' 'attestati'");
        }

        InputStream in = Attestato.class.getResourceAsStream("/attestato.html");
        String html = IOUtils.toString(in, "UTF-8");
        in.close();

        Template template = Mustache.compiler()
                .defaultValue("{{{name}}}")
                .compile(html);

        Reader data = new InputStreamReader(new FileInputStream(args[0]), "UTF-8");

        CSVParser parser = CSVFormat.DEFAULT
                .withHeader()
                .parse(data);

        log.info("Read data from CSV at " + args[0]);

        File baseDir = new File(args[1]);

        if (baseDir.exists())
          throw new RuntimeException("Directory " + baseDir + " already exists!");

        FileUtils.forceMkdir(baseDir);

        log.info("Workind directory is " + baseDir);

        Set<String> processed = new HashSet<>();

        for (CSVRecord record : parser.getRecords()) {
            String id = record.get("id");
            String lastName = record.get("cognome").toUpperCase();
            String firstName = record.get("nome").toUpperCase();

            if (processed.contains(id))
                continue;

            processed.add(id);

            Attestato attestato = new Attestato(template);
            attestato.bind("name", record.get("cognome") + " " + record.get("nome"));
            attestato.bind("job_type", record.get("qualifica"));
            attestato.bind("birth_place", record.get("comune di nascita"));
            attestato.bind("birth_date", record.get("data di nascita"));
            attestato.bind("odm", record.get("OdM"));
            attestato.bind("n", record.get("num. OdM"));
            attestato.bind("finish_date", record.get("data completamento"));

            File file = new File(baseDir, "99499_Attestato " + lastName + " " + firstName + " " + id + ".pdf");

            if (file.exists())
                throw new RuntimeException("File already exists: " + file);

            OutputStream out = new FileOutputStream(file);
            attestato.writePdf(out);
            out.flush();
            out.close();

            log.info("Generated " + file);
        }

        data.close();

    }

}
