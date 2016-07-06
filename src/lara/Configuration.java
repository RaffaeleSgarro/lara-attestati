package lara;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Map;

public class Configuration {

    private String dataFilename;
    private String dataEncoding;
    private String destination;
    private String pdfFilePrefix;
    private String templateFilename;
    private String templateEncoding;
    private Map<String, String> columns;

    public String getDataFilename() {
        return dataFilename;
    }

    public void setDataFilename(String dataFilename) {
        this.dataFilename = dataFilename;
    }

    public String getDataEncoding() {
        return dataEncoding;
    }

    public void setDataEncoding(String dataEncoding) {
        this.dataEncoding = dataEncoding;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPdfFilePrefix() {
        return pdfFilePrefix;
    }

    public void setPdfFilePrefix(String pdfFilePrefix) {
        this.pdfFilePrefix = pdfFilePrefix;
    }

    public String getTemplateFilename() {
        return templateFilename;
    }

    public void setTemplateFilename(String templateFilename) {
        this.templateFilename = templateFilename;
    }

    public String getTemplateEncoding() {
        return templateEncoding;
    }

    public void setTemplateEncoding(String templateEncoding) {
        this.templateEncoding = templateEncoding;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, String> columns) {
        this.columns = columns;
    }

    private File getTemplateFile() {
        return new File(templateFilename);
    }

    public Reader openDataReader() throws IOException {
        return new InputStreamReader(new FileInputStream(dataFilename), dataEncoding);
    }

    public String getTemplateText() throws IOException {
        return FileUtils.readFileToString(getTemplateFile(), templateEncoding);
    }

    public String getMappedColumnName(String columnName) {
        return columns.containsKey(columnName) ? columns.get(columnName) : columnName;
    }

    public String getTemplateBaseURL() throws MalformedURLException {
        return getTemplateFile().getParentFile().toURI().toURL().toExternalForm();
    }
}
