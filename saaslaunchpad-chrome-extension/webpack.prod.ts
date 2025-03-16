import {Configuration} from 'webpack'
import merge from 'webpack-merge'
import config from './webpack.common'


const merged: Configuration = merge<Configuration>(config, {
    mode: 'production',
    devtool: 'source-map',
})

export default merged
