
// karate's configuration for *.feature file, we can reuse some config in *.feature file
function fn() {

    // RESTful API
    var protocol = 'http'

    var zeppelin_hostname = karate.properties['zeppelin.server.hostname']
                            || 'zeppelin-server.test-65-testing'
    var zeppelin_port = karate.properties['zeppelin.server.port'] || '80'

    var config = {
        zeppelin: {
            base_url: protocol + '://' + zeppelin_hostname + ':' + zeppelin_port,
            api_version: '/api',
            path: {
                cluster: '/sparkcluster',
                database: '/database',
                table: '/table'
            }
        }
    }
    return config
}