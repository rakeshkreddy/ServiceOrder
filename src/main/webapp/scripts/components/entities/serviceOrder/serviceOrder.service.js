'use strict';

angular.module('serviceorderApp')
    .factory('ServiceOrder', function ($resource, DateUtils) {
        return $resource('api/serviceOrders/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
