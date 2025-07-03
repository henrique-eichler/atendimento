import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import {NodeGlobalsPolyfillPlugin} from '@esbuild-plugins/node-globals-polyfill'
import {resolve} from 'path'

// Configuration factory that returns different configs based on mode
export default defineConfig(({mode}) => {
    const isDebug = mode === 'debug';

    return {
        plugins: [vue()],
        resolve: {
            alias: {
                vue: 'vue/dist/vue.esm-bundler.js'
            }
        },
        build: {
            outDir: resolve(__dirname, '../resources/static'), // 🔥 adiciona isso
            emptyOutDir: true,
            // Enable source maps in debug mode
            sourcemap: isDebug,
            // Minimize only in production mode
            minify: !isDebug ? 'esbuild' : false
        },
        optimizeDeps: {
            esbuildOptions: {
                define: {
                    global: 'globalThis'
                },
                plugins: [
                    NodeGlobalsPolyfillPlugin({
                        buffer: true,
                        process: true
                    })
                ]
            }
        }
    };
})
