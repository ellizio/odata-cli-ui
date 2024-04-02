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
    private readonly Tracker _tracker;

    private CliTool _odataCliTool;

    public PluginHost(ISolution solution, Tracker tracker)
    {
        _solution = solution;
        var protocolModel = solution.GetProtocolSolution().GetProtocolModel();
        protocolModel.GetODataCliTool.SetSync(GetODataCliTool);
        protocolModel.AddEmbeddedResource.SetVoidAsync(AddEmbeddedResourceAsync);

        _tracker = tracker;
        tracker.DotNetToolCacheChanged += OnDotNetToolCacheChanged;
        tracker.Start();
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

    private CliTool GetODataCliTool(Lifetime lifetime, Unit unit) => _odataCliTool;

    private void OnDotNetToolCacheChanged(DotNetToolCache cache)
    {
        var localTool = cache.ToolLocalCache.GetAllLocalTools().FirstOrDefault(t => t.PackageId == Constants.MicrosoftODataCliPackageId);
        if (localTool is not null)
        {
            _odataCliTool = new CliTool(true, $"Local, {localTool.Version}");
            return;
        }

        var globalTool = cache.ToolGlobalCache.GetGlobalTool(Constants.MicrosoftODataCliPackageId)?.FirstOrDefault();
        if (globalTool is not null)
        {
            _odataCliTool = new CliTool(true, $"Global, {globalTool.Version}");
            return;
        }

        _odataCliTool = new CliTool(false, null);
    }

    public void Dispose()
    {
        if (_tracker is not null)
        {
            _tracker.DotNetToolCacheChanged -= OnDotNetToolCacheChanged;
        }
    }
}