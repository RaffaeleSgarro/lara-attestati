package lara;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Main {
    public static void main(String... args) throws Exception {
        Attestato attestato = new Attestato();
        OutputStream out = new FileOutputStream(new File("attestati", "test.pdf"));
        attestato.writePdf(out);
        out.flush();
        out.close();
    }
}
