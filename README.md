# Pyright Language Server for PyCharm Professional

[![Build](https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/actions/workflows/build.yaml/badge.svg)][5]
[![Docs](https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/actions/workflows/docs.yaml/badge.svg)][6]
[![Version](https://img.shields.io/jetbrains/plugin/v/24146)][7]
[![Rating](https://img.shields.io/jetbrains/plugin/r/rating/24146)][8]
[![Downloads](https://img.shields.io/jetbrains/plugin/d/24146)][9]

![](./.github/readme/demo-1.png)

<!-- Plugin description -->
Pyright language server integration for PyCharm Professional.

This plugin acts as a bridge between [the Pyright language server][1]
and [the experimental LSP API][2] added in PyCharm Professional 2023.2
to give you diagnostics about your code as you edit your Python files.

Note: If you use PyCharm Community,
install [the <i>Pyright</i> plugin][3] instead.


## Usage

Go to <b>Settings</b> | <b>Tools</b> |
<b>Pyright LS</b> | <b>Global</b> / <b>Project</b> and
set the path to your Pyright language server executable.
It is typically named `pyright-langserver`/`pyright-python-langserver`.

(Not sure what this means? See [the docs][4] for more information.)

You might need to reopen your files or restart the IDE
for the files to be recognized by the language server.


## Logging

You are strongly encouraged to enable logging.
This will allow corresponding logs to be recorded in `idea.log`
for further analysis should a problem arises.

Add the following line to the <b>Help</b> | <b>Diagnostic Tools</b> |
<b>Debug Log Settings</b> panel:

```text
#com.intellij.platform.lsp
```


  [1]: https://github.com/microsoft/pyright
  [2]: https://plugins.jetbrains.com/docs/intellij/language-server-protocol.html
  [3]: https://github.com/InSyncWithFoo/pyright-for-pycharm
  [4]: https://insyncwithfoo.github.io/pyright-langserver-for-pycharm/configurations/#executable
<!-- Plugin description end -->


## Installation

This plugin [is available][8] on the Marketplace.
You can also download the ZIP files manually from [the <i>Releases</i> tab][10],
[the `build` branch][11] or [the <i>Actions</i> tab][12]
and follow the instructions described [here][13].

Currently supported versions:
2024.1 (build 241.14494.241) and later.


## Credits

Most of the code is derived from [@koxudaxi/ruff-pycharm-plugin][14].
It is such a fortune that that plugin does almost the same thing
and is also written in Kotlin, and hence easily understandable.

The SVG and PNG logos are derived from [the README image][15]
of the [@microsoft/pyright][1] repository,
generated using Inkscape's autotrace feature.

Some other files are based on or copied directly from
[@JetBrains/intellij-platform-plugin-template][16].


  [5]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/actions/workflows/build.yaml
  [6]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/actions/workflows/docs.yaml
  [7]: https://plugins.jetbrains.com/plugin/24146/versions
  [8]: https://plugins.jetbrains.com/plugin/24146/reviews
  [9]: https://plugins.jetbrains.com/plugin/24146
  [10]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/releases
  [11]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/tree/build
  [12]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/actions/workflows/build.yaml
  [13]: https://www.jetbrains.com/help/pycharm/managing-plugins.html#install_plugin_from_disk
  [14]: https://github.com/koxudaxi/ruff-pycharm-plugin
  [15]: https://github.com/microsoft/pyright/blob/main/docs/img/PyrightLarge.png
  [16]: https://github.com/JetBrains/intellij-platform-plugin-template
