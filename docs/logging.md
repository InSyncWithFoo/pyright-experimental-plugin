For debugging purposes, the plugins may log some informational data.
When reporting issues, always include the relevant log entries if applicable.


## Where to find the IDE log files

Open the action panel using <kbd>Ctrl</kbd> <kbd>Shift</kbd> <kbd>A</kbd>
or your corresponding shortcut, then choose <i>Show Log in Explorer</i>.
On macOS, this action is called <i>Show Log in Finder</i>.

This should open a directory containing a file called `idea.log`
and possibly other files that have the `idea.<number>.log` naming schema.
`idea.log` is the most recent log file; `idea.<number>.log`s are archived ones.

Open `idea.log` using your favourite text editor/reader.
Note that it might be up to 10 MB in size.

Alternatively, navigate to the directories documented [here][1].


## Log entries format

| Event        | Content                | Keywords to look for                           |
|--------------|------------------------|------------------------------------------------|
| Server start | Plugin configurations  | `PyrightLSDescriptor - AllConfigurations`      |
| LSP messages | Message content (JSON) | `Lsp4jServerConnector`, `PyrightLSDescriptor@` |


If language server logging [is enabled][2],
every request and response will also be logged,
potentially truncated if it is too long.


  [1]: https://www.jetbrains.com/help/pycharm/directories-used-by-the-ide-to-store-settings-caches-plugins-and-logs.html#logs-directory
  [2]: how-to.md#how-to-enable-language-server-logging
