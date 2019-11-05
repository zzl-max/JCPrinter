var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'JCPrinter', 'coolMethod', [arg0]);
};
exports.getInstance = function (arg0, success, error) {
    exec(success, error, 'JCPrinter', 'getInstance', [arg0]);
};
