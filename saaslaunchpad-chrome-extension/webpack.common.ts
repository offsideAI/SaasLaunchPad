import path from "path"
import webpack from "webpack"
import CopyWebpackPlugin from "copy-webpack-plugin"

const config: webpack.Configuration = {
  entry: {
    background: "./src/background.ts",
    content: "./src/content.ts",
    popup: "./src/popup.ts",
    options: "./src/options.ts",
  },
  resolve: {
    extensions: [".ts"],
  },
  module: {
    rules: [
      {
        test: /\.ts$/,
        loader: "ts-loader",
        exclude: /node_modules/,
      },
      {
        test: /\.css$/,
        use: [
          "style-loader",
          "css-loader",
          {
            loader: "postcss-loader",
            options: {
              postcssOptions: {
                plugins: ["postcss-import", "tailwindcss"],
              },
            },
          },
        ],
      }
    ],
  },
  output: {
    filename: "[name].js",
    path: path.resolve(__dirname, "dist"),
    clean: true, // Clean the output directory before emit.
  },
  plugins: [
    new CopyWebpackPlugin({
      patterns: [{ from: "static" }, { from: "src/images", to: "images" }],
    }),
  ],
}

export default config
