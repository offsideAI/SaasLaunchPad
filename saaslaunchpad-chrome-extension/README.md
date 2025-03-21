# SaasLaunchPad Chrome Extension

# Prerequisites

```
╰─❯ node -v
v23.8.0

╭─ ~/repos/offsideAI/githubrepos/SaasLaunchPad main \*1 ··············································································································································· 1293 23:49:36
╰─❯ npm -v
11.2.0
```

* Create package.json

*  Run commands to install webpack

```
npm install --save-dev webpack webpack-cli webpack-merge copy-webpack-plugin
```

- Check webpack version

```
╰─❯ ./node_modules/.bin/webpack --version

  System:
    OS: macOS 15.2
    CPU: (12) arm64 Apple M2 Max
    Memory: 90.55 MB / 64.00 GB
  Binaries:
    Node: 23.8.0 - ~/.nvm/versions/node/v23.8.0/bin/node
    Yarn: 1.22.22 - ~/.nvm/versions/node/v23.8.0/bin/yarn
    npm: 11.2.0 - ~/.nvm/versions/node/v23.8.0/bin/npm
  Browsers:
    Brave Browser: 121.1.62.165
    Chrome: 134.0.6998.89
    Edge: 134.0.3124.68
    Safari: 18.2
  Packages:
    copy-webpack-plugin: ^13.0.0 => 13.0.0
    webpack: ^5.98.0 => 5.98.0
    webpack-cli: ^6.0.1 => 6.0.1
    webpack-merge: ^6.0.1 => 6.0.1
```

* Run webpack

```
./node_modules/.bin/webpack --watch --config webpack.dev.js

OR

./node_modules/.bin/webpack --watch --config webpack.dev.ts --require ts-node/register

OR

npm run watch
```

* webpack runs as below

```
./node_modules/.bin/webpack --watch --config webpack.dev.js

assets by path *.js 9.76 KiB
  asset content.js 5.4 KiB [emitted] (name: content)
  asset popup.js 2.87 KiB [emitted] (name: popup)
  asset background.js 1.48 KiB [emitted] (name: background)
asset popup.css 1.95 KiB [emitted] [from: static/popup.css] [copied]
asset manifest.json 758 bytes [emitted] [from: static/manifest.json] [copied]
asset popup.html 537 bytes [emitted] [from: static/popup.html] [copied]
./src/background.js 397 bytes [built] [code generated]
./src/content.js 1.9 KiB [built] [code generated]
./src/popup.js 926 bytes [built] [code generated]
webpack 5.98.0 compiled successfully in 59 ms

```

* Install TypeScript

```
npm install --save-dev typescript @tsconfig/recommended ts-node ts-loader @types/chrome
```

* Add tsconfig.json for TypeScript configuration

```
{
  "extends": "@tsconfig/recommended/tsconfig.json",
  "compilerOptions": {
    "target": "es2015",
    "module": "es2015",
    "sourceMap": true,
    "outDir": "./dist",
    "rootDir": "./src",
    "strict": true,
    "esModuleInterop": true,
    "lib": ["dom", "es2015"],
    "moduleResolution": "node"
  },
  "include": ["src/**/*.ts"],
  "exclude": ["node_modules"]
}
```

* Install eslint

```
npm install --save-dev eslint @eslint/js @types/eslint__js typescript-eslint
```

* Run lint checker

```
    npm run lint
```

* Add CSS framework
```
npm install --save-dev style-loader css-loader

```
