
module.exports = {
  transpileDependencies: [],
  
  configureWebpack: {
    output: {
      filename: 'js/[name].[hash:8].js',
      chunkFilename: 'js/[name].[hash:8].js'
    }
  },

  devServer: {
    port: 8080,
    allowedHosts: ['all'],
    hot: true,
    headers: {
      'Cache-Control': 'no-store'
    },
    proxy: {
      '/api': {
        target: 'http://localhost:9999',
        changeOrigin: true,
        pathRewrite: { '^/api': '' }
      },
      '/file': {
        target: 'http://localhost:9999',
        changeOrigin: true
      }
    }
  },

  chainWebpack: config => {
    config.plugin('html').tap(args => {
      args[0].title = "管理系统";
      return args;
    });

    if (process.env.NODE_ENV === 'development') {
      config.output.filename('js/[name].[hash:8].js');
    }
  }
}

