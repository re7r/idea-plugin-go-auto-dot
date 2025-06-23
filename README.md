# Go Auto Dot

## Auto-import with dot alias.

Go Auto Dot is an IntelliJ IDEA / GoLand plugin that lets you use <code>.</code> as an import alias  
for Go packages and remembers your choice automatically across the entire project.

## Features

- The Go language allows dot-imports to bring package functions and types directly into the current scope.
- This plugin automates that behavior — once you choose to use a dot alias, it will be applied automatically during
  auto-import, code completion, and auto-formatting in any file.
- Quickly toggle dot alias on any import statement or package name via intention actions (<kbd>Alt+Enter</kbd>):
  - **Remember dot as import alias** — sets alias to <code>.</code> for the selected package during auto-import, code completion, and auto-formatting.
  - **Forget dot alias for this package** — reverts to normal import behavior.

## Usage

First apply the **"Add dot alias import"** intention on an import statement.  
Press <kbd>Alt+Enter</kbd> again and select **"Remember dot as import alias"** to set <code>.</code>  
as the alias for the selected package during auto-imports, code completions and formatting.

To disable, use **"Forget dot alias for this package"** from the same menu.

## Settings

You can manage dot alias preferences manually under:

**File → Settings → Go → Go Auto Dot Plugin**

There you'll find a list of packages with dot aliasing enabled.  
You can manually add or remove packages as needed.

## Contributing

Contributions and issues are welcome! Please open a GitHub issue or pull request.

## License

MIT License © [Re7r](mailto:re7r@proton.me)
