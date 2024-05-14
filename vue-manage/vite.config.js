import vue from '@vitejs/plugin-vue'

export default {
    base: './',
    plugins: [vue()],
    optimizeDeps: {
        include: ['schart.js']
    },
    server: {
        host: '0.0.0.0',
        proxy: {
            '/dev': {
                target: 'http://127.0.0.1:8082',
                changeOrigin: true,
                rewrite: path => path.replace(/^\/dev/, '')
            },
            '/api': {
                target:'https://zhtc.aldwxa.top/api',
                changeOrigin: true,
                rewrite: path => path.replace(/^\/prod/, '')
            },
        },
    }
}
