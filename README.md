# Lara attestati

Project is developed with Intellij IDEA (Community Edition) 16.
Main class is `lara.Main`.

Configuration is loaded from a file `conf.yaml` in the current directory and platform encoding.
Both filename and encoding can be overridden with command line parameters:

```
java lara.Main conf/gusto.yaml CP1252
```

### Workflow

- Prepare the HTML template by copying and editing an example in `examples/html`
- Set up a YAML configuration file (again, template is available in `examples`
- Export the CSV with the template contents
- Enjoy :)
