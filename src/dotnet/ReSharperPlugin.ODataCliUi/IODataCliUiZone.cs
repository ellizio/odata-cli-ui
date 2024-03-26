using JetBrains.Application.BuildScript.Application.Zones;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.NuGet;

namespace ReSharperPlugin.ODataCliUi
{
    [ZoneDefinition]
    // [ZoneDefinitionConfigurableFeature("Title", "Description", IsInProductSection: false)]
    public interface IODataCliUiZone : IZone,
        IRequire<IProjectModelZone>,
        IRequire<INuGetZone>;
}
