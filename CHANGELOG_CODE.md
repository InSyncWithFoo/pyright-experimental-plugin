<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Code changelog


This page documents code changes.
For user-facing changes, see [`CHANGELOG.md`][_-1].


  [_-1]: ./CHANGELOG.md


## [Unreleased]

### Added

* [`PyrightLSDescriptor`][40-1] now logs all configurations on initialization.
  (501b9cea)
* Diagnostics-related construct, including those newly added for quick fixes,
  are moved to the [`.diagnostics`][40-2] module. (HEAD)

### Changed

* [The Qodana Gradle plugin][40-3] and its corresponding action
  [@JetBrains/qodana-action][40-4] are updated to 2024.1.4.
  (647bd2d5, 26a9fcf9, b952ef10, 1ae17a00, bed1cfe6, 6bd68dfd)
* A new branch is added to [`executablePathResolvingHint()`][40-5]. (906f7abe)
* [Kotlin JVM plugin][40-6] is updated to 1.9.24. (0e28e9ae)
* `RoamingType.LOCAL` is used for [application-level configurations][40-7]
  instead of `RoamingType.DISABLED`. (ac6bf02a)
* The "Run Plugin" task now runs with the new UI enabled and
  the `.idea` subdirectory not hidden. (5fb15568)


  [40-1]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/501b9cea/src/main/kotlin/com/insyncwithfoo/pyrightls/server/PyrightLSDescriptor.kt
  [40-2]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/HEAD/src/main/kotlin/com/insyncwithfoo/pyrightls/server/diagnostics
  [40-3]: https://plugins.gradle.org/plugin/org.jetbrains.qodana
  [40-4]: https://github.com/JetBrains/qodana-action
  [40-5]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/906f7abe/src/main/kotlin/com/insyncwithfoo/pyright/configuration/PathResolvingHint.kt
  [40-6]: https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
  [40-7]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/ac6bf02a/src/main/kotlin/com/insyncwithfoo/pyrightls/configuration/application/ConfigurationService.kt


## [0.3.0] - 2024-04-24

### Added

* [`PyrightLSDescriptor`][30-1]'s `.lspGoToDefinitionSupport`
  is overridden to respect the corresponding user-provided values. (2a2a4f29)
* The "Log level" global configuration, which corresponds to
  Pyright's `python.analysis.logLevel` option, is added.
  [`Settings.kt`][30-2] is rewritten.
  (1c937fb6, d4814c3a)
* Project option "Workspace folders" is added. (5e35f935)
* [Message key names][30-3] are slightly changed. (6b6b2a56)
* Global option "Disable tagged hints" is added. (cd9dc597)
* Global option "Auto import completions" is added. (d4814c3a)


  [30-1]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/2a2a4f29/src/main/kotlin/com/insyncwithfoo/pyrightls/server/PyrightLSDescriptor.kt
  [30-2]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/d4814c3a/src/main/kotlin/com/insyncwithfoo/pyrightls/server/Settings.kt
  [30-3]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/6b6b2a56/src/main/resources/messages/pyrightls.properties


## [0.2.0] - 2024-04-16

### Added

* Three new properties are added to [`PyrightLSInspection`][20-1]
  to allow configuring highlight severity levels. (d36526b8)
* Global option "Link error codes" is added. (e348bf9f)
* [`PyrightLSDescriptor`][20-2]'s `.lspHoverSupport` and `lspCompletionSupport`
  are overridden to respect the corresponding user-provided values.
  (6c58ffe9, f449161e)

### Changed

* [`DiagnosticsSupport.getMessage()`][20-3] is overridden
  to return `Diagnostic.suffixedMessage`, which adds error codes to messages.
  (4ca00c40)
* [@gradle/actions/wrapper-validation][20-4] is updated to 3.3.0. (7c80f8d6)

### Fixed

* `com.intellij.modules.ultimate`, which is only available in paid IDEs,
  is now a required dependency. This effectively makes the plugin
  no longer installable on/compatible with PyCharm Community. (57a1ee98)


  [20-1]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/e348bf9f/src/main/kotlin/com/insyncwithfoo/pyrightls/PyrightLSInspection.kt
  [20-2]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/f449161e/src/main/kotlin/com/insyncwithfoo/pyrightls/server/PyrightLSDescriptor.kt
  [20-3]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/4ca00c40/src/main/kotlin/com/insyncwithfoo/pyrightls/server/DiagnosticsSupport.kt
  [20-4]: https://github.com/gradle/actions/wrapper-validation


## [0.1.0] - 2024-04-10

### Added

* [`Settings.kt`][10-1] is added. (fce7406e)
* [`LICENSE_TEMPLATE.txt`][10-2] is added. (fce7406e)

### Changed

* `released.yaml` is renamed to [`publish.yml`][10-3].
  Steps overlapping with other jobs are removed. (fce7406e)
* [@gradle/wrapper-validation-action][10-4] is updated to 2.1.3. (1ca18f6d)

### Fixed

* The project interpreter path is now correctly passed to Pyright.
  (ba0947ba, 7a1e3b11)
* [`PyrightLSConfigurable`][10-5]'s `apply()`, `isModified()` and `reset()`
  now call the corresponding methods of `panel`.
  This fixes a(nother) regression introduced in v0.1.0-poc.3
  which has been causing the configuration panels to be unresetable. (fce7406e)

### Removed

