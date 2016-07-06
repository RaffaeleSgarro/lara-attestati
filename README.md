# Lara attestati

Project is developed with Intellij IDEA (Community Edition) 16.
Main class is `lara.Main`.

Configuration is loaded from a file `conf.yaml` in the current directory and platform encoding.
Both filename and encoding can be overridden with command line parameters:

```
java -cp 'lib/*' lara.Main conf/gusto.yaml UTF8
```

### Usage

- Prepare the HTML template by copying and editing an example in `examples/html`
- Set up a YAML configuration file (again, template is available in `examples`
- Export the CSV with the template contents
- Enjoy :)

### Release

An Intellij task is provided to make distributable artifacts in `out/artifacts`

- Build > Build Artifacts > Genera-Attestati-Release
