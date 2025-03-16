import {Configuration} from 'webpack'
import merge from 'webpack-merge'
import config from './webpack.common'
import { webpack } from 'webpack'

const merged: Configuration = merge<Configuration>(config, {
    mode: 'development',
    devtool: 'inline-source-map',
})

export default merged