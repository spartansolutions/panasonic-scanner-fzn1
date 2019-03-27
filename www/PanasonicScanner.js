var exec = require('cordova/exec');

exports.activate = function(arg0, success, error) {
    exec(success, error, "PanasonicScanner", "activate", [arg0]);
};
exports.deactivate = function(arg0, success, error) {
    exec(success, error, "PanasonicScanner", "deactivate", [arg0]);
};
exports.startScanning = function(arg0, success, error) {
    exec(success, error, "PanasonicScanner", "startScanning", [arg0]);
};
exports.stopScanning = function(arg0, success, error) {
    exec(success, error, "PanasonicScanner", "stopScanning", [arg0]);
};