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

1. Output metadata .xml file is always named `OData ServiceCsdl.xml` which will throw a runtime exception. See more [https://github.com/OData/ODataConnectedService/issues/384](https://github.com/OData/ODataConnectedService/issues/384)\
There is a workaround:\
`a.` Rename `OData ServiceCsdl.xml` to `<Service name>Csdl.xml`, where `<Service name>` is value from OData CLI UI dialog\
`b.` Adjust embedded resource path in `.csproj` file\
`c.` Find `GeneratedEdmModel.filePath` constant in `Reference.cs` and change value from `OData ServiceCsdl.xml` to `<Service name>Csdl.xml`, where `<Service name>` is value from OData CLI UI dialog

## Additional References

- [Changelog](https://github.com/ellizio/rider--plugin--odata-cli-ui/blob/master/CHANGELOG.md)
- [OData CLI](https://learn.microsoft.com/en-us/odata/odatacli/getting-started)
