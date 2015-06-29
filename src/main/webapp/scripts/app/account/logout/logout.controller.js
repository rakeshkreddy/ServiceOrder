'use strict';

angular.module('serviceorderApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
