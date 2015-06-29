'use strict';

angular.module('serviceorderApp')
    .controller('ServiceOrderController', function ($scope, ServiceOrder) {
        $scope.serviceOrders = [];
        $scope.loadAll = function() {
            ServiceOrder.query(function(result) {
               $scope.serviceOrders = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ServiceOrder.get({id: id}, function(result) {
                $scope.serviceOrder = result;
                $('#saveServiceOrderModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.serviceOrder.id != null) {
                ServiceOrder.update($scope.serviceOrder,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ServiceOrder.save($scope.serviceOrder,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ServiceOrder.get({id: id}, function(result) {
                $scope.serviceOrder = result;
                $('#deleteServiceOrderConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ServiceOrder.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteServiceOrderConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveServiceOrderModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.serviceOrder = {orderNumber: null, description: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
