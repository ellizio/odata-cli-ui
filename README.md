# <p align="center"> OData CLI UI </p>

<p align="center"> Rider plugin that provides UI to generate OData Reference using OData CLI </p>

<p align="center">
  <a href="https://plugins.jetbrains.com/plugin/24117-odata-cli-ui" target="_blank">
    <img src="/img/marketplace.png" alt="Marketplace link">
  </a>
</p>

---

## Installation

1. Install plugin from [Marketplace](https://plugins.jetbrains.com/plugin/24117-odata-cli-ui) or download from [Releases page](https://github.com/ellizio/rider--plugin--odata-cli-ui/releases) and install manually
2. Install the latest version of the OData CLI tool with the command `dotnet tool install -g Microsoft.OData.Cli`

## Usage

![](/img/step1.png)
![](/img/step2.png)
![](/img/step3.png)
![](/img/step4.png)

## ⚠️ Known Restrictions

1. ✅ Waiting for release with fix\
Output metadata .xml file is always named `OData ServiceCsdl.xml` which will throw a runtime exception. See more [https://github.com/OData/ODataConnectedService/issues/384](https://github.com/OData/ODataConnectedService/issues/384)\
There is a workaround:\
`a.` Rename `OData ServiceCsdl.xml` to `<Service name>Csdl.xml`, where `<Service name>` is value from OData CLI UI dialog\
`b.` Adjust embedded resource path in `.csproj` file\
`c.` Find `GeneratedEdmModel.filePath` constant in `Reference.cs` and change value from `OData ServiceCsdl.xml` to `<Service name>Csdl.xml`, where `<Service name>` is value from OData CLI UI dialog

## Additional References

- [Changelog](https://github.com/ellizio/rider--plugin--odata-cli-ui/blob/master/CHANGELOG.md)
- [OData CLI](https://learn.microsoft.com/en-us/odata/odatacli/getting-started)
