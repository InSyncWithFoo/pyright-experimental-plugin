<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog

This page documents user-facing changes.
For code changes, see [`CHANGELOG_CODE.md`][_-1].


  [_-1]: ./CHANGELOG_CODE.md


## [Unreleased]

### Added

* The icon for `pyrightconfig.json` is now replaced with Pyright's logo.


## [1.0.1] - 2024-07-14

### Fixed

* The active interpreter is now detected correcly on non-PyCharm IDEs.
* The "autocomplete parentheses" monkeypatch now works correctly
  with auto-import (and other `textEdit`-based) completions.


## [1.0.0] - 2024-06-23

### Added

* The list of recognized file extensions can now be configured
  and includes `py` and `pyi` by default.
  Previously, only `.py` files are recognized.
* PyCharm has a bug leading to extra quotes being added
  when autocompleting `Literal` strings and `TypedDict` keys.
  A monkeypatch has been added to mitigate the issues.
  It can be disabled using an UI option.
* The diagnostic mode is now configurable.
  Previously, no corresponding value is sent to the language server.
* The server can now be asked not to
  automatically add common search paths (e.g. `src`).
  Previously, they were always added by default.

### Changed

* The "Auto-import" completion item detail monkeypatch
  introduced in the previous version can now be disabled.
* The support version cap is removed.
  The plugin is now declared as compatible with
  all versions from 2024.1 and later.


## [0.5.0] - 2024-05-27

### Added

* Functions, methods and constructors
  can now be autocompleted with parentheses.
* Auto-import completions now use the source as the supporting detail.
  Previously the detail was "Auto-import" or a similar localized message.
* Diagnostics support can now be disabled separately.
  Previously the only way to do so would be to disable
  the entire language server by disabling the corresponding inspection.
* The server can now be restarted automatically on configuration change
  if the corresponding setting is enabled.
  Previously it needed to be restarted manually.
* UI components are slightly changed.


## [0.4.0] - 2024-05-15

### Added

* Diagnostics can now be suppressed using quick fixes.

### Changed

* An informational hint will now be given if
  the given executable file's name is not a known name.
* Application-level settings can now be exported using the
  <i>Export Settings...</i> action.
  Previously they would be omitted when exporting
  and can only be found in the IDE's configuration directory.


## [0.3.0] - 2024-04-24

### Added

* Go-to-definition support is now configurable and disabled by default.
  Previously this was always enabled.
* The log level is now configurable.
* Source roots can now be passed to the language server
  as "workspace folders" in lieu of the project's base directories
  (which has been and still is the default option).
  This is useful if you have your `pyproject.toml` or `pyrightconfig.json`
  files placed somewhere other than the project root (for example, a monorepo).
* "Unnecessary" and "Deprecated" hints, which are displayed as
  faded-out and striken-through in the IDE, can now be disabled.
  Previously this was always enabled.
* The language server can now be configured to not offer
  auto-import completions. Previously this was always enabled.
* UI components are slightly changed.


## [0.2.0] - 2024-04-16

### Added

* Highlight severity levels are now configurable
  using the options provided in the <i>Inspection</i> panel.
  The inspection is renamed to "Pyright language server diagnostics".
* Hover support can now be disabled. Previously this was always enabled.
* Completion support is now configurable and disabled by default.
  Previously this was always enabled.

### Changed

* Rule codes, if given, are now appended to messages.
  They can be made links if the corresponding option is enabled.
* UI messages are slightly changed.

### Fixed

* The plugin is no longer technically installable on PyCharm Community.


## [0.1.0] - 2024-04-10

### Fixed

* Earlier versions could not resolve available packages.
  This has been fixed.

### Removed

* Versions lower than 2024.1 are no longer supported.


## [0.1.0-poc.4] - 2024-04-07

### Added

* Global options "Use editor font" and "Prefix tooltips" are added.

### Fixed

* v0.1.0-poc.3 has a bug causing the configurations to be always unsavable.
  This has been fixed.


## [0.1.0-poc.3] - 2024-03-31

### Added

* UI hints are added to ease the process of setting paths.


## [0.1.0-poc.2] - 2024-03-24

### Added

* The plugin will now suggest setting an executable
  for the current project if one can be found locally.
  To turn off suggestion for a project, uncheck
  the corresponding option in the project configuration panel.
  The notification group used is named "<i>Pyright LS notifications</i>".

### Changed

* The project is renamed from `pyright-experimental-plugin` to
  `pyright-langserver` and the marketplace name of the plugin is now
  "<i>Pyright Language Server</i>" instead of "<i>Pyright (Experimental)</i>".
* UI components and messages are slightly changed.


## [0.1.0-poc.1] - 2024-03-17

### Added

* Project initialized.


  [Unreleased]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v1.0.1..HEAD
  [1.0.1]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v1.0.0..v1.0.1
  [1.0.0]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.5.0..v1.0.0
  [0.5.0]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.4.0..v0.5.0
  [0.4.0]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.3.0..v0.4.0
  [0.3.0]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.2.0..v0.3.0
  [0.2.0]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.1.0..v0.2.0
  [0.1.0]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.1.0-poc.4..v0.1.0
  [0.1.0-poc.4]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.1.0-poc.3..v0.1.0-poc.4
  [0.1.0-poc.3]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.1.0-poc.2..v0.1.0-poc.3
  [0.1.0-poc.2]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/compare/v0.1.0-poc.1..v0.1.0-poc.2
  [0.1.0-poc.1]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/commits
