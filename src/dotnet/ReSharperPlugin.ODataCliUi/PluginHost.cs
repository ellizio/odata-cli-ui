using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.NuGet.DotNetTools;
using JetBrains.Rd.Tasks;
using JetBrains.ReSharper.Feature.Services.Protocol;
using JetBrains.Rider.Model;
using System;
using System.Linq;

namespace ReSharperPlugin.ODataCliUi;

[SolutionComponent]
public sealed class PluginHost : IDisposable
{
    private readonly Tracker _tracker;
    
    private CliTool _odataCliTool;

    public PluginHost(ISolution solution, Tracker tracker)
    {
        var protocolModel = solution.GetProtocolSolution().GetProtocolModel();
        protocolModel.GetODataCliTool.SetSync(GetODataCliTool);
        
        _tracker = tracker;
        tracker.DotNetToolCacheChanged += OnDotNetToolCacheChanged;
        tracker.Start();
    }

    private CliTool GetODataCliTool(Lifetime lifetime, Unit unit) => _odataCliTool;

    private void OnDotNetToolCacheChanged(DotNetToolCache cache)
    {
        var localTool = cache.ToolLocalCache.GetAllLocalTools().FirstOrDefault(t => t.PackageId == Constants.ODataCliPackageId);
        if (localTool is not null)
        {
            _odataCliTool = new CliTool(true, $"Local, {localTool.Version}");
            return;
        }

        var tool = cache.ToolGlobalCache.GetGlobalTool(Constants.ODataCliPackageId)?.FirstOrDefault();
        if (tool is not null)
        {
            _odataCliTool = new CliTool(true, $"Global, {tool.Version}");
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