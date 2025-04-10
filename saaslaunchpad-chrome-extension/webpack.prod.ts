import webpack from "webpack"
import merge from "webpack-merge"
import config from "./webpack.common"

const merged: webpack.Configuration = merge(config, {
  mode: "production",
  devtool: "source-map",
})

export default merged
