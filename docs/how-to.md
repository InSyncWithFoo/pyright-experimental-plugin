## How to install the Pyright executables

Choose one that works for you:

```shell
$ pip install pyright
$ uv pip install pyright
$ npm install pyright
$ yarn add pyright
$ pnpm install pyright
$ bun install pyright
$ brew install pyright
```

See also [Pyright's official installation guide][1].


## How to restart the language server

In the status bar, find the cell that has
"Pyright" next to a pair of braces.
Click it, then click the loop icon.

![](./assets/restart-server-button.png)


## How to enable language server logging

Add the following line to the <b>Debug Log Settings</b> panel
(<b>Help</b> | <b>Diagnostic Tools</b>):

```text
#com.intellij.platform.lsp
```


  [1]: https://microsoft.github.io/pyright/#/installation?id=command-line
