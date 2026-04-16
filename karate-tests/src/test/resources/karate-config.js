function fn() {

    var env = karate.env || 'local';  // default = local
    var config = {};

    if (env === 'local') {
        config.baseUrl = 'http://localhost:8091';
    }
    else if (env === 'docker') {
        config.baseUrl = 'http://api-gateway:8091';
    }

    return config;
}