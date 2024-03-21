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
    private readonly ProtocolModel _protocolModel;
    private readonly Tracker _tracker;

    public PluginHost(ISolution solution, Tracker tracker)
    {
        _protocolModel = solution.GetProtocolSolution().GetProtocolModel();
        _protocolModel.GetCliVersion.SetSync(GetCliVersion);
        
        _tracker = tracker;
        tracker.DotNetToolCacheChanged += OnDotNetToolCacheChanged;
        tracker.Start();
    }

    private string GetCliVersion(Lifetime lifetime, Unit unit) => _protocolModel.CliVersion.Value;

    private void OnDotNetToolCacheChanged(DotNetToolCache cache)
    {
        var localTool = cache.ToolLocalCache.GetAllLocalTools().FirstOrDefault(t => t.PackageId == Constants.ODataCliPackageId);
        if (localTool is not null)
        {
            _protocolModel.CliVersion.Value = $"Local, {localTool.Version}";
            return;
        }

        var tool = cache.ToolGlobalCache.GetGlobalTool(Constants.ODataCliPackageId)?.FirstOrDefault();
        if (tool is not null)
        {
            _protocolModel.CliVersion.Value = $"Global, {tool.Version}";
            return;
        }

        _protocolModel.CliVersion.Value = "Not installed";
    }

    public void Dispose()
    {
        if (_tracker is not null)
        {
            _tracker.DotNetToolCacheChanged -= OnDotNetToolCacheChanged;
        }
    }
}