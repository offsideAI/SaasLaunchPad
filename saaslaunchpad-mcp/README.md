## SaaSLaunchPad MCP

### Steps for saaslaunchpad-mcp

```
npm init -y
```


### Steps for deployment

```
npm run build

claude mcp add saaslaunchpad "node dist/main.js"

```

* Runing the above deployment looks as follows - 

```
╰─❯ claude mcp add saaslaunchpad "node dist/main.js"
Added stdio MCP server saaslaunchpad with command: node dist/main.js  to project config
```