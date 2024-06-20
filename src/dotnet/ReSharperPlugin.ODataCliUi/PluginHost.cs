using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.NuGet.DotNetTools;
using JetBrains.Rd.Tasks;
using JetBrains.ReSharper.Feature.Services.Protocol;
using JetBrains.ReSharper.Resources.Shell;
using JetBrains.Rider.Model;
using System;
using System.Linq;
using System.Threading.Tasks;

namespace ReSharperPlugin.ODataCliUi;

[SolutionComponent]
public sealed class PluginHost : IDisposable
{
    private readonly ISolution _solution;
    private readonly DotnetToolsTracker _dotnetToolsTracker;

    private DotnetToolDefinition _odataCliTool;

    public PluginHost(ISolution solution, DotnetToolsTracker dotnetToolsTracker)
    {
        _solution = solution;
        var protocolModel = solution.GetProtocolSolution().GetProtocolModel();
        protocolModel.GetODataCliTool.SetSync(GetODataCliTool);
        protocolModel.AddEmbeddedResource.SetVoidAsync(AddEmbeddedResourceAsync);

        _dotnetToolsTracker = dotnetToolsTracker;
        dotnetToolsTracker.DotnetToolsCacheChanged += OnDotnetToolsCacheChanged;
        dotnetToolsTracker.Start();
    }
    
    private Task AddEmbeddedResourceAsync(Lifetime lifetime, EmbeddedResourceDefinition definition)
    {
        IProject project;
        using (ReadLockCookie.Create())
            project = _solution.GetProjectByName(definition.ProjectName);
        if (project is null)
            return Task.CompletedTask;
        
        var modifier = new ProjectModifier(project);
        modifier.AddEmbeddedResource(definition.Include);

        return Task.CompletedTask;
    }

    private DotnetToolDefinition GetODataCliTool(Lifetime lifetime, Unit unit) => _odataCliTool;

    private void OnDotnetToolsCacheChanged(DotNetToolCache cache)
    {
        var tool = cache.ToolGlobalCache.GetGlobalTool(Constants.MicrosoftODataCliPackageId)?.FirstOrDefault();
        _odataCliTool = tool is null
            ? _odataCliTool = new DotnetToolDefinition(false, null)
            : _odataCliTool = new DotnetToolDefinition(true, new DotnetToolVersionDefinition(tool.Version.Major, tool.Version.Minor, tool.Version.Patch));
    }

    public void Dispose()
    {
        if (_dotnetToolsTracker is not null)
        {
            _dotnetToolsTracker.DotnetToolsCacheChanged -= OnDotnetToolsCacheChanged;
        }
    }
}