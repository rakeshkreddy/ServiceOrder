/* globals $ */
'use strict';

angular.module('serviceorderApp')
    .directive('serviceorderAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