* Support for versions lower than 2024.1 is discontinued. (92b029a5, d3fd01bf)


  [10-1]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/7a1e3b11/src/main/kotlin/com/insyncwithfoo/pyrightls/server/Settings.kt
  [10-2]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/fce7406e/LICENSE_TEMPLATE.txt
  [10-3]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/fce7406e/.github/workflows/publish.yaml
  [10-4]: https://github.com/gradle/wrapper-validation-action
  [10-5]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/fce7406e/src/main/kotlin/com/insyncwithfoo/pyrightls/configuration/PyrightLSConfigurable.kt


## [0.1.0-poc.4] - 2024-04-07

### Added

* Global options "Use editor font" and "Prefix tooltips" are added. (9ebda455)
* Tests are added. (0ba420d7)

### Changed

* [`pluginIcon.svg`][4-1] is resized to 40 by 40 to comply with
  [the Approval Guidelines][4-2]. (96d974b5)
* [`README.md`][4-3] is rewritten to alter the plugin description. (801c9546)
* Bug reports and feature requests now have automatic assignees. (801c9546)
* [`build.yaml`][4-4] now runs tests on all three platforms.
  (801c9546, 616e6363)
* Publicly-facing classes [`SupportProvider`][4-5] and [`Descriptor`][4-6]
  are renamed. (7c96ada8)

### Fixed

* A `panel.apply()` call is added to
  [`PyrightLSConfigurable.isModified()`][4-7].
  This ensures that the state of panel is synchronized
  before being compared with the original state,
  which was not the case in v0.1.0-poc.3. (9ebda455)


  [4-1]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/96d974b5/src/main/resources/META-INF/pluginIcon.svg
  [4-2]: https://plugins.jetbrains.com/legal/approval-guidelines
  [4-3]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/801c9546/README.md
  [4-4]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/616e6363/.github/workflows/build.yaml
  [4-5]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/7c96ada8/src/main/kotlin/com/insyncwithfoo/pyrightls/server/PyrightLSSupportProvider.kt
  [4-6]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/7c96ada8/src/main/kotlin/com/insyncwithfoo/pyrightls/server/PyrightLSDescriptor.kt
  [4-7]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/9ebda455/src/main/kotlin/com/insyncwithfoo/pyrightls/configuration/PyrightLSConfigurable.kt


## [0.1.0-poc.3] - 2024-03-31

### Added

* [`PathResolvingHint.kt`][3-1] is added. (a31ced27)

### Changed

* The [`build.yaml`][3-2] workflow now:
  * Edits old releases when the changelogs are changed, and
  * Uploads corresponding artifacts as new drafts are created.
  
  The two helper Python scripts are added under [`.scripts`][3-3].
  (ca944192)

* Plugin verifier results are now always uploaded. (ca944192)
* [The Qodana Gradle plugin][3-4] and its corresponding action
  [@JetBrains/qodana-action][3-5] are updated to 2023.3.2. (474b797c)
* [The IntelliJ Platform Gradle plugin][3-6] is updated to 1.17.3. (5546f8fa)
* UI-related code is rewritten to use [Kotlin UI DSL][3-7]. (a31ced27)
* All APIs are now either internal or private.
  * Some of them no longer have the prefix `PyrightLS` in their names.
  
  (a31ced27)

### Changed

* The `configuration.common` module is removed
  in favor of [`PyrightLSConfigurable.kt`][3-8]. (a31ced27)


  [3-1]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/a31ced27/src/main/kotlin/com/insyncwithfoo/pyrightls/configuration/PathResolvingHint.kt
  [3-2]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/ca944192/.github/workflows/build.yaml
  [3-3]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/ca944192/.scripts/
  [3-4]: https://plugins.gradle.org/plugin/org.jetbrains.qodana
  [3-5]: https://github.com/JetBrains/qodana-action
  [3-6]: https://github.com/JetBrains/intellij-platform-gradle-plugin
  [3-7]: https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl-version-2.html
  [3-8]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/blob/a31ced27/src/main/kotlin/com/insyncwithfoo/pyrightls/configuration/PyrightLSConfigurable.kt


## [0.1.0-poc.2] - 2024-03-24

### Added

* Project option "Auto-suggest executable" is added. (6dffdaa6)
* [Icons][2-1] are added. (6dffdaa6)
* Messages are merged into one single bundle. (6dffdaa6)
* The "Always use global" option is moved to
  the second column of the second row. (6dffdaa6)
* `PyrightLSAllConfigurations` is added. (6dffdaa6)

### Changed

* Project is renamed. (f821150f)
* Configuration constructs are now marked internal. (6dffdaa6)
* [`CHANGELOG.md`][2-2] is rewritten and
  `CHANGELOG_CODE.md` is added. (6dffdaa6)
* Gradle is updated to 8.7. (4ad34a46)


  [2-1]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/tree/6dffdaa6/src/main/resources/icons
  [2-2]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/tree/6dffdaa6/CHANGELOG.md


## [0.1.0-poc.1] - 2024-03-17

### Added

* Project initialized.


  [Unreleased]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.3.0..HEAD
  [0.3.0]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.2.0..v0.3.0
  [0.2.0]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.1.0..v0.2.0
  [0.1.0]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.1.0-poc.4..v0.1.0
  [0.1.0-poc.4]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.1.0-poc.3..v0.1.0-poc.4
  [0.1.0-poc.3]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.1.0-poc.2..v0.1.0-poc.3
  [0.1.0-poc.2]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.1.0-poc.1..v0.1.0-poc.2
  [0.1.0-poc.1]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/commits
