﻿using System.Threading;
using JetBrains.Application.BuildScript.Application.Zones;
using JetBrains.ReSharper.Feature.Services;
using JetBrains.ReSharper.Psi.CSharp;
using JetBrains.ReSharper.TestFramework;
using JetBrains.TestFramework;
using JetBrains.TestFramework.Application.Zones;
using NUnit.Framework;

[assembly: Apartment(ApartmentState.STA)]

namespace ReSharperPlugin.ODataCliUi.Tests
{
    [ZoneDefinition]
    public class ODataCliUiTestEnvironmentZone : ITestsEnvZone, IRequire<PsiFeatureTestZone>, IRequire<IODataCliUiZone> { }

    [ZoneMarker]
    public class ZoneMarker : IRequire<ICodeEditingZone>, IRequire<ILanguageCSharpZone>, IRequire<ODataCliUiTestEnvironmentZone> { }

    [SetUpFixture]
    public class ODataCliUiTestsAssembly : ExtensionTestEnvironmentAssembly<ODataCliUiTestEnvironmentZone> { }
}
