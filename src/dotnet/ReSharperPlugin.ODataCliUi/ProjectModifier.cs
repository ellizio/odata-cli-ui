using JetBrains.Application.Components;
using JetBrains.Platform.MsBuildHost.ProjectModel;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.ProjectsHost;
using JetBrains.ProjectModel.ProjectsHost.MsBuild;
using JetBrains.ProjectModel.ProjectsHost.SolutionHost;
using JetBrains.ProjectModel.ProjectsHost.SolutionHost.Impl;

namespace ReSharperPlugin.ODataCliUi;

public class ProjectModifier
{
    private const string EmbeddedResource = nameof(EmbeddedResource);
    
    private readonly IProjectMark _mark;
    private readonly MsBuildProjectHost _host;

    public ProjectModifier(IProject project)
    {
        _mark = project.GetProjectMark();

        var solutionHost = project.GetSolution().ProjectsHostContainer().GetComponent<SolutionHost>();
        _host = solutionHost.GetProjectHost(_mark!) as MsBuildProjectHost;
    }
    
    public void AddEmbeddedResource(string include)
    {
        _host.EditProject(_mark, session => session.AddItem(_mark, EmbeddedResource, include));
    }
}