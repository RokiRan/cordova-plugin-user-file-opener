var exec = require('cordova/exec');

var PLUGIN_NAME = 'HGFileOpenerPlugin';

var HGFileOpenerPlugin = {
    
    install: function(filePath, onSuccess, onFail) {
        return exec(onSuccess, onFail, PLUGIN_NAME, 'install', [filePath]);
    }
    
};

module.exports = HGFileOpenerPlugin;
