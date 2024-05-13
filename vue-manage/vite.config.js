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
                target: 'http://192.168.0.104:8081',
                changeOrigin: true,
                rewrite: path => path.replace(/^\/dev/, '')
            },
            '/api': {
                target:'https://qd-zhtc',
                changeOrigin: true,
                rewrite: path => path.replace(/^\/prod/, '')
            },
        },
    }
}
