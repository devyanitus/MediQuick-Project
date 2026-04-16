// function fn() {
//
//     var env = karate.env || 'local';  // default to local
//
//     karate.log('Running in environment:', env);
//
//     if (env === 'local') {
//         return { baseUrl: 'http://localhost:8091' };
//     }
//
//     if (env === 'docker') {
//         return { baseUrl: 'http://api-gateway:8091' };
//     }
//
//     if (env === 'ci') {
//         return { baseUrl: 'http://localhost:8091' };
//     }
//
//     return { baseUrl: 'http://localhost:8091' };
// }