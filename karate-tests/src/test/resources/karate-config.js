function fn() {

    var env = karate.env || 'docker';
    karate.log('Running Karate in:', env);

    var config = {};

    if (env === 'docker') {
        config.baseUrl = 'http://api-gateway:8091';
    } else {
        config.baseUrl = 'http://api-gateway:8091'; // force docker
    }

    karate.log('Base URL:', config.baseUrl);

    return config;
}