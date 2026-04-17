function fn() {

    var env = karate.env || 'docker';
    var config = {};

    if (env === 'docker') {
        config.baseUrl = 'http://api-gateway:8091';
    } else {
        config.baseUrl = 'http://localhost:8091';
    }

    return config;
}