'use strict';

angular.module('serviceorderApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('serviceOrder', {
                parent: 'entity',
                url: '/serviceOrder',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ServiceOrders'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/serviceOrder/serviceOrders.html',
                        controller: 'ServiceOrderController'
                    }
                },
                resolve: {
                }
            })
            .state('serviceOrderDetail', {
                parent: 'entity',
                url: '/serviceOrder/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ServiceOrder'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/serviceOrder/serviceOrder-detail.html',
                        controller: 'ServiceOrderDetailController'
                    }
                },
                resolve: {
                }
            });
    });
