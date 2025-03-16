import path from 'path'
import webpack from 'webpack'
import CopyWebpackPlugin from 'copy-webpack-plugin';


const config: webpack.Configuration = {
    entry: {
        background: './src/background.js',
        content: './src/content.js',
        popup: './src/popup.js',
    },
    output: {
        filename: '[name].js',
        path: path.resolve(__dirname, 'dist'),
        clean: true, // Clean the output directory before emit.
    },
    plugins: [
        new CopyWebpackPlugin({
            patterns: [
                { from: 'static' },
                { from: 'src/images', to: 'images' }
            ],
        }),
    ]
}

export default config