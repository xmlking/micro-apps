import sveltePreprocess from 'svelte-preprocess';
import staticAdapter from '@sveltejs/adapter-static';
import nodeAdapter from '@sveltejs/adapter-node';
import {resolve} from "path";

// const pkg = JSON.parse(fs.readFileSync(new URL('package.json', import.meta.url), 'utf8'));

function getAdapter() {
    switch (process.env.ADAPTER) {
        case 'node':
            return nodeAdapter({
                out: 'build/dist',
                precompress: false,
                /*
                env: {
                    host: 'HOST',
                    port: 'PORT'
                }
                */
            });
        default:
            return staticAdapter({
                pages: 'build/dist',
                assets: 'build/dist',
                fallback: 'index.html' // 404.html
            });
    }
}

/** @type {import('@sveltejs/kit').Config} */
const config = {
    // Consult https://github.com/sveltejs/svelte-preprocess
    // for more information about preprocessors
    preprocess: sveltePreprocess(),

    kit: {
        // hydrate the <div id="svelte"> element in src/app.html
        target: '#svelte',
        adapter: getAdapter(),
        vite: () => ({
            resolve: {
                alias: {
                    $components: resolve('./src/lib/shared/components'),
                    $ui: resolve('./src/lib/shared/ui'),
                    $shared: resolve('./src/lib/shared'),
                    $models: resolve('./src/lib/models'),
                    $data: resolve('./src/lib/data'),
                    $core: resolve('./src/lib/core'),
                    $utils: resolve('./src/lib/utils'),
                    $environment: resolve('./src/environments'),
                },
            }
        }),
    }
};

export default config;
