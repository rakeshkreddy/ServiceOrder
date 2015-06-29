/* globals $ */
'use strict';

angular.module('serviceorderApp')
    .directive('serviceorderAppPager', function() {
        return {
            templateUrl: 'scripts/components/form/pager.html'
        };
    });
