'use strict';

angular.module('serviceorderApp')
    .controller('ServiceOrderDetailController', function ($scope, $stateParams, ServiceOrder) {
        $scope.serviceOrder = {};
        $scope.load = function (id) {
            ServiceOrder.get({id: id}, function(result) {
              $scope.serviceOrder = result;
            });
        };
        $scope.load($stateParams.id);
    });
