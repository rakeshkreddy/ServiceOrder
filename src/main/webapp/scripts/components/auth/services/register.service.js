'use strict';

angular.module('serviceorderApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


