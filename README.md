# OData CLI UI

Rider plugin to generate OData Reference using OData CLI

[![Rider](https://img.shields.io/jetbrains/plugin/v/RIDER_PLUGIN_ID.svg?label=Rider&colorB=0A7BBB&style=for-the-badge&logo=rider)](https://plugins.jetbrains.com/plugin/RIDER_PLUGIN_ID)

---

## Installation

1. Install plugin
2. Install the latest version of the OData CLI tool with the command `dotnet tool install -g Microsoft.OData.Cli`

## Usage

1. Open solution
2. Select `OData Reference...` action under project or Web Reference context menu\
![](/img/action.png)
3. Fill required `Service name` and `Metadata source` and other optional parameters according [documentation](https://learn.microsoft.com/en-us/odata/odatacli/getting-started#options-1)\
![](/img/dialog.png)
4. Click `OK` and wait until generation finishes
![](/img/terminal.png)

## ⚠️ Known Restrictions

TODO

## Additional References

- [Changelog](https://github.com/ellizio/rider--plugin--odata-cli-ui/blob/master/CHANGELOG.md)
- [OData CLI](https://learn.microsoft.com/en-us/odata/odatacli/getting-started)