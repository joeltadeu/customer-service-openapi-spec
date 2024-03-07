function fn() {
    const config= {};
    config.showLog = 'true';
    config.connectTimeout = 120000;
    config.readTimeout = 120000;

    config.default = read('_defaults.json');
    config.env = karate.env ? karate.env : "local";
    config.customerServiceUrl = `http://localhost:${karate.properties['port']}`;

    config.FunctionsUtils = {
        get: function (obj, propertyName) {
            let value = obj[propertyName];
            if (value == null) {
                throw new Error(propertyName + 'must be non null')
            }
            karate.log(propertyName + ': ' + JSON.stringify(value));
            return value;
        },
        getOrDefault: function (obj, propertyName) {
            let value = obj[propertyName];
            if (value == null) {
                karate.log("using default value");
                value = config.defaults[propertyName];
            }
            if (value == null) {
                throw new Error(propertyName + 'must be non null, and has got no default value')
            }
            karate.log(propertyName + ': ' + JSON.stringify(value));
            return value;
        },
        getNullable: function (obj, propertyName) {
            let value = obj[propertyName];
            karate.log(propertyName + ': ' + JSON.stringify(value));
            return value;
        }
    }

    return config;
}