// This file loads the appropriate TypeScript config
require('ts-node').register();

// Determine which config to use based on NODE_ENV
const env = process.env.NODE_ENV || 'development';
const configPath = env === 'production' ? './webpack.prod.ts' : './webpack.dev.ts';

// Export the config
module.exports = require(configPath);