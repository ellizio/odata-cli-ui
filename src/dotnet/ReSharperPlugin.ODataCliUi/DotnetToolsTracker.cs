using JetBrains.Application.Parts;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.NuGet.DotNetTools;
using JetBrains.Threading;
using System;

namespace ReSharperPlugin.ODataCliUi;

[SolutionComponent(InstantiationEx.LegacyDefault)]
public class DotnetToolsTracker
{
    private readonly JetFastSemiReenterableRWLock _lock = new();
    private readonly Lifetime _lifetime;
    private readonly SolutionDotnetToolsTracker _dotnetToolsTracker;

    public event Action<DotNetToolCache> DotnetToolsCacheChanged;

    public DotnetToolsTracker(Lifetime lifetime, SolutionDotnetToolsTracker dotnetToolsTracker)
    {
        _lifetime = lifetime;
        _dotnetToolsTracker = dotnetToolsTracker;
    }
    
    public void Start()
    {
        _dotnetToolsTracker.DotNetToolCache.Change.Advise(_lifetime, args =>
        {
            if (!args.HasNew || args.New is null)
                return;

            using var _ = _lock.UsingWriteLock();
            var cache = args.New;
            DotnetToolsCacheChanged?.Invoke(cache);
        });
    }
}