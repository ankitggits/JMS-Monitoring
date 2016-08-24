angular.module('sampleApp.appRoutes', []).config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {

    $routeProvider

        // home page
        .when('/', {
            templateUrl: 'views/home.html',
            controller: 'HomeController'
        })

        .when('/trackPage', {
            templateUrl: 'views/track.html',
            controller: 'TrackController'
        })

        .when('/msgBoardPage', {
            templateUrl: 'views/msgBoard.html',
            controller: 'MessageBoardController'
        })

        .when('/testPage', {
            templateUrl: 'views/test.html',
            controller: 'TestController'
        })

        .when('/msgPage', {
            templateUrl: 'views/msg.html',
            controller: 'MessageController'
        });



    $locationProvider.html5Mode(false);

}]);